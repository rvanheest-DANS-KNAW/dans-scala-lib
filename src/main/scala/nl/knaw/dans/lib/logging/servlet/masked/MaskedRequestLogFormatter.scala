package nl.knaw.dans.lib.logging.servlet.masked

import nl.knaw.dans.lib.logging.servlet.RequestLogFormatter
import org.scalatra.ScalatraBase

trait MaskedRequestLogFormatter extends RequestLogFormatter
  with MaskedCookieFormatter
  with MaskedAuthorizationHeader
  with MaskedAuthenticationParameters
  with MaskedRemoteAddress {
  this: ScalatraBase =>
}
