package com.doogie.entitypractice.model

import org.hibernate.HibernateException
import org.hibernate.annotations.Tuplizer
import org.hibernate.mapping.Component
import org.hibernate.mapping.PersistentClass
import org.hibernate.tuple.Instantiator
import org.hibernate.tuple.component.PojoComponentTuplizer
import org.hibernate.tuple.entity.EntityMetamodel
import org.hibernate.tuple.entity.PojoEntityTuplizer
import java.io.Serializable
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.HashMap
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
@Tuplizer(impl = DynamicEntityTuplizer::class)
interface Cuisine {
    @get:Id
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?

    var name: String

    @get:Tuplizer(impl = DynamicEmbeddableTuplizer::class)
    var country: Country
}

@Embeddable
interface Country {
    @get:Column(name = "CountryName")
    var name: String
}

class DynamicEntityTuplizer(
    entityMetamodel: EntityMetamodel,
    mappedEntity: PersistentClass
) : PojoEntityTuplizer(entityMetamodel, mappedEntity) {

    override fun buildInstantiator(
        entityMetamodel: EntityMetamodel,
        persistentClass: PersistentClass): Instantiator {
        return DynamicInstantiator(
            persistentClass.className
        )
    }
}

class DynamicEmbeddableTuplizer(embeddable: Component) : PojoComponentTuplizer(embeddable) {
    override fun buildInstantiator(embeddable: Component): Instantiator {
        return DynamicInstantiator(
            embeddable.componentClassName
        )
    }
}

class DynamicInstantiator(targetClassName: String) : Instantiator {
    private val targetClass: Class<*>

    init {
        try {
            this.targetClass = Class.forName(targetClassName)
        } catch (e: ClassNotFoundException) {
            throw HibernateException(e)
        }

    }

    override fun instantiate(id: Serializable?): Any {
        return ProxyHelper.newProxy(targetClass, id)
    }

    override fun instantiate(): Any {
        return instantiate(null)
    }

    override fun isInstance(`object`: Any): Boolean {
        try {
            return targetClass.isInstance(`object`)
        } catch (t: Throwable) {
            throw HibernateException(
                "could not get handle to entity as interface : $t"
            )
        }

    }
}

object ProxyHelper {
    fun <T> newProxy(targetClass: Class<T>, id: Serializable?): T {
        return Proxy.newProxyInstance(
            targetClass.classLoader,
            arrayOf<Class<*>>(targetClass),
            DataProxyHandler(
                targetClass.name,
                id
            )
        ) as T
    }

    fun extractEntityName(`object`: Any): String? {
        if (Proxy.isProxyClass(`object`.javaClass)) {
            val handler = Proxy.getInvocationHandler(
                `object`
            )
            if (DataProxyHandler::class.java.isAssignableFrom(handler.javaClass)) {
                val myHandler = handler as DataProxyHandler
                return myHandler.entityName
            }
        }
        return null
    }
}

class DataProxyHandler(val entityName: String, id: Serializable?) : InvocationHandler {
    private val data = HashMap<String, Any?>()

    init {
        data["Id"] = id
    }

    @Throws(Throwable::class)
    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
        val methodName = method.name
        when {
            methodName.startsWith("set") -> {
                val propertyName = methodName.substring(3)
                data[propertyName] = args[0]
            }
            methodName.startsWith("get") -> {
                val propertyName = methodName.substring(3)
                return data[propertyName]
            }
            "toString" == methodName -> return entityName + "#" + data["Id"]
            "hashCode" == methodName -> return this.hashCode()
        }
        return null
    }
}