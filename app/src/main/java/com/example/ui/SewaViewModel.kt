package com.example.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SewaViewModel(
    application: Application,
    private val repository: SewaRepository
) : AndroidViewModel(application) {

    // Localization: "en" for English, "ne" for Nepali string references
    var currentLang by mutableStateOf("en")
        private set

    fun toggleLanguage() {
        currentLang = if (currentLang == "en") "ne" else "en"
    }

    // Active Screen Tab navigation: "catalog", "checker", "tracking", "profile", "specs"
    var activeTab by mutableStateOf("catalog")
        private set

    fun navigateToTab(tab: String) {
        activeTab = tab
    }

    // Accessibility States
    var isHighContrast by mutableStateOf(false)
        private set

    fun toggleHighContrast() {
        isHighContrast = !isHighContrast
    }

    // --- SERVICE CATALOG NAVIGATION STATS ---
    private val _searchQueryFlow = MutableStateFlow("")
    private val _categoryFlow = MutableStateFlow("All")

    var searchQuery by mutableStateOf("")
        private set

    var selectedCategoryEn by mutableStateOf("All")
        private set

    var selectedServiceDetail by mutableStateOf<SarkariService?>(null)
        private set

    fun updateSearchQuery(query: String) {
        searchQuery = query
        _searchQueryFlow.value = query
    }

    fun selectCategory(categoryEn: String) {
        selectedCategoryEn = categoryEn
        _categoryFlow.value = categoryEn
    }

    fun showServiceDetails(service: SarkariService?) {
        selectedServiceDetail = service
    }

    // Filtering services based on query and category
    val filteredServices: StateFlow<List<SarkariService>> = _searchQueryFlow
        .combine(_categoryFlow) { query, cat ->
            CatalogData.services.filter { service ->
                val matchesCategory = if (cat == "All") {
                    true
                } else {
                    service.categoryEn.equals(cat, ignoreCase = true)
                }

                val matchesSearch = if (query.isBlank()) {
                    true
                } else {
                    service.titleEn.contains(query, ignoreCase = true) ||
                    service.titleNe.contains(query, ignoreCase = true) ||
                    service.ministryEn.contains(query, ignoreCase = true) ||
                    service.ministryNe.contains(query, ignoreCase = true) ||
                    service.detailsEn.contains(query, ignoreCase = true) ||
                    service.detailsNe.contains(query, ignoreCase = true)
                }

                matchesCategory && matchesSearch
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, CatalogData.services)


    // --- ELIGIBILITY CHECKER STATE ---
    var eligibilityService by mutableStateOf<SarkariService?>(null)
        private set

    // Answers represent: [QuestionIndex -> Answered Yes(true) or No(false)]
    var eligibilityAnswersByStep = mutableStateOf<Map<Int, Boolean>>(emptyMap())
        private set

    var eligibilityCurrentStep by mutableStateOf(0)
        private set

    // Simple questions for verification (Passport & Nat ID has 3 questions)
    val eligibilityQuestionsEn = listOf(
        "Are you a verified citizen of Nepal?",
        "Do you have all your original citizenship certificates on hand?",
        "Do you have a registered mobile number for secure logins / OTP verification?"
    )

    val eligibilityQuestionsNe = listOf(
        "के तपाईं प्रमाणित नेपाली नागरिक हुनुहुन्छ?",
        "के तपाईंसँग सम्बन्धित नागरिकताको प्रमाणपत्रको सक्कलप्रति साथमा छ?",
        "के तपाईंसँग सुरक्षित अनलाइन प्रमाणीकरणका लागि आफ्नो नाममा दर्ता भएको सिम कार्ड छ?"
    )

    fun startEligibilityCheck(service: SarkariService) {
        eligibilityService = service
        eligibilityAnswersByStep.value = emptyMap()
        eligibilityCurrentStep = 0
        navigateToTab("checker")
    }

    fun answerEligibilityQuestion(answer: Boolean) {
        val currentAnswers = eligibilityAnswersByStep.value.toMutableMap()
        currentAnswers[eligibilityCurrentStep] = answer
        eligibilityAnswersByStep.value = currentAnswers

        if (eligibilityCurrentStep < eligibilityQuestionsEn.size - 1) {
            eligibilityCurrentStep++
        } else {
            // Finished
            eligibilityCurrentStep = eligibilityQuestionsEn.size
        }
    }

    fun resetEligibilityWizard() {
        eligibilityService = null
        eligibilityAnswersByStep.value = emptyMap()
        eligibilityCurrentStep = 0
    }

    // --- CITIZEN PROFILE FORM STATE & ROOM ---
    val primaryProfile: StateFlow<CitizenProfile?> = repository.primaryProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Form editing inputs
    var formFullName by mutableStateOf("")
    var formDob by mutableStateOf("")
    var formCitizenshipNo by mutableStateOf("")
    var formDistrict by mutableStateOf("")
    var formEmail by mutableStateOf("")
    var formPhone by mutableStateOf("")

    fun loadProfileIntoForm(profile: CitizenProfile?) {
        if (profile != null) {
            formFullName = profile.fullName
            formDob = profile.dob
            formCitizenshipNo = profile.citizenshipNumber
            formDistrict = profile.district
            formEmail = profile.email
            formPhone = profile.phone
        } else {
            formFullName = ""
            formDob = "2045-03-12"
            formCitizenshipNo = "27-01-72-04533"
            formDistrict = "Kathmandu"
            formEmail = "citizen.nepal@gmail.com"
            formPhone = "+977-9851000000"
        }
    }

    fun saveCitizenProfile() {
        viewModelScope.launch {
            val updated = CitizenProfile(
                fullName = formFullName,
                dob = formDob,
                citizenshipNumber = formCitizenshipNo,
                district = formDistrict,
                email = formEmail,
                phone = formPhone
            )
            repository.saveProfile(updated)
        }
    }

    fun deleteProfile() {
        viewModelScope.launch {
            repository.clearProfile()
            loadProfileIntoForm(null)
        }
    }


    // --- APPLICATION STATUS TRACKING & ROOM ---
    val allTrackedApplications: StateFlow<List<TrackedApplication>> = repository.allTracked
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var trackNumberInput by mutableStateOf("")
    var trackApplicantName by mutableStateOf("")
    var trackServiceIdInput by mutableStateOf("passport") // Default
    var isTrackingQueryLoading by mutableStateOf(false)

    fun submitTrackingQuery() {
        if (trackNumberInput.isBlank()) return

        isTrackingQueryLoading = true
        viewModelScope.launch {
            // Query simulated gateway and insert into local Room Cache
            repository.queryStatusSimulated(
                trackingNumber = trackNumberInput,
                serviceId = trackServiceIdInput,
                applicantName = trackApplicantName
            )
            isTrackingQueryLoading = false
            // Reset input field
            trackNumberInput = ""
            trackApplicantName = ""
        }
    }

    fun deleteTrackedApplication(trackingNumber: String) {
        viewModelScope.launch {
            repository.deleteTrackedByNumber(trackingNumber)
        }
    }

    // Interactive pre-filled simulation of automatic tracker
    init {
        // Pre-load default values on installation so that the citizen first-launch database shows status checks!
        loadProfileIntoForm(null)
        viewModelScope.launch {
            // Seed a default tracked passport status so they see a demo tracking progress card on first start!
            val demoPassportTracking = TrackedApplication(
                trackingNumber = "NID-99371-NE",
                serviceId = "national_id",
                applicantName = "Bahadur Gurung",
                serviceNameEn = "National ID Registration",
                serviceNameNe = "राष्ट्रिय परिचयपत्र दर्ता",
                statusTextEn = "Biometrics approved. Card batch requested at NITC cabinet.",
                statusTextNe = "बायोमेट्रिक स्वीकृत भएको छ। कार्ड कार्ड छपाई कार्यसूचीमा रहेको छ।",
                progressPercent = 65,
                lastChecked = "2026-06-06 01:05:00"
            )
            repository.addTrackedApplication(demoPassportTracking)
        }
    }
}

// Factory pattern required for initializing AndroidViewModel with Repository arguments cleanly
class SewaViewModelFactory(
    private val application: Application,
    private val repository: SewaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SewaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SewaViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
