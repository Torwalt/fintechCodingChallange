package com.ada.prtfrbl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PortfolioRebalancerApplication

fun main(args: Array<String>) {
    runApplication<PortfolioRebalancerApplication>(*args)
}
