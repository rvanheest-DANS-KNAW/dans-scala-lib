package nl.knaw.dans.lib.logging.servlet.examples

import nl.knaw.dans.lib.logging.DebugEnhancedLogging
import nl.knaw.dans.lib.logging.servlet._
import nl.knaw.dans.lib.logging.servlet.masked.Masker
import org.scalatra.{ Ok, ScalatraBase, ScalatraServlet }

trait NamedCookieMasker extends RequestLogExtensionBase with ResponseLogExtensionBase {
  this: ScalatraBase =>

  abstract override protected def formatHeader(header: HeaderMapEntry): HeaderMapEntry = {
    Masker.formatCookieHeader("cookie")(formatCookie)(super.formatHeader(header))
  }

  abstract override protected def formatResponseHeader(header: HeaderMapEntry): HeaderMapEntry = {
    Masker.formatCookieHeader("set-cookie")(formatCookie)(super.formatResponseHeader(header))
  }

  private def formatCookie(value: String): String = {
    val cookieName = value.replaceAll("=.*", "")
    if (cookieName == "my-cookie") {
      val cookieValue = value.replaceFirst("[^=]+=", "")
      // replace sequences of chars without dots
      val maskedCookieValue = cookieValue.replaceAll("[^.]+", "****")
      s"$cookieName=$maskedCookieValue"
    }
    else value
  }
}

class MaskSpecificCookieServlet extends ScalatraServlet
  with ServletLogger
  with PlainLogFormatter
  with NamedCookieMasker
  with DebugEnhancedLogging {

  get("/") {
    Ok("foobar").logResponse
  }
}
