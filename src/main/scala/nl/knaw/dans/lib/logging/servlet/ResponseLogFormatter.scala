package nl.knaw.dans.lib.logging.servlet

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import org.scalatra.{ ActionResult, ScalatraBase }

import scala.collection.JavaConverters._

trait ResponseLogFormatter {
  this: ScalatraBase =>

  protected def formatResponseLog(actionResult: ActionResult)
                                 (implicit request: HttpServletRequest,
                                  response: HttpServletResponse): String = {
    val method = request.getMethod
    val status = actionResult.status
    val formattedAuthHeaders = formatResponseHeaders(getHeaderMap(response)).makeString
    val formattedActionHeaders = formatActionHeaders(actionResult).makeString

    s"$method returned status=$status; authHeaders=$formattedAuthHeaders; actionHeaders=$formattedActionHeaders"
  }

  protected def formatResponseHeaders(headers: HeaderMap): HeaderMap = headers.map(formatResponseHeader)

  protected def formatResponseHeader(entry: HeaderMapEntry): HeaderMapEntry = entry

  private def getHeaderMap(response: HttpServletResponse): HeaderMap = {
    response.getHeaderNames.asScala.toSeq
      .map(name => name -> Option(response.getHeaders(name)).fold(Seq[String]())(_.asScala.toSeq))
      .toMap
  }

  protected def formatActionHeaders(actionResult: ActionResult): Map[String, String] = {
    actionResult.headers
  }
}
