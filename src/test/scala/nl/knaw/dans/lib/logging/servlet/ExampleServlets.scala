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

import nl.knaw.dans.lib.logging.DebugEnhancedLogging
import nl.knaw.dans.lib.logging.servlet.masked._
import org.scalatra.{ Ok, ScalatraBase, ScalatraServlet }

trait PlainServletLogger extends ServletLogger
  with PlainLogFormatter
  with DebugEnhancedLogging {
  this: ScalatraBase =>
}

trait MaskedServletLogger extends ServletLogger
  with MaskedLogFormatter
  with DebugEnhancedLogging {
  this: ScalatraBase =>
}

trait CustomServletLogger extends ServletLogger
  with PlainLogFormatter
  with MaskedRemoteAddress
  with MaskedRemoteUser
  with DebugEnhancedLogging {
  this: ScalatraBase =>
  // TODO how to mask (for example) cookies with a different name in a different way
  //  with a single override or trait per type? For now we only have `scentry.auth.default.user`.
}


class PlainLogServlet extends ScalatraServlet with PlainServletLogger {

  get("/") {
    Ok("foobar").logResponse
  }
}

class MaskedLogServlet extends ScalatraServlet with MaskedServletLogger {

  get("/") {
    Ok("foobar").logResponse
  }
}

class PartiallyMaskedLogServlet extends ScalatraServlet
  with ServletLogger
  with PlainLogFormatter
  with MaskedRemoteAddress
  with MaskedRemoteUser
  with DebugEnhancedLogging {

  get("/") {
    Ok("foobar").logResponse
  }
}
