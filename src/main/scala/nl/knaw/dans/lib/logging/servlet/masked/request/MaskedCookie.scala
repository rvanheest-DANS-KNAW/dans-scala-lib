package nl.knaw.dans.lib.logging.servlet.masked.request

import nl.knaw.dans.lib.logging.servlet.{ HeaderMapEntry, RequestLogFormatter }
import org.scalatra.ScalatraBase

trait MaskedCookie extends RequestLogFormatter {
  this: ScalatraBase =>

  abstract override protected def formatHeader(entry: HeaderMapEntry): HeaderMapEntry = {
    super.formatHeader(entry) match {
      case (name, values) if name.toLowerCase == "cookie" =>
        name -> values.map(formatCookieValue)
      case otherwise => otherwise
    }
  }

  private def formatCookieValue(value: String): String = {
    val cookieName = value.replaceAll("=.*", "")
    val cookieValue = value.replaceAll(".*=", "")
    val maskedCookieValue = cookieValue.replaceAll("[^.=]", "*") // replace everything but dots
    s"$cookieName=$maskedCookieValue"
  }
}
