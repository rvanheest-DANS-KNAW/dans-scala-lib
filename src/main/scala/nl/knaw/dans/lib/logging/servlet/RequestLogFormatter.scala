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

import javax.servlet.http.HttpServletRequest
import org.scalatra.{ MultiParams, ScalatraBase }

import scala.collection.JavaConverters._

trait RequestLogFormatter {
  this: ScalatraBase =>

  protected def formatRequestLog: String = {
    val method = request.getMethod
    val requestURL = request.getRequestURL.toString
    val formattedHeaders = headersToString(formatHeaders(getHeaderMap(request)))
    val formattedParams = parametersToString(formatParameters(multiParams))
    val formattedRemoteAddress = formatRemoteAddress(Option(request.getRemoteAddr).getOrElse(""))

    // TODO perhaps more of https://github.com/scalatra/scalatra/blob/2.7.x/core/src/main/scala/org/scalatra/util/RequestLogging.scala#L70-L85
    s"$method $requestURL remote=$formattedRemoteAddress; params=$formattedParams; headers=$formattedHeaders"
  }

  protected def headersToString(headers: HeaderMap): String = headers.makeString

  protected def formatHeaders(headers: HeaderMap): HeaderMap = headers

  private def getHeaderMap(request: HttpServletRequest): HeaderMap = {
    // looks the same method as for ResponseLogFormatter, but everywhere different classes
    request.getHeaderNames.asScala.toSeq
      .map(name => name -> Option(request.getHeaders(name)).fold(Seq[String]())(_.asScala.toSeq))
      .toMap
  }

  protected def parametersToString(params: MultiParams): String = params.makeString

  protected def formatParameters(params: MultiParams): MultiParams = params

  protected def formatRemoteAddress(remoteAddress: String): String = remoteAddress
}
