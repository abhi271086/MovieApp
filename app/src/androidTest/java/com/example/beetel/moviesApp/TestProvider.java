package com.example.beetel.moviesApp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.beetel.moviesApp.data.model.MovieContract;
import com.example.beetel.moviesApp.data.model.MovieDbHelper;

/**
 * Created by beetel on 14/03/2016.
 */
public class TestProvider extends AndroidTestCase {
    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void testDeleteAllRecords() throws Throwable {
        mContext.getContentResolver().delete(MovieContract.Movie.CONTENT_URI, null, null);
        Cursor cursor = mContext.getContentResolver().query(MovieContract.Movie.CONTENT_URI, null, null, null, null);
        assertEquals(cursor.getCount(), 0);
        cursor.close();
    }

    public void testInsertProvider() throws Throwable {
        SQLiteDatabase db = new MovieDbHelper(mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MovieContract.Movie.COLUMN_TITLE, "The Hunger Games:MokingJay-Part2");
        values.put(MovieContract.Movie.COLUMN_POSTER_URL, "/nN4cEJMHJHbJBsp3vvvhtNWLGqg.jpg");
        values.put(MovieContract.Movie.COLUMN_BACK_DROP_URL, "/9aegDgArYL5fSCusQV1vIyZROqe.jpg");
        values.put(MovieContract.Movie.COLUMN_ORIGINAL_TITLE, "The Hunger Games:MokingJay- Part 2");
        values.put(MovieContract.Movie.COLUMN_PLOT, "With the nation of Panem in a full scale war, Katniss confronts President Snow in the final showdown. Teamed with a group of her closest friends – including Gale, Finnick, and Peeta – Katniss goes off on a mission with the unit from District 13 as they risk their lives to stage an assassination attempt on President Snow who has become increasingly obsessed with destroying her. The mortal traps, enemies, and moral choices that await Katniss will challenge her more than any arena she faced in The Hunger Games.");
        values.put(MovieContract.Movie.COLUMN_RATING, "6.9");
        values.put(MovieContract.Movie.COLUMN_RELEASE_DATE, "2015-11-20");
        values.put(MovieContract.Movie.COLUMN_MOVIE_ID, "281950");

        Uri rowUri;
        rowUri = mContext.getContentResolver().insert(MovieContract.Movie.CONTENT_URI, values);
        long rowId = ContentUris.parseId(rowUri);

        Log.d(LOG_TAG, "New row id inserted via provider:" + rowId);
        ContentValues values1 = new ContentValues(values);
        values1.put(MovieContract.Movie.COLUMN_MOVIE_ID, "281950");
        values1.put(MovieContract.Movie.COLUMN_TITLE, "The Hunger Games:MokingJay-Part2");
        int count = mContext.getContentResolver().update(MovieContract.Movie.CONTENT_URI, values1, MovieContract.Movie.COLUMN_MOVIE_ID + "=?", new String[]{"281950"});
        assertEquals(count, 1);
        String[] columns = {
                MovieContract.Movie.ID,
                MovieContract.Movie.COLUMN_TITLE,
                MovieContract.Movie.COLUMN_POSTER_URL,
                MovieContract.Movie.COLUMN_BACK_DROP_URL,
                MovieContract.Movie.COLUMN_ORIGINAL_TITLE,
                MovieContract.Movie.COLUMN_PLOT,
                MovieContract.Movie.COLUMN_RATING,
                MovieContract.Movie.COLUMN_RELEASE_DATE,
                MovieContract.Movie.COLUMN_MOVIE_ID
        };
        Cursor c1 = mContext.getContentResolver().query(MovieContract.Movie.CONTENT_URI, null, null, null, null);
        Cursor c = mContext.getContentResolver().query(MovieContract.Movie.buildMoviewithId("281950"), null, null, null,null);
        Log.d(LOG_TAG, String.valueOf(c1.getCount()));
        while (c.moveToNext()) {
            c.getString(c.getColumnIndex(MovieContract.Movie.COLUMN_TITLE));
            c.getString(c.getColumnIndex(MovieContract.Movie.COLUMN_POSTER_URL));
            c.getString(c.getColumnIndex(MovieContract.Movie.COLUMN_BACK_DROP_URL));
            c.getString(c.getColumnIndex(MovieContract.Movie.COLUMN_ORIGINAL_TITLE));
            c.getString(c.getColumnIndex(MovieContract.Movie.COLUMN_PLOT));
            String id = c.getString(c.getColumnIndex(MovieContract.Movie.COLUMN_MOVIE_ID));
            Log.d(LOG_TAG, "Movie id" + id);
            assertEquals("281950", id);

        }
    }

    public void testGetType() throws Throwable {
        String type = mContext.getContentResolver().getType(MovieContract.Movie.CONTENT_URI);
        assertEquals(MovieContract.Movie.CONTENT_TYPE, type);

        Log.d(LOG_TAG, type);
        String testMovieID = "281950";
        type = mContext.getContentResolver().getType(MovieContract.Movie.buildMoviewithId(testMovieID));
        assertEquals(MovieContract.Movie.CONTENT_ITEM_TYPE, type);
        Log.d(LOG_TAG, type);
    }
}