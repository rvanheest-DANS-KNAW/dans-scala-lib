package nl.knaw.dans.lib.logging.servlet

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import org.scalamock.scalatest.MockFactory
import org.scalatest.TestSuite
import org.scalatra.{ ActionResult, MultiParams, ScalatraServlet }

trait TestServletFixture {
  this: TestSuite with MockFactory =>

  protected class TestServlet(params: MultiParams = mockParams,
                              implicit override val request: HttpServletRequest = mockRequest,
                              implicit override val response: HttpServletResponse = mockResponse,
                             ) extends ScalatraServlet with RequestLogFormatter with ResponseLogFormatter {
    override def multiParams(implicit request: HttpServletRequest): MultiParams = params

    override def formatRequestLog: String = super.formatRequestLog

    override def formatResponseLog(actionResult: ActionResult): String = super.formatResponseLog(actionResult)
  }

  protected val mockParams: MultiParams = Map()

  protected def mockRequest: HttpServletRequest = mock[HttpServletRequest]

  protected def mockResponse: HttpServletResponse = mock[HttpServletResponse]
}
