package com.example.data

import kotlinx.coroutines.flow.Flow

class SewaRepository(private val sewaDao: SewaDao) {

    // Profiles Flow & Suspends
    val primaryProfile: Flow<CitizenProfile?> = sewaDao.getPrimaryProfile()

    suspend fun saveProfile(profile: CitizenProfile) {
        sewaDao.clearProfiles() // Keep only 1 primary profile
        sewaDao.insertProfile(profile)
    }

    suspend fun clearProfile() {
        sewaDao.clearProfiles()
    }

    // Applications Tracked Flow & Suspends
    val allTracked: Flow<List<TrackedApplication>> = sewaDao.getAllTracked()

    suspend fun addTrackedApplication(app: TrackedApplication) {
        sewaDao.insertTracked(app)
    }

    suspend fun deleteTrackedByNumber(trackingNumber: String) {
        sewaDao.deleteTrackedByNumber(trackingNumber)
    }

    suspend fun queryStatusSimulated(trackingNumber: String, serviceId: String, applicantName: String): TrackedApplication {
        // Here we simulate an integrated headless API response / external government portal query.
        // It provides progress steps based on the tracking numbers to verify state mock connections properly!
        val uppercaseNum = trackingNumber.uppercase().trim()
        val pPercent: Int
        val statusEn: String
        val statusNe: String

        // Deterministic status based on last character of tracking number
        val lastChar = uppercaseNum.lastOrNull() ?: '0'
        when {
            lastChar in '0'..'2' -> {
                pPercent = 25
                statusEn = "Form Submitted & Documents Verified"
                statusNe = "निवेदन दर्ता र कागजात प्रमाणित भएको"
            }
            lastChar in '3'..'5' -> {
                pPercent = 60
                statusEn = "Processing at Ministry/Department Head Office"
                statusNe = "सम्बन्धित मन्त्रालय/विभाग केन्द्रमा प्रशोधन भइरहेको"
            }
            lastChar in '6'..'7' -> {
                pPercent = 85
                statusEn = "Printing Completed & Batch Dispatched for Shipping"
                statusNe = "कागजात छपाई सम्पन्न, सम्बन्धित कार्यालय पठाउन तयार"
            }
            else -> {
                pPercent = 100
                statusEn = "Application Approved and Ready to Collect!"
                statusNe = "सवारी/राहदानी स्वीकृत भएको, सम्बन्धित कार्यालयबाट बुझ्न सकिने!"
            }
        }

        // Get Name of Service
        val serviceMatch = CatalogData.services.find { it.id == serviceId }
        val sNameEn = serviceMatch?.titleEn ?: "Public Service Request"
        val sNameNe = serviceMatch?.titleNe ?: "सार्वजनिक सेवा निवेदन"

        val application = TrackedApplication(
            trackingNumber = uppercaseNum,
            serviceId = serviceId,
            applicantName = applicantName.ifBlank { "Citizen Applicant" },
            serviceNameEn = sNameEn,
            serviceNameNe = sNameNe,
            statusTextEn = statusEn,
            statusTextNe = statusNe,
            progressPercent = pPercent,
            lastChecked = "2026-06-06 03:18:22 (UTC)"
        )

        sewaDao.insertTracked(application)
        return application
    }
}
