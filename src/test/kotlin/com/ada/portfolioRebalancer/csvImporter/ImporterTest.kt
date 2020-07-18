package com.ada.portfolioRebalancer.csvImporter

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import java.nio.file.Files.delete
import java.nio.file.Paths

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ImporterTest {
    private val tmpDir = System.getProperty("java.io.tmpdir")
    private val strategiesFileName = "strategies.csv"
    private val customersFileName = "customers.csv"

    @BeforeEach
    fun setUp() {
        val strategies = File(tmpDir, strategiesFileName)
        strategies.createNewFile()
        val customers = File(tmpDir, customersFileName)
        customers.createNewFile()
    }

    @AfterEach
    fun tearDown() {
        delete(Paths.get(tmpDir, strategiesFileName))
        delete(Paths.get(tmpDir, customersFileName))
    }

    init {
    }

    @Test
    fun `strategy is imported`() {
        val csvReader = mockk<CsvReader>()
        val file = File(tmpDir, strategiesFileName)
        every { csvReader.readAllWithHeader(allAny<File>()) } returns listOf(
            mapOf(
                "strategyId" to "1",
                "minRiskLevel" to "1",
                "maxRiskLevel" to "2",
                "minYearsToRetirement" to "10",
                "maxYearsToRetirement" to "15",
                "stocksPercentage" to "20",
                "cashPercentage" to "30",
                "bondsPercentage" to "50"
            ),
            mapOf(
                "strategyId" to "2",
                "minRiskLevel" to "2",
                "maxRiskLevel" to "4",
                "minYearsToRetirement" to "8",
                "maxYearsToRetirement" to "12",
                "stocksPercentage" to "50",
                "cashPercentage" to "20",
                "bondsPercentage" to "30"
            )
        )
        val importer = Importer(tmpDir, csvReader)
        importer.import()
        Assertions.assertEquals(importer.importedStrategies.count(), 2)
        Assertions.assertEquals(importer.importedStrategies[0].maxYearsToRetirement, "15")
        Assertions.assertEquals(importer.importedStrategies[0].id, "1")
    }

    @Test
    fun `customer is imported`() {
        val csvReader = mockk<CsvReader>()
        val file = File(tmpDir, customersFileName)
        every { csvReader.readAllWithHeader(allAny<File>()) } returns listOf(
            mapOf(
                "customerId" to "1",
                "email" to "test1@test.test",
                "dateOfBirth" to "1989-09-01",
                "riskLevel" to "2",
                "retirementAge" to "65"
            ),
            mapOf(
                "customerId" to "2",
                "email" to "test2@test.test",
                "dateOfBirth" to "1971-01-10",
                "riskLevel" to "5",
                "retirementAge" to "67"
            )
        )
        val importer = Importer(tmpDir, csvReader)
        importer.import()
        Assertions.assertEquals(importer.importedCustomers.count(), 2)
        Assertions.assertEquals(importer.importedCustomers[0].id, "1")
        Assertions.assertEquals(importer.importedCustomers[0].email, "test1@test.test")
    }
}
