package equidiaryDB.services

import io.javalin.http.Context

fun Context.getHorseUuid() = pathParam("horseUuid")
