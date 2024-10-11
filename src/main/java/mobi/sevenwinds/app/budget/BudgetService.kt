package mobi.sevenwinds.app.budget

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobi.sevenwinds.app.author.AuthorEntity
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object BudgetService {
    suspend fun addRecord(param: BudgetAuthorParam, body: BudgetRecord):
            BudgetRecordFio = withContext(Dispatchers.IO) {
        transaction {
            val authorEntity = AuthorEntity[param.authorId]

            val entity = BudgetEntity.new {
                this.year = body.year
                this.month = body.month
                this.amount = body.amount
                this.type = body.type
                this.authorEntity = authorEntity
            }

            return@transaction entity.toResponseFio()
        }
    }

    suspend fun getYearStats(param: BudgetYearParam): BudgetYearStatsResponse = withContext(Dispatchers.IO) {
        transaction {
            var query = BudgetTable
                .select { BudgetTable.year eq param.year }

            val total = query.count()

            val data = BudgetEntity.wrapRows(query).map {
                it.toResponseFio()
            }

            val sumByType = data.groupBy { it.type.name }.mapValues { it.value.sumOf { v -> v.amount } }

            query = query.limit(param.limit, param.offset)

            var sortedData = BudgetEntity.wrapRows(query).map { it.toResponseFio() }
                .sortedWith(compareBy<BudgetRecordFio> { it.month }.thenByDescending { it.amount })
            if (param.fio.isNotEmpty()) {
                sortedData = sortedData.filter { n -> n.fio == param.fio }
            }

            return@transaction BudgetYearStatsResponse(
                total = total,
                totalByType = sumByType,
                items = sortedData
            )
        }
    }
}