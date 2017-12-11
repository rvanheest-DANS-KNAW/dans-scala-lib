/**
 * Copyright (C) 2016 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.lib

package object string {

  implicit class StringExtensions(val s: String) extends AnyVal {

    /**
     * Return true when all characters in the `String` are classified as ''whitespace'', false otherwise.
     *
     * @return whether all characters are whitespace
     */
    def isBlank: Boolean = {
      s match {
        case null | "" => true
        case _ => s.forall(Character.isWhitespace)
      }
    }

    /**
     * Wraps the `String` in an `Option` and returns `Option.empty` if all characters are ''whitespace''.
     *
     * @return `None` if all characters are whitespace, `Some(s)` otherwise
     */
    def toOption: Option[String] = {
      if (s.isBlank) Option.empty
      else Option(s)
    }

    /**
     * Returns the empty `String` if all characters are ''whitespace'', the original `String` otherwise.
     *
     * @return the empty `String` if all characters are ''whitespace'', the input otherwise
     */
    def emptyIfBlank: String = s.toOption.getOrElse("")
  }
}
