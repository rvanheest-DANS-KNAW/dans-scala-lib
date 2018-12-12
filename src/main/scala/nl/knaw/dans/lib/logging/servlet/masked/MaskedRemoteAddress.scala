package nl.knaw.dans.lib.logging.servlet.masked

import nl.knaw.dans.lib.logging.servlet.RequestLogFormatter
import org.scalatra.ScalatraBase

trait MaskedRemoteAddress extends RequestLogFormatter {
  this: ScalatraBase =>

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
  override protected def formatRemoteAddress(remoteAddress: String): String = {
    // TODO https://docs.oracle.com/javase/9/docs/api/java/net/Inet6Address.html
    remoteAddress.replaceAll("([0-9]+[.]){3}", "**.**.**.")
  }
}
