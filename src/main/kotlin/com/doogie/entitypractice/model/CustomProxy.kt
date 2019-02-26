package com.doogie.entitypractice.model

import org.hibernate.annotations.Proxy
import javax.persistence.Entity
import javax.persistence.Id

interface Identifiable {
    var id: Long?
}

@Entity(name = "ProxiedBook")
@Proxy(proxyClass = Identifiable::class)
class ProxiedBook : Identifiable {
    @Id
    override var id: Long? = null

    var title: String? = null

    var author: String? = null
}
