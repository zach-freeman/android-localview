# LocalView (Android) #

This is a simple universal Android app that pulls images from the Flickr API based on the user's current location.

### Startup
You can get an API Key from your Flickr account. You need this to use the app. Just drop it in FlickrConstants.swift.

This project was developed with Android Studio. It should open just fine with the latest version.

### Dependencies ###
* [OkHttp](http://square.github.io/okhttp/)
* [Fresco](https://github.com/facebook/fresco)

### To-Do ###
* Handle landscape rotation in the full image view. Currently, we lock the orientation to portrait
