package com.example.newsshare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDbHandler extends SQLiteOpenHelper {

    public MyDbHandler(Context context){
        super(context,Params.DB_NAME,null,Params.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE "+Params.TABLE_NAME+"("
                +Params.KEY_ID+" INTEGER PRIMARY KEY, "
                +Params.KEY_NEWS_IMAGE+" TEXT, "
                +Params.KEY_NEWS_DESC+" TEXT, "
                +Params.KEY_NEWS_URL+" TEXT, "
                +Params.KEY_NEWS_DATE+" TEXT)";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addNews(SavedModel meme){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Params.KEY_NEWS_IMAGE,meme.getImageUrl());
        values.put(Params.KEY_NEWS_DESC,meme.getDescription());
        values.put(Params.KEY_NEWS_DATE,meme.getDate());
        values.put(Params.KEY_NEWS_URL,meme.getNewsUrl());
        Log.d("newsDB",meme.getDescription()+"\n"+meme.getImageUrl()+"\n"+meme.getDate()+"\n"+meme.getNewsUrl()+" Successfully inserted");
        db.insert(Params.TABLE_NAME,null,values);
        db.close();
    }

    public List<SavedModel> getAllTodos(){
        List<SavedModel>memesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM "+Params.TABLE_NAME;
        Cursor cursor = db.rawQuery(select,null);
        if(cursor.moveToFirst()){
            do{
                SavedModel meme = new SavedModel();
                meme.setId(Integer.parseInt(cursor.getString(0)));
                meme.setImageUrl(cursor.getString(1));
                meme.setNewsUrl(cursor.getString(2));
                meme.setDescription(cursor.getString(3));
                meme.setDate(cursor.getString(4));
                memesList.add(meme);
                Log.d("newsDB",meme.getDescription()+"cursor moved");
            }while(cursor.moveToNext());
        }
        cursor.close();
        return memesList;
    }
    public void deleteTodo(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(Params.TABLE_NAME,Params.KEY_ID+"=?",new String[]{String.valueOf(id)});
        db.close();
    }
}
