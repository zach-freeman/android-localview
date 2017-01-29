package com.sparkwing.localview;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.inject.Inject;
import com.sparkwing.localview.Models.FlickrPhotoSearch;
import com.sparkwing.localview.Models.Photo;
import com.sparkwing.localview.Models.Photos;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import roboguice.RoboGuice;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zachfreeman on 9/19/15.
 */
public class PhotoListManager implements LocationUpdaterListener, PhotoListFetcherListener {
    private static final String TAG = PhotoListManager.class.getSimpleName();

    public Boolean getPhotoListFetched() {
        return mPhotoListFetched;
    }

    private Boolean mPhotoListFetched;
    @Inject private LocationUpdater mLocationUpdater;
    private PhotoListManagerListener mPhotoListManagerListener;

    public void setPhotoListManagerListener(PhotoListManagerListener mPhotoListManagerListener) {
        Log.d(TAG, "setting photomanagerlistener");
        this.mPhotoListManagerListener = mPhotoListManagerListener;
        this.mLocationUpdater.setupLocationService();
    }

    @Inject
    public PhotoListManager(Context context) {
        RoboGuice.getInjector(context).injectMembers(this);
        this.mPhotoListFetched = false;
        //this.mLocationUpdater = new LocationUpdater(context);
        if (Reachability.isConnected(context)) {
            this.mLocationUpdater.setLocationUpdaterListener(this);
        } else {
            Toast.makeText(context,
                    "Network Unavailable", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void locationAvailable(Location currentLocation) {
        if (!mPhotoListFetched) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .baseUrl(FlickrConstants.HTTP_SCHEME + "://" + FlickrConstants.API_HOST)
                    .build();

            FlickrInterface flickrInterface = retrofit.create(FlickrInterface.class);
            final Observable<FlickrPhotoSearch> photos = flickrInterface.getSearchPhotos(
                    FlickrConstants.API_KEY,
                    String.valueOf(currentLocation.getLatitude()),
                    String.valueOf(currentLocation.getLongitude()),
                    FlickrConstants.NUMBER_OF_PHOTOS,
                    FlickrConstants.FORMAT_TYPE,
                    FlickrConstants.PRIVACY_FILTER,
                    FlickrConstants.JSON_CALLBACK);

            photos.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<FlickrPhotoSearch>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, e.getLocalizedMessage());
                        }

                        @Override
                        public void onNext(FlickrPhotoSearch photoSearch) {
                            if (photoSearch != null) {
                                onPhotoListFetched(photoSearch.getPhotos().getPhotoList());
                                Log.e(TAG, photoSearch.getStat());
                            }
                        }
                    });
        }
    }

    @Override
    public void onPhotoListFetched(List<Photo> photoList) {
        this.mPhotoListFetched = true;
        if (this.mPhotoListManagerListener != null) {
            this.mPhotoListManagerListener.photoListManagerDidFinish(photoList);
        } else {
            Log.d(TAG, "not doing shit");
        }
    }
}
