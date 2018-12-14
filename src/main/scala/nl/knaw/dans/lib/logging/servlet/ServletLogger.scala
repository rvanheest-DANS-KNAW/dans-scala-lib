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

import com.typesafe.scalalogging.{ LazyLogging, Logger }
import org.scalatra.{ ActionResult, ScalatraBase }

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
   *
   * @example
   * {{{
   *   import nl.knaw.dans.lib.logging.DebugEnhancedLogging
   *   import nl.knaw.dans.lib.logging.servlet._
   *   import org.scalatra.{ Ok, ScalatraServlet }
   *
   *   class ExampleServlet extends ScalatraServlet with ServletLogger with DebugEnhancedLogging {
   *     get("/") {
   *       logResponse(Ok("All is well"))
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
