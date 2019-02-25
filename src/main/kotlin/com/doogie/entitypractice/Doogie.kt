package com.doogie.entitypractice

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Doogie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?

    private constructor(id: Long? = null) {
        this.id = id
    }

//    fun getId(): Long {
//       return this.id
//    }
//
//    fun setId(id: Long) {
//        this.id = id
//    }
    companion object {
        fun getInstance(id: Long? = null): Doogie {
            return Doogie(id)
        }
    }

    override fun toString(): String {
        return "Doogie(id=$id)"
    }
}
