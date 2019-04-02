package com.doogie.entitypractice.chapter2

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany


@Entity(name = "Person")
data class Person(
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   var id: Long? = null,
   @OneToMany(cascade = [CascadeType.ALL])
   var phones: Set<Phone> = setOf()
)

@Entity(name = "Phone")
data class Phone(
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   var id: Long? = null,
   var type: String? = null,
   @Column(name = "number")
   var number: String? = null
)
