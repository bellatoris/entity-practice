package com.doogie.entitypractice.low

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class LowPackageDoogie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?

    private constructor(id: Long? = null) {
        this.id = id
    }

    companion object {
        fun getInstance(id: Long? = null): LowPackageDoogie {
            return LowPackageDoogie(id)
        }
    }

    override fun toString(): String {
        return "LowPackageDoogie(id=$id)"
    }
}

interface LowPackageDoogieRepository: JpaRepository<LowPackageDoogie, Long>
