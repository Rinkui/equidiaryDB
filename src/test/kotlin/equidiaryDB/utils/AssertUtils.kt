package equidiaryDB.utils

import com.mashape.unirest.http.HttpResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

fun <T> HttpResponse<T>.isBadRequest() = assertEquals(400, code)
fun <T> HttpResponse<T>.isForbidden() = assertEquals(503, code)
fun <T> HttpResponse<T>.resourceNotFound() = assertEquals(404, code)

fun HttpResponse<String>.bodyIs(body: String) = assertEquals(body, this.body)
fun HttpResponse<String>.bodyLike(body: Regex) {
    println(this.body)
    assertTrue(this.body.contains(body))
}

fun <T> HttpResponse<T>.isOk(): HttpResponse<T> {
    assertEquals(200, code)
    return this
}
