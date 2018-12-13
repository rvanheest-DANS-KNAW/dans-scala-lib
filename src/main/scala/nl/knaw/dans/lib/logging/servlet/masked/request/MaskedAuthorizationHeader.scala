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
package nl.knaw.dans.lib.logging.servlet.masked.request

import nl.knaw.dans.lib.logging.servlet.{ HeaderMapEntry, RequestLogFormatter }
import org.scalatra.ScalatraBase

private[masked] trait MaskedAuthorizationHeader extends RequestLogFormatter {
  this: ScalatraBase =>

  abstract override protected def formatHeader(entry: HeaderMapEntry): HeaderMapEntry = {
    super.formatHeader(entry) match {
      case (name, values) if name.toLowerCase.endsWith("authorization") =>
        name -> values.map(formatAuthorizationHeader)
      case otherwise => otherwise
    }
  }

  /**
   * Formats the value of headers with a case insensitive name ending with "authorization".
   * This implementation keeps the key like "basic", "digest" and "bearer" but masks the actual
   * credentials.
   */
  private def formatAuthorizationHeader(value: String): String = {
    value.replaceAll(" .+", " *****")
  }
}
