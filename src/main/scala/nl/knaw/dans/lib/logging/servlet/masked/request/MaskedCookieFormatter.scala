package nl.knaw.dans.lib.logging.servlet.masked.request

import org.scalatra.ScalatraBase

trait MaskedCookieFormatter extends MaskedHeaders {
  this: ScalatraBase =>

  abstract override protected def formatHeader(headerName: String,
                                               headerValues: Seq[String]): (String, Seq[String]) = {
    super.formatHeader(headerName, headerValues) match {
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
