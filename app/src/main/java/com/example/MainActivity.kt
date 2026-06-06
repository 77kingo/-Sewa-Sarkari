package com.example

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.ui.SewaViewModel
import com.example.ui.SewaViewModelFactory
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Init local Database, Repository, and ViewModelProvider cleanly without KTX issues
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = SewaRepository(database.sewaDao())
        val factory = SewaViewModelFactory(application, repository)
        val viewModel = ViewModelProvider(this, factory)[SewaViewModel::class.java]

        setContent {
            MyApplicationTheme(
                isHighContrast = viewModel.isHighContrast
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainSewaAppScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainSewaAppScreen(viewModel: SewaViewModel) {
    val currentLang = viewModel.currentLang

    // Layout margins that adapt to notches and system navbar gestures safely
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SewaHeaderView(
                currentLang = currentLang,
                onLangToggle = { viewModel.toggleLanguage() },
                isContrast = viewModel.isHighContrast,
                onContrastToggle = { viewModel.toggleHighContrast() }
            )
        },
        bottomBar = {
            SewaBottomNav(
                activeTab = viewModel.activeTab,
                onTabSelect = { viewModel.navigateToTab(it) },
                currentLang = currentLang
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    if (viewModel.isHighContrast) Color.Black
                    else MaterialTheme.colorScheme.background
                )
        ) {
            // Display quick status bar for offline simulator representation
            OfflineCacheBanner(currentLang = currentLang)

            Box(
                modifier = Modifier
                    .fillSomeMaxSize(this)
                    .weight(1f)
            ) {
                // Render selected tab screen layout with fluent animation
                AnimatedContent(
                    targetState = viewModel.activeTab,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "tab_transitions"
                ) { tab ->
                    when (tab) {
                        "catalog" -> ServiceCatalogScreen(viewModel = viewModel)
                        "checker" -> EligibilityCheckerScreen(viewModel = viewModel)
                        "tracking" -> ApplicationTrackingScreen(viewModel = viewModel)
                        "profile" -> CitizenProfileScreen(viewModel = viewModel)
                        "specs" -> DeveloperSpecsScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

// Extends space checking to prevent layout issues
fun Modifier.fillSomeMaxSize(columnScope: ColumnScope): Modifier {
    return this.fillMaxWidth().fillMaxHeight()
}

// --- TOP APPLICATION TITLE AND OPTIONS BAR ---
@Composable
fun SewaHeaderView(
    currentLang: String,
    onLangToggle: () -> Unit,
    isContrast: Boolean,
    onContrastToggle: () -> Unit
) {
    Surface(
        tonalElevation = if (isContrast) 6.dp else 0.dp,
        color = if (isContrast) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
        modifier = Modifier.statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Nepal National Emblem/Flag Representation
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isContrast) Color.White else MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
                        .padding(if (isContrast) 2.dp else 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Small Nepalese flag colors rendering for absolute authenticity
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRect(Color(0xFFC01C3F))
                        drawCircle(Color.White, radius = size.minDimension / 4f)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (currentLang == "en") "Sarkari Sewa" else "सरकारी सेवा",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isContrast) Color.White else MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = if (currentLang == "en") "NAGARIK APP • ACCESS HUB" else "नागरिक एप • डिजिटल केन्द्र",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isContrast) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Accessibility High Contrast Toggle
                IconButton(
                    onClick = onContrastToggle,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("high_contrast_btn")
                ) {
                    Icon(
                        imageVector = if (isContrast) Icons.Default.Star else Icons.Default.Settings,
                        contentDescription = "Contrast Mode",
                        tint = if (isContrast) Color.White else MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                // Internationalization Toggle
                Button(
                    onClick = onLangToggle,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isContrast) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.tertiary,
                        contentColor = if (isContrast) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onTertiary
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.height(38.dp).testTag("lang_toggle_btn")
                ) {
                    Text(
                        text = if (currentLang == "en") "🌐 नेपाली" else "🌐 English",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

// --- OFFLINE DATA MODE STATUS BAR ---
@Composable
fun OfflineCacheBanner(currentLang: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0F2C59))
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Offline Cache",
            tint = Color(0xFF64B5F6),
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = if (currentLang == "en") "Offline Mode Enabled. Cache matches Nepal Gov standards." 
                   else "अफलाइन मोड उपलब्ध छ। डाटा दर्ता सुरक्षित छ।",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// --- BOTTOM PERSISTENT NAVIGATION ---
@Composable
fun SewaBottomNav(
    activeTab: String,
    onTabSelect: (String) -> Unit,
    currentLang: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Divider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
        NavigationBar(
            tonalElevation = 0.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.navigationBarsPadding().height(72.dp)
        ) {
        val navItems = listOf(
            Triple("catalog", if (currentLang == "en") "Catalog" else "सूची", Icons.Default.Search),
            Triple("checker", if (currentLang == "en") "Check" else "योग्यता", Icons.Default.Done),
            Triple("tracking", if (currentLang == "en") "Track" else "ट्र्याक", Icons.Default.Refresh),
            Triple("profile", if (currentLang == "en") "Profile" else "प्रोफाइल", Icons.Default.Person),
            Triple("specs", if (currentLang == "en") "Dev Spec" else "विवरण", Icons.Default.Build)
        )

        navItems.forEach { (tab, label, icon) ->
            NavigationBarItem(
                selected = activeTab == tab,
                onClick = { onTabSelect(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (activeTab == tab) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.testTag("nav_item_$tab")
            )
        }
    }
}
}


// ==========================================
// SCREEN 1: UNITED SERVICES CATALOG VIEW
// ==========================================
@Composable
fun ServiceCatalogScreen(viewModel: SewaViewModel) {
    val currentLang = viewModel.currentLang
    val searchResults by viewModel.filteredServices.collectAsStateWithLifecycle()
    val categories = if (currentLang == "en") CatalogData.categoriesEn else CatalogData.categoriesNe

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Search Input Header
        item {
            Text(
                text = if (currentLang == "en") "Aggregated Public Services" else "एकीकृत सरकारी सेवा सूची",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (currentLang == "en") "Search trusted portals and fast pre-enrollment guidelines." 
                       else "भरोसायोग्य अनलाइन पोर्टलहरू र द्रुत प्रिल-एनरोलमेन्ट निर्देशिका।",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(14.dp))
            
            // Text Search Box
            TextField(
                value = viewModel.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = {
                    Text(text = if (currentLang == "en") "Search Passports, PAN cards, Land..." else "राहदानी, कर, सवारी अनुमति खोज्नुहोस्...")
                },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("catalog_search_box"),
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }

        // Horizontal Category Filter Carousel
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 2.dp)
            ) {
                items(categories) { category ->
                    // Since categories list could be translated, we map back for selection check
                    val index = categories.indexOf(category)
                    val correspondingEn = CatalogData.categoriesEn[index]
                    val isSelected = viewModel.selectedCategoryEn == correspondingEn

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary 
                                else MaterialTheme.colorScheme.surfaceVariant,
                        tonalElevation = 2.dp,
                        modifier = Modifier
                            .clickable { viewModel.selectCategory(correspondingEn) }
                            .testTag("category_pill_$correspondingEn")
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White 
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Search outcomes or Empty State card
        if (searchResults.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Not found",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (currentLang == "en") "No Services Match Your Query" else "तपाईंको खोजी अनुसार कुनै सेवा भेटिएन",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (currentLang == "en") "Try clearing filters or search simpler terms." else "अन्य विकल्पहरू प्रयोग गरी पुन: प्रयास गर्नुहोस्।",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        } else {
            items(searchResults) { service ->
                ServiceItemCard(
                    service = service,
                    currentLang = currentLang,
                    onSelected = { viewModel.showServiceDetails(service) }
                )
            }
        }
    }

    // Modal dialog overlay displaying selected services in full detail
    val detail = viewModel.selectedServiceDetail
    if (detail != null) {
        AlertDialog(
            onDismissRequest = { viewModel.showServiceDetails(null) },
            confirmButton = {
                Button(
                    onClick = { 
                        viewModel.startEligibilityCheck(detail)
                        viewModel.showServiceDetails(null)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = if (currentLang == "en") "Check Eligibility" else "योग्यता जाँच")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showServiceDetails(null) }) {
                    Text(text = if (currentLang == "en") "Close" else "बन्द")
                }
            },
            title = {
                Text(
                    text = if (currentLang == "en") detail.titleEn else detail.titleNe,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Quick Stats Table
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text(text = if (currentLang == "en") "Min. Cost:" else "लागतः", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text(text = if (currentLang == "en") detail.feeEn else detail.feeNe, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                            }
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text(text = if (currentLang == "en") "Est. Time:" else "अवधिः", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text(text = if (currentLang == "en") detail.timeEn else detail.timeNe, fontSize = 11.sp)
                            }
                        }
                    }

                    Text(
                        text = if (currentLang == "en") detail.detailsEn else detail.detailsNe,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (currentLang == "en") "Required Documents (कागजातहरू):" else "आवश्यक कागजात सूचीः",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp
                    )
                    val docs = if (currentLang == "en") detail.requiredDocsEn else detail.requiredDocsNe
                    docs.forEach { doc ->
                        Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "doc icon",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(16.dp).padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = doc, fontSize = 12.sp, lineHeight = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (currentLang == "en") "Pre-enrollment Guide Links:" else "आधिकारिक पोर्टल लिङ्कहरू:",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp
                    )
                    Text(
                        text = detail.externalUrl,
                        color = Color(0xFF1E88E5),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { }
                    )
                }
            }
        )
    }
}

@Composable
fun ServiceItemCard(
    service: SarkariService,
    currentLang: String,
    onSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelected() }
            .testTag("service_card_${service.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = if (currentLang == "en") service.titleEn else service.titleNe,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
                // Cost Pill
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = if (currentLang == "en") "Info Cache" else "अफलाइन दर्ता",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (currentLang == "en") service.ministryEn else service.ministryNe,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (currentLang == "en") "Est: ${service.timeEn}" else "समयः ${service.timeNe}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (currentLang == "en") "View Details ➔" else "विवरण हेर्नुहोस् ➔",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}


// ==========================================
// SCREEN 2: DYNAMIC ELIGIBILITY CHECKER
// ==========================================
@Composable
fun EligibilityCheckerScreen(viewModel: SewaViewModel) {
    val currentLang = viewModel.currentLang
    val selected = viewModel.eligibilityService

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (currentLang == "en") "Eligibility Advisor" else "योग्यता सल्लाहकार",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        if (selected == null) {
            // First step: Choose service to verify eligibility
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Advisor Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (currentLang == "en") "Select Service to Start Analysis" else "जाँच सुरु गर्न सेवा छनौट गर्नुहोस्",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    CatalogData.services.forEach { s ->
                        Button(
                            onClick = { viewModel.startEligibilityCheck(s) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = if (currentLang == "en") s.titleEn else s.titleNe)
                        }
                    }
                }
            }
        } else {
            // Wizard step
            val step = viewModel.eligibilityCurrentStep
            val questions = if (currentLang == "en") viewModel.eligibilityQuestionsEn else viewModel.eligibilityQuestionsNe
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (currentLang == "en") "Checking: ${selected.titleEn}" else "योग्यता जाँच: ${selected.titleNe}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (step < questions.size) {
                        // Progress layout bar
                        LinearProgressIndicator(
                            progress = { (step.toFloat() / questions.size.toFloat()) },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (currentLang == "en") "STEP ${step + 1} of ${questions.size}" else "चरण ${step + 1} / ${questions.size}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = questions[step],
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 24.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { viewModel.answerEligibilityQuestion(false) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("elig_no_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text(text = if (currentLang == "en") "No" else "होइन")
                            }

                            Button(
                                onClick = { viewModel.answerEligibilityQuestion(true) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("elig_yes_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text(text = if (currentLang == "en") "Yes" else "हो")
                            }
                        }
                    } else {
                        // Finished checking, yield result
                        val allYes = viewModel.eligibilityAnswersByStep.value.values.all { it }
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (allYes) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (allYes) Icons.Default.CheckCircle else Icons.Default.Warning,
                                    contentDescription = "Verdict",
                                    tint = if (allYes) Color(0xFF2E7D32) else Color(0xFFC62828),
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = if (allYes) {
                                        if (currentLang == "en") "ELIGIBLE! Ready to Pre-Enroll" else "तपाईं योग्य हुनुहुन्छ! प्रक्रिया सुरु गर्नुहोस्"
                                    } else {
                                        if (currentLang == "en") "ACTION REQUIRED: Insufficient Credentials" else "अयोग्य: कागजात तथा सर्त अपूर्ण"
                                    },
                                    fontWeight = FontWeight.Bold,
                                    color = if (allYes) Color(0xFF2E7D32) else Color(0xFFC62828)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (allYes) {
                                if (currentLang == "en") "Everything seems verified. To speed up access, visit the Nepal Gov pre-enroll link below to complete official online registration profiles."
                                else "तपाईंसँग सबै आवश्यक कागजातहरू तथा योग्यता छन्। सरकारी पोर्टलमा अनलाइन प्रिल-एनरोलमेन्ट सिफारिस गरिन्छ।"
                            } else {
                                if (currentLang == "en") "Please review the missing items checklist. Obtain your national identification indices and verified citizenship cards before applying to avoid rejection."
                                else "तपाईंसँग प्रक्रिया सुरु गर्न उल्लेखित कागजात पूरा हुनुपर्नेछ। राष्ट्रिय परिचयपत्र वा नागरिकता विसंगतिलाई सच्याएर मात्र आवेदन दिनुहोस्।"
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { viewModel.resetEligibilityWizard() },
                            modifier = Modifier.fillMaxWidth().testTag("reset_elig_btn")
                        ) {
                            Text(text = if (currentLang == "en") "Test Another Service" else "अन्य सेवा जाँच गर्नुहोस्")
                        }
                    }
                }
            }
        }
    }
}


// ==========================================
// SCREEN 3: LOCAL APPLICATION TRACKER (ROOM)
// ==========================================
@Composable
fun ApplicationTrackingScreen(viewModel: SewaViewModel) {
    val currentLang = viewModel.currentLang
    val trackedList by viewModel.allTrackedApplications.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = if (currentLang == "en") "Live Application Tracker" else "आवेदन स्थिति ट्र्याकिङ",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (currentLang == "en") "Secure local record database for tracking license, citizenship, and passports approvals offline." 
                       else "लाइसेन्स, नागरिकता र राहदानी स्वीकृतिको स्थिति सुरक्षित रूपमा अफलाइन ट्र्याक गर्नुहोस्।",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
        }

        // Search Input Fields Card for adding a Tracking Number to Room DB
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (currentLang == "en") "Query Portals & Save Application" else "नयाँ आवेदन ट्र्याक गरी थप्नुहोस्",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Reference Serial Number Input
                    OutlinedTextField(
                        value = viewModel.trackNumberInput,
                        onValueChange = { viewModel.trackNumberInput = it },
                        label = { Text(text = if (currentLang == "en") "Reference/Tracking Symbol (eg: N-129-A)" else "संकेत नम्बर (उदाहरण: N-129-A)") },
                        modifier = Modifier.fillMaxWidth().testTag("track_number_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    // Applicant Citizen Name input
                    OutlinedTextField(
                        value = viewModel.trackApplicantName,
                        onValueChange = { viewModel.trackApplicantName = it },
                        label = { Text(text = if (currentLang == "en") "Applicant Full Name" else "आवेदकको पूरा नाम") },
                        modifier = Modifier.fillMaxWidth().testTag("track_name_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    // Service Dropdown Category Toggle
                    Text(
                        text = if (currentLang == "en") "Choose Service Type:" else "सेवा प्रकार र विधा छनौटः",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(
                            Pair("passport", if (currentLang == "en") "Passport" else "राहदानी"),
                            Pair("national_id", if (currentLang == "en") "National ID" else "राष्ट्रिय ID"),
                            Pair("vehicle_tax", if (currentLang == "en") "Vehicle Tax" else "सवारी कर")
                        ).forEach { (id, label) ->
                            val active = viewModel.trackServiceIdInput == id
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.trackServiceIdInput = id }
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    color = if (active) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    if (viewModel.isTrackingQueryLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).size(24.dp))
                    } else {
                        Button(
                            onClick = { viewModel.submitTrackingQuery() },
                            modifier = Modifier.fillMaxWidth().testTag("submit_track_btn"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = if (currentLang == "en") "Fetch Live Database & Track" else "डेटाबेस खोजी गरी सुरक्षित गर्नुहोस्")
                        }
                    }
                }
            }
        }

        // Database records list (Flow collected reactively from Room)
        item {
            Text(
                text = if (currentLang == "en") "Active Saved Inquiries" else "सुरक्षित रहेका ट्र्याकिङ सोधपुछहरू",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (trackedList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (currentLang == "en") "No Saved Trackers. Query above to seed." 
                               else "कुनै सक्रिय ट्र्याकिङ छैन। सुरक्षित गर्न माथि संकेत नम्बर हाल्नुहोस्।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(trackedList) { app ->
                TrackedAppCard(
                    app = app,
                    currentLang = currentLang,
                    onDelete = { viewModel.deleteTrackedApplication(app.trackingNumber) }
                )
            }
        }
    }
}

@Composable
fun TrackedAppCard(
    app: TrackedApplication,
    currentLang: String,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = app.applicantName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = if (currentLang == "en") app.serviceNameEn else app.serviceNameNe,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Track",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (currentLang == "en") "Ref ID: ${app.trackingNumber}" else "संकेत नम्बरः ${app.trackingNumber}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${app.progressPercent}% Completed",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { app.progressPercent.toFloat() / 100f },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = when {
                    app.progressPercent < 40 -> Color.Red
                    app.progressPercent < 90 -> Color.Yellow
                    else -> Color.Green
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (currentLang == "en") "Portal Update: ${app.statusTextEn}" else "पोर्टल सन्देशः ${app.statusTextNe}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (currentLang == "en") "Checked: ${app.lastChecked}" else "प्रमाणीकरणः ${app.lastChecked}",
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}


// ==========================================
// SCREEN 4: CITIZEN PROFILE & FORMS AUTO-FILL
// ==========================================
@Composable
fun CitizenProfileScreen(viewModel: SewaViewModel) {
    val currentLang = viewModel.currentLang
    val savedProfile by viewModel.primaryProfile.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (currentLang == "en") "Forms Personal Profile Autofill" else "फारम व्यक्तिगत डिजिटल प्रोफाइल",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = if (currentLang == "en") "Store profile variables securely in local Room secure sandbox database. All form templates auto-fill with credentials." 
                   else "सुरक्षित स्थानीय डेटाबासमा आफ्नो नागरिक प्रविष्टि भण्डार गर्नुहोस्। सबै सरकारी फारम यसै अनुसार भरिनेछन्।",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
        )

        // Active Saved Values Preview Badge if present
        if (savedProfile != null) {
            Surface(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, "verified badge", tint = Color(0xFF2E7D32))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (currentLang == "en") "Personal Profile Activated in Cache" 
                               else "व्यक्तिगत प्रोफाइल डाटाबेसमा सक्रिय छ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }

        // Form Inputs Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.formFullName,
                    onValueChange = { viewModel.formFullName = it },
                    label = { Text(if (currentLang == "en") "Full Name" else "पूरा नाम") },
                    modifier = Modifier.fillMaxWidth().testTag("profile_name_input"),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = viewModel.formDob,
                    onValueChange = { viewModel.formDob = it },
                    label = { Text(if (currentLang == "en") "DOB (BS/AD formats)" else "जन्म मिति (वि.सं/ई.सं)") },
                    modifier = Modifier.fillMaxWidth().testTag("profile_dob_input"),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = viewModel.formCitizenshipNo,
                    onValueChange = { viewModel.formCitizenshipNo = it },
                    label = { Text(if (currentLang == "en") "Citizenship Card ID" else "नागरिकता प्रमाणपत्र नम्बर") },
                    modifier = Modifier.fillMaxWidth().testTag("profile_citizen_input"),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = viewModel.formDistrict,
                    onValueChange = { viewModel.formDistrict = it },
                    label = { Text(if (currentLang == "en") "District Registry" else "सम्बन्धित जिल्ला") },
                    modifier = Modifier.fillMaxWidth().testTag("profile_district_input"),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = viewModel.formEmail,
                    onValueChange = { viewModel.formEmail = it },
                    label = { Text(if (currentLang == "en") "Contact Email" else "इमेल ठेगाना") },
                    modifier = Modifier.fillMaxWidth().testTag("profile_email_input"),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = viewModel.formPhone,
                    onValueChange = { viewModel.formPhone = it },
                    label = { Text(if (currentLang == "en") "Mobile/Phone" else "मोबाइल नम्बर") },
                    modifier = Modifier.fillMaxWidth().testTag("profile_phone_input"),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = { viewModel.deleteProfile() },
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(if (currentLang == "en") "Reset" else "पुन:सेट")
                    }

                    Button(
                        onClick = { viewModel.saveCitizenProfile() },
                        modifier = Modifier.weight(1.5f).height(48.dp).testTag("save_profile_btn"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (currentLang == "en") "Secure Profile" else "डाटाबेसमा सुरक्षित गर्नुहोस्")
                    }
                }
            }
        }

        // TEMPLATE SPEC SHOWCASE FOR AUTOFILL DEMO
        Text(
            text = if (currentLang == "en") "Interactive Form Auto-fill Simulator" else "अन्तरक्रियात्मक सरकारी फारम अटो-फिल",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Digital printed sheet representation showing autofilled values inside template blocks
        val active = savedProfile
        Surface(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFFAF9F6), // Warm old paper aesthetic
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "NEPAL GOVERNMENT PORTAL — DIGITAL PRE-ENROLLMENT SLIP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Gray)

                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "FORM CODE: IRD-PAN-01-M3", fontSize = 9.sp, color = Color.Gray)
                    Text(text = "DATE: 2026-06-06", fontSize = 9.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Render parameters loaded from active Profile
                TemplateFieldBox(label = "APPLICANT REGISTERED NAME / आवेदकको नाम", value = active?.fullName ?: "_________________")
                TemplateFieldBox(label = "CITIZENSHIP NUMBER / नागरिकता देशक नम्बर", value = active?.citizenshipNumber ?: "_________________")
                TemplateFieldBox(label = "BORN DATE (BS/AD) / जन्म मिति", value = active?.dob ?: "_________________")
                TemplateFieldBox(label = "REGISTRY DISTRICT / जिल्ला सुची", value = active?.district ?: "_________________")
                TemplateFieldBox(label = "CONTACT LOG / फोन र इमेल", value = "${active?.phone ?: "_____________"} / ${active?.email ?: "_____________"}")

                Spacer(modifier = Modifier.height(14.dp))
                if (active == null) {
                    Text(
                        text = "⚠ PROFILE EMPTY: Save credentials above to see them instantly auto-filled in the government slip simulator.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        text = "✓ AUTOFILL PERFECT. Ready to export, copy or sync to Department of Customs/Internal Revenue portals securely without re-typing.",
                        fontSize = 11.sp,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun TemplateFieldBox(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, fontSize = 9.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFEBE9), shape = RoundedCornerShape(4.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}


// ==========================================
// SCREEN 5: DEVELOPER ARCHITECTURE SPEC SHEET
// ==========================================
@Composable
fun DeveloperSpecsScreen(viewModel: SewaViewModel) {
    val currentLang = viewModel.currentLang

    // Internal tab index state for architectural docs subsections
    var specTab by remember { mutableStateOf("architecture") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = if (currentLang == "en") "Project Architectural Spec Sheets" else "आयोजना प्राविधिक दस्तावेज",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (currentLang == "en") "Actionable deliverables for systems integration, data models, APIs, and microservices plans." 
                       else "सरकारी एकीकरण, डेटा मोडेल, एपीआई र माइक्रोसेवा सम्बन्धी कागजात।",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
        }

        // Subtabs selection switcher
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val subtabs = listOf(
                Pair("architecture", if (currentLang == "en") "System Architecture" else "प्रणाली खाका"),
                Pair("api", if (currentLang == "en") "API Contracts" else "API सम्झौता"),
                Pair("schema", if (currentLang == "en") "Data Models" else "डेटाबेस मोडेल"),
                Pair("governance", if (currentLang == "en") "Governance & Roadmap" else "शासीत र रोडम्याप")
            )
            items(subtabs) { (id, label) ->
                val active = specTab == id
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .clickable { specTab = id }
                        .testTag("specs_tab_$id")
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (active) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Divider(color = MaterialTheme.colorScheme.outlineVariant)

        // Contents Box based on selected spec subtab
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            when (specTab) {
                "architecture" -> {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "System Microservices Blueprint", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(
                            text = "To scale successfully across 30 Million citizens and maintain high accessibility, Sarkari Sewa adopts a decoupled Serverless Gateway architecture.",
                            fontSize = 13.sp
                        )

                        // Visual diagram rendering in text
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Black, shape = RoundedCornerShape(8.dp))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = """
                                [ Citizen Mobile / Web Client ]
                                               | (HTTPS requests via Client-SDK)
                                               v
                                [ AWS / Cloudflare Serverless Gateway ]
                                               | (JWT Authentication / Rate Limiter)
                                               +-------------------+
                                               |                   |
                                               v                   v
                                     [ Catalog Sync Service ] [ Civil Status Engine ]
                                               |                   |
                                               v                   v
                                     [ Web-Scraper Cache ]   [ Headless API Hooks ]
                                               |                   |
                                               v                   v
                                     (Official Portals)      (District Gov DBs)
                                """.trimIndent(),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = Color.Green,
                                lineHeight = 14.sp
                            )
                        }

                        Text(
                            text = "Core Components:\n" +
                                   "1. API Gateway: Enforces OAuth checks, parses Nepali unicode search payloads.\n" +
                                   "2. Web Scraper Cache: Periodically queries MoFA passport and TMIS transit centers. Refreshes every 24 hours to prevent official portal load.\n" +
                                   "3. Status Checking System: Headless API connection mapping.",
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
                "api" -> {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "REST API Blueprint Contracts", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(
                            text = "Standard API endpoints deployed at api.sarkarisewa.gov.np for federating citizen query lookups.",
                            fontSize = 13.sp
                        )

                        Text(text = "1. GET /api/v1/services", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(6.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = """
                                {
                                  "status": "success",
                                  "count": 6,
                                  "data": [
                                    {
                                      "id": "passport",
                                      "title_en": "E-Passport Application",
                                      "title_ne": "विद्युतीय राहदानी आवेदन",
                                      "fee": 5000,
                                      "time_est": "15 days"
                                    }
                                  ]
                                }
                                """.trimIndent(),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = Color(0xFFE0E2E5)
                            )
                        }

                        Text(text = "2. POST /api/v1/applications/status", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(6.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = """
                                {
                                  "tracking_number": "NID-9912",
                                  "service_id": "national_id",
                                  "status_en": "Card printing completed",
                                  "progress_percent": 85
                                }
                                """.trimIndent(),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = Color(0xFFE0E2E5)
                            )
                        }
                    }
                }
                "schema" -> {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "Local Database Mapping Schemas", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(
                            text = "To guarantee offline usage across rural high-altitude regions of Nepal, the client utilizes Room SQLite schemas.",
                            fontSize = 13.sp
                        )

                        Text(text = "A. CitizenProfile Table Structure", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEFEBE9), shape = RoundedCornerShape(6.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = """
                                CREATE TABLE citizen_profiles (
                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    fullName TEXT NOT NULL,
                                    dob TEXT NOT NULL,
                                    citizenshipNumber TEXT UNIQUE,
                                    district TEXT,
                                    email TEXT,
                                    phone TEXT
                                );
                                """.trimIndent(),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = Color.Black
                            )
                        }

                        Text(text = "B. TrackedApplication Table Structure", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEFEBE9), shape = RoundedCornerShape(6.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = """
                                CREATE TABLE tracked_applications (
                                    trackingNumber TEXT PRIMARY KEY,
                                    serviceId TEXT NOT NULL,
                                    applicantName TEXT,
                                    serviceNameEn TEXT,
                                    progressPercent INTEGER,
                                    lastChecked TEXT
                                );
                                """.trimIndent(),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
                "governance" -> {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "Governance, Security & Project Roadmap", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        
                        Text(text = "Governance & Data Freshness", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(
                            text = "Government service guidelines change yearly based on parliamentary finance bills. " +
                                   "Sarkari Sewa enforces a 24-hour cache TTL policy. The server pushes gzip patches to clients to ensure correct fees are shown offline.",
                            fontSize = 12.sp
                        )

                        Text(text = "Phased Roadmap Timeline", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        
                        // Phase timeline
                        Surface(
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                TimelineItem(phase = "Phase 1: MVP Core (Current)", desc = "Static Service Catalog, Dual Language Toggle, Local Profiling Form-fill, Mock-connected Trackers database.")
                                TimelineItem(phase = "Phase 2: Secure Sync (Q3 2026)", desc = "SMS-OTP authentication integrations, Direct PDF form exports matching Department standards.")
                                TimelineItem(phase = "Phase 3: Wallet Registry (Q1 2027)", desc = "Biometric credential locker sandbox, automatic tax calculations.")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimelineItem(phase: String, desc: String) {
    Column {
        Text(text = phase, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
