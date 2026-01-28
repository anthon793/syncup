# SyncUp 📱

<p align="center">
  <strong>A Modern Team Collaboration App for Students</strong>
</p>

<p align="center">
  Built with Kotlin • Jetpack Compose • Material Design 3
</p>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Problem Statement](#-problem-statement)
- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Architecture](#-architecture)
- [Screens & Navigation](#-screens--navigation)
- [Key Components](#-key-components)
- [Getting Started](#-getting-started)
- [Project Structure](#-project-structure)
- [Demo](#-demo)
- [Future Enhancements](#-future-enhancements)

---

## 🎯 Overview

**SyncUp** is a mobile application designed to streamline team collaboration for university students working on group projects. The app provides a centralized platform for task management, team communication, progress tracking, and milestone management.

### Key Value Propositions
- **Unified Platform** - Single destination for all project activities
- **Smart Task Management** - Intuitive Kanban boards with quick status updates
- **Real-time Collaboration** - Team chat and activity feeds
- **Progress Tracking** - Visual milestones and completion metrics
- **Role-based Access** - Different permissions for leads, members, and viewers

---

## 🔍 Problem Statement

University students often struggle with coordinating group projects due to:

| Challenge | Impact |
|-----------|--------|
| **Scattered Communication** | Messages spread across WhatsApp, Discord, email |
| **No Task Visibility** | Unclear who's doing what and when |
| **Progress Blindness** | No way to track overall project health |
| **Deadline Chaos** | Missed milestones and last-minute rushes |
| **Contribution Tracking** | Difficult to see individual contributions |

### Our Solution

SyncUp consolidates all project management needs into one mobile-first experience, giving students a clear view of tasks, teammates, and timelines.

---

## ✨ Features

### 🏠 Dashboard (Home)
| Feature | Description |
|---------|-------------|
| **Priority Project Card** | Highlights urgent projects with countdown timers |
| **Active Teammates** | Shows who's online and currently working |
| **Focus Projects** | Quick access to active projects with progress indicators |
| **Quick Actions Menu** | Floating action button with shortcuts |
| **Notifications Panel** | Expandable center for all updates |

### ✅ Task Management
| Feature | Description |
|---------|-------------|
| **Kanban Board** | Tasks organized by To-Do, Doing, Done, Backlog |
| **Quick Status Toggle** | One-tap status changes on task cards |
| **Task Creation** | Dialog with title, description, priority, assignee |
| **Priority Indicators** | Visual tags for Critical, Medium, Low |
| **Personal Highlights** | Cards highlight your assigned tasks |

### 💬 Team Collaboration
| Feature | Description |
|---------|-------------|
| **Activity Feed** | Chronological feed of team activities |
| **Post Updates** | Share comments, files, or milestone achievements |
| **File Sharing** | Quick access chips for shared documents |
| **Blocker Alerts** | Visual highlight for items needing attention |
| **Milestone Celebrations** | Celebrate completed milestones |

### 👥 Team Management
| Feature | Description |
|---------|-------------|
| **Member Directory** | View all team members with statistics |
| **Sub-Group Cards** | Organize team into functional groups |
| **Progress Actions** | Quick +10%, +25%, Complete buttons |
| **Workload Indicators** | Visual workload levels per member |
| **Capacity Tracking** | Monitor group capacity and availability |

### 📊 Project Management (Advanced)
| Feature | Description |
|---------|-------------|
| **Project Overview** | Comprehensive statistics dashboard |
| **Milestone Tracking** | Expandable cards with task lists |
| **Role-based Chat** | Real-time messaging with permissions |
| **Task Status Dropdown** | Detailed status management |

---

## 🛠 Technology Stack

### Core Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| **Kotlin** | 1.9.22 | Primary programming language |
| **Jetpack Compose** | 1.5.8 | Modern declarative UI framework |
| **Material Design 3** | Latest | UI components and theming |
| **Android SDK** | 34 | Target platform |
| **Min SDK** | 24 | Minimum Android 7.0 support |

### Architecture & Patterns

| Component | Implementation |
|-----------|----------------|
| **Architecture** | MVVM + Clean Architecture |
| **Dependency Injection** | Hilt 2.48.1 |
| **Async Operations** | Kotlin Coroutines + Flow |
| **State Management** | StateFlow + MutableStateListOf |
| **Navigation** | Jetpack Navigation Compose |

### Key Dependencies

```kotlin
// Compose BOM - Manages all Compose versions
implementation(platform("androidx.compose:compose-bom:2024.02.00"))

// Core Android
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
implementation("androidx.activity:activity-compose:1.8.2")

// Jetpack Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")

// Navigation
implementation("androidx.navigation:navigation-compose:2.7.6")
implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

// Dependency Injection
implementation("com.google.dagger:hilt-android:2.48.1")
kapt("com.google.dagger:hilt-compiler:2.48.1")

// Networking (Backend Ready)
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
```

---

## 🏗 Architecture

SyncUp follows **Clean Architecture** principles with clear separation between layers:

```
┌──────────────────────────────────────────────────────────────────┐
│                      PRESENTATION LAYER                          │
│  ┌──────────────┐  ┌──────────────┐  ┌────────────────────────┐  │
│  │   Screens    │  │  ViewModels  │  │      Components        │  │
│  │  (Compose)   │  │   (State)    │  │    (Reusable UI)       │  │
│  └──────────────┘  └──────────────┘  └────────────────────────┘  │
│         UI rendering        State holders      Shared widgets    │
└──────────────────────────────────────────────────────────────────┘
                               │
                    StateFlow / Events
                               │
                               ▼
┌──────────────────────────────────────────────────────────────────┐
│                        DOMAIN LAYER                              │
│  ┌──────────────┐  ┌──────────────┐  ┌────────────────────────┐  │
│  │    Models    │  │   Services   │  │      Use Cases         │  │
│  │  (Entities)  │  │  (Business)  │  │    (Interactors)       │  │
│  └──────────────┘  └──────────────┘  └────────────────────────┘  │
│      Data classes      Logic        Single responsibility ops   │
└──────────────────────────────────────────────────────────────────┘
                               │
                     Repository Interface
                               │
                               ▼
┌──────────────────────────────────────────────────────────────────┐
│                         DATA LAYER                               │
│  ┌──────────────┐  ┌──────────────┐  ┌────────────────────────┐  │
│  │  Repository  │  │    Remote    │  │        Local           │  │
│  │    (Impl)    │  │    (API)     │  │     (Database)         │  │
│  └──────────────┘  └──────────────┘  └────────────────────────┘  │
│    Data operations    Network calls     Room persistence        │
└──────────────────────────────────────────────────────────────────┘
```

### Data Flow

```
User Action → ViewModel → Repository → Data Source
                 ↓
            State Update
                 ↓
         Compose Recomposition → UI Update
```

### State Management Strategy

| State Type | Implementation | Usage |
|------------|----------------|-------|
| **UI State** | `StateFlow<UiState>` | Screen-level state |
| **List State** | `mutableStateListOf<T>()` | Dynamic lists (tasks, activities) |
| **Form State** | `mutableStateOf<T>()` | Input fields, dialogs |
| **Navigation** | `NavController` | Screen transitions |

---

## 📱 Screens & Navigation

### Navigation Architecture

```
                         ┌─────────────┐
                         │ SplashScreen│
                         └──────┬──────┘
                                │
                                ▼
                         ┌─────────────┐
                         │ LoginScreen │
                         └──────┬──────┘
                                │ (Authentication)
                                ▼
┌───────────────────────────────────────────────────────────────────┐
│                          MainScreen                               │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │                    Content Area                             │  │
│  │  ┌─────────┐ ┌─────────┐ ┌───────────┐ ┌─────────┐         │  │
│  │  │Dashboard│ │  Tasks  │ │Collaborate│ │  Team   │         │  │
│  │  └─────────┘ └─────────┘ └───────────┘ └─────────┘         │  │
│  └─────────────────────────────────────────────────────────────┘  │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │   🏠        ✅         💬          👥     Bottom Nav        │  │
│  └─────────────────────────────────────────────────────────────┘  │
└───────────────────────────────────────────────────────────────────┘
         │
    Side Navigation
         │
    ├── Profile
    ├── Project Management → ┌─────────────────────┐
    │                        │ ProjectManagement   │
    │                        │  ├─ Tasks Tab       │
    │                        │  ├─ Milestones Tab  │
    │                        │  ├─ Chat Tab        │
    │                        │  └─ Overview Tab    │
    │                        └─────────────────────┘
    ├── Invite Teammates
    └── Settings
```

### Screen Details

#### 1. Login Screen (`LoginScreen.kt`)

**Visual Design:**
- Animated gradient background (purple to blue)
- Floating decorative circles
- Pulsing logo animation
- Modern card-based input fields

**Functionality:**
- Email/password authentication
- Password visibility toggle
- Error handling with animated feedback
- Demo mode with preset credentials

**Code Highlight - Animated Logo:**
```kotlin
@Composable
fun AnimatedLogo() {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        )
    )
    // Logo with scale modifier
}
```

---

#### 2. Dashboard Screen (`DashboardScreen.kt`)

**Layout Structure:**
```
┌────────────────────────────────────────┐
│  👤 Profile    SyncUp     🔔  ☰       │  ← Header
├────────────────────────────────────────┤
│  🔍 Search projects...                 │  ← Search Bar
├────────────────────────────────────────┤
│  ┌──────────────────────────────────┐  │
│  │  🔴 PRIORITY                     │  │
│  │  CS 301 Final Project            │  │  ← Priority Card
│  │  Due in 2 days  ████░░ 45%       │  │
│  └──────────────────────────────────┘  │
├────────────────────────────────────────┤
│  Active Teammates                      │
│  (👤)(👤)(👤)(+2)  3 working now      │  ← Teammate Row
├────────────────────────────────────────┤
│  Focus Projects                        │
│  ┌────────┐ ┌────────┐ ┌────────┐     │
│  │Project1│ │Project2│ │Project3│     │  ← Horizontal Scroll
│  └────────┘ └────────┘ └────────┘     │
├────────────────────────────────────────┤
│                               ┌─────┐  │
│                               │ ➕  │  │  ← FAB (Quick Actions)
│                               └─────┘  │
└────────────────────────────────────────┘
```

**Key Components:**
- `NotificationsPanel` - Expandable notification list
- `QuickActionFab` - Multi-action floating button
- `FocusProjectCard` - Progress-showing project cards

---

#### 3. Task Board Screen (`TaskBoardScreen.kt`)

**Layout Structure:**
```
┌────────────────────────────────────────┐
│  ← Task Board              + Create    │  ← Header
├────────────────────────────────────────┤
│  [To-Do] [Doing] [Done] [Backlog]     │  ← Tab Row
├────────────────────────────────────────┤
│  ┌──────────────────────────────────┐  │
│  │  Research Paper                  │  │
│  │  📅 Jan 15  🏷️ CRITICAL          │  │
│  │  ┌─────┐ ┌─────┐ ┌─────┐        │  │  ← Task Card
│  │  │ ⏸️  │ │ ▶️  │ │ ✓  │        │  │  ← Status Toggle
│  │  └─────┘ └─────┘ └─────┘        │  │
│  └──────────────────────────────────┘  │
│                                        │
│  ┌──────────────────────────────────┐  │
│  │  Coming Up                       │  │
│  │  📹 Team Sync - Today 3:00 PM    │  │  ← Meeting Card
│  └──────────────────────────────────┘  │
└────────────────────────────────────────┘
```

**State Management:**
```kotlin
// Reactive task list using mutableStateListOf
val tasks = remember {
    mutableStateListOf<Task>(
        Task(id = "1", title = "Research", status = TaskStatus.TODO, ...),
        Task(id = "2", title = "Design", status = TaskStatus.IN_PROGRESS, ...)
    )
}

// Status change triggers automatic recomposition
fun onStatusChange(taskId: String, newStatus: TaskStatus) {
    val index = tasks.indexOfFirst { it.id == taskId }
    if (index != -1) {
        tasks[index] = tasks[index].copy(status = newStatus)
    }
}
```

---

#### 4. Collaborate Screen (`CollaborateScreen.kt`)

**Layout Structure:**
```
┌────────────────────────────────────────┐
│  ← Team Pulse                          │  ← Header
├────────────────────────────────────────┤
│  ┌──────────────────────────────────┐  │
│  │  + Quick Post                    │  │  ← Quick Post Card
│  │  Share an update with your team  │  │
│  └──────────────────────────────────┘  │
├────────────────────────────────────────┤
│  📎 Shared Files                       │
│  [research.pdf] [design.fig] [+3]     │  ← File Chips
├────────────────────────────────────────┤
│  Activity Feed                         │
│  ┌──────────────────────────────────┐  │
│  │ 👤 Sarah completed "UI Design"   │  │
│  │    Just now  🎉 Milestone!       │  │  ← Activity Item
│  └──────────────────────────────────┘  │
│  ┌──────────────────────────────────┐  │
│  │ 👤 Mike flagged a blocker        │  │
│  │    2h ago  ⚠️ Needs attention    │  │  ← Blocker Alert
│  └──────────────────────────────────┘  │
└────────────────────────────────────────┘
```

**Post Types:**
| Type | Icon | Color | Use Case |
|------|------|-------|----------|
| Comment | 💬 | Blue | General updates |
| File | 📎 | Teal | Document sharing |
| Milestone | 🏆 | Gold | Achievement announcements |

---

#### 5. Team Screen (`TeamScreen.kt`)

**Layout Structure:**
```
┌────────────────────────────────────────┐
│  ← Team                                │  ← Header
├────────────────────────────────────────┤
│  Overall Completion: 67%               │
│  ████████████░░░░░░░                   │  ← Progress Bar
├────────────────────────────────────────┤
│  Team Members (6)                      │
│  ┌──────────────────────────────────┐  │
│  │ 👤 Sarah L.   🟢 Online   15 ✓   │  │
│  │ 👤 Mike R.    🟡 Busy     12 ✓   │  │  ← Member List
│  │ 👤 Emma K.    ⚫ Offline   8 ✓   │  │
│  └──────────────────────────────────┘  │
├────────────────────────────────────────┤
│  Sub-Groups                            │
│  ┌──────────────────────────────────┐  │
│  │  📐 Design Team          ▼      │  │
│  │  ────────────────────────────── │  │
│  │  Progress: 75%   👥 3 members   │  │  ← Expandable Card
│  │  ┌─────┐ ┌─────┐ ┌────────────┐ │  │
│  │  │+10% │ │+25% │ │ Complete ✓ │ │  │  ← Quick Actions
│  │  └─────┘ └─────┘ └────────────┘ │  │
│  └──────────────────────────────────┘  │
└────────────────────────────────────────┘
```

---

#### 6. Project Management Screen (`ProjectManagementScreen.kt`)

**Tab Navigation:**
| Tab | Content | Features |
|-----|---------|----------|
| **Tasks** | Task list with status dropdown | Change status, view details |
| **Milestones** | Expandable milestone cards | Progress tracking, task lists |
| **Chat** | Role-based messaging | Real-time chat, permission controls |
| **Overview** | Project statistics | Members, progress, deadlines |

**Chat Implementation:**
```kotlin
// Scrollable chat with auto-scroll to bottom
val listState = rememberLazyListState()

LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
        listState.animateScrollToItem(messages.size - 1)
    }
}

LazyColumn(state = listState) {
    items(messages) { message ->
        ChatMessageBubble(message = message, currentUserId = currentUser.id)
    }
}
```

---

## 🧩 Key Components

### Reusable UI Components

#### SimpleAvatar
Generates colorful avatars from user names.

```kotlin
@Composable
fun SimpleAvatar(
    name: String,
    size: Dp = 40.dp,
    isOnline: Boolean = false,
    showBorder: Boolean = false
)
```

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `name` | String | Required | User's name for initials |
| `size` | Dp | 40.dp | Avatar diameter |
| `isOnline` | Boolean | false | Show green online indicator |
| `showBorder` | Boolean | false | White border for stacking |

#### AvatarStack
Displays overlapping avatar group.

```kotlin
@Composable
fun AvatarStack(
    names: List<String>,
    maxDisplay: Int = 3,
    avatarSize: Dp = 32.dp
)
```

#### QuickStatusToggle
Three-button status switcher for task cards.

```kotlin
@Composable
fun QuickStatusToggle(
    currentStatus: TaskStatus,
    currentTab: BoardTab,
    onStatusChange: (TaskStatus) -> Unit
)
```

### Domain Models

#### Task
```kotlin
data class Task(
    val id: String,
    val projectId: String,
    val title: String,
    val description: String?,
    val assignedTo: String?,
    val status: TaskStatus,        // TODO, IN_PROGRESS, DONE, BACKLOG
    val priority: TaskPriority,    // CRITICAL, MEDIUM, LOW
    val dueDate: Long?,
    val labels: List<String>
)
```

#### RoleBasedUser
```kotlin
data class RoleBasedUser(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole  // ADMIN, MEMBER, VIEWER
) {
    fun canSendMessages(): Boolean = role != UserRole.VIEWER
    fun canEditTasks(): Boolean = role in listOf(UserRole.ADMIN, UserRole.MEMBER)
    fun canManageMembers(): Boolean = role == UserRole.ADMIN
}
```

#### Activity
```kotlin
data class Activity(
    val id: String,
    val projectId: String,
    val userId: String,
    val userName: String,
    val action: String,
    val target: String,
    val timestamp: String,
    val type: ActivityType  // TASK_UPDATE, MESSAGE, MILESTONE, FILE_UPLOAD
)
```

---

## 🚀 Getting Started

### Prerequisites

| Requirement | Version |
|-------------|---------|
| Android Studio | Hedgehog (2023.1.1)+ |
| JDK | 17 |
| Android SDK | 34 |
| Kotlin | 1.9.22 |
| Gradle | 8.2 |

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/syncup.git
   cd syncup
   ```

2. **Open in Android Studio**
   - File → Open → Select the `syncup` folder
   - Wait for Gradle sync to complete

3. **Build the project**
   ```bash
   # Windows
   set JAVA_HOME=C:\Program Files\Microsoft\jdk-17.0.17.10-hotspot
   .\gradlew.bat assembleDebug

   # macOS/Linux
   export JAVA_HOME=/path/to/jdk17
   ./gradlew assembleDebug
   ```

4. **Install on device**
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   adb shell am start -n com.syncup/.MainActivity
   ```

---

## 🎮 Demo

### Login Credentials
| Field | Value |
|-------|-------|
| Email | `admin@gmail.com` |
| Password | `admin123` |

### Demo Walkthrough

1. **Login** → Enter demo credentials
2. **Dashboard** → View priority projects, tap FAB for quick actions
3. **Tasks** → Switch tabs, tap status buttons to change task status
4. **Collaborate** → Tap "Quick Post" to add updates
5. **Team** → Expand sub-group cards, tap progress buttons
6. **Project Management** → Access via side menu, explore all tabs

---

## 📁 Project Structure

```
syncup/
├── app/
│   ├── src/main/
│   │   ├── java/com/syncup/
│   │   │   │
│   │   │   ├── data/                    # DATA LAYER
│   │   │   │   ├── local/               # Local database
│   │   │   │   │   └── entity/          # Room entities
│   │   │   │   ├── remote/              # Network layer
│   │   │   │   │   ├── SyncUpApi.kt     # Retrofit API interface
│   │   │   │   │   └── dto/             # Data transfer objects
│   │   │   │   └── repository/          # Repository implementations
│   │   │   │
│   │   │   ├── domain/                  # DOMAIN LAYER
│   │   │   │   ├── model/               # Business models
│   │   │   │   │   ├── Task.kt
│   │   │   │   │   ├── Project.kt
│   │   │   │   │   ├── User.kt
│   │   │   │   │   └── Activity.kt
│   │   │   │   ├── service/             # Business logic
│   │   │   │   │   └── RoleBasedChatService.kt
│   │   │   │   └── repository/          # Repository interfaces
│   │   │   │
│   │   │   ├── presentation/            # PRESENTATION LAYER
│   │   │   │   ├── components/          # Reusable UI components
│   │   │   │   │   └── SimpleAvatar.kt
│   │   │   │   ├── mock/                # Mock data for demos
│   │   │   │   │   └── MockData.kt
│   │   │   │   ├── navigation/          # Navigation setup
│   │   │   │   │   └── Navigation.kt
│   │   │   │   ├── screen/              # Screen composables
│   │   │   │   │   ├── LoginScreen.kt
│   │   │   │   │   ├── DashboardScreen.kt
│   │   │   │   │   ├── TaskBoardScreen.kt
│   │   │   │   │   ├── CollaborateScreen.kt
│   │   │   │   │   ├── TeamScreen.kt
│   │   │   │   │   └── ProjectManagementScreen.kt
│   │   │   │   └── viewmodel/           # ViewModels
│   │   │   │       └── AuthViewModel.kt
│   │   │   │
│   │   │   ├── di/                      # Dependency Injection
│   │   │   │   └── AppModule.kt
│   │   │   │
│   │   │   ├── ui/theme/                # Theming
│   │   │   │   ├── Color.kt
│   │   │   │   ├── Theme.kt
│   │   │   │   └── Type.kt
│   │   │   │
│   │   │   ├── MainActivity.kt          # Entry point
│   │   │   └── SyncUpApp.kt             # Application class
│   │   │
│   │   └── res/
│   │       ├── drawable/                # Vector drawables
│   │       ├── mipmap-*/                # App icons (all densities)
│   │       └── values/
│   │           ├── colors.xml
│   │           ├── strings.xml
│   │           └── themes.xml
│   │
│   └── build.gradle.kts                 # App-level build config
│
├── gradle/
│   └── wrapper/
├── build.gradle.kts                     # Project-level build config
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

---

## 🎨 Design System

### Color Palette

| Color | Hex | Usage |
|-------|-----|-------|
| 🟢 Teal Primary | `#00897B` | Primary actions, FABs, headers |
| 🟢 Teal Dark | `#00695C` | Pressed states, accents |
| 🟢 Teal Light | `#4DB6AC` | Highlights, secondary elements |
| ⬜ Background | `#F8F9FA` | Screen backgrounds |
| ⬛ Dark Text | `#1A1A2E` | Headlines, primary text |
| ⬜ Secondary Text | `#6B7280` | Descriptions, labels |
| ⬜ Light Gray | `#9CA3AF` | Disabled states |
| 🟢 Success | `#22C55E` | Completed tasks, online |
| 🟡 Warning | `#F59E0B` | In-progress, attention |
| 🔴 Critical | `#EF4444` | Urgent items, errors |

### Design Principles

- **Clean Aesthetic**: Modern, minimalist UI with teal accents
- **Visual Consistency**: Unified color palette across all screens
- **Accessibility**: High contrast ratios for readability
- **Modern Material Design 3**: Following latest Android UI guidelines

### Typography Scale

| Style | Size | Weight | Usage |
|-------|------|--------|-------|
| Display | 32sp | Bold | Large headers |
| Headline | 24sp | Bold | Screen titles |
| Title | 20sp | SemiBold | Section headers |
| Body Large | 16sp | Regular | Primary content |
| Body | 14sp | Regular | Secondary content |
| Label | 12sp | Bold | Chips, badges |
| Caption | 10sp | Medium | Timestamps |

### Spacing Guidelines

| Token | Value | Usage |
|-------|-------|-------|
| `xs` | 4dp | Tight spacing |
| `sm` | 8dp | Component internal |
| `md` | 16dp | Standard padding |
| `lg` | 24dp | Section spacing |
| `xl` | 32dp | Large gaps |

---

## 🔮 Future Enhancements

### Phase 2 - Backend Integration
- [ ] Firebase Authentication for real users
- [ ] Cloud Firestore for real-time data sync
- [ ] Firebase Cloud Messaging for push notifications
- [ ] Cloud Storage for file uploads

### Phase 3 - Advanced Features
- [ ] Calendar integration with Google Calendar
- [ ] Video calling via WebRTC
- [ ] AI-powered task suggestions
- [ ] Analytics dashboard for insights
- [ ] Offline mode with background sync

### Phase 4 - Platform Expansion
- [ ] iOS version using Kotlin Multiplatform
- [ ] Web dashboard for desktop access
- [ ] VS Code extension for developers

---

## 📊 Technical Metrics

| Metric | Value |
|--------|-------|
| **Screens** | 8 main screens |
| **Components** | 25+ reusable composables |
| **Models** | 10+ domain entities |
| **Lines of Code** | ~5,000 LOC |
| **Min SDK** | Android 7.0 (API 24) |
| **Target SDK** | Android 14 (API 34) |

---

## 👥 Team

| Role | Responsibilities |
|------|-----------------|
| **Project Lead** | Architecture design, code review |
| **Mobile Developer** | Screen implementations, state management |
| **UI/UX Designer** | Design system, user flows |
| **QA Engineer** | Testing, bug identification |

---

## 📄 License

This project was developed as part of an academic capstone project.

© 2026 SyncUp Team. All rights reserved.

---

<p align="center">
  <strong>Built with ❤️ using Kotlin & Jetpack Compose</strong>
</p>

<p align="center">
  <em>Making student collaboration seamless, one project at a time.</em>
</p>
