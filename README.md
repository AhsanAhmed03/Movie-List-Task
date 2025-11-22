# 🎬 MoviesList

A simple Android app that displays a list of **Studio Ghibli movies** using a free API.
Users can view movie details, mark movies as favorites, and filter between **All / Favorites**.

---

## 🖇 API Used

* **Studio Ghibli API** – Free, no API key required
  Endpoint: `https://ghibliapi.vercel.app/films`
  Example fields: `title`, `original_title`, `image`, `description`, `release_date`

---

## 🏗 Architecture Overview

* **Pattern:** MVVM (Model-View-ViewModel)
* **Layers:**

  * **Data:** Handles API calls (Retrofit) and local database (Room)
  * **Domain:** Repository pattern (fetches and provides data)
  * **Presentation:** ViewModel exposes UI state via `StateFlow` and the UI observes it
* **Dependency Injection:** Hilt
* **UI:** XML layouts with **ViewBinding**, `RecyclerView` for movie list

---

## ⚡ Coroutine Usage

* All network and database operations run in **Kotlin coroutines**
* **ViewModelScope** is used to launch asynchronous jobs
* **StateFlow** and **Flow** manage reactive UI state updates
* Sealed class `UiState` used for Loading, Success, and Error states

---

## 💾 Favorites Persistence

* Favorites stored locally in **Room Database**
* Only **movie IDs** are persisted
* UI observes changes and updates the list in real-time
* Filter (radio buttons) allows switching between **All** and **Favorites**

---

## 🏃 How to Run

1. Clone the repository

   ```bash
   git clone https://github.com/AhsanAhmed03/Movie-List-Task.git
   ```
2. Open project in **Android Studio 2023+**
3. Sync Gradle and build the project
4. Run on emulator or physical device
5. No API key or login is required
