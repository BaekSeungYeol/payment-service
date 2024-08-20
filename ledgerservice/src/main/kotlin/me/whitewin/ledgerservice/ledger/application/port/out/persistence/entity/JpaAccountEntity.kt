package me.whitewin.ledgerservice.ledger.application.port.out.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "accounts")
class JpaAccountEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String
)