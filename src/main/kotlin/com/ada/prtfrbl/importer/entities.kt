package com.ada.prtfrbl.importer

data class Strategy(
    val id: String? = "0",
    val minRiskLevel: String? = "0",
    val maxRiskLevel: String? = "0",
    val minYearsToRetirement: String? = "0",
    val maxYearsToRetirement: String? = "0",
    val stocksPercentage: String? = "0",
    val cashPercentage: String? = "100",
    val bondsPercentage: String? = "0"
) {}

data class Customer(
    val id: String? = "0",
    val email: String? = "",
    val dateOfBirth: String? = "1980-01-01",
    val riskLevel: String? = "0",
    val retirementAge: String? = "0"
) {}
