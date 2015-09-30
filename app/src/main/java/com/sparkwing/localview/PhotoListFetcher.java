package com.sparkwing.localview;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zachfreeman on 9/12/15.
 */
public class PhotoListFetcher extends AsyncTask<String, Integer, String> {

    private static final String TAG = PhotoListFetcher.class.getSimpleName();

    private PhotoListFetcherListener mPhotoListFetcherListener;

    public void setPhotoListFetcherListener(PhotoListFetcherListener mPhotoListFetcherListener) {
        this.mPhotoListFetcherListener = mPhotoListFetcherListener;
    }

    @Override
    protected String doInBackground(String... coordinates) {
        Response response = null;
        OkHttpClient client = new OkHttpClient();
        HttpUrl flickrSearchUrl = new HttpUrl.Builder()
                .scheme(FlickrConstants.HTTP_SCHEME)
                .host(FlickrConstants.API_HOST)
                .addPathSegment(FlickrConstants.SERVICE_PATH)
                .addPathSegment(FlickrConstants.REST_PATH)
                .addQueryParameter("method", FlickrConstants.SEARCH_METHOD)
                .addQueryParameter("api_key", FlickrConstants.API_KEY)
                .addQueryParameter("lat", coordinates[0])
                .addQueryParameter("lon", coordinates[1])
                .addQueryParameter("per_page", FlickrConstants.NUMBER_OF_PHOTOS)
                .addQueryParameter("format", FlickrConstants.FORMAT_TYPE)
                .addQueryParameter("privacy_filter", FlickrConstants.PRIVACY_FILTER)
                .addQueryParameter("nojsoncallback", FlickrConstants.JSON_CALLBACK)
                .build();
        Request request = new Request.Builder()
                .url(flickrSearchUrl)
                .build();

        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        List<FlickrPhoto> flickrPhotoList = FlickrPhotoUtils.unpackSearchResult(result);
        this.mPhotoListFetcherListener.onPhotoListFetched(flickrPhotoList);

    }

}
