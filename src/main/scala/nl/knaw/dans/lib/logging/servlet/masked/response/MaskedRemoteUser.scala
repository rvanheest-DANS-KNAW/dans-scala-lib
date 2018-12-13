package nl.knaw.dans.lib.logging.servlet.masked.response

import nl.knaw.dans.lib.logging.servlet.{ HeaderMapEntry, ResponseLogFormatter }
import org.scalatra.ScalatraBase

trait MaskedRemoteUser extends ResponseLogFormatter {
  this: ScalatraBase =>

  abstract override def formatResponseHeader(entry: HeaderMapEntry): HeaderMapEntry = {
    super.formatResponseHeader(entry) match {
      case (name, values) if name.toLowerCase == "remote_user" =>
        name -> values.map(formatRemoteUserValue)
      case otherwise => otherwise
    }
  }

  private def formatRemoteUserValue(value: String): String = "*****"
}
