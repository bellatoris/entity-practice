package com.doogie.entitypractice.chapter2

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "ArraysPerson")
data class ArraysPerson(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var phones: Array<String> = arrayOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArraysPerson

        if (id != other.id) return false
        if (!phones.contentEquals(other.phones)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + phones.contentHashCode()
        return result
    }
}
