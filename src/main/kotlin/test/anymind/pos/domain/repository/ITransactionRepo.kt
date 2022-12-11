package test.anymind.pos.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import test.anymind.pos.domain.entity.TransactionEntity
import java.time.LocalDateTime

interface ITransactionRepo : JpaRepository<TransactionEntity, Int> {

    // Below query is using dto instead of list of maps, more readable but cause problem in junit testing

//    @Query("""
//        select new test.anymind.pos.domain.repository.HourlySaleReportDTO(DATE_FORMAT(dateTime, '%Y-%m-%d %H:00:00'), sum(price), sum(point))
//        from transaction
//        where userId = :userId and dateTime between :startDate and :endDate
//        group by DATE_FORMAT(dateTime, '%Y-%m-%d %H:00:00')
//    """)

    @Query(value = """
        select DATE_FORMAT(date_time, '%Y-%m-%d %H:00:00') as date, sum(price) as sales, sum(point) as points
        from transaction
        where user_id = :userId and date_time between :startDate and :endDate
        group by DATE_FORMAT(date_time, '%Y-%m-%d %H:00:00')
    """, nativeQuery = true)
    fun findHourlySaleReport(
        @Param("userId") userId: Int,
        @Param("startDate") startDateTime: LocalDateTime,
        @Param("endDate") endDateTime: LocalDateTime
    ): List<Map<String, Any>>
}

// As mentioned in up, this is the related dtop
//data class HourlySaleReportDTO(
//    val date_time: String,
//    val sales: Double,
//    val points: Double
//)