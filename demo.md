# Spring Boot OAuth2 Login Demo
This project demonstrates how to implement OAuth2 authentication in a Spring Boot application using custom login pages with JTE (Java Template Engine) and Tailwind CSS. It includes both traditional form login and OAuth2 login with Google and GitHub.

## Tech Stack
* Java 17 or later
* Spring Boot 3.3.4
* Maven
* JTE (Java Template Engine) 3.1.12
* Tailwind CSS
* Google OAuth2
* GitHub OAuth2

## Getting Started
To get started, follow these steps:
1. Clone the repository
```bash
git clone <repository-url>
cd spring-boot-oauth-demo
```
2. Configure OAuth credentials (see OAuth Setup sections below)
3. Set environment variables
```bash
export GOOGLE_CLIENT_ID=YOUR_GOOGLE_CLIENT_ID
export GOOGLE_CLIENT_SECRET=YOUR_GOOGLE_CLIENT_SECRET
export GITHUB_CLIENT_ID=YOUR_GITHUB_CLIENT_ID
export GITHUB_CLIENT_SECRET=YOUR_GITHUB_CLIENT_SECRET
```
4. Run the application
```bash
mvn spring-boot:run
```
5. Visit http://localhost:8080

## Project Structure
```markdown
src/
  main/
    java/
      dev.danvega/
          Application.java    # Spring Boot application configuration
          SecurityConfig.java  # Spring Security configuration
          LoginController.java  # Login handling
          DashboardController.java # Dashboard pages
    resources/
      application.yml           # Application configuration
      jte/
        layout/
          default.jte          # Base template
        pages/
          login.jte           # Login page
          dashboard.jte        # Dashboard page
          home.jte            # Home page
```

## Setup & Configuration
Create a new file named `.env` in the root of your project with the following content:
```makefile
GOOGLE_CLIENT_ID=YOUR_GOOGLE_CLIENT_ID
GOOGLE_CLIENT_SECRET=YOUR_GOOGLE_CLIENT_SECRET
GITHUB_CLIENT_ID=YOUR_GITHUB_CLIENT_ID
GITHUB_CLIENT_SECRET=YOUR_GITHUB_CLIENT_SECRET
```
Replace `YOUR_GOOGLE_CLIENT_ID`, `YOUR_GOOGLE_CLIENT_SECRET`, `YOUR_GITHUB_CLIENT_ID`, and `YOUR_GITHUB_CLIENT_SECRET` with your actual OAuth2 client IDs and secrets.

## Docker Configuration
To containerize the application, create a `Dockerfile` with the following content:

```dockerfile
# Stage 1: Build the application
FROM maven:3.8.6-jdk-17 as build
WORKDIR /app
COPY backend/pom.xml .
COPY backend/src src
RUN mvn clean package

# Stage 2: Create the Docker image
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/jte-login-0.0.1-SNAPSHOT.jar /app/jte-login.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "jte-login.jar"]
```
Create a `docker-compose.yml` file with the following content:
```yml
version: '3'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID}
      - GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET}
      - GITHUB_CLIENT_ID=${GITHUB_CLIENT_ID}
      - GITHUB_CLIENT_SECRET=${GITHUB_CLIENT_SECRET}
```
Run the application using Docker Compose:
```bash
docker-compose up
```
Visit http://localhost:8080 to access the application.

## OAuth2 Setup
### Google OAuth2 Setup
1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
2. Create a new project or select an existing one.
3. Configure the OAuth consent screen:
    * Go to "APIs & Services" > "OAuth consent screen".
    * Choose "External" user type.
    * Fill in required information:
        * App name
        * User support email
        * Developer contact information
    * Add scopes: email, profile, openid
    * Add test users if using external user type.
4. Create OAuth2 credentials:
    * Go to "APIs & Services" > "Credentials".
    * Click "Create Credentials" > "OAuth client ID".
    * Choose "Web application".
    * Add these URLs:
      ```
      Authorized JavaScript origins:
      http://localhost:8080
 
      Authorized redirect URIs:
      http://localhost:8080/login/oauth2/code/google
      ```
    * Note your client ID and client secret.

### GitHub OAuth Setup
1. Go to the [GitHub Developer Settings](https://github.com/settings/developers).
2. Click "New OAuth App".
3. Fill in the application details:
   ```
   Application name: Your App Name
   Homepage URL: http://localhost:8080
   Authorization callback URL: http://localhost:8080/login/oauth2/code/github
   ```
4. Register the application.
5. Note your client ID and generate a client secret.

## Troubleshooting
### OAuth2 Issues
1. Redirect URI Mismatch
    * Verify the exact URIs in your OAuth provider settings.
    * For Google: `http://localhost:8080/login/oauth2/code/google`
    * For GitHub: `http://localhost:8080/login/oauth2/code/github`
    * No trailing slashes.
    * Correct protocol (http/https).
    * Correct port number.
2. Authentication Errors
    * Clear browser cookies and cache.
    * Check environment variables are set correctly.
    * Verify OAuth provider console settings.
    * Check application logs for detailed error messages.

### Common Issues
1. Login Page Not Loading
    * Verify JTE configuration.
    * Check template paths.
    * Clear browser cache.
2. Authentication Not Working
    * Verify default user credentials.
    * Check OAuth configuration.
    * Ensure CSRF token is present in forms.

## Security Considerations
1. Production Deployment
    * Use HTTPS.
    * Update OAuth redirect URIs for production domain.
    * Secure client secrets.
    * Enable CSRF protection.
    * Consider session management settings.
2. OAuth Provider Setup
    * Restrict OAuth scopes to minimum required.
    * Verify redirect URIs.
    * Protect client secrets.
    * Use environment variables.