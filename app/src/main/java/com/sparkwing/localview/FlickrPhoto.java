package com.sparkwing.localview;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zachfreeman on 9/12/15.
 */
public class FlickrPhoto implements Parcelable {

    public static final String BUNDLE_KEY = "FlickrPhoto";
    private String mTitleComment;
    private String mSmallImageUrl;
    private String mBigImageUrl;

    public FlickrPhoto(String titleComment, String smallImageUrl, String bigImageUrl){
        this.mTitleComment = titleComment;
        this.mSmallImageUrl = smallImageUrl;
        this.mBigImageUrl = bigImageUrl;
    }

    public FlickrPhoto(Parcel in) {
        this.mTitleComment = in.readString();
        this.mSmallImageUrl = in.readString();
        this.mBigImageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTitleComment);
        dest.writeString(this.mSmallImageUrl);
        dest.writeString(this.mBigImageUrl);
    }

    public static final Parcelable.Creator<FlickrPhoto> CREATOR
            = new Parcelable.Creator<FlickrPhoto>() {
        public FlickrPhoto createFromParcel(Parcel in) {
            return new FlickrPhoto(in);
        }

        public FlickrPhoto[] newArray(int size) {
            return new FlickrPhoto[size];
        }
    };

    public String getTitleComment() {
        return mTitleComment;
    }

    public void setTitleComment(String titleComment) {
        this.mTitleComment = titleComment;
    }

    public String getSmallImageUrl() {
        return mSmallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.mSmallImageUrl = smallImageUrl;
    }

    public String getBigImageUrl() {
        return mBigImageUrl;
    }

    public void setBigImageUrl(String bigImageUrl) {
        this.mBigImageUrl = bigImageUrl;
    }



}
