package nl.knaw.dans.lib.logging.servlet

import org.scalatra.{ MultiParams, ScalatraBase }

trait RequestLogExtensionBase extends RequestLogFormatter {
  this: ScalatraBase =>

  /**
   * @inheritdoc
   */
  override protected def formatHeaders(headers: HeaderMap): HeaderMap = {
    headers.map(formatHeader)
  }

  /**
   * Formats (masking, prettyprinting, etc.) the given header's value for logging purposes.
   * Note that this does not change the content of the specific header in the actual request.
   *
   * @param header the header to be formatted
   * @return the formatted header
   */
  protected def formatHeader(header: HeaderMapEntry): HeaderMapEntry = header

  /**
   * @inheritdoc
   */
  override protected def formatParameters(params: MultiParams): MultiParams = {
    params.map(formatParameter)
  }

  /**
   * Formats (masking, prettyprinting, etc.) the given parameter's value for logging purposes.
   * By default it leaves the parameter untouched, but other implementations may provide other
   * formattings.
   *
   * Note that this does not change the content of the specific parameter in the actual request.
   *
   * @param param the parameter to be formatted
   * @return the formatted parameter
   */
  protected def formatParameter(param: MultiParamsEntry): MultiParamsEntry = param
}
