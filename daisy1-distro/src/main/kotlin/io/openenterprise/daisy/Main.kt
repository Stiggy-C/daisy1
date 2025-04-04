package io.openenterprise.daisy

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = ["io.openenterprise.daisy"])
object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.main(args)
    }

}