import com.sparkwing.localview.FlickrConstants
import com.sparkwing.localview.FlickrApiUtils
import org.json.JSONObject
import org.robolectric.annotation.Config
import pl.polidea.robospock.RoboSpecification
import spock.lang.Unroll

import static org.robolectric.annotation.Config.NONE

@Config(manifest=NONE)
class FlickrApiUtilsSpec extends RoboSpecification {

    @Unroll
    def "test get photo url sizes"() {
        given:
        def jsonObject = new JSONObject()
                .put("id", "1")
                .put("server", "2")
                .put("farm", "3")
                .put("secret", "4")

        expect:
        FlickrApiUtils.getPhotoUrlForSize(photoSize, jsonObject) == url

        where:
        photoSize                         |  url
        FlickrConstants.SMALL_IMAGE_SIZE  |  "https://farm3.static.flickr.com/2/1_4_s.jpg"
        FlickrConstants.BIG_IMAGE_SIZE    |  "https://farm3.static.flickr.com/2/1_4_b.jpg"
    }

    @Unroll
    def "test get photo url null"() {

        expect:
        FlickrApiUtils.getPhotoUrlForSize(FlickrConstants.SMALL_IMAGE_SIZE, jsonObject) == url

        where:
        jsonObject ||   url
        null       ||   null
    }

    @Unroll
    def "test unpackSearchResults"() {

        expect:
        def flickrPhotoList = FlickrApiUtils.unpackSearchResult(fileContents)
        listSize == flickrPhotoList.size()

        where:
        fileContents                                            | listSize
        getResourceFileContents('good-flickr-response.json')    | 5
        getResourceFileContents('bad-flickr-response.json')     | 0

    }

    def getResourceFileContents(String resourceFilename) {
        def resourcePath = System.getProperty("user.dir") + '/src/test/groovy/unittest/res/'
        def resourceFilepath = resourcePath + resourceFilename
        return new File(resourceFilepath).text
    }

}