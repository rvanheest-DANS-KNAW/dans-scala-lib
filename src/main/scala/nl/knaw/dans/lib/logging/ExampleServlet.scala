package nl.knaw.dans.lib.logging

import nl.knaw.dans.lib.logging.servlet._
import org.scalatra.{ Ok, ScalatraServlet }

class ExampleServlet extends ScalatraServlet with ServletLogger with DebugEnhancedLogging {

  get("/") {
    Ok("All is well").logResponse
  }
}
