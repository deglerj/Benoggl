package org.jd.benoggl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableR2dbcRepositories
@EnableScheduling
class BenogglApplication

fun main(args: Array<String>) {
    runApplication<BenogglApplication>(*args)
}
