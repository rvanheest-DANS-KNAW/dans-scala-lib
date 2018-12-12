package nl.knaw.dans.lib.logging.servlet

import com.typesafe.scalalogging.LazyLogging
import org.scalatra.{ ActionResult, ScalatraBase }

trait AbstractResponseLogger {
  this: ScalatraBase with ResponseLogFormatter =>

  implicit val responseLogger: AbstractResponseLogger = this

  def logResponse(actionResult: ActionResult): ActionResult
}

trait ResponseLogger extends AbstractResponseLogger with ResponseLogFormatter {
  this: ScalatraBase with LazyLogging =>

  override def logResponse(actionResult: ActionResult): ActionResult = {
    logger.info(formatResponseLog(actionResult))
    actionResult
  }
}
