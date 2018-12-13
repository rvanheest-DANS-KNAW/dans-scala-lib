package nl.knaw.dans.lib.logging.servlet

package object masked {

  type MaskedAuthenticationParameters = request.MaskedAuthenticationParameters
  type MaskedAuthorizationHeader = request.MaskedAuthorizationHeader
  type MaskedCookie = request.MaskedCookie
  type MaskedRemoteAddress = request.MaskedRemoteAddress
  type MaskedRequestLogFormatter = request.MaskedRequestLogFormatter
  type MaskedRemoteUser = response.MaskedRemoteUser
  type MaskedResponseLogFormatter = response.MaskedResponseLogFormatter
  type MaskedSetCookie = response.MaskedSetCookie
}
