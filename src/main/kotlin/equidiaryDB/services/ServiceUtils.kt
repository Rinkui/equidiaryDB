package equidiaryDB.services

import io.javalin.http.Context

fun Context.getHorseUuid() = pathParam("horseUuid")
fun Context.getUserUuid() = pathParam("userUuid")
