package equidiaryDB.database

import equidiaryDB.EquidiaryDB
import org.junit.jupiter.api.Test

class DBMigrationTest {
    @Test
    fun toto() {
        givenEmptyDataBase()
        whenStartApp()
        thenAppStartCorrectly()
    }

    private fun thenAppStartCorrectly() {}

    private fun whenStartApp() {
        EquidiaryDB.start()
    }

    private fun givenEmptyDataBase() {}
}
