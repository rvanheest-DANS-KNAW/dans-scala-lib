package nl.knaw.dans.lib.logging.servlet

import com.typesafe.scalalogging.LazyLogging
import nl.knaw.dans.lib.logging.servlet.masked.MaskedRequestLogFormatter
import org.scalatra.ScalatraBase

trait AbstractRequestLogger {
  this: ScalatraBase with RequestLogFormatter =>

  def logRequest(): Unit

  before() {
    logRequest()
  }
}

trait RequestLogger extends AbstractRequestLogger with RequestLogFormatter {
  this: ScalatraBase with LazyLogging =>

  override def logRequest(): Unit = logger.info(formatRequestLog)
}

trait MaskedRequestLogger extends RequestLogger with MaskedRequestLogFormatter {
  this: ScalatraBase with LazyLogging =>
}
