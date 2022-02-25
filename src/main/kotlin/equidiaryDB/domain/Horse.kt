package equidiaryDB.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class Horse @JsonCreator constructor(
    @JsonProperty("name") @get:JsonProperty val name: String,
    @JsonProperty("height") @get:JsonProperty val height: Int,
    @JsonProperty("weight") @get:JsonProperty val weight: Int?,
    @JsonProperty("uuid") @get:JsonProperty val uuid: String,
    @JsonProperty("birthDate") @get:JsonProperty val birthDate: LocalDate,
)