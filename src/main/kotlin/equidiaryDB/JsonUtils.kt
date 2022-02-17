package equidiaryDB

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.introspect.VisibilityChecker
import com.fasterxml.jackson.databind.module.SimpleModule
import equidiaryDB.JsonUtils.ZONED_DATE_TIME_FORMATTER
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.*
import java.util.*

interface JsonSerializable {

    fun toJson(): String = JsonUtils.toJson(this)
}

data class StringMessageJsonSerializable @JsonCreator constructor(
    @JsonProperty("message") @get:JsonProperty val message: String,
) : JsonSerializable

data class StringSetJsonSerializable @JsonCreator constructor(
    @JsonProperty("items") @get:JsonProperty val items: Set<String>,
) : JsonSerializable

data class LocalDateJsonSerializable @JsonCreator constructor(
    @JsonProperty("today") @get:JsonProperty val today: LocalDate,
) : JsonSerializable

object JsonUtils {
    val LOCALE_DATE_TIME_FORMATTER: DateTimeFormatter = ofPattern("yyyy-MM-dd HH:mm:ss")
    val ZONED_DATE_TIME_FORMATTER: DateTimeFormatter = ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    val mongoModule: SimpleModule = SimpleModule()
        .addSerializer(LocalDate::class.java, MongoLocalDateSerializer())
        .addDeserializer(LocalDate::class.java, MongoLocalDateDeserializer())
        .addSerializer(OffsetDateTime::class.java, MongoOffsetDateTimeSerializer())
        .addDeserializer(OffsetDateTime::class.java, MongoOffsetDateTimeDeserializer())

    private val defaultModule: SimpleModule = SimpleModule()
        .addSerializer(LocalDate::class.java, LocalDateSerializer())
        .addDeserializer(LocalDate::class.java, LocalDateDeserializer())
        .addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer())
        .addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer())
        .addSerializer(OffsetDateTime::class.java, OffsetDateTimeSerializer())
        .addDeserializer(OffsetDateTime::class.java, OffsetDateTimeDeserializer())
        .addSerializer(ZonedDateTime::class.java, ZonedDateTimeSerializer())

    val visibilityChecker: VisibilityChecker<*> = ObjectMapper().deserializationConfig.defaultVisibilityChecker
        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
        .withFieldVisibility(JsonAutoDetect.Visibility.NONE)
        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
        .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)

    val objectMapper: ObjectMapper
        get() {
            val objectMapper = ObjectMapper()
            objectMapper.registerModule(defaultModule)
            objectMapper.setVisibility(visibilityChecker)
            return objectMapper
        }

    fun <T> toJson(rawObject: T): String {

        return objectMapper.writeValueAsString(rawObject)
    }

    fun <T> fromJson(json: String, clazz: Class<T>): T {

        try {
            return objectMapper.readValue(json, clazz)
        } catch (e: IOException) {
            throw JsonException(e)
        }
    }

    fun toJsonNode(obj: JsonSerializable): JsonNode {

        return fromJson(obj.toJson(), JsonNode::class.java)
    }

    fun fromJsonFile(filePath: Path): JsonNode {

        try {
            val fileContent = String(Files.readAllBytes(filePath))
            return objectMapper.readTree(fileContent)
        } catch (e: IOException) {
            throw JsonException(e)
        }
    }

    fun <T> fromJsonFile(path: Path, clazz: Class<T>): T {

        val jsonNode = fromJsonFile(path)
        return fromJson(jsonNode.toString(), clazz)
    }

    fun fromInputStream(inputStream: InputStream): JsonNode {

        return objectMapper.readTree(inputStream)
    }
}

class JsonException(cause: Throwable) : Exception(cause)

class LocalDateSerializer : JsonSerializer<LocalDate>() {

    override fun serialize(localDate: LocalDate, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) {

        jsonGenerator.writeString(localDate.format(ISO_LOCAL_DATE))
    }
}

class LocalDateDeserializer : JsonDeserializer<LocalDate>() {

    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): LocalDate {

        return LocalDate.parse(jsonParser.valueAsString, ISO_LOCAL_DATE)
    }
}

class LocalDateTimeSerializer : JsonSerializer<LocalDateTime>() {

    override fun serialize(
        localDateTime: LocalDateTime,
        jsonGenerator: JsonGenerator,
        serializerProvider: SerializerProvider,
    ) {

        jsonGenerator.writeString(localDateTime.format(JsonUtils.LOCALE_DATE_TIME_FORMATTER))
    }
}

class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime>() {

    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): LocalDateTime {

        return LocalDateTime.parse(jsonParser.valueAsString, JsonUtils.LOCALE_DATE_TIME_FORMATTER)
    }
}

class ZonedDateTimeSerializer : JsonSerializer<ZonedDateTime>() {
    override fun serialize(p0: ZonedDateTime, p1: JsonGenerator, p2: SerializerProvider) {
        p1.writeString(p0.format(ZONED_DATE_TIME_FORMATTER))
    }
}

class OffsetDateTimeSerializer : JsonSerializer<OffsetDateTime>() {

    override fun serialize(
        offsetDateTime: OffsetDateTime,
        jsonGenerator: JsonGenerator,
        serializerProvider: SerializerProvider,
    ) {

        jsonGenerator.writeString(offsetDateTime.format(ISO_DATE_TIME))
    }
}

class OffsetDateTimeDeserializer : JsonDeserializer<OffsetDateTime>() {

    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): OffsetDateTime {

        return OffsetDateTime.parse(jsonParser.valueAsString, ISO_DATE_TIME)
    }
}

class MongoLocalDateSerializer : JsonSerializer<LocalDate>() {

    override fun serialize(value: LocalDate?, generator: JsonGenerator, provider: SerializerProvider) {

        val zonedDateTime = value?.atStartOfDay()?.atZone(ZoneOffset.UTC) ?: return generator.writeObject(null)
        generator.writeObject(Date.from(zonedDateTime.toInstant()))
    }
}

class MongoLocalDateDeserializer : JsonDeserializer<LocalDate?>() {

    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): LocalDate? {

        val date: Date = jsonParser.readValueAs(Date::class.java) ?: return null
        return LocalDate.ofInstant(date.toInstant(), ZoneOffset.UTC)
    }
}

class MongoOffsetDateTimeSerializer : JsonSerializer<OffsetDateTime>() {

    override fun serialize(value: OffsetDateTime?, gen: JsonGenerator, provider: SerializerProvider?) {

        val zonedDateTime = value ?: return gen.writeObject(null)
        gen.writeObject(Date.from(zonedDateTime.toInstant()))
    }
}

class MongoOffsetDateTimeDeserializer : JsonDeserializer<OffsetDateTime?>() {

    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext?): OffsetDateTime {

        val date: Date = jsonParser.readValueAs(Date::class.java)
        return OffsetDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC)
    }
}
