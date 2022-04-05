package equidiaryDB.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Professional @JsonCreator constructor(
    @JsonProperty("uuid") @get:JsonProperty val uuid: String,
    @JsonProperty("firstName") @get:JsonProperty val firstName: String,
    @JsonProperty("lastName") @get:JsonProperty val lastName: String,
    @JsonProperty("profession") @get:JsonProperty val profession: String,
)