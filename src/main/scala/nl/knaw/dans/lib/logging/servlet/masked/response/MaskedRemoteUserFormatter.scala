package nl.knaw.dans.lib.logging.servlet.masked.response

import org.scalatra.ScalatraBase

trait MaskedRemoteUserFormatter extends MaskedResponseHeaders {
  this: ScalatraBase =>

  abstract override def formatResponseHeader(headerName: String,
                                             headerValues: Seq[String]): (String, Seq[String]) = {
    super.formatResponseHeader(headerName, headerValues) match {
      case (name, values) if name.toLowerCase == "remote_user" =>
        name -> values.map(formatRemoteUserValue)
    }
  }

  private def formatRemoteUserValue(value: String): String = "*****"
}
