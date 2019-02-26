package com.doogie.entitypractice.model

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Transient

@Entity(name = "Library")
@Table(
    catalog = "public"
)
class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long = 0

    private var name: String = ""

    @OneToMany(cascade = [ CascadeType.ALL ])
    @JoinColumn(name = "book_id")
    private var _books: MutableSet<Book> = HashSet()

    @get:Transient
    var books: MutableSet<Book>
        get() = _books
        set(value) {
            _books = value
        }
}