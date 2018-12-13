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

import com.typesafe.scalalogging.LazyLogging
import org.scalatra.{ ActionResult, ScalatraBase }

trait AbstractServletLogger {
  this: ScalatraBase with RequestLogFormatter with ResponseLogFormatter =>

  implicit val responseLogger: AbstractServletLogger = this

  before() {
    logRequest()
  }

  def logRequest(): Unit

  def logResponse(actionResult: ActionResult): ActionResult
}

trait ServletLogger extends AbstractServletLogger with RequestLogFormatter with ResponseLogFormatter {
  this: ScalatraBase with LazyLogging =>

  override def logRequest(): Unit = logger.info(formatRequestLog)

  override def logResponse(actionResult: ActionResult): ActionResult = {
    logger.info(formatResponseLog(actionResult))
    actionResult
  }
}
