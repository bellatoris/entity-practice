package com.doogie.entitypractice.chapter2

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.JpaContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest
class OrderedListsTest {
    @Autowired
    lateinit var jpaContext: JpaContext

    fun <T> getEntityManager(clazz: Class<T>) =
        jpaContext.getEntityManagerByManagedType(clazz)

    @Test
    @Transactional
    @Rollback(false)
    fun insert() {
        val orderedPerson = OrderedPerson()
        orderedPerson.orderedPhones = orderedPerson.orderedPhones.plus(OrderedPhone(null, "landline", "124"))
        orderedPerson.orderedPhones = orderedPerson.orderedPhones.plus(OrderedPhone(null, "mobile", "234"))
        getEntityManager(orderedPerson::class.java).persist(orderedPerson)
        getEntityManager(orderedPerson::class.java).flush()
    }
    @Test
    @Transactional
    @Rollback(false)
    fun insertArticle() {
        val orderedPerson = OrderedPerson()
        orderedPerson.orderedArticles = orderedPerson.orderedArticles.plus(
            OrderedArticle(name = "mac", content = "mbp", orderedPerson = orderedPerson)
        )
        getEntityManager(orderedPerson::class.java).persist(orderedPerson)
        getEntityManager(orderedPerson::class.java).flush()
        getEntityManager(orderedPerson::class.java).find(OrderedPerson::class.java, 1L).orderedArticles.first().name
    }

    @Test
    @Transactional
    @Rollback(false)
    fun get() {
        val entityManager = getEntityManager(OrderedPerson::class.java)

        val orderedPerson = entityManager.find(OrderedPerson::class.java, 1L)
        val orderedPhone = orderedPerson.orderedPhones.first()
        entityManager.flush()
    }

    @Test
    @Transactional
    @Rollback(false)
    fun duplicate() {
        val entityManager = getEntityManager(OrderedPerson::class.java)

        val orderedPerson = entityManager.find(OrderedPerson::class.java, 1L)
        val orderedPhone = orderedPerson.orderedPhones.first()
        orderedPerson.orderedPhones = orderedPerson.orderedPhones.plus(orderedPhone)
        entityManager.flush()
    }

    @Test
    @Transactional
    @Rollback(false)
    fun delete() {
        val entityManager = getEntityManager(OrderedPerson::class.java)

        val orderedPerson = entityManager.find(OrderedPerson::class.java, 1L)
        val orderedPhone = entityManager.find(OrderedPhone::class.java, 1L)
        orderedPerson.orderedPhones = orderedPerson.orderedPhones.minus(orderedPhone)
        entityManager.flush()
    }

    @Test
    @Transactional
    @Rollback(false)
    fun `OrderColumn insert`() {
        val orderedPerson = OrderColumn.OrderedColumnPerson()
        orderedPerson.orderedColumnPhones = orderedPerson.orderedColumnPhones.plus(
            OrderColumn.OrderedColumnPhone(null, "landline", "124").also {
                it.orderedColumnPerson = orderedPerson
            }
        )
        orderedPerson.orderedColumnPhones = orderedPerson.orderedColumnPhones.plus(
            OrderColumn.OrderedColumnPhone(null, "mobile", "234").also {
                it.orderedColumnPerson = orderedPerson
            }
        )
        getEntityManager(OrderColumn.OrderedColumnPerson::class.java).persist(orderedPerson)
        getEntityManager(OrderColumn.OrderedColumnPerson::class.java).flush()
        getEntityManager(OrderColumn.OrderedColumnPerson::class.java).find(OrderColumn.OrderedColumnPerson::class.java, 1L).orderedColumnPhones.first()
    }
}
