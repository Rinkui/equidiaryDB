package equidiaryDB

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class WebTestCase : GenericTestCase() {
    companion object {
        private const val EQUIDIARYDB_PORT = "7001"
        private const val EQUIDIARYDB_IP = "localhost"
        const val EQUIDIARYDB_PATH = "http://$EQUIDIARYDB_IP:$EQUIDIARYDB_PORT"
    }

    @BeforeEach
    open fun setup() {
        EquidiaryDB.start()
    }

    @AfterEach
    fun teardown() {
        EquidiaryDB.stop()
    }
}