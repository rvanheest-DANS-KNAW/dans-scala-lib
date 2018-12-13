package nl.knaw.dans.lib.logging.servlet.masked

import nl.knaw.dans.lib.logging.servlet.masked.request.MaskedRequestLogFormatter
import nl.knaw.dans.lib.logging.servlet.masked.response.MaskedResponseLogFormatter
import org.scalatra.ScalatraBase

private[servlet] trait MaskedLogFormatter extends MaskedRequestLogFormatter with MaskedResponseLogFormatter {
  this: ScalatraBase =>
}
