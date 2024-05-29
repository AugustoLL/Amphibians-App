package com.example.amphibians.data

import com.example.amphibians.model.Amphibian
import com.example.amphibians.network.AmphibiansApiService

/**
 * Repository retrieves amphibian data from underlying data source.
 */
interface AmphibiansDataRepository {
    /** Retrieves list of amphibians from underlying data source */
    suspend fun getAmphibians(): List<Amphibian>
}

/**
 * Network Implementation of repository that retrieves amphibian data from underlying data source.
 */
class NetworkAmphibiansDataRepository(
    private val amphibiansApiService: AmphibiansApiService
): AmphibiansDataRepository {
    override suspend fun getAmphibians(): List<Amphibian> =
        amphibiansApiService.getAmphibians()
}