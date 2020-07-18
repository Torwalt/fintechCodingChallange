package com.ada.portfolioRebalancer.csvImporter

data class ImportedStrategy(
    val id: String?,
    val minRiskLevel: String?,
    val maxRiskLevel: String?,
    val minYearsToRetirement: String?,
    val maxYearsToRetirement: String?,
    val stocksPercentage: String?,
    val cashPercentage: String?,
    val bondsPercentage: String?
) {}

data class ImportedCustomer(
    val id: String?,
    val email: String?,
    val dateOfBirth: String?,
    val riskLevel: String?,
    val retirementAge: String?
) {}
