package equidiaryDB

import equidiaryDB.config.ConfigurationFile

open class GenericTestCase {
    companion object {
        init {
            ConfigurationFile.givenDefault()
        }
    }
}
