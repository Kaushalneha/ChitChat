# Project Overview


Chit Chat is a real-time Android messaging application. It focuses on simplicity and speed, allowing users to connect instantly using their phone numbers.



# Key Features
•
Real-time Messaging: Instant message exchange using Firebase Realtime Database.

•
Presence System: Shows live Online/Offline status of users (including a fix for visibility on dark/light toolbars).

•
Phone Authentication: Secure login using Firebase OTP (One-Time Password).

•
User Search: Find friends by their username.

•
Recent Chats: A dashboard to quickly access your ongoing conversations.

•
Modern UI: Built using Google's Material Design 3 components for a premium feel.



# Tech Stack


Language: Kotlin (Modern Android standard).


* Database: Firebase Realtime Database (for low-latency syncing).


* Auth: Firebase Phone Authentication.


* UI Layout: XML with ViewBinding for type-safe view access.


* Navigation: Jetpack Navigation Component (Fragment-based navigation)


# How to Run

1. Clone the Repo: git clone <your-repo-url>


2. Firebase Setup: Create a project in the Firebase Console and download google-services.json into the app/ folder.

  
3. Enable Auth: Turn on "Phone" as a sign-in provider in Firebase.


4. Database Rules: Set your database rules to allow authenticated read/writes.

  
5. Build: Open in Android Studio, sync Gradle, and run on a physical device or emulator.



##Author: 

kaushalneha



