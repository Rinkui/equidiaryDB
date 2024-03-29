package equidiaryDB.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class Appointment @JsonCreator constructor(
    @JsonProperty("date") @get:JsonProperty val date: LocalDate,
    @JsonProperty("type") @get:JsonProperty val type: String,
    @JsonProperty("comment") @get:JsonProperty val comment: String,
    @JsonProperty("uuid") @get:JsonProperty val uuid: String,
    @JsonProperty("horseUuid") @get:JsonProperty val horseUuid: String,
)