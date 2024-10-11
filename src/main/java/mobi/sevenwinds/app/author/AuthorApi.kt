package mobi.sevenwinds.app.author

import com.papsign.ktor.openapigen.annotations.type.string.length.MinLength
import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.post
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import org.joda.time.DateTime

fun NormalOpenAPIRoute.author() {
    route("/author") {
        route("/add").post<Unit, AuthorRecordDate, AuthorRecord>(info("Добавить автора")) {
            param, body -> respond(AuthorService.addRecord(body.fio))
        }
    }
}

data class AuthorRecordDate(
    @MinLength(10) val fio: String,
    val date: DateTime
)

data class AuthorRecord(
    @MinLength(10) val fio: String
)