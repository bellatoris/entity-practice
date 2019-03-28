package com.doogie.entitypractice.chapter2

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.JpaContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest
class BagsTest {
    @Autowired
    lateinit var jpaContext: JpaContext

    fun <T> getEntityManager(clazz: Class<T>) =
        jpaContext.getEntityManagerByManagedType(clazz)

    @Test
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Rollback(false)
    fun test() {
        val person = Person(1L)
        person.phones = person.phones.plus(Phone(1L, "landline", "124"))
        person.phones = person.phones.plus(Phone(2L, "mobile", "124"))
        getEntityManager(person::class.java).persist(person)
        getEntityManager(person::class.java).flush()
    }
}
