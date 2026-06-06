# Nepal Government Services Hub — Specification and Deliverables

This specification provides a complete architecture blueprint, data governance strategy, API contract, and roadmap for **Sarkari Sewa (Government Services Hub)**: a citizen-centric platform aimed at aggregating and accelerating access to publicly available government services in Nepal.

---

## 1. Scope and Target Users

### Target Users
*   **Casual Citizens (General Public)**: Users with basic digital literacy seeking info or simple service tracking (e.g., Passport status, driving license fee info, citizenship verification documents).
*   **Power Users (Service Facilitators, Cyber Cafés, Accounting Clerks)**: Users who process applications for others, requiring automation, forms auto-fill, offline tracking, and batched document preparation.
*   **Marginalized & Rural Groups**: Citizens with low-bandwidth, limited internet connectivity, or language barriers. Offline-first architecture and dual-language (Nepali & English) controls are essential here.

### Scope Boundaries
*   **Aggregator & Accelerator (Not Replacement)**: Connects and simplifies existing portals without attempting to replace backend government databases. Uses secure APIs or headless automation integration where APIs do not exist.
*   **Offline-First Cache**: All service requirements, local office contact details, and document templates are cached locally on the device.

---

## 2. Core Features (MVP to Phase 3)

1.  **Unified Service Catalog & Federated Search**: Fuzzy search that parses service descriptions and metadata in both Nepali and English (e.g., searching "राहदानी" or "Passport" yields the same accurate information).
2.  **Interactive Eligibility Checker**: Step-by-step smart tree indicating if a user is qualified to apply, required fees, and mandatory documents.
3.  **Local Application Status Tracking**: Room-persisted dashboard tracking multiple application serial numbers (e.g., Passport tracking, NITC PAN approval), showing step progress.
4.  **Forms Auto-Fill & Local Profiler**: Citizen profile saved securely on-device with custom templates (e.g., PAN form, Birth registration request).
5.  **Offline Cache & Sync**: Keeps data accessible when commuting or in low-signal rural areas.
6.  **Accessibility (WCAG 2.1)**: Dual-language switch (Nepali ⇄ English), standard 48dp+ tap targets, high contrast contrast indicators, and Screen Reader support.

---

## 3. Technical Stack & Architecture

```
                                  +-------------------------------------+
                                  |           Android Client            |
                                  |    (Jetpack Compose, Room, MVVM)    |
                                  +------------------+------------------+
                                                     | HTTPS
                                                     v
                                  +-------------------------------------+
                                  |        Serverless API Gateway       |
                                  |         (Rate Limiting, JWT)        |
                                  +------------------+------------------+
                                                     |
                    +--------------------------------+-------------------------------+
                    |                                |                               |
                    v                                v                               v
+---------------------------------------+ +------------------------------------+ +-----------------------------+
|        Citizen Microservice           | |         Catalog Scraper/Sync       | |    Status Checker Engine    |
| (Forms, Templates, Profiles - Dynamo) | |  (Rest API / Headless Automation)  | |  (External Government APIs) |
+---------------------------------------+ +------------------------------------+ +-----------------------------+
```

### Front-End Android Native
*   **Framework**: Jetpack Compose (Declarative UI) with Material Design 3.
*   **State Management**: Kotlin Coroutines, StateFlow, LiveData paired with MVVM lifecycle ViewModel.
*   **Local Caching**: SQLite fully managed via **Room Database Library**.

### Back-End / Edge Services
*   **API Gateway**: Lightweight Serverless Routing, rate-limiting, and standard CORS.
*   **API Framework**: Node.js Ktor or FastAPI for rapid routing.
*   **Authentication**: Passwordless on-device cryptographic verification, falling back to simple OAuth + OTP (SMS-based).

---

## 4. API Blueprint (High-Level REST Guidelines)

### 1. GET `/api/v1/services`
Fetches all aggregated services.
```json
{
  "status": "success",
  "data": [
    {
      "id": "passport_new",
      "title_en": "E-Passport Application",
      "title_ne": "विद्युतीय राहदानी आवेदन",
      "ministry": "Ministry of Foreign Affairs",
      "fee": 5000,
      "time_est": "15 working days",
      "category": "Citizenship & Identity"
    }
  ]
}
```

### 2. POST `/api/v1/applications/status`
Queries the live status of an active application.
```json
{
  "tracking_number": "NE-90872611",
  "service_id": "passport_new",
  "current_status": "PRINTING_COMPLETED",
  "status_message_en": "Your passport is printed and dispatched to the district administrative office.",
  "status_message_ne": "तपाईंको राहदानी छापिएको छ र सम्बन्धित जिल्ला प्रशासन कार्यालयमा पठाइएको छ।",
  "last_updated": "2026-06-05T14:30:00Z"
}
```

---

## 5. Detailed Data Model (Local Room Schema)

```sql
-- TrackedServices table
CREATE TABLE IF NOT EXISTS tracked_services (
    trackingNumber TEXT PRIMARY KEY NOT NULL,
    serviceId TEXT NOT NULL,
    applicantName TEXT NOT NULL,
    statusTextEn TEXT NOT NULL,
    statusTextNe TEXT NOT NULL,
    serviceNameEn TEXT NOT NULL,
    serviceNameNe TEXT NOT NULL,
    progressPercent INTEGER NOT NULL,
    lastChecked TEXT NOT NULL
);

-- CitizenProfile table
CREATE TABLE IF NOT EXISTS citizen_profiles (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    fullName TEXT NOT NULL,
    dob TEXT NOT NULL,
    citizenshipNumber TEXT NOT NULL,
    district TEXT NOT NULL,
    email TEXT NOT NULL,
    phone TEXT NOT NULL
);
```

---

## 6. Phased Roadmap

*   **Phase 1 (MVP)**: Service information indexer (Search/Catalog in Dual Language), Local profile saving, Static Eligibility wizard, basic Status tracking engine, full Offline-first database caching.
*   **Phase 2**: Live verification hooks with Nepal Gov portal scraping, Forms Autofill PDF exporters, local push notification alerts for key status changes.
*   **Phase 3**: Dynamic document upload vault (encrypted on-device local storage), integration with SMS-based OTP login trackers, and offline offline mesh file transfers for remote mountains.
