package com.doogie.entitypractice.model

import org.hibernate.annotations.Persister
import org.hibernate.persister.collection.CollectionPersister
import org.hibernate.persister.entity.EntityPersister
import java.util.HashSet
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany


@Entity
@Persister(impl = EntityPersister::class)
class Author {
    @Id
    var id: Int? = null

    @OneToMany(mappedBy = "author")
    @Persister(impl = CollectionPersister::class)
    var books: MutableSet<PersistBook> = HashSet()

    fun addBook(book: PersistBook) {
        this.books.add(book)
        book.author = this
    }
}

@Entity
@Persister(impl = EntityPersister::class)
class PersistBook {
    @Id
    var id: Int? = null

    var title: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var author: Author? = null
}