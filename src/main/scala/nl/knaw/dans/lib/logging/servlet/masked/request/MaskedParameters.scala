package nl.knaw.dans.lib.logging.servlet.masked.request

import nl.knaw.dans.lib.logging.servlet.RequestLogFormatter
import org.scalatra.{ MultiParams, ScalatraBase }

trait MaskedParameters extends RequestLogFormatter {
  this: ScalatraBase =>

  protected def formatParameter(paramName: String,
                                paramValues: Seq[String]): (String, Seq[String]) = {
    paramName -> paramValues
  }

  override protected def formatParameters(params: MultiParams): MultiParams = {
    params.map((formatParameter _).tupled)
  }
}
