package com.sparkwing.localview.Models;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photos implements Parcelable
{

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("pages")
    @Expose
    private Integer pages;
    @SerializedName("perpage")
    @Expose
    private Integer perpage;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("photo")
    @Expose
    private List<Photo> photoList = null;
    public final static Parcelable.Creator<Photos> CREATOR = new Creator<Photos>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Photos createFromParcel(Parcel in) {
            Photos instance = new Photos();
            instance.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.pages = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.perpage = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.total = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.photoList, (Photo.class.getClassLoader()));
            return instance;
        }

        public Photos[] newArray(int size) {
            return (new Photos[size]);
        }

    }
            ;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getPerpage() {
        return perpage;
    }

    public void setPerpage(Integer perpage) {
        this.perpage = perpage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(page);
        dest.writeValue(pages);
        dest.writeValue(perpage);
        dest.writeValue(total);
        dest.writeList(photoList);
    }

    public int describeContents() {
        return 0;
    }

}