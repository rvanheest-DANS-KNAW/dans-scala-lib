package nl.knaw.dans.lib.logging.servlet.masked.response

import nl.knaw.dans.lib.logging.servlet.{ HeaderMapEntry, ResponseLogFormatter }
import org.scalatra.ScalatraBase

trait MaskedSetCookie extends ResponseLogFormatter {
  this: ScalatraBase =>

  abstract override def formatResponseHeader(entry: HeaderMapEntry): HeaderMapEntry = {
    super.formatResponseHeader(entry) match {
      case (name, values) if name.toLowerCase == "set-cookie" =>
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
