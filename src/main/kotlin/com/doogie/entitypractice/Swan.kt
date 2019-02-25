package com.doogie.entitypractice

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Swan(@Id
           @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null) {

    override fun toString(): String {
        return "Swan(id=$id)"
    }
}
