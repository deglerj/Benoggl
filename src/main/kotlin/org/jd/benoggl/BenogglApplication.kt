package org.jd.benoggl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BenogglApplication

fun main(args: Array<String>) {
	runApplication<BenogglApplication>(*args)
}
