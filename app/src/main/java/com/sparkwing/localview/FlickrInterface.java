package com.sparkwing.localview;

import com.sparkwing.localview.Models.FlickrPhotoSearch;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zsfree00 on 1/15/17.
 */

public interface FlickrInterface {

    @GET("/services/rest/?method=flickr.photos.search")
    Observable<FlickrPhotoSearch> getSearchPhotos(@Query("api_key") String apiKey,
                                                  @Query("lat") String lattitude,
                                                  @Query("lon") String longitude,
                                                  @Query("per_page") String perPage,
                                                  @Query("format") String format,
                                                  @Query("privacy_filter") String privacyFilter,
                                                  @Query("nojsoncallback") String noJsonCallback);

    //Observable<String> getPhotoUrl()
}
