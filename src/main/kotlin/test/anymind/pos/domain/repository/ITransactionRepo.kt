package test.anymind.pos.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import test.anymind.pos.domain.entity.TransactionEntity

interface ITransactionRepo: JpaRepository<TransactionEntity, Int> {
}