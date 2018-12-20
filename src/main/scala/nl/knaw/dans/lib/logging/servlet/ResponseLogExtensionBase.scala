package nl.knaw.dans.lib.logging.servlet

import org.scalatra.ScalatraBase

trait ResponseLogExtensionBase extends ResponseLogFormatter {
  this: ScalatraBase =>

  /**
   * @inheritdoc
   */
  override protected def formatResponseHeaders(headers: HeaderMap): HeaderMap = {
    headers.map(formatResponseHeader)
  }

  /**
   * Formats (masking, prettyprinting, etc.) the given header's value for logging purposes.
   * Note that this does not change the content of the specific header in the actual response.
   *
   * @param header the header to be formatted
   * @return the formatted header
   */
  protected def formatResponseHeader(header: HeaderMapEntry): HeaderMapEntry = header

  /**
   * @inheritdoc
   */
  override protected def formatActionHeaders(actionHeaders: ActionHeadersMap): ActionHeadersMap = {
    actionHeaders.map(formatActionHeader)
  }

  /**
   * Formats (masking, prettyprinting, etc.) the given header's value for logging purposes.
   * Note that this does not change the content of the specific header in the actual response.
   *
   * @param header the header to be formatted
   * @return the formatted header
   */
  protected def formatActionHeader(header: ActionHeaderEntry): ActionHeaderEntry = header
}
