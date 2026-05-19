# Android Project (A.I. GENERATED)
This is a gamified to do list where you can set your tasks to start at a certain moment, they have a time limit that you can set when you create the respective task and if you complete tasks, you'll earn xp and level up on you're account.

❗This project was made with the goals of:
1. Learning how to efficiently use A.I. when you create projects.
2. Understanding how a client-server protocol is implemented.
3. Learning how a program can connect to a realtime database on firebase.
4. Geting used with creating the logic of a server made with the drogon framework.

❗I also used A.I. on a part of this read me to analyse how useful it is.

❗The code was 99% generated since the primary goal of this project was to learn how to efficiently give prompts to A.I. .

❗A.I. used: gemini(both the one on the web and the one integrated on android studio)

❗At the end of this read me are the conclusions of the analysation I made.

## 📝 Features
*Server c++ with drogon
*Client in android studio with kotlin and jetpack compose
*Realtime database in firebase

## 📷 Screenshots 
<img width="921" height="2048" alt="WhatsApp Image 2026-04-15 at 11 52 35" src="https://github.com/user-attachments/assets/512ee5a9-5a89-4edb-8cf6-9e0bd2ac11ce" />
<img width="921" height="2048" alt="WhatsApp Image 2026-04-15 at 11 52 35 (1)" src="https://github.com/user-attachments/assets/1e123542-5a69-4d42-a8c2-48d3109bba0f" />
<img width="921" height="2048" alt="WhatsApp Image 2026-04-15 at 11 52 35 (3)" src="https://github.com/user-attachments/assets/d46c6475-b9e9-4fcf-a139-3c3ac5e415bc" />
<img width="921" height="2048" alt="WhatsApp Image 2026-04-15 at 11 52 35 (2)" src="https://github.com/user-attachments/assets/b48eeefc-16c1-4b2e-a956-4c320a0dbdb4" />

## 🛠️ Tech Stack (A.I. GENERATED)
### 📱 Android App (Frontend)
Kotlin: The primary programming language used to build the app.

Jetpack Compose: Used to build the entire UI natively and declaratively.

Coroutines & Flow: Used to handle background tasks (like API calls) without freezing the app screen.

Ktor: Used to send HTTP requests and talk to the C++ server.

### ⚙️ Backend Server
C++: The core language used for a fast and lightweight server.

Drogon Framework: A C++ web framework used to create the server and handle HTTP requests asynchronously.

CMake: Used as the build system to compile the C++ code.

### ☁️ Database & Cloud
Firebase Realtime Database: Used to store and sync data across users instantly in real-time.

## 🏛️ Architecture (A.I. GENERATED)
This app uses the MVVM (Model-View-ViewModel) pattern to keep the code organized:

View (Jetpack Compose): Handles the UI and what the user sees on their screen.

ViewModel: Handles the screen logic and holds the data using Kotlin Flows so the UI updates automatically when data changes.

Model: Handles fetching and sending data to the Firebase Database and the C++ Drogon server.

## 🚀 How to Run
### 🖥️ The client
1. Clone the repository
2. Open Android Studio
3. Opne the file "the app"
❗You have to manually change the ip from the code, since it's hardcoded, so the connection between the client and the server will work.

###  🗄️ The server
1. in vs2022 - open folder - server (from repository)
❗It's possible that the connection with the realtime database from firebase won't be open, depending on when you'll open this project.

## Final conclusions (after analysing the using of A.I.)
### ⛔ Problems
1. When I tried to connect the server to the realtime database, it couldn't connect with the method that the A.I. used, because the firewall from my windows wasn't allowing the direct connection with the database, and the A.I. didn't try to analyse this type of situation, instead, it tried to recreate the code, with very small changes that didn't really metter.
2. When I wanted to send certain data to the server, it didn't work, because the format that the data was send wasn't written correctly, but again, the A.I. tried to change the logic of the code, instead of thinkig that the format it tried to use was incorrect.
3. On creating the certain part from the read me, it gived me too complex details and it tried to anticipate what my project had, instead of asking for more details, resulting in wrong informations (but I modified them).

### ✅ Solutions
1. Give all the necessary details about youself and about the project you make before you start giving it tasks.
2. Always verify the informations that the ai gives you.
3. If you don't know how to proparlly write a certain prompt, you can tell the A.I. to make it.
4. If you use an integrated A.I., also use an external one when it comes to write code. Create the code with the external one and tell the internal one to adapt it for your code.
5. If you think that a solution is too complex, tell it to simplify it or mention at the beginning of the conversation the level of complexity that you want.

### 🗣️ Final Words
The A.I. is very useful, but it's extremely important to know how to use it, so you won't create more damage than good.
