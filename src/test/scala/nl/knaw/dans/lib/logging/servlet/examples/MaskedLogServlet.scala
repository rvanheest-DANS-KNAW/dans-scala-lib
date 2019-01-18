package nl.knaw.dans.lib.logging.servlet.examples

import nl.knaw.dans.lib.logging.DebugEnhancedLogging
import nl.knaw.dans.lib.logging.servlet._
import org.scalatra.{ Ok, ScalatraServlet }

class MaskedLogServlet extends ScalatraServlet
  with ServletLogger
  with MaskedLogFormatter
  with DebugEnhancedLogging {

  get("/") {
    Ok("foobar").logResponse
  }
}
