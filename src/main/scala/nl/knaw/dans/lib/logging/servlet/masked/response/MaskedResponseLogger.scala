package nl.knaw.dans.lib.logging.servlet.masked.response

import com.typesafe.scalalogging.LazyLogging
import nl.knaw.dans.lib.logging.servlet.ResponseLogger
import org.scalatra.ScalatraBase

trait MaskedResponseLogger extends ResponseLogger with MaskedRemoteUserFormatter with MaskedSetCookieFormatter {
  this: ScalatraBase with LazyLogging =>
}
