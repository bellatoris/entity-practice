package com.doogie.entitypractice.chapter2

import org.hibernate.annotations.ListIndexBase
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OrderBy
import javax.persistence.OrderColumn


@Entity(name = "OrderedPerson")
data class OrderedPerson(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @OneToMany(cascade = [CascadeType.ALL])
    @OrderBy("number")
    var orderedPhones: List<OrderedPhone> = listOf(),

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "orderedPerson")
    @org.hibernate.annotations.OrderBy(
        clause = "CHAR_LENGTH(name) DESC"
    )
    var orderedArticles: List<OrderedArticle> = listOf()
)

@Entity(name = "OrderedPhone")
data class OrderedPhone(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var type: String? = null,

    @Column(name = "number")
    var number: String? = null
)

@Entity(name = "OrderedArticle")
data class OrderedArticle(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String? = null,
    var content: String? = null,
    @ManyToOne
    var orderedPerson: OrderedPerson? = null
)

object OrderColumn {
    @Entity(name = "OrderedColumnPerson")
    data class OrderedColumnPerson(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "orderedColumnPerson")
        @OrderColumn(name = "order_id")
        @get:ListIndexBase(100)
        @set:ListIndexBase(100)
        @ListIndexBase(100)
        var orderedColumnPhones: List<OrderedColumnPhone> = listOf()
    )

    @Entity(name = "OrderedColumnPhone")
    data class OrderedColumnPhone(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        var type: String? = null,
        @Column(name = "number")
        var number: String? = null,
        @ManyToOne
        var orderedColumnPerson: OrderedColumnPerson? = null
    )
}