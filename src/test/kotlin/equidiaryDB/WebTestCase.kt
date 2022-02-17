package equidiaryDB

import com.mashape.unirest.http.HttpResponse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class WebTestCase : GenericTestCase() {
    companion object {
        private const val EQUIDIARYDB_PORT = "7001"
        private const val EQUIDIARYDB_IP = "localhost"
        const val EQUIDIARYDB_PATH = "http://$EQUIDIARYDB_IP:$EQUIDIARYDB_PORT"
        const val GET_HORSE_ENDPOINT = "/horse"
        private var response: HttpResponse<String>? = null
    }

    @BeforeEach
    fun setup() {
        EquidiaryDB.start()
    }

    @AfterEach
    fun teardown() {
        EquidiaryDB.stop()
        response = null
    }
}