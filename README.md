# TaskMaster2

TaskMaster2 is a powerful and intuitive Android application designed to help users manage their daily tasks and notes efficiently. Built with modern Android development practices, it offers a clean interface and robust functionality.

## 🚀 Features

- **Task Management:** Create, read, update, and delete notes or tasks with ease.
- **Priority Levels:** Assign priorities (High, Medium, Low) to tasks to stay focused on what matters most.
- **Reminders:** Schedule exact notifications for your tasks so you never miss a deadline.
- **Search:** Quickly find specific notes using the built-in search functionality.
- **Modern UI:** A clean, responsive design using Material Design components.
- **Offline First:** All data is stored locally using Room Database, ensuring your tasks are always accessible.

## 🛠️ Tech Stack

- **Language:** [Kotlin](https://kotlinlang.org/)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Database:** [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- **UI Components:** [Material Components for Android](https://material.io/develop/android)
- **Navigation:** [Navigation Component](https://developer.android.com/guide/navigation)
- **Asynchronous Tasks:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- **Lifecycle Management:** ViewModel, LiveData
- **Notifications:** AlarmManager for exact scheduling

## 🏗️ Project Structure

The project follows a clean architecture approach:
- `adapter`: RecyclerView adapters for displaying task lists.
- `database`: Room database configuration and DAOs.
- `fragments`: UI controllers for different screens (Add, Edit, List).
- `model`: Data classes representing the core entities (e.g., `Note`).
- `notification`: Helper classes for managing alerts and reminders.
- `repository`: Abstracts data sources from the rest of the app.
- `viewmodel`: Handles UI-related data and communication with the repository.

## 🚦 Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/TaskMaster2.git
   ```
2. **Open in Android Studio:**
   Import the project and let Gradle sync.
3. **Run the app:**
   Select your device/emulator and click the "Run" button.

*Note: On Android 12 (API 31) and higher, the app will request permission to schedule exact alarms for reminders.*



## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
