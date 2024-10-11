package mobi.sevenwinds.app.author

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object AuthorTable : IntIdTable("author", columnName = "id") {
    val fio = text("fio")
    val date = date("date")
}

class AuthorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AuthorEntity>(AuthorTable)
    var fio by AuthorTable.fio
    var date by AuthorTable.date

    fun toResponse(): AuthorRecordDate {
        return AuthorRecordDate(fio, date)
    }
}