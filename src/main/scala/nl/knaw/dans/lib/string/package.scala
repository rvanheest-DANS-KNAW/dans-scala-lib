package nl.knaw.dans.lib

package object string {

  implicit class StringExtensions(val s: String) extends AnyVal {

    def isBlank: Boolean = {
      s match {
        case null | "" => true
        case _ => s.exists(!Character.isWhitespace(_))
      }
    }

    def toOption: Option[String] = {
      if (s.isBlank) Option.empty
      else Option(s)
    }
  }
}
