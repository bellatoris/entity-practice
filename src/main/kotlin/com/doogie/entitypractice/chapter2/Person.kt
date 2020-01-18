package com.doogie.entitypractice.chapter2

import java.io.Serializable
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.ManyToOne

@Entity
@IdClass(Person2Id::class)
data class Person2(
    @get:Id
    @get:ManyToOne
    var phone: Phone2? = null,

    @get:Id
    var createdAt: Instant
) {

}

@Entity
data class Phone2(
    @get:Id
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {

}


data class Person2Id(
    var phone: Long? = null,
    var createdAt: Instant = Instant.EPOCH
) : Serializable {

}