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

import nl.knaw.dans.lib.fixtures.ServletFixture
import nl.knaw.dans.lib.logging.servlet.masked.MaskedRemoteAddress
import org.scalatest.{ FlatSpec, Matchers }
import org.scalatra.test.scalatest.ScalatraSuite
import org.scalatra.{ ActionResult, Ok, ScalatraBase, ScalatraServlet }

class LoggerSpec extends FlatSpec with Matchers with ServletFixture with ScalatraSuite {

  private class TestServlet() extends ScalatraServlet {
    this: AbstractServletLogger =>

    get("/") {
      contentType = "text/plain"
      Ok("How y'all doin'?").logResponse
    }
  }

  private val stringBuilder = new StringBuilder
  private val testLoggersPath = "/combinedLogger"
  private val maskedLoggersPath = "/requestLogger"

  addServlet(new TestServlet() with TestLoggers, testLoggersPath)
  addServlet(new TestServlet() with TestLoggers with MaskedRemoteAddress, maskedLoggersPath)

  trait TestLoggers extends AbstractServletLogger
    with ResponseLogFormatter
    with RequestLogFormatter {
    this: ScalatraBase =>

    override def logResponse(actionResult: ActionResult): ActionResult = {
      stringBuilder append formatResponseLog(actionResult) append "\n"
      actionResult
    }

    override def logRequest(): Unit = stringBuilder append formatRequestLog append "\n"
  }

  "combined custom loggers" should "override default loggers" in {
    shouldDivertLogging(testLoggersPath)
  }

  "custom request formatter" should "alter logged content" in {
    shouldDivertLogging(maskedLoggersPath, formattedRemote = "**.**.**.1")
  }

  private def shouldDivertLogging(path: String = "/", formattedRemote: String = "127.0.0.1") = {
    stringBuilder.clear()
    val realPath = if (path startsWith "/") path
                   else s"/$path"

    get(uri = realPath) {
      status shouldBe 200
      body shouldBe "How y'all doin'?"
      val port = localPort.getOrElse("None")
      val Array(requestLine, responseLine) = stringBuilder.toString().split("\n")

      requestLine should startWith(s"GET http://localhost:$port$path")

      responseLine should startWith(s"GET returned status=200; ")
      responseLine.toLowerCase() should include(s"content-type -> [text/plain;charset=utf-8]")
      responseLine should include(s"actionHeaders=[]")
    }
  }
}
