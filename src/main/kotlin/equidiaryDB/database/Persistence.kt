package equidiaryDB.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object Horses : Table("horse") {
    val uuid = varchar("uuid", 50).uniqueIndex()
    val name = varchar("horse_name", 150)
    val height = integer("height")
    val weight = integer("weight").nullable()
    val birthDate = date("birth_date")
    val userUuid = varchar("userUuid", 50).references(Users.uuid)
}

object Appointments : Table("appointment") {
    val uuid = varchar("uuid", 50).uniqueIndex()
    val date = date("appointment_date")
    val type = varchar("appointment_type", 50)
    val comment = varchar("comment", 512)
    val horseUuid = varchar("horseUuid", 50).references(Horses.uuid)
    //file name (comme ça on peut faire une vision fichier avec les noms d'origines
    //chemin sur le serveur Uuid UUID.randomUUID().toString()
}

object Users : Table("equidiary_user") {
    val uuid = varchar("uuid", 50).uniqueIndex()
    val userName = varchar("user_name", 100).uniqueIndex()
    val password = binary("password", 100)
}

object Professionals : Table("professional") {
    val uuid = varchar("uuid", 50).uniqueIndex()
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val profession = varchar("profession", 50)
}

object HorseProfessionals : Table("horse_professional") {
    val proUuid = varchar("pro_uuid", 50).uniqueIndex().references(Professionals.uuid)
    val horseUuid = varchar("horse_uuid", 50).uniqueIndex().references(Appointments.uuid)
}