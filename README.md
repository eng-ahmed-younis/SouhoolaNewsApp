# 📰 News App - Android Jetpack Compose

Android news application built with **Jetpack Compose** that fetches news articles from [NewsAPI.org](https://newsapi.org/) with advanced features like pagination, sorting, offline handling, and clean architecture following MVI pattern.

## 🎯 Features

### Core Features
- ** Paginated News List**: Infinite scrolling with efficient pagination
- ** Search Functionality**: Search articles with real-time query
- ** Sorting Options**: Sort by newest, relevance, or popularity
- ** Article Details**: Detailed view with "Read More" and "Share" options
- ** API Authentication**: Secure NewsAPI integration
- ** Connectivity Handling**: Offline mode with connection status
- **♻ Pull-to-Refresh**: Easy content refresh mechanism

### Technical Features  
- ** Clean Architecture**: Separation of concerns with MVI pattern
- ** Reactive Programming**: Coroutines and Flow for async operations
- ** Efficient Caching**: Paging 3 library for optimized data loading
- ** Modern UI**: Material 3 design system
- ** Responsive Design**: Adaptive layouts for different screen sizes
- ** Error Handling**: Comprehensive error states and retry mechanisms

##  Architecture

This project follows **Clean Architecture** principles with **Simplified MVI (Model-View-Intent)** pattern for state management without traditional reducers.

### Architecture Layers

```
 com.souhoola.newsapp
├── 📂 data/                    # Data Layer
│   ├── 📂 base/               # Base classes & error handling
│   ├── 📂 dto/                # Data Transfer Objects
│   ├── 📂 mappers/            # DTO ↔ Domain mappers
│   ├── 📂 paging/             # Pagination implementation
│   ├── 📂 remote/             # API services
│   └── 📂 repository/         # Repository implementations
├── 📂 domain/                  # Domain Layer
│   ├── 📂 model/              # Domain models
│   ├── 📂 query/              # Query objects
│   ├── 📂 repository/         # Repository interfaces
│   └── 📂 use_case/           # Business logic use cases
├── 📂 ui/                      # Presentation Layer
│   ├── 📂 screens/            # UI screens with MVI
│   ├── 📂 navigation/         # Navigation setup
│   ├── 📂 components/         # Reusable UI components
│   └── 📂 theme/              # Material 3 theming
├── 📂 utils/                   # Utilities
│   ├── 📂 dispatchers/        # Coroutine dispatchers
│   └── 📂 connection/         # Network connectivity
└── 📂 di/                      # Dependency Injection
```

### Simple MVI Pattern Flow

```
┌─────────────┐    Intent    ┌──────────────┐ Direct State ┌─────────────┐
│     View    │────────────▶│  ViewModel   │─────Update───▶│    State    │
│             │              │              │               │             │
│  (Compose)  │              │ (Simplified  │               │ (StateFlow) │
│             │◀─────────────│    MVI)      │◀──────────────│             │
└─────────────┘    State     └──────────────┘               └─────────────┘
                                     │
                                     ▼ Side Effects
                             ┌──────────────┐
                             │   Effects    │
                             │ (Navigation, │
                             │ URL Opening, │
                             │  Sharing)    │
                             └──────────────┘
```

**Key Components:**
- **Intent**: User actions from UI (LoadArticle, ShareArticle, etc.)
- **ViewModel**: Processes intents and updates state directly
- **State**: Immutable data class representing UI state
- **Side Effects**: One-time actions like navigation, opening URLs

## 🛠️ Tech Stack & Libraries

### Core Android
- **Kotlin** - Programming language
- **Jetpack Compose** - Modern declarative UI toolkit
- **Material 3** - Latest Material Design system
- **Coroutines & Flow** - Asynchronous programming

### Architecture & DI
- **Hilt** - Dependency injection framework
- **ViewModel** - UI-related data holder
- **Navigation Compose** - Type-safe navigation

### Networking & Data
- **Ktor Client** - HTTP client for API calls
- **Kotlinx Serialization** - JSON serialization/deserialization
- **Paging 3** - Efficient data pagination
- **Coil** - Modern image loading library

### Development & Debugging  
- **Chucker** - Network inspection tool (debug builds)
- **Kotlin DSL** - Gradle build scripts

