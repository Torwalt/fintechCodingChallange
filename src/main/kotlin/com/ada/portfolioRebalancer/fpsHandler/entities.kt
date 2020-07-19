package com.ada.portfolioRebalancer.fpsHandler

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CustomerPortfolio(
    val customerId: Int = 1,
    val stocks: Int = 6700,
    val bonds: Int = 1200,
    val cash: Int = 400
)

data class PortfolioDistribution(
    val stocks: Double,
    val bonds: Double,
    val cash: Double
)
