package test.anymind.pos.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import test.anymind.pos.domain.entity.TransactionEntity
import java.time.LocalDateTime

interface ITransactionRepo : JpaRepository<TransactionEntity, Int> {
    @Query(
        """
        select new test.anymind.pos.domain.repository.HourlySaleReport(DATE_FORMAT(dateTime, '%Y-%m-%d %H:00:00'), sum(price), sum(point))
        from transaction
        where userId = :userId and dateTime between :startDate and :endDate
        group by DATE_FORMAT(dateTime, '%Y-%m-%d %H:00:00')
    """
    )
    fun findHourlySaleReport(
        @Param("userId") userId: Int,
        @Param("startDate") startDateTime: LocalDateTime,
        @Param("endDate") endDateTime: LocalDateTime
    ): List<HourlySaleReport>
}

data class HourlySaleReport(
    val date_time: String,
    val sales: Double,
    val points: Double
)