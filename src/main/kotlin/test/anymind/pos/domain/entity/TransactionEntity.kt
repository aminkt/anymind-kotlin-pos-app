package test.anymind.pos.domain.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class TransactionEntity(
    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int?,
    @Column(nullable = false)
    var price: Double? = null,
    @Column(nullable = false)
    var point: Double? = null,
    @Column()
    var dateTime: LocalDateTime = LocalDateTime.now(),
    @Column(columnDefinition = "json")
    var additionalItem: String? = null
)