package com.example.data

data class SarkariService(
    val id: String,
    val titleEn: String,
    val titleNe: String,
    val ministryEn: String,
    val ministryNe: String,
    val categoryEn: String,
    val categoryNe: String,
    val feeEn: String,
    val feeNe: String,
    val timeEn: String,
    val timeNe: String,
    val detailsEn: String,
    val detailsNe: String,
    val requiredDocsEn: List<String>,
    val requiredDocsNe: List<String>,
    val eligibilityEn: List<String>,
    val eligibilityNe: List<String>,
    val externalUrl: String,
    val stepGuideEn: List<String>,
    val stepGuideNe: List<String>
)

object CatalogData {
    val categoriesEn = listOf("All", "Identity & Citizenship", "Taxes & Finance", "Licenses & Transport", "Vital Records", "Utilities & Land")
    val categoriesNe = listOf("सबै", "परिचय र नागरिकता", "कर र वित्त", "अनुमति पत्र र यातायात", "व्यक्तिगत घटना दर्ता", "उपयोगिता र भूमि")

    // Realistic list of Nepal Government Portals & Services
    val services = listOf(
        SarkariService(
            id = "passport",
            titleEn = "E-Passport (Rahadani) Application",
            titleNe = "विद्युतीय राहदानी (राहदानी) आवेदन",
            ministryEn = "Department of Passports, Ministry of Foreign Affairs",
            ministryNe = "राहदानी विभाग, परराष्ट्र मन्त्रालय",
            categoryEn = "Identity & Citizenship",
            categoryNe = "परिचय र नागरिकता",
            feeEn = "NPR 12,000 (Express - 2 Days) / NPR 5,000 (Normal - 15 Days)",
            feeNe = "रु १२,००० (द्रुत सेवा - २ दिन) / रु ५,००० (साधारण - १५ दिन)",
            timeEn = "2 to 15 Working Days",
            timeNe = "२ देखि १५ कार्यदिन",
            detailsEn = "Apply for or renew your biometric electronic passport. Pre-enrollment must be completed online, following which biometric details are submitted at District Administration Offices (DAO) or the Passport Department.",
            detailsNe = "बायोमेट्रिक विद्युतीय राहदानीको लागि नयाँ वा नवीकरण आवेदन। अनलाइन मार्फत प्रिल-एनरोलमेन्ट फारम भरी जिल्ला प्रशासन कार्यालय वा राहदानी विभागमा बायोमेट्रिक्स बुझाउनुपर्नेछ।",
            requiredDocsEn = CastToList(
                "Original Nepali Citizenship Certificate",
                "National Identity Number (NID) Card or Printout",
                "Old Passport Booklet (if applying for renewal or replacement)",
                "Pre-enrollment Form Printout with Barcode QR"
            ),
            requiredDocsNe = CastToList(
                "नेपाली नागरिकताको प्रमाणपत्रको सक्कलप्रति",
                "राष्ट्रिय परिचयपत्र नम्बर (NID) वा छापिएको प्रति",
                "पुरानो राहदानी (नवीकरण वा स्थानान्तरणको हकमा)",
                "बारकोड क्युआर सहितको प्रिल-एनरोलमेन्ट फारम प्रिन्ट"
            ),
            eligibilityEn = CastToList(
                "Must be a bonafide Citizen of Nepal",
                "Must hold a verified National Identity Card/Number (NID)",
                "For minors (under 16), parents must provide original citizenship profile"
            ),
            eligibilityNe = CastToList(
                "नेपालको वास्तविक नागरिक हुनुपर्ने",
                "प्रमाणित राष्ट्रिय परिचयपत्र नम्बर (NID) हुनुपर्ने",
                "नाबालकको हकमा (१६ वर्ष मुनि), अभिभावकको नागरिकता र जन्मदर्ता आवश्यक"
            ),
            externalUrl = "https://pre-enrollment.nepalpassport.gov.np/",
            stepGuideEn = CastToList(
                "Step 1: Locate your National ID (NID) number.",
                "Step 2: Access the official Passport Department portal and choose Pre-enrollment.",
                "Step 3: Fill in your personal details, and lock your biometric appointment slot at your DAO.",
                "Step 4: Download and print the barcode sheet; pay the specified license fees.",
                "Step 5: Visit the selected centre on your appointment date for biometric capture."
            ),
            stepGuideNe = CastToList(
                "चरण १: आफ्नो राष्ट्रिय परिचयपत्र (NID) नम्बर प्राप्त गर्नुहोस्।",
                "चरण २: राहदानी विभागको पोर्टलमा गइ प्रिल-एनरोलमेन्ट विकल्प छनोट गर्नुहोस्।",
                "चरण ३: विवरणहरू भरी जिल्ला प्रशासन कार्यालयमा बायोमेट्रिक मिति र समय बुक गर्नुहोस्।",
                "चरण ४: प्राप्त क्युआर बारकोडसहितको फारम प्रिन्ट गरी राजस्व शूल्क तिर्नुहोस्।",
                "चरण ५: तोकिएको मितिमा बायोमेट्रिक्स दिन जिल्ला प्रशासन कार्यालयमा जानुहोस्।"
            )
        ),
        SarkariService(
            id = "national_id",
            titleEn = "National ID (Rastriya Parichayapatra)",
            titleNe = "राष्ट्रिय परिचयपत्र दर्ता",
            ministryEn = "Department of National ID and Civil Registration (DoNIDCR)",
            ministryNe = "राष्ट्रिय परिचयपत्र तथा पञ्जीकरण विभाग",
            categoryEn = "Identity & Citizenship",
            categoryNe = "परिचय र नागरिकता",
            feeEn = "Free (First placement) / NPR 1,000 (Replacement copy)",
            feeNe = "नि:शुल्क (पहिलो पटक) / रु १,००० (प्रतिलिपि वा हराएको हकमा)",
            timeEn = "15-30 days to generate NID Number",
            timeNe = "१५-३० दिन भित्र नम्बर प्राप्त हुने",
            detailsEn = "The National Identity Card is a biometric-enabled digital identification smart card required for obtaining passports, renewing driving licenses, and accessing voter lists in Nepal.",
            detailsNe = "राष्ट्रिय परिचयपत्र एक बायोमेट्रिक डिजिटल स्मार्ट कार्ड हो जुन राहदानी लिन, सवारी चालक अनुमति पत्र नवीकरण गर्न र नेपालमा मतदाता नामावली सूची सुरक्षित गर्न अनिवार्य छ।",
            requiredDocsEn = CastToList(
                "Original Nepali Citizenship Certificate",
                "Marriage Registration Certificate (for married individuals if surname changes)",
                "Birth Registration Certificate (for minors or where DOB lacks in Citizenship)",
                "Migration Certificate (if permanent address has officially migrated)"
            ),
            requiredDocsNe = CastToList(
                "नेपाली नागरिकताको प्रमाणपत्रको सक्कलप्रति",
                "विवाह दर्ता प्रमाणपत्र (विवाहित व्यक्तिको हकमा नागरिकतामा थर फरक भएमा)",
                "जन्मदर्ता प्रमाणपत्र (जन्ममिति स्पष्ट नभएकाहरूको हकमा वा नाबालक)",
                "बसाइँसराई दर्ता प्रमाणपत्र (स्थायी ठेगाना परिवर्तन भएको भएमा)"
            ),
            eligibilityEn = CastToList(
                "Citizen of Nepal",
                "Minimum age of 16 years",
                "Registration requires physical biometrics (fingerprints, iris, facial photo)"
            ),
            eligibilityNe = CastToList(
                "नेपाली नागरिक हुनुपर्ने",
                "न्यूनतम १६ वर्ष उमेर पुगेको हुनुपर्ने",
                "दर्ताको लागि औंठाछाप, आँखाको नानी (Iris) र फोटो खिच्न भौतिक उपस्थिति अनिवार्य"
            ),
            externalUrl = "https://nidpre-enrollment.donidcr.gov.np/",
            stepGuideEn = CastToList(
                "Step 1: Access the DoNIDCR Pre-enrollment system with a mobile OTP log.",
                "Step 2: Enter complete name, parents' details, and permanent address as on your Citizenship.",
                "Step 3: Select an active enrollment station (DAO or local ward camps).",
                "Step 4: Go to the station with your original citizenship for physical biometrics capturing.",
                "Step 5: Receive a confirmation sheet containing your NID number via mobile SMS."
            ),
            stepGuideNe = CastToList(
                "चरण १: राष्ट्रिय परिचयपत्र तथा पञ्जीकरण विभागको अनलाइन पोर्टल खोली मोबाइल ओटिपी मार्फत लगइन गर्नुहोस्।",
                "चरण २: नागरिकता अनुसारको पूरा नाम, बुबाआमाको नाम र स्थायी ठेगाना फारममा भर्नुहोस्।",
                "चरण ३: विवरणहरू बुझाउन र बायोमेट्रिक्स दिन जिल्ला प्रशासन कार्यालय वा स्थानीय शिविर छनौट गर्नुहोस्।",
                "चरण ४: सक्कल नागरिकता बोकी तोकिएको दर्ता केन्द्रमा पुगी औंठाछाप र आँखाको स्क्यान गराउनुहोस्।",
                "चरण ५: दर्ता सफल भएपछि एसएमएस मार्फत राष्ट्रिय परिचयपत्र ९NID० नम्बर प्राप्त गर्नुहोस्।"
            )
        ),
        SarkariService(
            id = "pan_card",
            titleEn = "Personal PAN Card Application",
            titleNe = "व्यक्तिगत स्थायी लेखा नम्बर (PAN) आवेदन",
            ministryEn = "Inland Revenue Department (IRD)",
            ministryNe = "आन्तरिक राजस्व विभाग",
            categoryEn = "Taxes & Finance",
            categoryNe = "कर र वित्त",
            feeEn = "Free",
            feeNe = "नि:शुल्क",
            timeEn = "Same Day (Instant Registration)",
            timeNe = "एकै दिनमा (तत्काल दर्ता)",
            detailsEn = "Get your Personal Permanent Account Number (PAN) required for salaried employment, banking, financial transfers, and operating small businesses in Nepal. Fast registration process online.",
            detailsNe = "नोकरी, सरकारी जागिर, बैंकिङ कारोबार तथा नेपालमा व्यापार व्यवसाय संचालनका लागि अनिवार्य रहेको व्यक्तिगत स्थायी लेखा नम्बर (PAN) अनलाइनबाट नि:शुल्क लिनुहोस्।",
            requiredDocsEn = CastToList(
                "Digital Passport Size Photo (JPEG)",
                "Scanned copy of Nepali Citizenship Certificate (Front & Back)",
                "Proof of Address (Utility bill or ward verification, optional)"
            ),
            requiredDocsNe = CastToList(
                "डिजिटल राहदानी आकारको फोटो (JPEG)",
                "नागरिकताको अगाडि र पछाडिको स्क्यान्ड वा स्पष्ट तस्बिर",
                "ठेगाना प्रमाण (वडा सिफारिस वा उपयोगिताको बिल, ऐच्छिक)"
            ),
            eligibilityEn = CastToList(
                " Nepali citizens or non-residents earning taxable income in Nepal",
                "No minimum age requirement; legal minors who are salaried also qualify"
            ),
            eligibilityNe = CastToList(
                "नेपाली नागरिक वा नेपालमा करयोग्य आम्दानी कमाउने विदेशी",
                "उमेरको बन्देज छैन; नोकरी वा आम्दानी गर्ने नाबालकले पनि लिन सक्ने"
            ),
            externalUrl = "https://ird.gov.np/",
            stepGuideEn = CastToList(
                "Step 1: Go to the IRD Integrated Tax System website.",
                "Step 2: Choose the Taxpayer Portal registration for Personal PAN.",
                "Step 3: Fill out the application form with your citizenship info and upload a photo.",
                "Step 4: Select the closest Inland Revenue Office (IRO - आन्तरिक राजस्व कार्यालय) location.",
                "Step 5: Submit form, visit the selected IRO with original citizenship to collect your printed laminated PAN card same-day."
            ),
            stepGuideNe = CastToList(
                "चरण १: आन्तरिक राजस्व विभागको औपचारिक Taxpayer Portal वेबसाइटमा जानुहोस्।",
                "चरण २: रजिष्ट्रेसन मेनु भित्र 'Taxpayer Registration' मा गइ 'Personal PAN' छनोट गर्नुहोस्।",
                "चरण ३: युजरनेम, पासवर्ड र कार्यालय छनौट गरी व्यक्तिगत र व्यावसायिक विवरणहरू भर्नुहोस्।",
                "चरण ४: नागरिकताको तस्बिर र आफ्नो पिपि साइज तस्बिर डिजिटल रूपमा अपलोड गर्नुहोस्।",
                "चरण ५: सबमिट गरी प्राप्त कागजातको प्रिन्ट लिई सम्बन्धित कर कार्यालयमा गइ सोही दिन प्यान कार्ड लिनुहोस्।"
            )
        ),
        SarkariService(
            id = "driving_license",
            titleEn = "Driving License Application / Renewal",
            titleNe = "सवारी चालक अनुमति पत्र आवेदन/नवीकरण",
            ministryEn = "Department of Transport Management (DoTM)",
            ministryNe = "यातायात व्यवस्था विभाग",
            categoryEn = "Licenses & Transport",
            categoryNe = "अनुमति पत्र र यातायात",
            feeEn = "NPR 1,500 to 4,000 depending on vehicle categories",
            feeNe = "सवारी वर्ग अनुसार रु १,५०० देखि रु ४,००० सम्म",
            timeEn = "Written test (within 3 days), Trial exam (within 7 days), Card dispatch (varies)",
            timeNe = "लिखित परीक्षा (३ दिन भित्र), प्रयोगात्मक (ट्रायल) (७ दिन भित्र), स्मार्ट कार्ड (समय लाग्ने)",
            detailsEn = "Apply for a new driving license or renew your existing license in Nepal. The application must be initiated online and followed by biometric verification split by province transport offices.",
            detailsNe = "नयाँ सवारी चालक अनुमति पत्र लिन वा पुरानो नवीकरण गर्न आवेदन। नवीकरण वा नयाँ दुबै आवेदन यातायात व्यवस्था विभागको अनलाइन प्रणालीबाट शुरू हुन्छ।",
            requiredDocsEn = CastToList(
                "Nepali Citizenship Certificate or Passport",
                "Medical Report by an authorized doctor (usually available near the office, costing NPR 100-200)",
                "For Renewals: Old Driving License Card copy"
            ),
            requiredDocsNe = CastToList(
                "नेपाली नागरिकताको प्रमाणपत्र वा राहदानी",
                "यातायात कार्यालयमा रहेका डाक्टरद्वारा जारी आँखा र रक्त समूह सम्बन्धी मेडिकल रिपोर्ट",
                "नवीकरणको लागि: पुरानो सवारी चालक अनुमति पत्र कार्डको प्रतिलिपि"
            ),
            eligibilityEn = CastToList(
                "Minimum age of 16 for Category A (Motorcycle/Scooter)",
                "Minimum age of 18 for Category B (Car/Jeep)",
                "Must pass the designated medical vision and physical balance checks"
            ),
            eligibilityNe = CastToList(
                "वर्ग 'ए' (मोटरसाइकल/स्कुटर) को लागि न्यूनतम १६ वर्ष उमेर पुगेको हुनुपर्ने",
                "वर्ग 'बी' (कार/जिप) को लागि न्यूनतम १८ वर्ष उमेर पुगेको हुनुपर्ने",
                "तोकिएको आँखा जाँच (विजन टेस्ट) र शारीरिक सन्तुलन परीक्षण पास गरेको हुनुपर्ने"
            ),
            externalUrl = "https://applydl.dotm.gov.np/",
            stepGuideEn = CastToList(
                "Step 1: Log in to the Online Driving License Registration System (ODLRS).",
                "Step 2: Provide citizenship information, personal details, select a category and Transport Office.",
                "Step 3: Book an appointment and take a copy of the registration details sheet.",
                "Step 4: Go to the transport office for biometrics and pay trial/exam fees.",
                "Step 5: Pass the physical medical test, written test, and practical trial."
            ),
            stepGuideNe = CastToList(
                "चरण १: यातायात विभागको अनलाइन लाइसेन्स दर्ता प्रणाली (ODLRS) मा नयाँ प्रोफाइल खोल्नुहोस्।",
                "चरण २: आफ्नो व्यक्तिगत र नागरिकता विवरण भरी सवारी वर्ग (जस्तै क, ख) र पायक पर्ने यातायात कार्यालय रोज्नुहोस्।",
                "चरण ३: बायोमेट्रिक्सका लागि फुर्सदिलो मिति बुक गरी रसिद प्रिन्ट गर्नुहोस्।",
                "चरण ४: तोकिएको दिन कार्यालय पुगी बायोमेट्रिक्स दिनुहोस्, डाक्टरको मेडिकल फारम बुझाउनुहोस् र दस्तुर तिर्नुहोस्।",
                "चरण ५: लिखित परीक्षा र प्रयोगात्मक परीक्षा (Trial) पास गरी लाइसेन्स सुरक्षित गर्नुहोस्।"
            )
        ),
        SarkariService(
            id = "vital_registration",
            titleEn = "Birth / Marriage / Death Civil Register",
            titleNe = "व्यक्तिगत घटना दर्ता (जन्म, विवाह, मृत्यु)",
            ministryEn = "Local Ward Offices, Department of Civil Registration",
            ministryNe = "सम्बन्धित वडा कार्यालय, पञ्जीकरण शाखा",
            categoryEn = "Vital Records",
            categoryNe = "व्यक्तिगत घटना दर्ता",
            feeEn = "Free (if registered within 35 days) / NPR 50 - 100 fine (if delayed)",
            feeNe = "नि:शुल्क (३५ दिन भित्र दर्ता गरेमा) / रु ५०-१०० जरिवाना (३५ दिन कटेमा)",
            timeEn = "Same Day Dispatch at Ward",
            timeNe = "एकै दिनमा वडा कार्यालयबाट प्राप्त गर्न सकिने",
            detailsEn = "Apply online and complete in-person verification for vital life status milestones: birth, marriage, divorce, transition, and death registration. Managed by local municipal ward offices.",
            detailsNe = "जन्म, विवाह, सम्बन्ध विच्छेद, बसाइँसराई र मृत्यु जस्ता व्यक्तिगत घटनाहरू नगरपालिका वा गाँउपालिकाको वडा कार्यालयमा दर्ता गराउनुहोस्। ३५ दिन भित्र निःशुल्क दर्ता हुन्छ।",
            requiredDocsEn = CastToList(
                "Birth: Hospital birth notification discharge card or midwife certificate, parents' citizenship cards",
                "Marriage: Couple's citizenship cards, passport size photos, ward witness credentials",
                "Death: Doctor/Hospital death certificate or police report, informant's citizenship"
            ),
            requiredDocsNe = CastToList(
                "जन्म दर्ता: अस्पतालको जन्म रसिद वा स्थानीय खोप कार्ड, बुबाआमाको नागरिकताको प्रतिलिपि",
                "विवाह दर्ता: दुलाहा-दुलहीको सक्कल नागरिकता, दुबैको फोटो र साक्षीको वडा परिचयपत्र",
                "मृत्यु दर्ता: अस्पतालको मृत्यु प्रमाणपत्र वा सर्जमिन मुचुल्का, सूचकको नागरिकता"
            ),
            eligibilityEn = CastToList(
                "Events must have occurred within Nepal's jurisdiction or to Nepalese citizens abroad",
                "Informant must be a direct first-degree relative or family head residing in the address"
            ),
            eligibilityNe = CastToList(
                "घटनाहरू नेपालको सिमाना भित्र वा विदेशमा रहेका नेपाली परिवारमा घटेको हुनुपर्ने",
                "दर्ताको निवेदन दिने सूचक एकाघरको मुख्य व्यक्ति वा नजिकको नातेदार हुनुपर्ने"
            ),
            externalUrl = "https://public.donidcr.gov.np/",
            stepGuideEn = CastToList(
                "Step 1: Open the national Public Portal of DoNIDCR online registration.",
                "Step 2: Choose the corresponding vital register (Family Registration / Birth / Marriage).",
                "Step 3: Insert details of the child/couple/deceased as authenticated by original papers.",
                "Step 4: Receive a temporary Token ID on your mobile layout.",
                "Step 5: Visit your municipality ward office within 30 days and provide Token ID to collect your printed registration certificate."
            ),
            stepGuideNe = CastToList(
                "चरण १: पञ्जीकरण विभागको अनलाइन सिभिल व्यक्तिगत घटना दर्ता पोर्टल खोल्नुहोस्।",
                "चरण २: घटना अनुसार (जन्म, विवाह, बसाइँसराई वा मृत्यु) दर्ता विकल्प छनौट गरी विवरण भर्नुहोस्।",
                "चरण ३: अस्पताल वा पुरानो कागजात अनुसारको विवरण भर्नुहोस् र दर्ता सुरक्षित गर्नुहोस्।",
                "चरण ४: विवरण बुझाए पछि प्राप्त हुने अस्थायी टोकन नम्बर (Token ID) मोबाइलमा टिप्नुहोस्।",
                "चरण ५: ३० दिन भित्र टोकन आइडी लिएर आफ्नो वडा कार्यालयमा पुगी सचिव मार्फत छाप लागेको कागजात लिनुहोस्।"
            )
        ),
        SarkariService(
            id = "vehicle_tax",
            titleEn = "Vehicle Tax & Bluebook Renewal",
            titleNe = "सवारी कर तथा ब्लुबुक नवीकरण",
            ministryEn = "Ministry of Physical Infrastructure and Transport (Provincial Transport Offices)",
            ministryNe = "भौतिक पूर्वाधार तथा यातायात मन्त्रालय (प्रादेशिक यातायात कार्यालय)",
            categoryEn = "Licenses & Transport",
            categoryNe = "अनुमति पत्र र यातायात",
            feeEn = "Custom rate based on CC / Engine rating & registration province",
            feeNe = "सवारी साधनको ईन्जिन क्षमता (CC) र दर्ता भएको प्रदेशको नियम अनुसार",
            timeEn = "1-2 Working Days (Online confirmation / Counter Stamp)",
            timeNe = "१-२ कार्यदिन (अनलाइन भुक्तानी र रसिद प्रमाणित)",
            detailsEn = "Pay annual vehicle road tax, renew your Bluebook, and pay third-party insurance online. Systems vary slightly across provinces (Bagmati, Gandaki, Koshi, etc.).",
            detailsNe = "वार्षिक सवारी कर तिर्न र आफ्नो ब्लुबुक सवारी दर्ता किताब नवीकरण गर्न तथा तेस्रो पक्ष विमा अनलाइन मार्फत भुक्तानी गर्न दर्ता प्रक्रिया।",
            requiredDocsEn = CastToList(
                "Original Bluebook (सवारी दर्ता किताब)",
                "Third-Party Vehicle Insurance Policy receipt",
                "Pollution test clearance certificate (Green sticker for specific cities)"
            ),
            requiredDocsNe = CastToList(
                "सक्कल सवारी दर्ता किताब (ब्लुबुक)",
                "तेस्रो पक्ष सवारी बीमा गरेको बीमा पोलिसी रसिद",
                "प्रदुषण जाँच प्रमाणपत्र (सम्बन्धित शहरको हरियो स्टिकर जाँच)"
            ),
            eligibilityEn = CastToList(
                "Vehicle must be registered under provincial transport offices of Nepal",
                "Taxes must be cleared up to the current fiscal year to avoid late penalties"
            ),
            eligibilityNe = CastToList(
                "नेपालको कुनै पनि प्रादेशिक यातायात कार्यालय अन्तर्गत दर्ता भएको सवारी साधन",
                "जरिवानाबाट बच्न चालु आर्थिक वर्ष समाप्त हुनु अगावै सवारी कर तिरिएको हुनुपर्ने"
            ),
            externalUrl = "https://tmis.bagmati.gov.np/",
            stepGuideEn = CastToList(
                "Step 1: Open the Transport Management Information System (TMIS) of your registration province.",
                "Step 2: Enter your Vehicle registration series (e.g., Ba 3 Pa 9012 or Provincial equivalents).",
                "Step 3: Check your pending tax breakdown and select third-party insurance details.",
                "Step 4: Execute payment using national e-wallet gateways (eSewa, Khalti, ConnectIPS).",
                "Step 5: Visit the Transport Office or designated kiosk counter within 30 days to get your Bluebook physically stamped."
            ),
            stepGuideNe = CastToList(
                "चरण १: आफ्नो सवारी दर्ता भएको प्रदेशको सवारी कर भुक्तानी पोर्टल (TMIS) खोल्नुहोस्।",
                "चरण २: सवारी साधन दर्ता नम्बर (जस्तैः बा ३ प ९०१२) वा नयाँ प्रादेशिक दर्ता ढाँचा प्रविष्ट गर्नुहोस्।",
                "चरण ३: बक्यौता करको हिसाव चेक गर्नुहोस् र तेस्रो पक्ष बिमा अनलाइन नवीकरण गर्नुहोस्।",
                "चरण ४: कनेक्ट-आइपीएस (ConnectIPS) वा मोबाइल वालेट मार्फत कर रकम भुक्तान गर्नुहोस्।",
                "चरण ५: भुक्तानी रसिद सुरक्षित राखी आफ्नो सवारी दर्ता किताब (ब्लुबुक) मा छाप लगाउन यातायात कार्यालय जानुहोस्।"
            )
        )
    )

    fun CastToList(vararg items: String): List<String> {
        return items.toList()
    }
}
