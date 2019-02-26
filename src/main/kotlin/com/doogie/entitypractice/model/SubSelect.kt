package com.doogie.entitypractice.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.GeneratedValue
import org.hibernate.annotations.Synchronize
import org.hibernate.annotations.Subselect

@Entity(name = "Client")
@Table(name = "client")
class Client {
    @Id
    var id: Long? = null

    @Column(name = "first_name")
    var firstName: String? = null

    @Column(name = "last_name")
    var lastName: String? = null
}

@Entity(name = "Account")
@Table(name = "account")
class Account {
    @Id
    var id: Long? = null

    @ManyToOne
    var client: Client? = null

    var description: String? = null
}

@Entity(name = "AccountTransaction")
@Table(name = "account_transaction")
class AccountTransaction {
    @Id
    @GeneratedValue
    var id: Long? = null

    @ManyToOne
    var account: Account? = null

    var cents: Int? = null

    var description: String? = null
}

@Entity(name = "AccountSummary")
@Subselect("""
    select
    	a.id as id,
    	concat(concat(c.first_name, ' '), c.last_name) as client_name,
    	sum(at.cents) as balance
    from account a
    join client c on c.id = a.client_id
    join account_transaction at on a.id = at.account_id
    group by a.id, client_name
    """)
@Synchronize("client", "account", "account_transaction")
class AccountSummary {
    @Id
    var id: Long? = null

    var clientName: String? = null

    var balance: Int = 0
}
