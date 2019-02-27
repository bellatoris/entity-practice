package com.doogie.entitypractice

import com.doogie.differenet.DifferentPackageDoogie
import com.doogie.differenet.DifferentPackageDoogieRepository
import com.doogie.entitypractice.low.LowPackageDoogie
import com.doogie.entitypractice.low.LowPackageDoogieRepository
import com.doogie.entitypractice.model.PersistBook
import org.hibernate.testing.transaction.TransactionUtil.doInJPA
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.support.TransactionTemplate
import javax.persistence.Entity
import javax.persistence.EntityManagerFactory
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@SpringBootApplication
@EnableJpaRepositories("com.doogie")
@EntityScan("com.doogie")
class EntityPracticeApplication {
    @Entity
    class LowLevelDoogie(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
    ) {
        override fun toString(): String {
            return "LowLevelDoogie(id=$id)"
        }
    }
}

interface LowLevelDoogieRepository : JpaRepository<EntityPracticeApplication.LowLevelDoogie, Long>


fun main(args: Array<String>) {
    runApplication<EntityPracticeApplication>(*args).use {
        testPojo(it)
        testMapping(it)
    }
}

private fun testMapping(it: ConfigurableApplicationContext) {
    doInJPA({ it.beanFactory.getBean(EntityManagerFactory::class.java) }) { em ->
        val book = PersistBook()
        em.persist(book)
        em.flush()
        val book1 = em.find(PersistBook::class.java, 1L)
        val book2 = em.find(PersistBook::class.java, 1L)
        assert(book1 == book2)
    }
}

private fun testPojo(it: ConfigurableApplicationContext) {
    val doogie = Doogie.getInstance()
    val transactionTemplate = it
        .beanFactory
        .getBean(TransactionTemplate::class.java)

    transactionTemplate.execute { _ ->
        val savedDoogie = it.getBean(DoogieRepository::class.java).save(doogie)
        println(savedDoogie)
    }
    transactionTemplate.execute { _ ->
        val getDoogie = it.getBean(DoogieRepository::class.java).getOne(1)
        println(getDoogie)
    }

    val lowLevelDoogie = EntityPracticeApplication.LowLevelDoogie()
    val savedLowLevelDoogie = it.getBean(LowLevelDoogieRepository::class.java).save(lowLevelDoogie)
    println(savedLowLevelDoogie)

    val lowPackageDoogie = LowPackageDoogie.getInstance()
    val savedLowPackageDoogie = it.getBean(LowPackageDoogieRepository::class.java).save(lowPackageDoogie)
    println(savedLowPackageDoogie)

    val differentPackageDoogie = DifferentPackageDoogie.getInstance()
    val savedDifferentPackageDoogie = it.getBean(DifferentPackageDoogieRepository::class.java).save(differentPackageDoogie)
    println(savedDifferentPackageDoogie)
}
