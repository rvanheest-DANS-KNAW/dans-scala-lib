package nl.knaw.dans.lib.logging.servlet.masked.response

import org.scalatra.ScalatraBase

trait MaskedSetCookieFormatter extends MaskedResponseHeaders {
  this: ScalatraBase =>

  abstract override def formatResponseHeader(headerName: String,
                                             headerValues: Seq[String]): (String, Seq[String]) = {
    super.formatResponseHeader(headerName, headerValues) match {
      case (name, values) if name.toLowerCase == "set-cookie" =>
        name -> values.map(formatCookieValue)
    }
  }

  private def formatCookieValue(value: String): String = {
    val cookieName = value.replaceAll("=.*", "")
    val cookieValue = value.replaceAll(".*=", "")
    val maskedCookieValue = cookieValue.replaceAll("[^.=]", "*") // replace everything but dots
    s"$cookieName=$maskedCookieValue"
  }
}
