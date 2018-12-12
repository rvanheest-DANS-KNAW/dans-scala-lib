package nl.knaw.dans.lib.logging.servlet.masked

import org.scalatra.ScalatraBase

trait MaskedAuthorizationHeader extends MaskedHeaders {
  this: ScalatraBase =>

  abstract override protected def formatHeader(headerName: String,
                                               headerValues: Seq[String]): (String, Seq[String]) = {
    super.formatHeader(headerName, headerValues) match {
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
