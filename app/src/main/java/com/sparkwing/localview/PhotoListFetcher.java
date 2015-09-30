package com.sparkwing.localview;

import android.os.AsyncTask;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
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
        HttpUrl flickrSearchUrl = FlickrApiUtils
                .getFlickrSearchUrlForCoordinates(coordinates[0], coordinates[1]);
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
        List<FlickrPhoto> flickrPhotoList = FlickrApiUtils.unpackSearchResult(result);
        this.mPhotoListFetcherListener.onPhotoListFetched(flickrPhotoList);

    }

}
