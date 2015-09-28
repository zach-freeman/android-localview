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

    private List<FlickrPhoto> mFlickrPhotoList;

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
        unPackSearchResult(result);
        this.mPhotoListFetcherListener.onPhotoListFetched(this.mFlickrPhotoList);

    }

    private void unPackSearchResult(String searchResult) {
        mFlickrPhotoList = new ArrayList<FlickrPhoto>();
        try {
            JSONObject searchResultJsonObject = new JSONObject(searchResult);
            JSONObject photosJsonObject = searchResultJsonObject.getJSONObject("photos");
            JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");
            Log.d(TAG, "We retrieved " + photoJsonArray.length() + " photos");
            for (int i = 0; i < photoJsonArray.length(); i++) {
                JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

                String title = photoJsonObject.getString("title");
                if (title.length() == 0 || title == null) {
                    title = "";
                }
                String smallImageUrl = FlickrPhotoUtils.getPhotoUrlForSize(FlickrConstants.SMALL_IMAGE_SIZE, photoJsonObject);
                String bigImageUrl = FlickrPhotoUtils.getPhotoUrlForSize(FlickrConstants.BIG_IMAGE_SIZE, photoJsonObject);
                FlickrPhoto flickrPhoto = new FlickrPhoto(title, smallImageUrl, bigImageUrl);
                mFlickrPhotoList.add(flickrPhoto);
            }
        } catch (JSONException jsonException) {
            Log.e(TAG, "malformed json response encountered in flickr search result");
        }

    }
}
