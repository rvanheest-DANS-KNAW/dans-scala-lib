package nl.knaw.dans.lib.logging.servlet.masked.request

import nl.knaw.dans.lib.logging.servlet.{ MultiParamsEntry, RequestLogFormatter }
import org.scalatra.ScalatraBase

trait MaskedAuthenticationParameters extends RequestLogFormatter {
  this: ScalatraBase =>

  abstract override protected def formatParameter(param: MultiParamsEntry): MultiParamsEntry = {
    super.formatParameter(param) match {
      case (name, values) if Seq("login", "password").contains(name.toLowerCase) =>
        name -> values.map(_ => "*****")
      case otherwise => otherwise
    }
  }
}
