package com.mohanastrology.commodity.lazy;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.widget.ImageView;

import com.mohanastrology.commodity.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class OnlineImageLoader {

    MemoryCache memoryCache = new MemoryCache();

    FileCache fileCache;

    private Map<ImageView, String> imageViews = Collections.synchronizedMap(
                                           new WeakHashMap<ImageView, String>());
    ExecutorService executorService;

    //handler to display images in UI thread
    Handler handler = new Handler();
    static Context c;
    int width;
    boolean isFromWeb;

    @SuppressWarnings("static-access")
	public OnlineImageLoader(Context context,int width,boolean isFromWeb){

        fileCache = new FileCache(context);
         this.width=width;
        // Creates a thread pool that reuses a fixed number of
        // threads operating off a shared unbounded queue.
        executorService=Executors.newFixedThreadPool(5);
        this.isFromWeb=isFromWeb;
        this.c=context;
    }

    // default image show in list (Before online image download)
    final int stub_id= R.drawable.adele1;

    public void DisplayImage(String url, ImageView imageView)
    {
        //Store image and url in Map
        imageViews.put(imageView, url);

        //Check image is stored in MemoryCache Map or not (see MemoryCache.java)
        Bitmap bitmap = memoryCache.get(url);

        if(bitmap!=null){
            // if image is stored in MemoryCache Map then
            // Show image in listview row
            imageView.setImageBitmap(bitmap);
        }
        else
        {
            //queue Photo to download from url
            queuePhoto(url, imageView);

            //Before downloading image show default image
            imageView.setImageResource(stub_id);
        }
    }

    private void queuePhoto(String url, ImageView imageView)
    {
        // Store image and url in PhotoToLoad object
        PhotoToLoad p = new PhotoToLoad(url, imageView);

        // pass PhotoToLoad object to PhotosLoader runnable class
        // and submit PhotosLoader runnable to executers to run runnable
        // Submits a PhotosLoader runnable task for execution

        executorService.submit(new PhotosLoader(p));
    }

    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u;
            imageView=i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }

        @Override
        public void run() {
            try{
                //Check if image already downloaded
                if(imageViewReused(photoToLoad))
                    return;
                // download image from web url
                //TODO change the local like
                //Bitmap bmp = getBitmap(photoToLoad.url);
               Bitmap bmp;
                if(!isFromWeb){
                	bmp = getLocalBitmap(photoToLoad.url);

                }else{
                	bmp = getBitmapFromServer(photoToLoad.url);
                }

                // set image data in Memory Cache
                memoryCache.put(photoToLoad.url, bmp);

                if(imageViewReused(photoToLoad))
                    return;

                // Get bitmap to display
                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);

                // Causes the Runnable bd (BitmapDisplayer) to be added to the message queue.
                // The runnable will be run on the thread to which this handler is attached.
                // BitmapDisplayer run method will call
                handler.post(bd);

            }catch(Throwable th){
                th.printStackTrace();
            }
        }
    }

   private Bitmap getLocalBitmap(String url){
	   File f=fileCache.getFile(url);

       //from SD cache
       //CHECK : if trying to decode file which not exist in cache return null
       Bitmap b = decodeFile(f);
       if(b!=null)
           return b;
       try {
    	  return Utils.scaleImage(url, width, c);

       } catch (Throwable ex){
          ex.printStackTrace();
          if(ex instanceof OutOfMemoryError)
              memoryCache.clear();
          return null;
       }

   }
 /*  public static Bitmap getBitmapFromServer(String url){
	   Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail( url, MediaStore.Video.Thumbnails.MINI_KIND);
	   ByteArrayOutputStream stream = new ByteArrayOutputStream();
	   thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
	   byte[] data = stream.toByteArray();	  

	  	if(data!=null){
	  		return thumbnail;
	  	}
	  	return BitmapFactory.decodeResource(c.getResources(),R.drawable.adele);
	}*/
   
   private Bitmap getBitmapFromServer(String url){
	   MediaMetadataRetriever retriever = new MediaMetadataRetriever(); 	   
	  	retriever.setDataSource(url);	  	
	  	byte[] data=retriever.getEmbeddedPicture();
	  	if(data!=null){
	  		return getScaledImage(data, 70, c);
	  	}
	  	return BitmapFactory.decodeResource(c.getResources(),R.drawable.adele1);
	}
   
   public  Bitmap getScaledImage(byte[] image, float width, Context c) {
		try {
			Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(image, 0, image.length);
			options.inSampleSize = calculateInSampleSize(options, (int) width,
					(int) width);
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeByteArray(image, 0, image.length,options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// bitmap.recycle();
		return null;
	}
   
	public static int calculateInSampleSize(Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
	
  
    private Bitmap decodeFile(File f){

        try {

            //Decode image size
            Options o = new Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1=new FileInputStream(f);
            BitmapFactory.decodeStream(stream1,null,o);
            stream1.close();

          //Find the correct scale value. It should be the power of 2.

            // Set width/height of recreated image
            final int REQUIRED_SIZE=85;

            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2 < REQUIRED_SIZE || height_tmp/2 < REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //decode with current scale values
            Options o2 = new Options();
            o2.inSampleSize=scale;
            FileInputStream stream2=new FileInputStream(f);
            Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;

        } catch (FileNotFoundException e) {
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    boolean imageViewReused(PhotoToLoad photoToLoad){

        String tag=imageViews.get(photoToLoad.imageView);
        //Check url is already exist in imageViews MAP
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;

            // Show bitmap on UI
            if(bitmap!=null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        //Clear cache directory downloaded images and stored data in maps
        memoryCache.clear();
        fileCache.clear();
    }

}