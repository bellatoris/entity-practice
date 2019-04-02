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
class BidirectionalBagsTest {
    @Autowired
    lateinit var jpaContext: JpaContext

    fun <T> getEntityManager(clazz: Class<T>) =
        jpaContext.getEntityManagerByManagedType(clazz)

    @Test
    @Transactional
    @Rollback(false)
    fun insert() {
        val biPerson = BiPerson()
        biPerson.addBiPhone(BiPhone(null, "landline", "124"))
        biPerson.addBiPhone(BiPhone(null, "mobile", "124"))
        getEntityManager(biPerson::class.java).persist(biPerson)
        getEntityManager(biPerson::class.java).flush()
    }

    @Test
    @Transactional
    @Rollback(false)
    fun duplicate() {
        val entityManager = getEntityManager(BiPerson::class.java)

        val biPerson = entityManager.find(BiPerson::class.java, 1L)
        val biPhone = biPerson.biPhones.first()
        biPerson.addBiPhone(biPhone)
        entityManager.flush()
    }

    @Test
    @Transactional
    @Rollback(false)
    fun delete() {
        val entityManager = getEntityManager(BiPerson::class.java)

        val biPerson = entityManager.find(BiPerson::class.java, 1L)
        val biPhone = entityManager.find(BiPhone::class.java, 1L)
        biPerson.removeBiPhone(biPhone)
        entityManager.flush()
    }
}
