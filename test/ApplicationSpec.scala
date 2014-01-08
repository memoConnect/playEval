import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.libs.json.Json
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "render the index page" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentAsString(home) must contain ("Hello Api")
    }

    "create a poll" in new WithApplication{

      val json = Json.obj( "options" -> Seq("one", "two", "three"))
      val req = FakeRequest(POST, "/createPolling").withJsonBody(json)
      val create = route(req).get

      status(create) must equalTo(OK)

    }

    "cast a vote" in new WithApplication{

      val json = Json.obj( "nameVoter" -> "schmusi", "votes" -> Seq(Seq("one"), Seq("two","five"), Seq("three")))
      val req = FakeRequest(POST, "/castVote/12345abcde").withJsonBody(json)
      val create = route(req).get

      status(create) must equalTo(OK)

    }


  }
}
