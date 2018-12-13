package nl.knaw.dans.lib.logging.servlet

import com.typesafe.scalalogging.LazyLogging
import org.scalatra.ScalatraBase

trait ServletLogger extends RequestLogger with ResponseLogger {
  this: ScalatraBase with LazyLogging =>
}
