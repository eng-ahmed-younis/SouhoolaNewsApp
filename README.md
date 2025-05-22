# News App - Android Jetpack Compose

A modern Android news application built with Jetpack Compose that fetches news articles from NewsAPI.org with advanced features like pagination, sorting, offline handling, and clean architecture following MVI pattern.

## Features

### Core Features
- Paginated News List: Infinite scrolling with efficient pagination
- Search Functionality: Search articles with real-time query
- Sorting Options: Sort by newest, relevance, or popularity
- Article Details: Detailed view with "Read More" and "Share" options
- API Authentication: Secure NewsAPI integration
- Connectivity Handling: Offline mode with connection status
- Pull-to-Refresh: Easy content refresh mechanism

### Technical Features  
- Clean Architecture: Separation of concerns with MVI pattern
- Reactive Programming: Coroutines and Flow for async operations
- Efficient Caching: Paging 3 library for optimized data loading
- Modern UI: Material 3 design system
- Responsive Design: Adaptive layouts for different screen sizes
- Error Handling: Comprehensive error states and retry mechanisms

## Architecture

This project follows Clean Architecture principles with MVI (Model-View-Intent) pattern for state management.

### Architecture Layers

```
com.souhoola.newsapp
├── data/                    # Data Layer
│   ├── base/               # Base classes & error handling
│   ├── dto/                # Data Transfer Objects
│   ├── mappers/            # DTO ↔ Domain mappers
│   ├── paging/             # Pagination implementation
│   ├── remote/             # API services
│   └── repository/         # Repository implementations
├── domain/                  # Domain Layer
│   ├── model/              # Domain models
│   ├── query/              # Query objects
│   ├── repository/         # Repository interfaces
│   └── use_case/           # Business logic use cases
├── ui/                      # Presentation Layer
│   ├── screens/            # UI screens with MVI
│   ├── navigation/         # Navigation setup
│   ├── components/         # Reusable UI components
│   └── theme/              # Material 3 theming
├── utils/                   # Utilities
│   ├── mvi/               # MVI base classes
│   └── dispatchers/       # Coroutine dispatchers
└── di/                      # Dependency Injection
```

### MVI Pattern Flow

```
┌─────────────┐    Intent    ┌──────────────┐    Action    ┌─────────────┐
│     View    │────────────▶│  ViewModel   │────────────▶│   Reducer   │
│             │              │              │              │             │
│  (Compose)  │              │    (MVI)     │              │  (State)    │
│             │◀─────────────│              │◀─────────────│             │
└─────────────┘    State     └──────────────┘    State     └─────────────┘
                                     │
                                     ▼ Effect
                             ┌──────────────┐
                             │ Side Effects │
                             │ (Navigation, │
                             │  Dialogs)    │
                             └──────────────┘
```

## Tech Stack & Libraries

### Core Android
- Kotlin - Programming language
- Jetpack Compose - Modern declarative UI toolkit
- Material 3 - Latest Material Design system
- Coroutines & Flow - Asynchronous programming

### Architecture & DI
- Hilt - Dependency injection framework
- ViewModel - UI-related data holder
- Navigation Compose - Type-safe navigation

### Networking & Data
- Ktor Client - HTTP client for API calls
- Kotlinx Serialization - JSON serialization/deserialization
- Paging 3 - Efficient data pagination
- Coil - Modern image loading library

### Development & Debugging  
- Chucker - Network inspection tool (debug builds)
- Kotlin DSL - Gradle build scripts

### Complete Dependencies

```kotlin
// Core Android
implementation(libs.androidx.core.ktx)
implementation(libs.androidx.lifecycle.runtime.ktx)
implementation(libs.androidx.activity.compose)
implementation(libs.androidx.lifecycle.viewmodel.compose)

// Compose BOM & UI
implementation(platform(libs.androidx.compose.bom))
implementation(libs.androidx.ui)
implementation(libs.androidx.ui.graphics)
implementation(libs.androidx.ui.tooling.preview)
implementation(libs.androidx.material3)
implementation(libs.androidx.material.icons.extended)

// Navigation
implementation(libs.androidx.navigation.compose)
implementation(libs.androidx.hilt.navigation.compose)

// Networking
implementation(libs.ktor.client.core)
implementation(libs.ktor.client.okhttp)
implementation(libs.ktor.client.content.negotiation)
implementation(libs.ktor.client.logging)
implementation(libs.ktor.serialization.kotlinx.json)

// Serialization
implementation(libs.kotlinx.serialization.json)
implementation(libs.kotlinx.serialization.core)

// Dependency Injection
implementation(libs.hilt.android)
kapt(libs.hilt.compiler)

// Paging
implementation(libs.androidx.paging.compose)
implementation(libs.androidx.paging.runtime)

// Image Loading
implementation(libs.coil.compose)

// Pull to Refresh
implementation(libs.compose.material3.pullrefresh)

// Debug Tools
debugImplementation(libs.chucker.library)
debugImplementation(libs.androidx.ui.tooling)
debugImplementation(libs.androidx.ui.test.manifest)
