package com.doogie.entitypractice.chapter2

import java.time.Instant
import java.util.Date
import javax.persistence.CascadeType
import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Embeddable
import javax.persistence.Entity
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToOne
import javax.persistence.MapKey
import javax.persistence.MapKeyClass
import javax.persistence.MapKeyColumn
import javax.persistence.MapKeyEnumerated
import javax.persistence.MapKeyTemporal
import javax.persistence.OneToMany
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Transient

enum class PhoneType {
    LAND_LINE,
    MOBILE
}


@Entity(name = "MappedPerson")
data class MappedPerson(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @JvmSuppressWildcards
    @ElementCollection
    @CollectionTable(name = "phone_register")
    @Column(name = "since")
    var phoneRegister: Map<MappedPhone, Date> = mapOf(),

    @ElementCollection
    @JvmSuppressWildcards
    @get:CollectionTable(
        name = "call_register",
        joinColumns = [JoinColumn(name = "person_id")]
    )
    @MapKeyColumn(name = "call_timestamp_epoch")
    @MapKeyClass(MobilePhone::class)
    @Column(name = "call_register")
    var callRegister: Map<PhoneNumber, Int> = mapOf(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JvmSuppressWildcards
    @JoinTable(
        name = "uni_mapped_phone_register",
        joinColumns = [JoinColumn(name = "phone_id")],
        inverseJoinColumns = [JoinColumn(name = "person_id")]
    )
    @MapKey(name = "since")
    @MapKeyTemporal(TemporalType.TIMESTAMP)
    var uniMappedPhoneRegister: Map<Instant, UniMappedPhone> = mapOf(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "mappedPerson")
    @JvmSuppressWildcards
    @MapKey(name = "type")
    @MapKeyEnumerated
    var biMappedPhoneRegister: Map<PhoneType, BiMappedPhone> = mapOf()
) {
    @Transient
    fun addPhone(phone: UniMappedPhone) {
        uniMappedPhoneRegister = uniMappedPhoneRegister.plus(Pair(phone.since!!, phone))
    }

    @Transient
    fun addPhone(phone: BiMappedPhone) {
        biMappedPhoneRegister = biMappedPhoneRegister.plus(Pair(phone.type!!, phone))
    }
}

@Embeddable
data class MappedPhone(
    @Enumerated
    var type: PhoneType? = null,
    @Column(name = "number")
    var number: String? = null
)

@Entity(name = "UniMappedPhone")
data class UniMappedPhone(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var type: PhoneType? = null,
    @Column(name = "number")
    var number: String? = null,
    var since: Instant? = null
)

@Entity(name = "BiMappedPhone")
data class BiMappedPhone(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var type: PhoneType? = null,
    @Column(name = "number")
    var number: String? = null,
    var since: Instant? = null,
    @ManyToOne
    var mappedPerson: MappedPerson? = null
)

interface PhoneNumber {
    fun get(): String
}

@Embeddable
data class MobilePhone(
    @Column(name = "country_code")
    var countryCode: String? = null,
    @Column(name = "operator_code")
    var operatorCode: String? = null,
    @Column(name = "subscriber_code")
    var subscriberCode: String? = null
) : PhoneNumber {
    override fun get(): String {
        return String.format(
            "%s-%s-%s",
            countryCode,
            operatorCode,
            subscriberCode
        )
    }

    companion object {
        fun fromString(phoneNumber: String): PhoneNumber {
            val tokens = phoneNumber.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (tokens.size != 3) {
                throw IllegalArgumentException("invalid phone number: $phoneNumber")
            }
            var i = 0
            return MobilePhone(
                tokens[i++],
                tokens[i++],
                tokens[i]
            )
        }
    }
}
