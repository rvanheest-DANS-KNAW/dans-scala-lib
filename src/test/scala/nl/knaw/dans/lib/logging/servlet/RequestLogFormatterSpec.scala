package nl.knaw.dans.lib.logging.servlet

import javax.servlet.http.HttpServletRequest
import nl.knaw.dans.lib.fixtures.TestServletFixture
import nl.knaw.dans.lib.logging.servlet.masked.request.MaskedRequestLogFormatter
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ FlatSpec, Matchers }
import org.scalatra.MultiParams

import scala.collection.JavaConverters._

class RequestLogFormatterSpec extends FlatSpec with Matchers with MockFactory with TestServletFixture {

  override protected val mockParams: MultiParams = Map(
    "password" -> Seq("secret"),
    "login" -> Seq("mystery"),
  )

  private val mockHeaders: HeaderMap = Map(
    "cookie" -> Seq("scentry.auth.default.user=abc456.pq.xy"),
    "HTTP_AUTHORIZATION" -> Seq("basic 123x_"),
    "foo" -> Seq("bar"),
  )

  override protected def mockRequest: HttpServletRequest = {
    val request = super.mockRequest
    val headers = mockHeaders

    for ((key, values) <- headers) {
      request.getHeaders _ expects key anyNumberOfTimes() returning values.iterator.asJavaEnumeration
    }

    (() => request.getMethod) expects() anyNumberOfTimes() returning "GET"
    (() => request.getRequestURL) expects() anyNumberOfTimes() returning new StringBuffer("http://does.not.exist.dans.knaw.nl")
    (() => request.getRemoteAddr) expects() anyNumberOfTimes() returning "12.34.56.78"
    (() => request.getHeaderNames) expects() anyNumberOfTimes() returning headers.keys.iterator.asJavaEnumeration

    request
  }

  "formatRequestLog" should "return a formatted log String for the request" in {
    new TestServlet().formatRequestLog shouldBe
      "GET http://does.not.exist.dans.knaw.nl remote=12.34.56.78; params=[password -> [secret], login -> [mystery]]; headers=[cookie -> [scentry.auth.default.user=abc456.pq.xy], HTTP_AUTHORIZATION -> [basic 123x_], foo -> [bar]]"
  }

  it should "mask everything when using the MaskedRequestLogFormatter" in {
    (new TestServlet() with MaskedRequestLogFormatter).formatRequestLog shouldBe
      "GET http://does.not.exist.dans.knaw.nl remote=**.**.**.78; params=[password -> [*****], login -> [*****]]; headers=[cookie -> [scentry.auth.default.user=******.**.**], HTTP_AUTHORIZATION -> [basic *****], foo -> [bar]]"
  }
}
