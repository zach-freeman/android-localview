package com.sparkwing.localview;

import android.util.Log;

import com.squareup.okhttp.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zachfreeman on 9/22/15.
 */
public class FlickrApiUtils {

    private static final String TAG = FlickrApiUtils.class.getSimpleName();

    public enum FlickrPhotoSize {
        PhotoSizeUnknown,
        PhotoSizeCollectionIconLarge,
        PhotoSizeBuddyIcon,
        PhotoSizeSmallSquare75,
        PhotoSizeLargeSquare150,
        PhotoSizeThumbnail100,
        PhotoSizeSmall240,
        PhotoSizeSmall320,
        PhotoSizeMedium500,
        PhotoSizeMedium640,
        PhotoSizeMedium800,
        PhotoSizeLarge1024,
        PhotoSizeLarge1600,
        PhotoSizeLarge2048,
        PhotoSizeOriginal,
        PhotoSizeVideoOriginal,
        PhotoSizeVideoHDMP4,
        PhotoSizeVideoSiteMP4,
        PhotoSizeVideoMobileMP4,
        PhotoSizeVideoPlayer
    };

    public static ArrayList<FlickrPhoto> unpackSearchResult(String searchResult) {
        ArrayList<FlickrPhoto> flickrPhotoList = new ArrayList<FlickrPhoto>();
        try {
            JSONObject searchResultJsonObject = new JSONObject(searchResult);
            JSONObject photosJsonObject = searchResultJsonObject.getJSONObject("photos");
            JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");
            for (int i = 0; i < photoJsonArray.length(); i++) {
                JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

                String title = photoJsonObject.getString("title");
                if (title.length() == 0 || title == null) {
                    title = "";
                }
                String smallImageUrl = FlickrApiUtils.getPhotoUrlForSize(FlickrConstants.SMALL_IMAGE_SIZE, photoJsonObject);
                String bigImageUrl = FlickrApiUtils.getPhotoUrlForSize(FlickrConstants.BIG_IMAGE_SIZE, photoJsonObject);
                FlickrPhoto flickrPhoto = new FlickrPhoto(title, smallImageUrl, bigImageUrl);
                flickrPhotoList.add(flickrPhoto);
            }
        } catch (JSONException jsonException) {
            Log.e(TAG, "malformed json response encountered in flickr search result");
        }
        return flickrPhotoList;

    }

    public static HttpUrl getFlickrSearchUrlForCoordinates(String latitude, String longitude) {
        HttpUrl flickrSearchUrl = new HttpUrl.Builder()
                .scheme(FlickrConstants.HTTP_SCHEME)
                .host(FlickrConstants.API_HOST)
                .addPathSegment(FlickrConstants.SERVICE_PATH)
                .addPathSegment(FlickrConstants.REST_PATH)
                .addQueryParameter("method", FlickrConstants.SEARCH_METHOD)
                .addQueryParameter("api_key", FlickrConstants.API_KEY)
                .addQueryParameter("lat", latitude)
                .addQueryParameter("lon", longitude)
                .addQueryParameter("per_page", FlickrConstants.NUMBER_OF_PHOTOS)
                .addQueryParameter("format", FlickrConstants.FORMAT_TYPE)
                .addQueryParameter("privacy_filter", FlickrConstants.PRIVACY_FILTER)
                .addQueryParameter("nojsoncallback", FlickrConstants.JSON_CALLBACK)
                .build();
        return flickrSearchUrl;
    }

    public static String getPhotoUrlForSize(FlickrPhotoSize size, JSONObject photoJsonObject) throws JSONException {
        String photoUrlString = null;
        if (null != photoJsonObject) {
            String id = photoJsonObject.getString("id");
            String server = photoJsonObject.getString("server");
            String farm = photoJsonObject.getString("farm");
            String secret = photoJsonObject.getString("secret");
            photoUrlString = getPhotoUrl(size, id, server, farm, secret);
        }
        return photoUrlString;
    }

    private static String getPhotoUrl(FlickrPhotoSize size, String id, String server, String farm, String secret) {
        // https://farm{farm-id}.static.flickr.com/{server-id}/{id}_{secret}_[mstb].jpg
        String photoUrlString = String.format("%s://farm%s.%s/%s/%s_%s_%s.jpg",
                FlickrConstants.HTTP_SCHEME,
                farm,
                FlickrConstants.FLICKR_PHOTO_SOURCE_HOST,
                server,
                id,
                secret,
                getSuffixForSize(size));
        return photoUrlString;
    }

    private static String getSuffixForSize(FlickrPhotoSize size) {
        String [] suffixArray = {"",
                "collectionIconLarge",
                "buddyIcon",
                "s",
                "q",
                "t",
                "m",
                "n",
                "",
                "z",
                "c",
                "b",
                "h",
                "k",
                "o",
                "",
                "",
                "",
                "",
                ""};
        return suffixArray[size.ordinal()];
    }
}
