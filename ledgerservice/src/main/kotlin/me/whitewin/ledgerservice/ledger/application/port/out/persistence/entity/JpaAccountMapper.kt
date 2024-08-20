package me.whitewin.ledgerservice.ledger.application.port.out.persistence.entity

import me.whitewin.ledgerservice.ledger.domain.Account
import org.springframework.stereotype.Component

@Component
class JpaAccountMapper {

    fun mapToDomainEntity(jpaAccountEntity: JpaAccountEntity): Account {
        return Account(
            id = jpaAccountEntity.id!!,
            name = jpaAccountEntity.name
        )
    }
}