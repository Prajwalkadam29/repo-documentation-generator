# 🚀 README Generator Pro

**Analyze. Architect. Document.** README Generator Pro is an intelligent full-stack utility that transforms raw codebases into professional, production-ready documentation. By analyzing your project's tech stack, it not only describes your code but proactively suggests **Dockerfiles**, **Orchestration logic**, and **Environment templates** even if they don't exist yet.

---

## ✨ Features

* **🤖 Multi-Model AI Strategy:** Seamlessly switch between **Google Gemini 1.5**, **Groq (Llama 3.3)**, or run locally with **Ollama**.


* **📂 Hybrid Collection:** Support for fetching from **Public/Private GitHub Repos** or **Local Directories**.


* **🛠️ Proactive DevOps:** Synthesizes suggested containerization and configuration logic based on code analysis.


* **📝 Live Markdown Preview:** Real-time rendering of generated documentation with GFM (GitHub Flavored Markdown) support.


* **🔗 Swagger/OpenAPI Integration:** Fully documented REST API for headless integration.


* **💾 One-Click Export:** Instant `.md` file download directly from the browser.


* **🧪 Robust & Tested:** 100% test coverage for core business logic, collectors, and controllers.

---

## ⚙️ Configuration (.env & application.properties)

### 1. API Keys (.env)
The application requires API keys and tokens to function. Create a `.env` file in the root directory to store your sensitive credentials:
```
# AI API Keys
GOOGLE_API_KEY=your_google_gemini_key
GROQ_API_KEY=your_groq_api_key

# GitHub Access
# Token needs 'repo' scope for private repos, or just public access.
GITHUB_TOKEN=your_github_personal_access_token

# Local AI (Ollama)
# When using Docker, use host.docker.internal to reach your Windows host.
OLLAMA_BASE_URL=http://host.docker.internal:11434
```

### 2. Customizing File Discovery
By default, the application analyzes common files like `.java`, `.py`, `.js`, `.yml`, and `.txt`.

**To include additional file types (e.g., Rust, Go, Terraform):**
1. In backend, navigate to `src/main/resources/application.properties`.


2. Locate the `app.github.include-patterns` key.


3. Append your desired extensions (e.g., `*.rs`, `**/*.go`, `*.tf`).
```properties
# Example: Adding Rust and Go support
app.github.include-patterns=...,*.rs,**/*.rs,*.go,**/*.go
```

---

## 🐳 Running with Docker (Recommended)
This is the fastest way to get the entire stack running in a production-like environment.

**1. Start the Services:**

```bash
docker-compose up --build
```
   
**2. Access the App:**

* **Frontend:** http://localhost:5173


* **Backend API:** http://localhost:8080


* **Swagger UI:** http://localhost:8080/swagger-ui.html

---

## 🛠️ Running Separately (Development Mode)
If you wish to run the components individually for debugging:

### 1. Backend (Spring Boot)

i) Navigate to the `backend` folder.

ii) Ensure environment variables from `.env` are exported or set in your IDE.

iii) Run the application:
```bash
mvn spring-boot:run
```
**Note:** If you don't have maven installed on the system, then use the following command:
```bash
./mvnw spring-boot:run
```

### 2. Frontend (React + Vite)

i) Navigate to the `frontend` folder.

ii) Install dependencies:
```bash
npm install
```

iii) Start the development server:
```bash
npm run dev
```
---

## 📖 API Documentation (Swagger)
The backend exposes a full **OpenAPI 3.0 specification**. This allows you to test the documentation generation without the UI.

* **URL:** `http://localhost:8080/swagger-ui.html`


* **Key Endpoint:** `POST /api/v1/documentation/generate`


* **Payload Example:**
```json
{
  "githubUrl": "https://github.com/user/repo",
  "aiService": "gemini"
}
```

---

## 📂 Project Structure

```text
.
├── backend/                # Java 21 + Spring Boot 3.5
│   ├── src/main/java       # Core logic, AI Strategies, Controllers
│   ├── src/test/java       # Unit & Integration Tests
│   └── Dockerfile          # Multi-stage build for JRE 21
├── frontend/               # React 18 + Tailwind v4
│   ├── src/App.jsx         # Main UI logic & Markdown rendering
│   ├── nginx.conf          # Custom Nginx config for React routing
│   └── Dockerfile          # Nginx-based production image
└── docker-compose.yml      # Service orchestration
```
---

## 🧪 Quality Assurance
We maintain code quality using JUnit 5 and Mockito. To run the full test suite:
```bash
mvn clean test
```

The suite includes tests for:
* **Path Matchers:** Ensuring complex glob patterns work across OS types.
* **API Clients:** Mocked `RestClient` flows for GitHub communication.
* **AI Factory:** Validation of the Strategy pattern implementation.

---

## 💡 Tips for Best Results

To get the most accurate and cost-effective documentation, keep the following best practices in mind:

### 🧠 Mind the AI Context Window
Large Language Models have a **Context Window** (a limit on how much text they can process at once).
* **Include Source, Not Data**: Only add extensions for code files. Avoid adding large data files like `.csv`, `.json` datasets, or `.log` files in `application.properties`, as these will consume your API tokens quickly without adding much value to the documentation.
* **Keep it Lean**: If your project is massive, use the `exclude-patterns` in `application.properties` to skip secondary directories or legacy code that doesn't define the core project structure.

### 🔑 Security First
The AI is instructed to identify environment variables and create a `.env.example` template for you.
* **Redaction**: The system is designed to provide placeholders like `YOUR_API_KEY`. However, as a best practice, always double-check the generated output before committing it to a public repository to ensure no sensitive hardcoded strings were accidentally included.

### 🐳 Docker Suggestions
Even if your project doesn't have a `Dockerfile`, the AI will suggest one. For the best suggestion, ensure your `pom.xml`, `package.json`, or `requirements.txt` are included in the file collection, as the AI uses these to determine the correct base image and build steps.

---

## 🤝 Contributing

1. Fork the Project.
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`).
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the Branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

---







