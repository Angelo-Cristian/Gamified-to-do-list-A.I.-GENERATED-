# 📱 Gamified To-Do List (AI-Assisted & Prompt-Driven Architecture)

A gamified task management Android application where users can set tasks with specific start times and custom time limits. Completing tasks rewards the user with Experience Points (XP), allowing them to level up their account. 

This project was built as a case study to explore **Prompt-Driven Engineering**, client-server architecture, and cross-framework integration under modern development workflows.

❗This read me is a version of my read me improved by the A.I. by making it look and sound more professional. From what I've tested, if you give it an already made read me, it will work pretty well, but if you tell it to create one by giving it just some informations, it will make it too complex and it won't exeed your expectations.

---

## 🎯 Project Goals & Learning Objectives
* **AI-Assisted Engineering:** Mastering efficient prompt engineering workflows to accelerate code generation, scaffolding, and documentation.
* **Network Protocol Implementation:** Understanding and designing a custom client-server communication protocol.
* **Realtime Cloud Integration:** Learning how to securely connect and sync data with the Firebase Realtime Database.
* **Modern Backend Architecture:** Gaining hands-on experience setting up, structuring, and deploying a high-performance C++ server using the **Drogon** framework.

---

## 📝 Features
* **C++ Backend:** High-performance server architecture built with the Drogon framework.
* **Modern Android Client:** Native UI developed in Android Studio using Kotlin and Jetpack Compose.
* **Real-time Synchronization:** Data persistence and instant updates powered by Firebase Realtime Database.
* **Gamification Logic:** Time-restricted task tracking with dynamic XP allocation and leveling mechanics.

---

## 📷 Screenshots 
<p align="center">
  <img width="22%" alt="Screenshot 1" src="https://github.com/user-attachments/assets/512ee5a9-5a89-4edb-8cf6-9e0bd2ac11ce" />
  <img width="22%" alt="Screenshot 2" src="https://github.com/user-attachments/assets/1e123542-5a69-4d42-a8c2-48d3109bba0f" />
  <img width="22%" alt="Screenshot 3" src="https://github.com/user-attachments/assets/d46c6475-b9e9-4fcf-a139-3c3ac5e415bc" />
  <img width="22%" alt="Screenshot 4" src="https://github.com/user-attachments/assets/b48eeefc-16c1-4b2e-a956-4c320a0dbdb4" />
</p>

---

## 🛠️ Tech Stack
### 📱 Android App (Frontend)
* **Language:** Kotlin
* **UI Framework:** Jetpack Compose (Declarative native UI)
* **Asynchronous Programming:** Coroutines & Flow (for non-blocking UI updates and API data streams)
* **Networking:** Ktor Client (HTTP requests handled asynchronously)

### ⚙️ Backend Server
* **Language:** C++ (Optimized for speed and minimal memory footprint)
* **Web Framework:** Drogon Framework (Asynchronous HTTP/REST application framework)
* **Build System:** CMake

### ☁️ Database & Cloud Services
* **Database:** Firebase Realtime Database (NoSQL cloud-hosted database for live synchronization)

---

## 🏛️ Architecture
The mobile client follows the **MVVM (Model-View-ViewModel)** architectural pattern to maintain a clean separation of concerns:
* **View (Jetpack Compose):** Renders the user interface and captures user interactions.
* **ViewModel:** Manages the UI state, processes screen logic, and exposes reactive data streams using Kotlin Flows.
* **Model:** Handles data operations, networking with the C++ Drogon server, and direct synchronization with Firebase.

---

## 🚀 How to Run

### 🖥️ 1. The Mobile Client
1. Clone the repository: `git clone https://github.com/Angelo-Cristian/[your-repo-name].git`
2. Open the `/the app` folder inside Android Studio.
3. > ⚠️ **Configuration Note:** Update the hardcoded IP address in the network configuration file to match your local server environment before building the project.
4. Run the application on an emulator or a physical device.

### 🗄️ 2. The Backend Server
1. Launch **Visual Studio 2022**.
2. Go to **Open -> Folder** and select the `/server` directory from the cloned repository.
3. Build and run the CMake project.
4. > ⚠️ **Database Note:** Depending on active Firebase security rules and local Windows Firewall configurations, you may need to white-list the server executable to allow the Firebase API connection.

---

## 📊 Case Study: Insights on AI-Assisted Development
The core codebase of this project was generated via precise prompt engineering using **Gemini** (Web interface & Android Studio integration). The objective was to test the limits of LLMs in building a multi-language stack (Kotlin + C++). 

Below are the engineering conclusions gathered from debugging and deploying this architecture:

### ⛔ Technical Limitations Overcome
1. **Network & Environment Blindspots:** When connecting the C++ server to Firebase, the AI repeatedly generated syntactically correct code that failed during execution. It failed to identify that the local Windows Firewall was blocking the connection, leading to a loop of redundant code rewrites. Human intervention was required to debug the network layer.
2. **Data Serialization Faults:** The AI frequently generated incorrect payload formats for HTTP requests between Ktor (Kotlin) and Drogon (C++). Instead of detecting format mismatches, the LLM attempted to refactor the core business logic. The serialization bugs had to be diagnosed and fixed manually.
3. **Documentation Hallucinations:** When generating boilerplate text, the AI tended to assume pre-existing project structures or overcomplicate features rather than prompting for context, requiring careful manual filtering.

### ✅ Best Practices for AI Prompt Engineering
1. **Context Initialization:** Always provide comprehensive project constraints, language versions, and stack architectures *before* requesting code.
2. **Rigorous Verification:** Treating AI output as a draft; cross-referencing generated code against official framework documentation (especially for lower-level setups like Drogon).
3. **Cross-Model Workflows:** Utilizing broad web-based LLMs to design high-level code architecture, while relying on IDE-integrated AI assistants to adapt the logic to existing files.
4. **Complexity Capping:** Explicitly defining target complexity constraints within the prompt to avoid unnecessarily nested code structures or over-engineered solutions.

### 🗣️ Final Words
While Generative AI drastically accelerates scaffolding and boilerplate creation, senior-level oversight, debugging skills, and architectural knowledge remain absolutely critical to preventing logic errors and deploying a functional, unified system.
