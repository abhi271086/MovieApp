package com.example.beetel.moviesApp.data.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by beetel on 12/03/2016.
 */
public class MovieProvider extends ContentProvider {

    public static final int MOVIE=100;
    public static final int MOVIE_WITH_ID=101;

    private static final UriMatcher uriMatcher=buildUriMatcher();
    private MovieDbHelper movieDbHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
        final String authority=MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,MovieContract.PATH_MOVIE,MOVIE);
        matcher.addURI(authority,MovieContract.PATH_MOVIE+"/*",MOVIE_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        movieDbHelper=new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
       Cursor retCursor;
        final int match=uriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                retCursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.Movie.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE_WITH_ID:
                retCursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.Movie.TABLE_NAME,
                        projection,
                        MovieContract.Movie.COLUMN_MOVIE_ID + "=" + ContentUris.parseId(uri) + "",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri:" + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;

        }


    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                return MovieContract.Movie.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.Movie.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri:" + uri);
        }
    }



    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match=uriMatcher.match(uri);
        Uri retUri;
        switch (match){
            case MOVIE:
        long id=movieDbHelper.getWritableDatabase().insert(MovieContract.Movie.TABLE_NAME,null,values);
        if(id>0)
            retUri=MovieContract.Movie.buildMoviewithId(String.valueOf(id));
        else
        throw new android.database.SQLException("Failed to insert row into"+uri);
        break;
        default:
        throw new UnsupportedOperationException("Unknown Uri"+uri);
    }
    getContext().getContentResolver().notifyChange(uri,null);
        return retUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case MOVIE:
                rowsDeleted = movieDbHelper.getWritableDatabase().delete(MovieContract.Movie.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }
        if (null == selection || 0 != rowsDeleted) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match=uriMatcher.match(uri);
        int rowsUpdated;

        switch (match){
            case MOVIE:
                rowsUpdated=movieDbHelper.getWritableDatabase().update(MovieContract.Movie.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri"+uri);
        }
        if(0!=rowsUpdated){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;
    }
    }
