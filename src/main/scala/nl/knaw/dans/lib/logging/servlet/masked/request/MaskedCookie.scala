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

import nl.knaw.dans.lib.logging.servlet.{ HeaderMapEntry, RequestLogExtensionBase }
import org.scalatra.ScalatraBase

private[masked] trait MaskedCookie extends RequestLogExtensionBase {
  this: ScalatraBase =>

  abstract override protected def formatHeader(header: HeaderMapEntry): HeaderMapEntry = {
    super.formatHeader(header) match {
      case (name, values) if name.toLowerCase == "cookie" =>
        name -> values.map(formatCookieHeader)
      case otherwise => otherwise
    }
  }

  private def formatCookieHeader(value: String): String = {
    val cookieName = value.replaceAll("=.*", "")
    val cookieValue = value.replaceAll(".*=", "")
    val maskedCookieValue = cookieValue.replaceAll("[^.]+", "****")// replace sequences of chars without dots
    s"$cookieName=$maskedCookieValue"
  }
}
