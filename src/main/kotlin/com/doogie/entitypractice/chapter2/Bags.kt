package com.doogie.entitypractice.chapter2

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany


@Entity(name = "Person")
data class Person(
   @Id
   var id: Long? = null,
   @OneToMany(cascade = [CascadeType.ALL])
   var phones: List<Phone> = listOf()
)

@Entity(name = "Phone")
data class Phone(
   @Id
   var id: Long? = null,
   var type: String? = null,
   @Column(name = "number")
   var number: String? = null
)
