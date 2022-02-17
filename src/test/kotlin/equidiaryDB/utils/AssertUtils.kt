package equidiaryDB.utils

import com.mashape.unirest.http.HttpResponse
import org.junit.jupiter.api.Assertions

fun <T> HttpResponse<T>.isBadRequest() = Assertions.assertEquals(400, code)

fun <T> HttpResponse<T>.resourceNotFound() = Assertions.assertEquals(404, code)

fun HttpResponse<String>.bodyIs(body: String) = Assertions.assertEquals(body, this.body)

fun <T> HttpResponse<T>.isOk(): HttpResponse<T> {
    Assertions.assertEquals(200, code)
    return this
}
