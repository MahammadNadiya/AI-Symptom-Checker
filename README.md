# AI Symptom Checker

A full-stack symptom-checking application that lets users select what they're feeling and get a ranked, honest read of possible conditions — with match confidence, severity, and first-aid guidance. Built with a **safety-first prediction design**: it never guesses a serious diagnosis from a single vague symptom, and always points users toward professional medical advice rather than replacing it.

> ⚠️ This project is for educational purposes only. It does not provide medical diagnoses. Always consult a qualified doctor for real health concerns.

\---

## Why this project is different

Most beginner symptom-checker projects just match symptoms to diseases and show whatever comes back — which can be genuinely alarming (e.g. showing "COVID-19" to someone who only reported a mild fever). This project deliberately avoids that:

* **Minimum evidence threshold** — a disease is only shown if at least 2 symptoms match **and** it covers at least 30% of that disease's typical symptom set. One coincidental symptom is never enough to name a scary disease.
* **Single-symptom safety mode** — if a user selects just one symptom, the app doesn't run disease matching at all. Instead, it gives calm, informational context about that specific symptom, and automatically flags it as a **warning sign** instead of "usually mild" if the symptom's own data indicates urgency (e.g. "Severe Abdominal Pain (Pregnancy)," "Reduced Fetal Movement").
* **Match transparency** — every result shows exactly how many symptoms matched out of the disease's total (e.g. "5 of 6 symptoms matched — 83%"), so the confidence level is never hidden behind a bare disease name.
* **Severity-aware guidance** — the top match's severity (Mild / Moderate / Severe) drives a clear, appropriate call to action, from "self-care may help" to "please consult a doctor promptly."

\---

## Features

* 🔐 **Authentication** — registration and login with BCrypt-encrypted passwords, passwords never exposed in any API response
* 🩺 **100+ symptoms** spanning general illness, digestive, respiratory, cardiovascular, mental health, and dedicated categories for women, men, children, elderly, and pregnancy/postpartum
* 🧬 **45+ diseases** linked to symptoms via a many-to-many relationship
* 🔍 **Symptom search** with partial, case-insensitive matching
* 🤖 **Rule-based prediction engine** with match-percentage scoring and severity-based risk advice
* 📋 **Medical history** — every prediction a logged-in user runs is saved and viewable later
* 💻 **Custom frontend** — single-page HTML/JS app with an animated, distinctive UI (no frameworks required)

\---

## Tech stack

|Layer|Technology|
|-|-|
|Backend|Java 21, Spring Boot, Spring Data JPA, Spring Security|
|Database|MySQL|
|Frontend|HTML, CSS, vanilla JavaScript (fetch API)|
|Auth|BCrypt password hashing|
|Tools|IntelliJ IDEA, Postman, MySQL Workbench, Git|

\---

## API overview

|Feature|Method|Endpoint|
|-|-|-|
|Register|POST|`/users`|
|Login|POST|`/users/login`|
|Get all users|GET|`/users`|
|Get / delete user|GET / DELETE|`/users/{id}`|
|List symptoms|GET|`/symptoms`|
|Search symptoms|GET|`/symptoms/search?keyword=`|
|Create symptom|POST|`/symptoms`|
|List diseases|GET|`/diseases`|
|Create disease|POST|`/diseases`|
|**Predict**|POST|`/diseases/predict`|
|Get user history|GET|`/history/{userId}`|

\---

## Project structure

```
AI-Symptom-Checker/
├── backend/
│   └── backend/            # Spring Boot application
│       └── src/main/java/com/nadiya/backend/
│           ├── controller/ # REST endpoints
│           ├── service/    # Business logic
│           ├── repository/ # Data access (Spring Data JPA)
│           ├── entity/     # Database models
│           ├── dto/        # Response wrappers
│           └── config/     # Spring Security config
├── frontend/
│   └── index.html          # Single-file frontend (HTML/CSS/JS)
└── README.md
```

\---

## Setup instructions

### Prerequisites

* Java 21+
* MySQL Server
* IntelliJ IDEA (or any Java IDE)

### 1\. Clone the repository

```bash
git clone https://github.com/MahammadNadiya/AI-Symptom-Checker.git
cd AI-Symptom-Checker
```

### 2\. Set up the database

Create a MySQL database:

```sql
CREATE DATABASE symptom\_checker;
```

### 3\. Configure the backend

Inside `backend/backend/src/main/resources/`, create `application.properties` (this file is gitignored for security) with:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/symptom\_checker
spring.datasource.username=root
spring.datasource.password=YOUR\_MYSQL\_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4\. Run the backend

Open `backend/backend` in IntelliJ and run `BackendApplication.java`, or from the terminal:

```bash
cd backend/backend
./mvnw spring-boot:run
```

The API will start on `http://localhost:8080`.

### 5\. Open the frontend

Simply open `frontend/index.html` in your browser — no build step or server required. Make sure the backend is running first.

\---

## Screenshots

*!\[Login page](screenshots/login.png)*

*!\[Symptom check results](screenshots/results.png)*

*!\[Medical history](screenshots/history.png)*

\---

## Future improvements

* Migrate authentication to JWT-based sessions
* Add an admin panel for managing symptoms/diseases without Postman
* Optional ML-based prediction (decision tree) as an alternative to the rule-based engine
* Nearby hospital lookup via a maps API
* PDF report export of prediction results

\---

## Disclaimer

This tool is a student/portfolio project and is **not a substitute for professional medical advice, diagnosis, or treatment**. Always consult a physician with any questions about a medical condition.

