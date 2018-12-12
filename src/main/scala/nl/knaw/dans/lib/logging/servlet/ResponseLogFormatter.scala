package nl.knaw.dans.lib.logging.servlet

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import org.scalatra.{ ActionResult, ScalatraBase }

import scala.collection.JavaConverters._

trait ResponseLogFormatter {
  this: ScalatraBase =>

  protected def formatResponseLog(actionResult: ActionResult)(implicit request: HttpServletRequest,
                                                    response: HttpServletResponse): String = {
    val method = request.getMethod
    val status = actionResult.status
    val formattedAuthHeaders = responseHeadersToString(formatResponseHeaders(getHeaderMap(response)))
    val formattedActionHeaders = actionHeadersToString(actionResult)

    s"$method returned status=$status; authHeaders=$formattedAuthHeaders; actionHeaders=$formattedActionHeaders"
  }

  protected def responseHeadersToString(headers: HeaderMap): String = headers.makeString

  protected def formatResponseHeaders(headers: HeaderMap): HeaderMap = headers

  private def getHeaderMap(response: HttpServletResponse): HeaderMap = {
    response.getHeaderNames.asScala.toSeq
      .map(name => name -> Option(response.getHeaders(name)).fold(Seq[String]())(_.asScala.toSeq))
      .toMap
  }

  protected def actionHeadersToString(actionResult: ActionResult): String = {
    formatActionHeaders(actionResult).makeString
  }

  protected def formatActionHeaders(actionResult: ActionResult): Map[String, String] = {
    actionResult.headers
  }
}
