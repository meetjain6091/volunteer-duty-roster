# 🗓️ Volunteer Duty Roster

> **23102C0068 — Meet Jain**  
> A full-stack web application for managing volunteer duty assignments with role-based workflows, built with modern DevOps practices.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)
![Maven](https://img.shields.io/badge/Maven-3.9-blue)
![Selenium](https://img.shields.io/badge/Selenium-4.x-brightgreen)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)
![Jenkins](https://img.shields.io/badge/Jenkins-CI%2FCD-red)

---

## 📋 Table of Contents

- [Problem Statement](#problem-statement)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [Build & Run](#build--run)
- [Testing](#testing)
- [Docker](#docker)
- [CI/CD Pipeline](#cicd-pipeline)
- [Branch Policy](#branch-policy)
- [Contributing](#contributing)

---

## 🎯 Problem Statement

Volunteer organizations struggle with manual duty scheduling, leading to:
- **Double-booking** of volunteers for the same time slot
- **No visibility** into who is assigned where and when
- **Lack of accountability** — no status tracking (assigned → confirmed → completed)
- **Inefficient communication** between coordinators and volunteers
- **No searchable history** of past duties

This application solves these pain points by providing a centralized, role-based duty roster management system.

---

## ✨ Features (MVP)

| # | Feature | Description |
|---|---------|-------------|
| 1 | **Create Duty** | Coordinators can create duty entries with date, time, location and description |
| 2 | **View Duties** | List all duties with filtering by date, status, and volunteer |
| 3 | **Update Duty** | Edit duty details, reassign volunteers |
| 4 | **Search Records** | Search duties by volunteer name, date range, or location |
| 5 | **Role-Based Workflow** | Status flow: DRAFT → ASSIGNED → CONFIRMED → COMPLETED / CANCELLED |
| 6 | **Summary Dashboard** | Visual dashboard showing duty counts, status breakdown, upcoming duties |
| 7 | **Volunteer Management** | Add/view/update volunteer profiles |
| 8 | **User Authentication** | Login with role-based access (Admin, Coordinator, Volunteer) |

---

## 🛠️ Technology Stack

| Layer | Technology |
|-------|-----------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.2 |
| **Build Tool** | Apache Maven |
| **Template Engine** | Thymeleaf |
| **Database (Dev)** | H2 (embedded) |
| **Database (Prod)** | MySQL 8 |
| **ORM** | Spring Data JPA / Hibernate |
| **Testing** | JUnit 5, Selenium WebDriver 4 |
| **CI/CD** | Jenkins (Pipeline as Code) |
| **Containerization** | Docker |
| **Server** | Embedded Tomcat / Standalone Tomcat 10 |
| **Config Management** | Ansible |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────┐
│                  Browser                     │
│            (Thymeleaf Views)                 │
└──────────────────┬──────────────────────────┘
                   │ HTTP
┌──────────────────▼──────────────────────────┐
│           Spring Boot Application            │
│  ┌──────────┐ ┌──────────┐ ┌──────────────┐ │
│  │Controller│ │ Service  │ │  Repository  │ │
│  │  Layer   │→│  Layer   │→│   Layer      │ │
│  └──────────┘ └──────────┘ └──────┬───────┘ │
└───────────────────────────────────┼─────────┘
                                    │ JPA
                   ┌────────────────▼─────────┐
                   │    H2 / MySQL Database    │
                   └──────────────────────────┘
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.9+
- Git
- Docker (optional)
- Jenkins (optional)

### Clone the Repository

```bash
git clone https://github.com/meetjain6091/volunteer-duty-roster.git
cd volunteer-duty-roster
```

---

## 🔨 Build & Run

### Build
```bash
mvn clean package
```

### Run Locally
```bash
mvn spring-boot:run
```

The application will be available at: **http://localhost:8080**

### Access H2 Console (Dev)
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:volunteerdb`
- Username: `sa`
- Password: *(empty)*

---

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Run Selenium Tests
```bash
mvn test -Dtest="*SeleniumTest"
```

### Generate Test Report
```bash
mvn surefire-report:report
```

---

## 🐳 Docker

### Build Image
```bash
docker build -t volunteer-duty-roster:latest .
```

### Run Container
```bash
docker run -d -p 8080:8080 --name vdr-app volunteer-duty-roster:latest
```

### Container Lifecycle
```bash
docker logs vdr-app          # View logs
docker stop vdr-app          # Stop
docker start vdr-app         # Restart
docker rm vdr-app            # Remove
```

---

## 🔄 CI/CD Pipeline

The Jenkins pipeline (`Jenkinsfile`) automates:

1. **Checkout** — Pull latest code from GitHub
2. **Build** — `mvn clean compile`
3. **Test** — Run unit + Selenium tests
4. **Package** — Create JAR/WAR artifact
5. **Docker Build** — Build versioned Docker image
6. **Deploy** — Deploy to Tomcat / Docker container

---

## 🌿 Branch Policy

| Branch | Purpose |
|--------|---------|
| `main` | Production-ready, tagged releases only |
| `develop` | Integration branch for features |
| `feature/*` | New feature development |
| `bugfix/*` | Bug fixes |
| `release/*` | Release preparation |

### Naming Convention
- Feature branches: `feature/<task-id>-<short-description>`
- Bug fixes: `bugfix/<task-id>-<short-description>`
- Releases: `release/v<major>.<minor>.<patch>`

---

## 🤝 Contributing

1. Create a feature branch from `develop`
2. Make your changes with meaningful commits
3. Raise a Pull Request to `develop`
4. Get code review approval
5. Merge and delete feature branch

---

## 📄 License

This project is developed as part of academic coursework (Roll No: 23102C0068).

---

*Built with ❤️ by Meet Jain*
