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

import com.typesafe.scalalogging.Logger
import org.scalatra.{ ActionResult, ScalatraBase }

/**
 * This trait is the base for every servlet logger. It provides two abstract methods: `logRequest`
 * and `logResponse`.
 * Furthermore it adds a call to `logRequest` to the 'before filters' of `ScalatraBase`,
 * such that this method is called automatically on every request that comes in.
 * Finally it provides a 'self-pointer' in implicit scope, such that `LogResponseSyntax` can be
 * used automatically (see the documentation of `logResponse` for an example).
 */
trait AbstractServletLogger {
  this: ScalatraBase =>

  /**
   * This instance of the `AbstractServletLogger` in implicit scope.
   */
  implicit val responseLogger: AbstractServletLogger = this

  before() {
    logRequest()
  }

  /**
   * Performs the side effect of the logging of the request.
   * This method is typically not called in user code, but rather in `ScalatraBase`'s `before` filter.
   */
  def logRequest(): Unit

  /**
   * Performs the side effect of the logging of the response, contained in the given `ActionResult`.
   * This method is either called directly or via the extension method provided by
   * `LogResponseSyntax`.
   * In the examples below the two syntaxes are shown. Please note that the only difference is
   * `logResponse { Ok() }` vs. `Ok().logResponse`.
   *
   * @example
   * {{{
   *   import nl.knaw.dans.lib.logging.DebugEnhancedLogging
   *   import nl.knaw.dans.lib.logging.servlet._
   *   import org.scalatra.{ Ok, ScalatraServlet }
   *
   *   class ExampleServlet extends ScalatraServlet with ServletLogger with DebugEnhancedLogging {
   *     get("/") {
   *       logResponse {
   *         Ok("All is well")
   *       }
   *     }
   *   }
   * }}}
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
   * @param actionResult the `ActionResult to be logged`
   * @return the original `ActionResult`
   */
  def logResponse(actionResult: ActionResult): ActionResult
}

/**
 * Default servlet logger implemented with calls to `logger.info` for both the request and response logging.
 * Please note that the `logger` field is not implemented. It is most common to mixin `DebugEnhancedLogging`
 * for this, but other logger implementations can be provided as well.
 *
 * @example
 * {{{
 *   class ExampleServlet extends ScalatraServlet with ServletLogger with DebugEnhancedLogging
 * }}}
 */
trait ServletLogger extends AbstractServletLogger with RequestLogFormatter with ResponseLogFormatter {
  this: ScalatraBase =>

  protected val logger: Logger

  /**
   * @inheritdoc
   */
  override def logRequest(): Unit = logger.info(formatRequestLog)

  /**
   * @inheritdoc
   */
  override def logResponse(actionResult: ActionResult): ActionResult = {
    logger.info(formatResponseLog(actionResult))
    actionResult
  }
}
