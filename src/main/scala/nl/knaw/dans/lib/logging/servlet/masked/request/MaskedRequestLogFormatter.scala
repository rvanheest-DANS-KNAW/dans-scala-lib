package nl.knaw.dans.lib.logging.servlet.masked.request

import nl.knaw.dans.lib.logging.servlet.RequestLogFormatter
import org.scalatra.ScalatraBase

trait MaskedRequestLogFormatter extends RequestLogFormatter
  with MaskedCookie
  with MaskedAuthorizationHeader
  with MaskedAuthenticationParameters
  with MaskedRemoteAddress {
  this: ScalatraBase =>
}
