package nl.knaw.dans.lib.logging.servlet.masked.request

import nl.knaw.dans.lib.logging.servlet.{ HeaderMapEntry, RequestLogFormatter }
import org.scalatra.ScalatraBase

trait MaskedAuthorizationHeader extends RequestLogFormatter {
  this: ScalatraBase =>

  abstract override protected def formatHeader(entry: HeaderMapEntry): HeaderMapEntry = {
    super.formatHeader(entry) match {
      case (name, values) if name.toLowerCase.endsWith("authorization") =>
        name -> values.map(formatAuthorizationHeader)
      case otherwise => otherwise
    }
  }

  /**
   * Formats the value of headers with a case insensitive name ending with "authorization".
   * This implementation keeps the key like "basic", "digest" and "bearer" but masks the actual
   * credentials.
   */
  private def formatAuthorizationHeader(value: String): String = {
    value.replaceAll(" .+", " *****")
  }
}
