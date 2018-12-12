package nl.knaw.dans.lib.logging.servlet.masked

import org.scalatra.ScalatraBase

trait MaskedAuthenticationParameters extends MaskedParameters {
  this: ScalatraBase =>

  abstract override protected def formatParameter(paramName: String,
                                                  paramValues: Seq[String]): (String, Seq[String]) = {
    super.formatParameter(paramName, paramValues) match {
      case (name, values) if Seq("login", "password").contains(name.toLowerCase) =>
        name -> values.map(_ => "*****")
      case otherwise => otherwise
    }
  }
}
