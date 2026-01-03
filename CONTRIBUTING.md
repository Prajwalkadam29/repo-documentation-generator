# 🤝 Contributing to README Generator Pro

First off, thank you for considering contributing to **README Generator Pro**! It’s contributions like yours that make this tool more robust for the global developer community.

Please follow these guidelines to ensure the project remains high-quality and easy to maintain.

---

## 🏗️ Architectural Core Principles

To keep the codebase production-grade, we adhere to the following principles:

1.  **AI Strategy Pattern**: Never hardcode AI logic into the services. All new LLM integrations must implement the `AiService` interface and be handled by the `AiServiceFactory`.
2.  **Feature-by-Package**: We organize the backend by domain (e.g., `features.collector`, `features.generator`) rather than by technical layer.
3.  **Headless API**: The backend is a pure REST API. All responses must be wrapped in the `ApiResponse` record.
4.  **Modern Java**: We utilize Java 21 features. Use `records` for DTOs and configuration, and pattern matching where appropriate.
5.  **Tailwind CSS v4**: Use utility classes for all styling. Avoid custom CSS files unless implementing complex animations.

---

## 🚀 Local Development Setup

### 1. Backend Setup (Spring Boot)
* **Java version**: 21+
* **Environment Variables**: Ensure your `.env` file contains `GOOGLE_API_KEY`, `GROQ_API_KEY`, and `GITHUB_TOKEN`.
* **Ollama**: If testing locally, ensure the Ollama service is running on `localhost:11434`.

### 2. Frontend Setup (React + Vite)
* **Node version**: 22+
* **Installation**:
    ```bash
    cd frontend
    npm install
    npm run dev
    ```

---

## 🛠️ Contribution Process

### 1. Reporting Bugs
* Check the [Issues](https://github.com/yourusername/readme-generator/issues) to see if the bug has already been reported.
* If not, open a new issue. Provide a clear title, a description of the bug, and steps to reproduce it.

### 2. Feature Requests
* Open an issue to discuss the feature before starting any code changes.
* Explain the "Why" behind the feature and how it benefits the users.

### 3. Pull Requests (PRs)
* **Branching**: Create a branch from `main` (e.g., `feature/add-anthropic-support` or `fix/collector-path-bug`).
* **Testing**: Ensure all tests pass before submitting.
    ```bash
    mvn clean test
    ```
* **Documentation**: If you change the API, ensure the Swagger/OpenAPI annotations are updated in the Controller.
* **Commits**: Use descriptive commit messages (e.g., `feat: added Groq strategy implementation`).

---

## 🧪 Testing Standards

We follow a strict "No Test, No Merge" policy for core logic.

* **Unit Tests**: Required for every new `AiService` implementation.
* **Mockito**: Use Mockito to mock external network calls.
* **Validation Tests**: Ensure any new DTOs are tested against the `GlobalExceptionHandler`.

---

## ⚖️ Code Style

* **Java**: Follow standard Google Java Style Guide.
* **React**: Use functional components and hooks.
* **Linting**: Run `npm run lint` for frontend changes.

Thank you for helping us build the best AI documentation tool on the web!