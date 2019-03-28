package com.doogie.entitypractice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EntityPracticeApplication {
}

fun main(args: Array<String>) {
    runApplication<EntityPracticeApplication>(*args).use {
    }
}
