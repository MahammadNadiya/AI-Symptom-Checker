# AI Symptom Checker

A full-stack symptom-checking application that allows users to select their symptoms or describe them in their own words using an AI assistant. The application uses a **rule-based symptom-matching engine** to identify possible condition matches, display transparent symptom coverage, provide severity-aware guidance, and save previous results for logged-in users.

The project follows a **safety-first design**: it avoids producing disease matches from a single symptom and clearly communicates that its results are informational and not medical diagnoses.

> ⚠️ \\\*\\\*Disclaimer:\\\*\\\* This project is intended for educational and portfolio purposes only. It does not provide medical diagnoses and should not be used as a substitute for professional medical advice. Always consult a qualified healthcare professional for real health concerns.

🔗 **Live site:** [mahammadnadiya.github.io/AI-Symptom-Checker](https://mahammadnadiya.github.io/AI-Symptom-Checker/)
🔗 **Backend:** Spring Boot API deployed on Render and connected to a cloud MySQL database.

\---

## Why this project is different

Instead of immediately displaying a disease based on a single symptom, the application uses configurable evidence thresholds and transparent symptom matching.

### Safety-first matching

* **Minimum evidence threshold** — a disease match is shown only when at least **2 symptoms match** and the matched symptoms cover at least **30%** of that condition's stored symptom set.
* **Single-symptom safety mode** — when only one symptom is selected, the application does not perform disease matching. Instead, it provides information about that symptom.
* **Warning-symptom handling** — predefined symptoms that are marked as urgent in the application's data can trigger warning-oriented guidance rather than generic reassurance.
* **Transparent symptom coverage** — results show the number of matched symptoms and the corresponding coverage percentage, for example, **5 of 6 symptoms matched — 83%**.
* **Severity-aware guidance** — the severity associated with a matched condition is used to determine the type of general guidance shown to the user.

> \\\*\\\*Important:\\\*\\\* Symptom coverage is not the probability that a user has a disease. It is only a measure of how many stored symptoms for a condition matched the user's selected symptoms.

\---

## Features

* 🔐 **Authentication** — user registration and login with BCrypt password hashing. Passwords are not returned in API responses.
* 🩺 **Symptom database** — symptoms organized across general, digestive, respiratory, cardiovascular, mental health, and demographic-specific categories such as women, men, children, elderly, and pregnancy/postpartum.
* 🧬 **Disease-symptom relationships** — conditions are linked to symptoms using a many-to-many relational model.
* 🔍 **Symptom search** — supports partial and case-insensitive symptom lookup.
* 🤖 **AI symptom assistant** — users can describe their symptoms in natural language, and Gemini is used to identify relevant symptoms from the application's known symptom list.
* 🧠 **Rule-based matching engine** — calculates symptom coverage and applies minimum matching thresholds before displaying possible condition matches.
* 📋 **Prediction history** — previous symptom-check results can be stored and viewed by logged-in users.
* 💻 **Custom frontend** — single-page application built with HTML, CSS, and vanilla JavaScript without a frontend framework.
* ☁️ **Cloud deployment** — frontend hosted on GitHub Pages, backend deployed on Render, and MySQL hosted on Aiven.

\---

## How the symptom-matching engine works

```
User selects symptoms
        ↓
Find conditions containing those symptoms
        ↓
Count matched symptoms
        ↓
Calculate symptom coverage
        ↓
Check minimum evidence threshold
        ↓
Rank matching conditions
        ↓
Apply severity-based guidance
        ↓
Display informational result

```

### Matching rule

A condition is considered for display when:

```
Matched symptoms >= 2
AND
Symptom coverage >= 30%

```

The coverage percentage is calculated as:

```
Matched symptoms
------------------------ × 100
Total symptoms for condition

```

For example:

```
5 matched symptoms
------------------ × 100 = 83%
6 total symptoms

```

This percentage represents **symptom coverage**, not the probability of having the condition.

\---

## AI Symptom Assistant

Users may not always know the exact medical term for what they are experiencing.

For example:

> "I feel dizzy, my head hurts, and I can't focus on anything."

The AI assistant uses Google Gemini to identify relevant symptoms from the application's known symptom list.

```
User's description
        ↓
    Gemini API
        ↓
Candidate symptom names
        ↓
Known symptom list
        ↓
Selected symptoms
        ↓
Rule-based matching engine

```

The AI assistant is used for **symptom identification**, while the actual condition matching is handled by the application's rule-based engine.

A Gemini API key is required to use this feature locally.

\---

## Tech Stack

|LayerTechnology||
|-|-|
|Backend|Java 21, Spring Boot, Spring Data JPA, Spring Security|
|Database|MySQL (Aiven Cloud)|
|ORM|Hibernate / JPA|
|Frontend|HTML, CSS, Vanilla JavaScript|
|API Communication|Fetch API / REST|
|AI|Google Gemini API|
|Authentication|BCrypt password hashing|
|Deployment|Render, Docker, GitHub Pages|
|Development Tools|IntelliJ IDEA, Postman, MySQL Workbench, Git|

\---

## API Overview

|FeatureMethodEndpoint|||
|-|-|-|
|Register|POST|`/users`|
|Login|POST|`/users/login`|
|Get users|GET|`/users`|
|Get / Delete user|GET / DELETE|`/users/{id}`|
|List symptoms|GET|`/symptoms`|
|Search symptoms|GET|`/symptoms/search?keyword=`|
|Create symptom|POST|`/symptoms`|
|List diseases|GET|`/diseases`|
|Create disease|POST|`/diseases`|
|Symptom matching|POST|`/diseases/predict`|
|AI symptom matching|POST|`/ai/match-symptoms`|
|Get prediction history|GET|`/history/{userId}`|

> The API list above describes the application's current endpoints. Authentication/authorization for administrative operations should be strengthened as the project evolves.

\---

## Project Structure

```
AI-Symptom-Checker/
├── backend/
│   └── backend/
│       ├── Dockerfile
│       └── src/
│           └── main/
│               ├── java/com/nadiya/backend/
│               │   ├── controller/
│               │   ├── service/
│               │   ├── repository/
│               │   ├── entity/
│               │   ├── dto/
│               │   └── config/
│               └── resources/
├── docs/
│   └── index.html
├── screenshots/
└── README.md

```

### Main backend layers

* **Controller** — exposes REST API endpoints.
* **Service** — contains application/business logic.
* **Repository** — communicates with the database using Spring Data JPA.
* **Entity** — represents database tables and relationships.
* **DTO** — controls the data exposed through API requests and responses.
* **Config** — contains security and application configuration.

\---

## Database Design

The application uses a relational MySQL database.

The main relationships include:

```
User
 │
 └── Prediction History

Disease
 │
 └── Many-to-Many
       │
       └── Symptom

```

The many-to-many relationship allows one disease to contain multiple symptoms while the same symptom can be associated with multiple diseases.

\---

## Local Setup

### Prerequisites

* Java 21 or later
* MySQL Server
* IntelliJ IDEA or another Java IDE
* Git
* A Gemini API key from [Google AI Studio](https://aistudio.google.com/) for the AI assistant

### 1\. Clone the repository

```
git clone https://github.com/MahammadNadiya/AI-Symptom-Checker.git
cd AI-Symptom-Checker

```

### 2\. Create the database

Open MySQL and run:

```
CREATE DATABASE symptom\\\_checker;

```

### 3\. Configure the backend

Create:

```
backend/backend/src/main/resources/application.properties

```

Add your local database configuration:

```
spring.datasource.url=jdbc:mysql://localhost:3306/symptom\\\_checker
spring.datasource.username=root
spring.datasource.password=YOUR\\\_MYSQL\\\_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

GEMINI\\\_API\\\_KEY=YOUR\\\_GEMINI\\\_API\\\_KEY

```

> \\\*\\\*Security:\\\*\\\* Never commit your real database password or Gemini API key to GitHub. Keep `application.properties` in `.gitignore` or use environment variables/secrets for deployment.

### 4\. Run the backend

#### Windows

```
cd backend/backend
mvnw.cmd spring-boot:run

```

#### Linux / macOS

```
cd backend/backend
./mvnw spring-boot:run

```

The backend should start on:

```
http://localhost:8080

```

### 5\. Run the frontend

Open:

```
docs/index.html

```

in your browser.

The frontend is configured to communicate with the deployed backend. For local development, update the API base URL in the JavaScript configuration to:

```
http://localhost:8080

```

\---

## Screenshots

*Add screenshots of the following pages here:*

\## Screenshots



The application provides a complete workflow for authentication, AI-assisted symptom selection, prediction results, and medical history.



<table>

&#x20; <tr>

&#x20;   <td align="center"><strong>Login / Registration</strong></td>

&#x20;   <td align="center"><strong>AI Symptom Assistant</strong></td>

&#x20; </tr>

&#x20; <tr>

&#x20;   <td align="center">

&#x20;     <img src="screenshots/login.png" alt="Login / Registration" width="420">

&#x20;   </td>

&#x20;   <td align="center">

&#x20;     <img src="screenshots/ai-assistant.png" alt="AI Symptom Assistant" width="420">

&#x20;   </td>

&#x20; </tr>

&#x20; <tr>

&#x20;   <td align="center"><strong>Prediction Results</strong></td>

&#x20;   <td align="center"><strong>Prediction History</strong></td>

&#x20; </tr>

&#x20; <tr>

&#x20;   <td align="center">

&#x20;     <img src="screenshots/results.png" alt="Prediction Results" width="420">

&#x20;   </td>

&#x20;   <td align="center">

&#x20;     <img src="screenshots/history.png" alt="Prediction History" width="420">

&#x20;   </td>

&#x20; </tr>

</table>



\---

## Challenges and Solutions

### Password was being returned in API responses

The application was updated to use password write-only handling so the password can be accepted during registration without being returned in normal API responses.

### Single symptom produced weak disease matches

A minimum matching threshold was introduced:

```
At least 2 matched symptoms
AND
At least 30% symptom coverage

```

### Urgent symptoms received generic guidance

Warning symptoms are handled separately so predefined urgent symptoms can trigger warning-oriented guidance instead of a generic mild-symptom response.

### Cloud database contained no expected data

The database deployment and data migration process was checked to ensure that the cloud database contained the required application data.

### AI assistant was not responding correctly

A JavaScript scope/handler issue was identified and corrected so the AI assistant could be triggered properly from the frontend.

\---

## Future Improvements

* 🔐 Migrate authentication to JWT-based stateless authentication.
* 👨‍💼 Add an admin dashboard for managing symptoms and conditions.
* 🧪 Add automated unit and integration tests for the matching engine and APIs.
* 🤖 Experiment with an ML-based model such as a decision tree and compare its results with the rule-based approach.
* 🏥 Add nearby healthcare facility lookup using a maps API.
* 📄 Add PDF export for symptom-check results.
* 🛡️ Improve role-based authorization for administrative endpoints.
* 📊 Add monitoring and logging for the deployed backend.

\---

## Limitations

* The matching rules are application-defined and are not medically validated.
* Symptom coverage should not be interpreted as the probability of having a condition.
* Different medical conditions can share similar symptoms.
* AI-generated symptom matching can depend on how clearly the user describes their symptoms.
* The application cannot perform a clinical examination, laboratory test, or professional medical assessment.
* The application should not be used for emergency medical decision-making.

\---

## Disclaimer

This is a **student/portfolio project for educational purposes**.

The application does not provide professional medical diagnosis, treatment, or medical advice. Its results are based on application-defined symptom relationships and matching rules.

**Always consult a qualified healthcare professional for concerns about your health. In an emergency, seek appropriate emergency medical care rather than relying on this application.**

