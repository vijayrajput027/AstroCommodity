package com.mohanastrology.commodity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohanastrology.commodity.database.ApplicationDatabaseHelper;
import com.mohanastrology.commodity.javafiles.InternetConnection;

import java.io.FileInputStream;

public class ImageDetail extends AppCompatActivity implements View.OnTouchListener{

    private TextView tvdate;
    private ImageView imageDetail;
    private static final String TAG = "Touch";
    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    private ApplicationDatabaseHelper applicationDatabaseHelper;
    String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Image Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        applicationDatabaseHelper=new ApplicationDatabaseHelper(this);

        String imageId = getIntent().getStringExtra("imageID");
        String date=getIntent().getStringExtra("date");
        int position=getIntent().getIntExtra("position", 0);
        String parent_id=getIntent().getStringExtra("parent_id");

        if(parent_id.equals("1")) {
            if (InternetConnection.checkNetworkConnection(this)) {
                int id = Integer.parseInt(imageId);
                picturePath = applicationDatabaseHelper.getCommodityImagePath(id);

            } else {
                int offlineId = position + 1;
                picturePath = applicationDatabaseHelper.getofflineCommodityImagePath(offlineId);

            }
        }
        else
        {
            if (InternetConnection.checkNetworkConnection(this)) {
                int id = Integer.parseInt(imageId);
                picturePath = applicationDatabaseHelper.getCurrencyImagePath(id);

            } else {
                int offlineId = position + 1;
                picturePath = applicationDatabaseHelper.getofflineCurrencyImagePath(offlineId);

            }
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitMapImage = BitmapFactory.decodeFile(picturePath, options);

        tvdate=(TextView)findViewById(R.id.textdate);
        imageDetail=(ImageView)findViewById(R.id.detailimage);
        tvdate.setText(date);
        imageDetail.setImageBitmap(bitMapImage);
        imageDetail.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:   // first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP: // first finger lifted
            case MotionEvent.ACTION_POINTER_UP: // second finger lifted
                mode = NONE;
                break;

            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                oldDist = spacing(event);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG)
                {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                }
                else if (mode == ZOOM)
                {
                    // pinch zooming
                    float newDist = spacing(event);
                    if (newDist > 5f)
                    {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist; // setting the scaling of the
                        // matrix...if scale > 1 means
                        // zoom in...if scale < 1 means
                        // zoom out
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true; // indicate event was handled
    }
    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }
    private void midPoint(PointF point, MotionEvent event)
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}