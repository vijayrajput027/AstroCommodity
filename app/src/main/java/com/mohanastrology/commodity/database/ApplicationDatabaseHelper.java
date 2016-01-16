package com.mohanastrology.commodity.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.mohanastrology.commodity.javafiles.HistoryPojo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by niraj on 11/19/2015.
 */
public class ApplicationDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;

    public ApplicationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    private static final int DATABASE_VERSION = 18;

    private static final String DATABASE_NAME = "ImageStorage";

    private static final String TABLE_COMMODITY_IMAGE= "CommodityImageInformation";
    private static final String TABLE_CURRENCY_IMAGE= "CurrencyImageInformation";

    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE_PATH = "imagePath";
    private static final String KEY_DATE = "date";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_SUB_CATEGORY = "subcategory";
    private static final String KEY_IMAGE_ID="imageId";

    private static final String CREATE_TABLE_COMMODITY = "CREATE TABLE " + TABLE_COMMODITY_IMAGE + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_DATE + " TEXT,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_SUB_CATEGORY + " TEXT,"
            + KEY_IMAGE_ID + " INTEGER,"
            + KEY_IMAGE_PATH + " TEXT)";

    private static final String CREATE_TABLE_CURRENCY = "CREATE TABLE " + TABLE_CURRENCY_IMAGE + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_DATE + " TEXT,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_SUB_CATEGORY + " TEXT,"
            + KEY_IMAGE_ID + " INTEGER,"
            + KEY_IMAGE_PATH + " TEXT)";



    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_COMMODITY);
            db.execSQL(CREATE_TABLE_CURRENCY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMODITY_IMAGE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENCY_IMAGE);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveToSdCardwithCommodityDB(Bitmap bitmap,String imageName, String date, String category,int imageId, String subcategory) {

        String picturePath = "";
        File sdcard = Environment.getExternalStorageDirectory();
        File folder = new File(sdcard.getAbsoluteFile(), "/MohanAstrology");
        folder.mkdir();
        File file = new File(folder.getAbsoluteFile(),imageName + ".jpg");
        picturePath = file.toString();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Save image path ro sqlite database

        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues newPictureValue = new ContentValues();
            newPictureValue.put(KEY_DATE, date);
            newPictureValue.put(KEY_IMAGE_PATH, picturePath);
            newPictureValue.put(KEY_CATEGORY, category);
            newPictureValue.put(KEY_IMAGE_ID, imageId);
            newPictureValue.put(KEY_SUB_CATEGORY, subcategory);
            db.insert(TABLE_COMMODITY_IMAGE, null, newPictureValue);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void saveToSdCardwithCurrencyDB(Bitmap bitmap, String imageName, String date, String category,int imageId, String subcategory) {

        String picturePath = "";
        File sdcard = Environment.getExternalStorageDirectory();
        File folder = new File(sdcard.getAbsoluteFile(), "/CurrencyAstrology");
        folder.mkdir();
        File file = new File(folder.getAbsoluteFile(), imageName + ".jpg");
        picturePath = file.toString();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Save image path ro sqlite database

        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues newPictureValue = new ContentValues();
            newPictureValue.put(KEY_DATE, date);
            newPictureValue.put(KEY_IMAGE_PATH, picturePath);
            newPictureValue.put(KEY_CATEGORY,category);
            newPictureValue.put(KEY_IMAGE_ID,imageId);
            newPictureValue.put(KEY_SUB_CATEGORY,subcategory);
            db.insert(TABLE_CURRENCY_IMAGE, null, newPictureValue);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<HistoryPojo> getOfflineCommodityImage() {
        ArrayList<HistoryPojo> offlinelist = new ArrayList<HistoryPojo>();
        String selectQuery = "SELECT imagePath,date,category,subcategory FROM " + TABLE_COMMODITY_IMAGE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String imagePath = cursor.getString(0);
                Bitmap bitMap = BitmapFactory.decodeFile(imagePath);
                String date = cursor.getString(1);
                String category=cursor.getString(2);
                String subcategory=cursor.getString(3);
                offlinelist.add(new HistoryPojo(imagePath,bitMap,date,category,subcategory));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return offlinelist;
    }


    public ArrayList<HistoryPojo> getOfflineCurrencyyImage() {
        ArrayList<HistoryPojo> offlinelist = new ArrayList<HistoryPojo>();
        String selectQuery = "SELECT imagePath,date,category,subcategory FROM " + TABLE_CURRENCY_IMAGE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String imagePath = cursor.getString(0);
                Bitmap bitMap = BitmapFactory.decodeFile(imagePath);
                String date = cursor.getString(1);
                String category=cursor.getString(2);
                String subcategory=cursor.getString(3);
                offlinelist.add(new HistoryPojo(imagePath,bitMap,date,category,subcategory));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return offlinelist;
    }

    public ArrayList<HistoryPojo> getOfflineFilterCommodity(String Category,String subCategory) {
        ArrayList<HistoryPojo> offlinelist = new ArrayList<HistoryPojo>();
        String selectQuery = "SELECT imagePath,date,category,subcategory FROM " + TABLE_COMMODITY_IMAGE +
              " where category like '%" + Category + "%' AND subcategory like '%" + subCategory + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String imagePath = cursor.getString(0);
                Bitmap bitMap = BitmapFactory.decodeFile(imagePath);
                String date = cursor.getString(1);
                String category=cursor.getString(2);
                String subcategory=cursor.getString(3);
                offlinelist.add(new HistoryPojo(imagePath,bitMap,date,category,subcategory));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return offlinelist;
    }

    public ArrayList<HistoryPojo> getOfflineFilterCurrency(String Category,String subCategory) {
        ArrayList<HistoryPojo> offlinelist = new ArrayList<HistoryPojo>();
        String selectQuery = "SELECT imagePath,date,category,subcategory FROM " + TABLE_CURRENCY_IMAGE +
                " where category like '%" + Category + "%' AND subcategory like '%" + subCategory + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String imagePath = cursor.getString(0);
                Bitmap bitMap = BitmapFactory.decodeFile(imagePath);
                String date = cursor.getString(1);
                String category=cursor.getString(2);
                String subcategory=cursor.getString(3);
                offlinelist.add(new HistoryPojo(imagePath,bitMap,date,category,subcategory));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return offlinelist;
    }


    public  String getCommodityImagePath(int imageId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select imagePath from " + TABLE_COMMODITY_IMAGE + " where imageId = " + imageId;
        Cursor cursor = db.rawQuery(Query, null);
        String imagePath=null;
        if (cursor.moveToFirst()) {
            do {
                 imagePath = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return imagePath;
    }

    public  String getCurrencyImagePath(int imageId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select imagePath from " + TABLE_CURRENCY_IMAGE + " where imageId = " + imageId;
        Cursor cursor = db.rawQuery(Query, null);
        String imagePath=null;
        if (cursor.moveToFirst()) {
            do {
                imagePath = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return imagePath;
    }


    public  String getofflineCommodityImagePath(int imageId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select imagePath from " + TABLE_COMMODITY_IMAGE + " where id = " + imageId;
        Cursor cursor = db.rawQuery(Query, null);
        String imagePath=null;
        if (cursor.moveToFirst()) {
            do {
                imagePath = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return imagePath;
    }

    public  String getofflineCurrencyImagePath(int imageId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select imagePath from " + TABLE_CURRENCY_IMAGE+ " where id = " + imageId;
        Cursor cursor = db.rawQuery(Query, null);
        String imagePath=null;
        if (cursor.moveToFirst()) {
            do {
                imagePath = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return imagePath;
    }


    public  boolean checkCommodityImage(int imageId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_COMMODITY_IMAGE + " where imageId = " + imageId;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    public  boolean checkCurrencyImage(int imageId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_CURRENCY_IMAGE + " where imageId = " + imageId;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }



}




