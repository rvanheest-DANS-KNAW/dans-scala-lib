package nl.knaw.dans.lib.logging.servlet.masked.response

import nl.knaw.dans.lib.logging.servlet.{ HeaderMap, ResponseLogFormatter }
import org.scalatra.ScalatraBase

trait MaskedResponseHeaders extends ResponseLogFormatter {
  this: ScalatraBase =>

  protected def formatResponseHeader(headerName: String,
                                     headerValues: Seq[String]): (String, Seq[String]) = {
    headerName -> headerValues
  }

  override protected def formatResponseHeaders(headers: HeaderMap): HeaderMap = {
    headers.map((formatResponseHeader _).tupled)
  }
}
