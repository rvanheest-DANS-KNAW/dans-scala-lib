package nl.knaw.dans.lib.logging.servlet.masked

import nl.knaw.dans.lib.logging.servlet.{ HeaderMapEntry, MultiParamsEntry }

private[masked] object Masker {

  def formatCookie(value: String): String = {
    val cookieName = value.replaceAll("=.*", "")
    val cookieValue = value.replaceFirst("[^=]+=", "")
    // replace sequences of chars without dots
    val maskedCookieValue = cookieValue.replaceAll("[^.]+", "****")
    s"$cookieName=$maskedCookieValue"
  }

  /**
   * Formats the value of the request property RemoteAddr.
   * The default implementation masks the network and part of the host within that network.
   * Thus it is no longer identifying a person while we still might have a chance
   * to identify sessions in the log for debugging purposes.
   *
   * https://www.bluecatnetworks.com/blog/ip-addresses-considered-personally-identifiable-information/
   * in case of link rot paste the url at the tail of https://web.archive.org/web/20181030102418/
   *
   * Services without public access might not need to mask.
   */
  def formatRemoteAddress(remoteAddress: String): String = {
    // TODO https://docs.oracle.com/javase/9/docs/api/java/net/Inet6Address.html
    remoteAddress.replaceAll("([0-9]+[.]){3}", "**.**.**.")
  }

  def formatCookieHeader(headerName: String): HeaderMapEntry => HeaderMapEntry = {
    formatTuple(_.toLowerCase == headerName)(formatCookie)
  }

  /**
   * Formats the value of headers with a case insensitive name ending with "authorization".
   * This implementation keeps the key like "basic", "digest" and "bearer" but masks the actual
   * credentials.
   */
  def formatAuthorizationHeader: HeaderMapEntry => HeaderMapEntry = {
    formatTuple(_.toLowerCase endsWith "authorization")(_.replaceAll(" .+", " *****"))
  }

  def formatRemoteUserHeader: HeaderMapEntry => HeaderMapEntry = {
    formatTuple(_.toLowerCase == "remote_user")(_ => "*****")
  }

  def formatAuthenticationParameter: MultiParamsEntry => MultiParamsEntry = {
    formatTuple(Seq("login", "password") contains _.toLowerCase)(_ => "*****")
  }

  private def formatTuple(predicate: String => Boolean)
                         (format: String => String)
                         (tuple: (String, Seq[String])): HeaderMapEntry = {
    tuple match {
      case (name, values) if predicate(name) => name -> values.map(format)
      case otherwise => otherwise
    }
  }
}
