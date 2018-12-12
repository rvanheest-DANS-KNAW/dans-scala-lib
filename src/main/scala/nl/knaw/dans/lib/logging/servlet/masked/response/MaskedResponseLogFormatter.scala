package nl.knaw.dans.lib.logging.servlet.masked.response

import nl.knaw.dans.lib.logging.servlet.ResponseLogFormatter
import org.scalatra.ScalatraBase

trait MaskedResponseLogFormatter extends ResponseLogFormatter
  with MaskedRemoteUserFormatter
  with MaskedSetCookieFormatter {
  this: ScalatraBase =>
}
