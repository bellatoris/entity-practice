package com.doogie.entitypractice.chapter2

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.JpaContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@RunWith(SpringRunner::class)
@SpringBootTest
class BagsTest {
    @Autowired
    lateinit var jpaContext: JpaContext

    fun <T> getEntityManager(clazz: Class<T>) =
        jpaContext.getEntityManagerByManagedType(clazz)

    @Test
    @Transactional
    fun hi() {
        val phone2 = Phone2()
        getEntityManager(phone2::class.java).persist(phone2)
        getEntityManager(phone2::class.java).flush()

        val person2 = Person2(phone2, Instant.now())
        getEntityManager(person2::class.java).persist(person2)
        getEntityManager(person2::class.java).flush()
    }

    @Test
    @Transactional
    @Rollback(false)
    fun insert() {
        val person = Person()
        person.phones = person.phones.plus(Phone(null, "landline", "124"))
        person.phones = person.phones.plus(Phone(null, "mobile", "124"))
        getEntityManager(person::class.java).persist(person)
        getEntityManager(person::class.java).flush()
    }

    @Test
    @Transactional
    @Rollback(false)
    fun duplicate() {
        val entityManager = getEntityManager(Person::class.java)

        val person = entityManager.find(Person::class.java, 1L)
        val phone = person.phones.first()
        person.phones = person.phones.plus(phone)
        entityManager.flush()
    }

    @Test
    @Transactional
    @Rollback(false)
    fun delete() {
        val entityManager = getEntityManager(Person::class.java)

        val person = entityManager.find(Person::class.java, 1L)
        val phone = entityManager.find(Phone::class.java, 1L)
        person.phones = person.phones.minus(phone)
        entityManager.flush()
    }
}
