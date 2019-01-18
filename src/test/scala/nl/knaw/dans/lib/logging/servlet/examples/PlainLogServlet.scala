package nl.knaw.dans.lib.logging.servlet.examples

import nl.knaw.dans.lib.logging.DebugEnhancedLogging
import nl.knaw.dans.lib.logging.servlet._
import org.scalatra.{ Ok, ScalatraServlet }

class PlainLogServlet extends ScalatraServlet
  with ServletLogger
  with PlainLogFormatter
  with DebugEnhancedLogging {

  get("/") {
    Ok("foobar").logResponse
  }
}
