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

/**
 * Package for logging servlet requests and responses in a standardized format.
 *
 * ==Rationale==
 * The request are logged with `before` filters. An `after` filter would not see an
 * `org.scalatra.ActionResult`. Its values are not saved in the implicit response provided by
 * `org.scalatra.ScalatraBase` as done by a trait for a `org.scalatra.ScalatraServlet` that extends
 * `org.scalatra.auth.ScentrySupport`.
 * See the last extensive readme version (documentation moved into an incomplete book and guides)
 * https://github.com/scalatra/scalatra/blob/6a614d17c38d19826467adcabf1dc746e3192dfc/README.markdown
 * sections #filters #action.
 *
 * Using `org.scalatra.util.RequestLogging` broke unit test in easy-deposit-api as it added a session
 * header to some responses.
 *
 * ==Usage==
 * To enable servlet logging, add the `ServletLogger` trait to servlet definition, together with
 * an implementation of a `com.typesafe.scalalogging.Logger`. In the example below we use
 * `DebugEnhancedLogging` for the latter.
 *
 * When the request/response contain privacy sensitive data, a `MaskedLogFormatter` might be added
 * as well in order mask things like authorization headers, cookies, remote addresses and
 * authentication parameters.
 *
 * {{{
 *    import nl.knaw.dans.lib.logging.DebugEnhancedLogging
 *    import nl.knaw.dans.lib.logging.servlet._
 *    import org.scalatra.{ Ok, ScalatraServlet }
 *
 *    class ExampleServlet extends ScalatraServlet with ServletLogger with DebugEnhancedLogging {
 *
 *      // I'd like to see a mandatory choice between a MaskedLogFormatter, a PlainLogFormatter or a CustomLogFormatter.
 *      // Not implicitly a plain log formatter.
 *
 *      get("/") {
 *        Ok("All is well").logResponse
 *      }
 *    }
 *
 *    class MaskedServlet extends ScalatraServlet with ServletLogger with MaskedLogFormatter with DebugEnhancedLogging {
 *      get("/") {
 *        Ok("All is well").logResponse
 *      }
 *    }
 * }}}
 *
 * ==Extension==
 *
 * // This example is a bridge too far.
 * // The typical use case would be a variation or mix of the PlainLogFormatter and/or the MaskedLogFormatter.
 * // A CustomLogFormatter could extend a mix of Plain/Masked/Custom-Request/Response-LogFormatter.
 * // The Custom-Request/Response-LogFormatter in turn can mix Plain/Masked/Custom traits.
 *
 * To write custom extensions to the log formatter, create a trait that extends either `RequestLogExtensionBase`
 * or `ResponseLogExtensionBase`. In this trait, implement the desired method (`formatHeader`, `formatParameter`,
 * `formatResponseHeader` or `formatActionHeader`), using an `abstract override` (which is important for mixing in
 * this formatter with the others). Keep in mind that the formatter should only return a formatted version of the
 * input and not mutate or perform side effects on it.
 *
 * {{{
 *    trait MyCustomRequestHeaderFormatter extends RequestLogExtensionBase {
 *      this: ScalatraBase =>
 *
 *      abstract override protected def formatHeader(header: HeaderMapEntry): HeaderMapEntry = {
 *        super.formatHeader(header) match {
 *          case (name, values) if name.toLowerCase == "my-header" =>
 *            name -> values.map(formatMyHeader)
 *          case otherwise => otherwise
 *        }
 *      }
 *
 *      private def formatMyHeader(value: String): String = {
 *        // return some formatting of 'value'
 *      }
 *    }
 *
 *    class ExampleServlet extends ScalatraServlet with ServletLogger with MyCustomRequestHeaderFormatter with DebugEnhancedLogging {
 *      get("/") {
 *        Ok("All is well").logResponse
 *      }
 *    }
 * }}}
 */
package object servlet {

  type HeaderMap = Map[String, Seq[String]]
  type HeaderMapEntry = (String, Seq[String])
  type ActionHeadersMap = Map[String, String]
  type ActionHeaderEntry = (String, String)
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
