/**
 * Copyright (C) 2016 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.lib.logging.servlet

import org.scalatra.{ MultiParams, ScalatraBase }

/**
 * Base trait for extending the `RequestLogFormatter`. Every custom request formatter must extend
 * this trait. For usage, see the documentation in the package object.
 */
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
   * By default it leaves the parameter unformatted, but other implementations may provide other
   * formattings.
   *
   * Note that this does not change the content of the specific parameter in the actual request.
   *
   * @param param the parameter to be formatted
   * @return the formatted parameter
   */
  protected def formatParameter(param: MultiParamsEntry): MultiParamsEntry = param
}
