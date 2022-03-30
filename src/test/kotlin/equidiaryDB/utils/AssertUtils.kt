package equidiaryDB.utils

import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.request.GetRequest
import com.mashape.unirest.request.HttpRequestWithBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

fun <T> HttpResponse<T>.isBadRequest(): HttpResponse<T> {
    assertEquals(400, code)
    return this
}

fun <T> HttpResponse<T>.isForbidden() = assertEquals(503, code)
fun <T> HttpResponse<T>.resourceNotFound() = assertEquals(404, code)
fun HttpResponse<String>.withBody(body: String) = assertEquals(body, this.body)
fun HttpResponse<String>.bodyLike(body: Regex) {
    println(this.body)
    assertTrue(this.body.contains(body))
}

fun <T> HttpResponse<T>.isOk(): HttpResponse<T> {
    assertEquals(200, code)
    return this
}

fun HttpRequestWithBody.withDefaultHeaders() = header("Authorization", "Bearer")!! //TODO mettre un token
fun GetRequest.withDefaultHeaders() = header("Authorization", "Bearer")!! //TODO mettre un token
