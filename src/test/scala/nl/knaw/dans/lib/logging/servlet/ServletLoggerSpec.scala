package nl.knaw.dans.lib.logging.servlet

import com.typesafe.scalalogging.Logger
import nl.knaw.dans.lib.fixtures.ServletFixture
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ FlatSpec, Matchers }
import org.scalatra.test.scalatest.ScalatraSuite
import org.scalatra.{ Ok, ScalatraBase, ScalatraServlet }
import org.slf4j.{ Logger => Underlying }

class ServletLoggerSpec extends FlatSpec with Matchers with MockFactory with ServletFixture with ScalatraSuite {

  private val mockedLogger = mock[Underlying]

  private trait TestLogger extends ServletLogger {
    this: ScalatraBase =>

    override protected val logger: Logger = Logger(mockedLogger)
  }

  private class TestServlet() extends ScalatraServlet with TestLogger {

    get("/") {
      contentType = "text/plain"
      Ok("How y'all doin'?").logResponse
    }
  }

  private val testLoggerPath = "/testLoggerPath"

  addServlet(new TestServlet(), testLoggerPath)

  "TestServlet" should "call the logRequest on an incoming request" in {
    val serverPort = localPort.fold("None")(_.toString)

    (() => mockedLogger.isInfoEnabled()) expects() twice() returning true
    (mockedLogger.info(_: String)) expects where {
      s: String =>
        (s startsWith s"GET http://localhost:$serverPort$testLoggerPath") &&
          (s contains "remote=127.0.0.1")
    } once()
    (mockedLogger.info(_: String)) expects * once()

    get(testLoggerPath) {
      body shouldBe "How y'all doin'?"
      status shouldBe 200
    }
  }

  it should "call the logResponse on sending a response" in {
    (() => mockedLogger.isInfoEnabled()) expects() twice() returning true
    (mockedLogger.info(_: String)) expects where {
      s: String =>
        (s startsWith s"GET returned status=200") &&
          (s.toLowerCase contains "content-type -> [text/plain;charset=utf-8]") &&
          (s contains "actionHeaders=[]")
    } once()
    (mockedLogger.info(_: String)) expects * once()

    get(testLoggerPath) {
      body shouldBe "How y'all doin'?"
      status shouldBe 200
    }
  }
}
