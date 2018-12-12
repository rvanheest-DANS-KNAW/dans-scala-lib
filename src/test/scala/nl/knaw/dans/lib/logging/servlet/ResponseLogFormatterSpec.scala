package nl.knaw.dans.lib.logging.servlet

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import nl.knaw.dans.lib.logging.servlet.masked.response.MaskedResponseLogFormatter
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ FlatSpec, Matchers }
import org.scalatra.{ ActionResult, Ok, ScalatraServlet }

import scala.collection.JavaConverters._

class ResponseLogFormatterSpec extends FlatSpec with Matchers with MockFactory {

  private val mockHeaders: HeaderMap = Map(
    "Set-Cookie" -> Seq("scentry.auth.default.user=abc456.pq.xy"),
    "REMOTE_USER" -> Seq("somebody"),
    "Expires" -> Seq("Thu, 01 Jan 1970 00:00:00 GMT"), // a date in the past means no cache for the returned content
  )

  private def mockResponse: HttpServletResponse = {
    val response = mock[HttpServletResponse]
    val headers = mockHeaders

    headers.foreach { case (key: String, values: Seq[String]) =>
      response.getHeaders _ expects key anyNumberOfTimes() returning values.asJava
    }
    (() => response.getHeaderNames) expects() anyNumberOfTimes() returning headers.keys.toSeq.asJava
    response
  }

  private def mockRequest: HttpServletRequest = {
    val req = mock[HttpServletRequest]
    (() => req.getMethod) expects() returning "GET" anyNumberOfTimes()
    req
  }

  private class TestServlet(implicit override val request: HttpServletRequest = mockRequest,
                            override val response: HttpServletResponse = mockResponse
                           ) extends ScalatraServlet with ResponseLogFormatter {

    def formatResponseLog(actionResult: ActionResult): String = super.formatResponseLog(actionResult)
  }

  "formatResponseLog" should "return a formatted log String for the response" in {
    new TestServlet().formatResponseLog(Ok(headers = Map("some" -> "header"))) shouldBe
      "GET returned status=200; authHeaders=[Set-Cookie -> [scentry.auth.default.user=abc456.pq.xy], REMOTE_USER -> [somebody], Expires -> [Thu, 01 Jan 1970 00:00:00 GMT]]; actionHeaders=[some -> header]"
  }

  it should "mask everything when using the MaskedResponseLogFormatter" in {
    (new TestServlet() with MaskedResponseLogFormatter).formatResponseLog(Ok()) shouldBe
      "GET returned status=200; authHeaders=[Set-Cookie -> [scentry.auth.default.user=******.**.**], REMOTE_USER -> [*****], Expires -> [Thu, 01 Jan 1970 00:00:00 GMT]]; actionHeaders=[]"
  }
}
