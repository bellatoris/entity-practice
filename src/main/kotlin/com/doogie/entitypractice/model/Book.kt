package com.doogie.entitypractice.model

import org.hibernate.annotations.NaturalId
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "Book")
@Table(
    catalog = "public",
    schema = "hi",
    name = "book"
)
class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private val id: Long?
    var id: Long?

    var author: String = ""
    var title: String = ""

    @NaturalId
    var isbn: String = ""

    constructor(id: Long? = null) {
        this.id = id
    }

    override fun toString(): String {
        return "Book(id=$id, author='$author', title='$title')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (id != other.id) return false
        if (author != other.author) return false
        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + author.hashCode()
        result = 31 * result + title.hashCode()
        return result
    }
}

