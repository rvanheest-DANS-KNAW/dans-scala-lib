package nl.knaw.dans.lib

package object string {

  implicit class StringExtensions(val s: String) extends AnyVal {

    def isBlank: Boolean = {
      s match {
        case null | "" => true
        case _ => s.forall(Character.isWhitespace)
      }
    }

    def toOption: Option[String] = {
      if (s.isBlank) Option.empty
      else Option(s)
    }

    def emptyIfBlank: String = s.toOption.getOrElse("")
  }
}
