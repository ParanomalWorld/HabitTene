/*
 * Copyright (C) 2016-2021 Álinson Santos Xavier <git@axavier.org>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.dengine.habitrack.sync.app

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.dengine.habitrack.sync.*

data class LinkRegisterRequestData(
    val syncKey: String,
)
fun LinkRegisterRequestData.toJson(): String = defaultMapper.writeValueAsString(this)

fun Routing.links(app: SyncApplication) {
    post("/links") {
        try {
            val data = call.receive<LinkRegisterRequestData>()
            val link = app.server.registerLink(data.syncKey)
            call.respond(HttpStatusCode.OK, link)
        } catch (e: ServiceUnavailable) {
            call.respond(HttpStatusCode.ServiceUnavailable)
        }
    }
    get("/links/{id}") {
        try {
            val id = call.parameters["id"]!!
            val link = app.server.getLink(id)
            call.respond(HttpStatusCode.OK, link)
        } catch (e: ServiceUnavailable) {
            call.respond(HttpStatusCode.ServiceUnavailable)
        } catch (e: KeyNotFoundException) {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}
