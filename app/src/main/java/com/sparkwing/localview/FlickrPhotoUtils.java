package com.sparkwing.localview;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zachfreeman on 9/22/15.
 */
public class FlickrPhotoUtils {

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
