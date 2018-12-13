package nl.knaw.dans.lib.logging.servlet

import nl.knaw.dans.lib.fixtures.ServletFixture
import nl.knaw.dans.lib.logging.servlet.masked.request.MaskedRemoteAddress
import org.scalatest.{ FlatSpec, Matchers }
import org.scalatra.test.scalatest.ScalatraSuite
import org.scalatra.{ ActionResult, Ok, ScalatraBase, ScalatraServlet }

class LoggerSpec extends FlatSpec with Matchers with ServletFixture with ScalatraSuite {

  private class TestServlet() extends ScalatraServlet {
    this: AbstractResponseLogger =>

    get("/") {
      contentType = "text/plain"
      Ok("How y'all doin'?").logResponse
    }
  }
  val stringBuilder = new StringBuilder

  trait TestLoggers extends AbstractResponseLogger
    with AbstractRequestLogger
    with ResponseLogFormatter
    with RequestLogFormatter {
    this: ScalatraBase =>

    override def logResponse(actionResult: ActionResult): ActionResult = {
      stringBuilder append formatResponseLog(actionResult) append "\n"
      actionResult
    }

    override def logRequest(): Unit = stringBuilder append formatRequestLog append "\n"
  }

  "separate custom loggers" should "override default loggers" in {
    class MyServlet() extends TestServlet with TestLoggers {}
    addServlet(new MyServlet(), "/*")

    shouldDivertLogging()
  }

  "combined custom loggers" should "override default loggers" in {
    class MyServlet() extends TestServlet with TestLoggers {}
    addServlet(new MyServlet(), "/*")

    shouldDivertLogging()
  }

  "custom request formatter" should "alter logged content" in {
    class MyServlet() extends TestServlet with TestLoggers with MaskedRemoteAddress {}
    addServlet(new MyServlet(), "/*")

    shouldDivertLogging(formattedRemote = "**.**.**.1")
  }

  private def shouldDivertLogging(formattedRemote: String = "127.0.0.1") = {
    stringBuilder.clear()
    get(uri = "/") {
      status shouldBe 200
      body shouldBe "How y'all doin'?"
      val port = localPort.getOrElse("None")
      val javaVersion = System.getProperty("java.version")
      val clientVersion = "4.5.3" // org.apache.httpcomponents dependency; may change when upgrading scalatra-scalatest
      val defaultHeaders =
        s"""Connection -> [keep-alive], Accept-Encoding -> [gzip,deflate], User-Agent -> [Apache-HttpClient/$clientVersion (Java/$javaVersion)], Host -> [localhost:$port]"""
      stringBuilder.toString() shouldBe
        s"""GET http://localhost:$port/ remote=$formattedRemote; params=[]; headers=[$defaultHeaders]
           |GET returned status=200; authHeaders=[Content-Type -> [text/plain;charset=UTF-8]]; actionHeaders=[]
           |""".stripMargin
    }
  }
}
