package test.anymind.pos.app

import com.expediagroup.graphql.server.spring.execution.DefaultSpringGraphQLContextFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException.Forbidden
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class AppGraphqlContextFactory: DefaultSpringGraphQLContextFactory() {
    override suspend fun generateContextMap(request: ServerRequest): Map<*, Any> {
        // Because authorization is necessary for POS application, these lines can be uncommented
        // to support JWT token to authorize user.
//        val token = request.headers().firstHeader("Authorization")
//        if (token === null) {
//            throw HttpServerErrorException(
//                HttpStatus.UNAUTHORIZED,
//                "You missed access-token in request header"
//            )
//        }
//        val userId = decodeToken(token)["userId"] ?: null
        return mapOf("userId" to 10)
    }
}