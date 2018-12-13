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
package nl.knaw.dans.lib.fixtures

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import nl.knaw.dans.lib.logging.servlet.{ RequestLogFormatter, ResponseLogFormatter }
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

    def formatResponseLog(actionResult: ActionResult): String = super.formatResponseLog(actionResult)
  }

  protected val mockParams: MultiParams = Map()

  protected def mockRequest: HttpServletRequest = mock[HttpServletRequest]

  protected def mockResponse: HttpServletResponse = mock[HttpServletResponse]
}
