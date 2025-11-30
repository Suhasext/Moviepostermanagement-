# CineVault - Personal Movie & Series Collection Manager

CineVault is an Android application designed for movie and web series enthusiasts to maintain a visual, organized collection of watched and to-watch content. The app transforms the traditional note-taking experience into an interactive poster gallery with rich metadata, ratings, and personalized organization features.

## Features

### Core Features (MVP)
- **Poster Gallery Management**: Upload and display movie/series posters in a responsive grid layout
- **Manual Content Entry**: Add movies and series with custom metadata
- **Content Details**: Display comprehensive metadata including plot, cast, director, genre, and runtime
- **Watched vs. Watchlist Management**: Organize content into "Watched" and "Watchlist" collections
- **Search Functionality**: Search through your collection by title
- **Content Detail View**: Detailed view with metadata and personal notes

### Technical Features
- **Offline Capable**: Core features work without internet connection
- **Local Storage**: Room database for metadata and file system for poster images
- **Modern UI**: Built with Jetpack Compose and Material Design 3
- **MVVM Architecture**: Clean separation of concerns with ViewModels and Repositories
- **Dependency Injection**: Hilt for managing dependencies

## Architecture

### Technology Stack
- **Platform**: Android (Minimum SDK: 26, Target SDK: 36)
- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **UI**: Jetpack Compose with Material Design 3
- **Database**: Room (SQLite wrapper)
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Coil
- **Dependency Injection**: Hilt
- **Navigation**: Jetpack Navigation Component

### Project Structure
```
app/src/main/java/com/example/moviepostermanagementapp/
├── data/
│   ├── local/           # Room database, DAOs
│   ├── remote/          # API services, repositories
│   ├── model/           # Data models and entities
│   └── repository/      # Repository implementations
├── ui/
│   ├── screens/         # Compose screens
│   ├── components/      # Reusable UI components
│   ├── viewmodel/       # ViewModels
│   └── theme/           # App theming
├── navigation/          # Navigation setup
├── di/                  # Dependency injection modules
└── MainActivity.kt       # Main activity
```

## Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 26 or higher
- Kotlin 2.0.21 or later

### Setup
1. Clone the repository
2. Open the project in Android Studio
3. Sync the project with Gradle files
4. Build and run the app

### Manual Content Entry
The app is designed to work completely offline with manual content entry:
1. Add movies and series by uploading poster images
2. Enter title, year, type (Movie/Series), and status (Watched/Watchlist)
3. Optionally add personal notes and ratings
4. All data is stored locally on your device

## Usage

### Adding Content
1. Tap the "+" floating action button
2. Select a poster image from your device
3. Enter the title and optional year
4. Choose content type (Movie/Series) and status (Watched/Watchlist)
5. Save the content

### Managing Your Collection
- **Main Screen**: Browse all content, watched items, or watchlist
- **Search**: Use the search icon to find specific content
- **Status Toggle**: Tap the status icon on any poster to move between collections
- **Detail View**: Tap any poster to view detailed information

### Content Organization
- **All**: View your entire collection
- **Watched**: Content you've already seen
- **Watchlist**: Content you plan to watch
- **Search**: Find specific titles across all collections

## Data Models

### ContentItem
The main data model representing a movie or series:
```kotlin
data class ContentItem(
    val id: String,
    val title: String,
    val posterPath: String,
    val type: ContentType, // MOVIE or SERIES
    val status: ContentStatus, // WATCHED or WATCHLIST
    val imdbRating: Float?,
    val rottenTomatoesScore: Int?,
    val description: String?,
    val releaseYear: Int?,
    val director: String?,
    val cast: List<String>,
    val genre: List<String>,
    val runtime: Int?,
    val userRating: Float?,
    val userNotes: String?,
    val dateAdded: Long,
    val dateWatched: Long?,
    val imdbId: String?
)
```

## Data Storage

### Local Storage
The app stores all data locally on your device:
- Poster images saved to internal storage
- Metadata stored in Room database
- No internet connection required
- Complete privacy - your data stays on your device

## Future Enhancements

### Planned Features (P1)
- Personal ratings and reviews
- Advanced search and filters
- Statistics dashboard
- Backup and restore functionality
- Dark mode support

### Advanced Features (P2)
- Custom collections and tags
- Social sharing capabilities
- API integration for automatic metadata fetching
- Streaming availability integration
- Recommendations engine
- Notifications and reminders

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Android team for Jetpack Compose and modern Android development tools
- Material Design team for the design system

## Support

For issues and feature requests, please create an issue in the GitHub repository.

---

**CineVault** - Your personal cinema companion for organizing and discovering movies and series.
