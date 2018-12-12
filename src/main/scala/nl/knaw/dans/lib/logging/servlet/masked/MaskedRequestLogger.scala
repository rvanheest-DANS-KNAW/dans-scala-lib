package nl.knaw.dans.lib.logging.servlet.masked

import com.typesafe.scalalogging.LazyLogging
import nl.knaw.dans.lib.logging.servlet.RequestLogger
import org.scalatra.ScalatraBase

trait MaskedRequestLogger extends RequestLogger with MaskedRequestLogFormatter {
  this: ScalatraBase with LazyLogging =>
}
