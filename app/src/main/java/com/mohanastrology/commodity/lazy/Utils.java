package com.mohanastrology.commodity.lazy;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.MediaMetadataRetriever;
import android.preference.PreferenceManager;

import com.mohanastrology.commodity.R;

import java.io.InputStream;
import java.io.OutputStream;


public class Utils {
	private static final String KEY_SEEK_POSITION="seek";
	private static final String KEY_SONG_POSITION="position";
	
	private static Utils utils;
	
	private  Utils() {
		
	}
	
	public static Utils getInstance(){
		if(utils==null){
			return new Utils();
		}
		return utils;
	}
	
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
             
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              //Read byte from input stream
                 
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
               
              //Write byte from output stream
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
    public void putCurrentStatus(Activity act,int seek_positon,int song_position){    	
    	SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(act);
    	Editor editor = prefs.edit();
    	editor.putInt(KEY_SONG_POSITION, song_position);
    	editor.commit();
    	editor.putInt(KEY_SEEK_POSITION, seek_positon);
    	editor.commit();
    }
    
    public int[] getCurrentStatus(Activity act){
    	int[] array=new int[2];
    	SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(act);
    	array[0]=prefs.getInt(KEY_SEEK_POSITION, -1);
    	array[1]=prefs.getInt(KEY_SONG_POSITION, -1);
    	return array;
    }
    
    public void clearStatus(Activity act){
    	SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(act);
    	Editor editor=prefs.edit();
    	editor.remove(KEY_SEEK_POSITION);
    	editor.remove(KEY_SONG_POSITION);
    	editor.commit();
    	
    }
    
    public static Bitmap scaleImage(String path, float width, Context c) {
		try {
			MediaMetadataRetriever dataRetriver = new MediaMetadataRetriever();
			dataRetriver.setDataSource(path);

			byte[] art = dataRetriver.getEmbeddedPicture();
			if (art != null) {
				Options options = new Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeByteArray(art, 0, art.length, options);

				options.inSampleSize = calculateInSampleSize(options,
						(int) width, (int) width);
				options.inJustDecodeBounds = false;
				return BitmapFactory.decodeByteArray(art, 0, art.length,
						options);
			} else {
				return BitmapFactory.decodeResource(c.getResources(),
						R.drawable.adele1);

			}
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
    
}