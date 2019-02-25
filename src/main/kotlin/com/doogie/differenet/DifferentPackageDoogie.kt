package com.doogie.differenet

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class DifferentPackageDoogie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?

    private constructor(id: Long? = null) {
        this.id = id
    }

    companion object {
        fun getInstance(id: Long? = null): DifferentPackageDoogie {
            return DifferentPackageDoogie(id)
        }
    }

    override fun toString(): String {
        return "DifferentPackageDoogie(id=$id)"
    }
}

interface DifferentPackageDoogieRepository: JpaRepository<DifferentPackageDoogie, Long>
