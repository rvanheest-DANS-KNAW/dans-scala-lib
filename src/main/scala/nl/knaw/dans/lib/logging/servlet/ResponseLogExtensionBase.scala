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

import org.scalatra.ScalatraBase

/**
 * Base trait for extending the `ResponseLogFormatter`. Every custom response formatter must extend
 * this trait. For usage, see the documentation in the package object.
 */
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
