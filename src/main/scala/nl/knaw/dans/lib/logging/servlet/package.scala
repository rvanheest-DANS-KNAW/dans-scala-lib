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
package nl.knaw.dans.lib.logging

import org.scalatra.ActionResult

package object servlet {

  type HeaderMap = Map[String, Seq[String]]
  type HeaderMapEntry = (String, Seq[String])
  type ActionHeadersMap = Map[String, String]
  type MultiParamsEntry = (String, Seq[String])

  implicit private[servlet] class MapExtensions[K, V](val stringMap: Map[K, V]) extends AnyVal {

    /** @return a toString like value with less class names */
    private[servlet] def makeString: String = {
      def formatSeq[T](seq: Seq[T]): String = {
        seq.map {
          case t: Seq[_] => formatSeq(t)
          case t => t
        }.mkString("[", ", ", "]")
      }

      stringMap.map {
        case (k, v: Seq[_]) => k -> formatSeq(v)
        case (k, v: Map[_, _]) => k -> v.makeString
        case kv => kv
      }.mkString("[", ", ", "]")
    }
  }

  /**
   * Convenience syntax for logging a response.
   *
   * @param actionResult the `ActionResult to be logged`
   */
  implicit class LogResponseSyntax(val actionResult: ActionResult) extends AnyVal {
    /**
     * Performs the side effect of the logging of the response, contained in the given `ActionResult`.\
     *
     * @example
     * {{{
     *   import nl.knaw.dans.lib.logging.DebugEnhancedLogging
     *   import nl.knaw.dans.lib.logging.servlet._
     *   import org.scalatra.{ Ok, ScalatraServlet }
     *
     *   class ExampleServlet extends ScalatraServlet with ServletLogger with DebugEnhancedLogging {
     *     get("/") {
     *       Ok("All is well").logResponse
     *     }
     *   }
     * }}}
     * @param responseLogger the logger with which to format/output the response
     * @return the original `ActionResult`
     */
    def logResponse(implicit responseLogger: AbstractServletLogger): ActionResult = {
      responseLogger.logResponse(actionResult)
    }
  }

  type MaskedLogFormatter = masked.MaskedLogFormatter
}
