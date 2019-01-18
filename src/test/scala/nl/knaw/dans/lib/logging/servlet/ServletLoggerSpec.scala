/**
 * Copyright (C) 2016 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

  private trait TestLogger extends ServletLogger with PlainLogFormatter {
    this: ScalatraBase =>

    override protected val logger: Logger = Logger(mockedLogger)
  }

  private class TestServlet() extends ScalatraServlet with TestLogger {

    get("/") {
      // TODO test also paths with parameters like uuid's and file names
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
