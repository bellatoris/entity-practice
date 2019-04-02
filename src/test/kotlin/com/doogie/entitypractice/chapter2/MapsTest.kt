package com.doogie.entitypractice.chapter2

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.JpaContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import java.sql.Date
import java.time.Instant

@RunWith(SpringRunner::class)
@SpringBootTest
class MapsTest {
    @Autowired
    lateinit var jpaContext: JpaContext

    fun <T> getEntityManager(clazz: Class<T>) =
        jpaContext.getEntityManagerByManagedType(clazz)

    @Test
    @Transactional
    @Rollback(false)
    fun insert() {
        val mappedPerson = MappedPerson()
        mappedPerson.phoneRegister = mappedPerson.phoneRegister.plus(
            Pair(MappedPhone(PhoneType.LAND_LINE, "1234"), Date(Instant.now().toEpochMilli()))
        )
        mappedPerson.phoneRegister = mappedPerson.phoneRegister.plus(
            Pair(MappedPhone(PhoneType.MOBILE, "324"), Date(Instant.now().toEpochMilli()))
        )
        getEntityManager(mappedPerson.javaClass).persist(mappedPerson)
        getEntityManager(mappedPerson.javaClass).flush()
    }

    @Test
    @Transactional
    @Rollback(false)
    fun insert2() {
        val mappedPerson = MappedPerson()
        mappedPerson.callRegister = mappedPerson.callRegister.plus(
            Pair(MobilePhone("01", "324", "324"), 101)
        )
        mappedPerson.callRegister = mappedPerson.callRegister.plus(
            Pair(MobilePhone("02", "324", "324"), 102)
        )
        getEntityManager(mappedPerson.javaClass).persist(mappedPerson)
        getEntityManager(mappedPerson.javaClass).flush()
    }

    @Test
    @Transactional
    @Rollback(false)
    fun get() {
        val person = getEntityManager(MappedPerson::class.java).find(MappedPerson::class.java, 1L)
        assertEquals(2, person.callRegister.size)
        assertEquals(
            Integer.valueOf(101),
            person.callRegister[MobilePhone.fromString("01-324-324")]
        )
        assertEquals(
            Integer.valueOf(102),
            person.callRegister[MobilePhone.fromString("02-324-324")]
        )
    }

    @Test
    @Transactional
    @Rollback(false)
    fun duplicate() {
        val entityManager = getEntityManager(Person::class.java)
    }

    @Test
    @Transactional
    @Rollback(false)
    fun delete() {
        val entityManager = getEntityManager(Person::class.java)
    }
}
