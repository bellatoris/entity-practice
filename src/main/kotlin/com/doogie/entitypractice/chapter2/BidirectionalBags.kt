package com.doogie.entitypractice.chapter2

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Transient


@Entity(name = "BiPerson")
data class BiPerson(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "biPerson", orphanRemoval = true)
    var biPhones: MutableList<BiPhone> = mutableListOf()
) {
    @Transient
    fun addBiPhone(biPhone: BiPhone) {
        biPhones.add(biPhone)
        biPhone.biPerson = this
    }

    @Transient
    fun removeBiPhone(biPhone: BiPhone) {
        biPhones.remove(biPhone)
        biPhone.biPerson = null
    }
}

@Entity(name = "BiPhone")
data class BiPhone(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var type: String? = null,

    @Column(name = "number")
    var number: String? = null,

    @ManyToOne
    var biPerson: BiPerson? = null
)
