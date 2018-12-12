package nl.knaw.dans.lib.logging.servlet.masked.request

import nl.knaw.dans.lib.logging.servlet.RequestLogFormatter
import org.scalatra.ScalatraBase

trait MaskedRequestLogFormatter extends RequestLogFormatter
  with MaskedCookieFormatter
  with MaskedAuthorizationHeader
  with MaskedAuthenticationParameters
  with MaskedRemoteAddress {
  this: ScalatraBase =>
}
