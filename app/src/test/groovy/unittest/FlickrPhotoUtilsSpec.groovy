import com.sparkwing.localview.FlickrConstants
import com.sparkwing.localview.FlickrPhotoUtils
import org.json.JSONObject
import org.robolectric.annotation.Config
import pl.polidea.robospock.RoboSpecification
import spock.lang.Unroll

import static org.robolectric.annotation.Config.NONE

@Config(manifest=NONE)
class FlickrPhotoUtilsSpec extends RoboSpecification {

    @Unroll
    def "test get photo url sizes"() {
        given:
        def jsonObject = new JSONObject()
                .put("id", "1")
                .put("server", "2")
                .put("farm", "3")
                .put("secret", "4")

        expect:
        FlickrPhotoUtils.getPhotoUrlForSize(photoSize, jsonObject) == url

        where:
        photoSize                         |  url
        FlickrConstants.SMALL_IMAGE_SIZE  |  "https://farm3.static.flickr.com/2/1_4_s.jpg"
        FlickrConstants.BIG_IMAGE_SIZE    |  "https://farm3.static.flickr.com/2/1_4_b.jpg"
    }

    @Unroll
    def "test get photo url null"() {

        expect:
        FlickrPhotoUtils.getPhotoUrlForSize(FlickrConstants.SMALL_IMAGE_SIZE, jsonObject) == url

        where:
        jsonObject ||   url
        null       ||   null
    }

}