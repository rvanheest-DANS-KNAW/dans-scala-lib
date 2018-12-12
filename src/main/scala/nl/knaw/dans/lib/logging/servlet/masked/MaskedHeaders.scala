package nl.knaw.dans.lib.logging.servlet.masked

import nl.knaw.dans.lib.logging.servlet.{ HeaderMap, RequestLogFormatter }
import org.scalatra.ScalatraBase

trait MaskedHeaders extends RequestLogFormatter {
  this: ScalatraBase =>

  protected def formatHeader(headerName: String,
                             headerValues: Seq[String]): (String, Seq[String]) = {
    headerName -> headerValues
  }

  override protected def formatHeaders(headers: HeaderMap): HeaderMap = {
    headers.map((formatHeader _).tupled)
  }
}
