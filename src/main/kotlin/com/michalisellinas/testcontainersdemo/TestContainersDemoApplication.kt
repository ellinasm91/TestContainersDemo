package com.michalisellinas.testcontainersdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TestContainersDemoApplication

fun main(args: Array<String>) {
	runApplication<TestContainersDemoApplication>(*args)
}

