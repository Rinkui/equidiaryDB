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
fun <T> HttpResponse<T>.isResourceNotFound() = assertEquals(404, code)
fun HttpResponse<String>.withBody(body: String) = assertEquals(body, this.body)
fun HttpResponse<String>.bodyLike(body: Regex) {
    println(this.body)
    assertTrue(this.body.contains(body))
}

fun <T> HttpResponse<T>.isOk(): HttpResponse<T> {
    assertEquals(200, code)
    return this
}

const val token =
    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXJpZSIsImV4cCI6NDAwMTY0ODY1ODAyMn0.A8dgi0B8zuCS9ZITxy47lYfajUnMYZxEM5E5j5MrLGLxB9pX8zQ27boCIkfuWCAJfoVc9S1gz_ziTTmUh03V4g"

fun HttpRequestWithBody.withDefaultHeaders() = header("Authorization", "Bearer $token")!!
fun GetRequest.withDefaultHeaders() = header("Authorization", "Bearer $token")!!
