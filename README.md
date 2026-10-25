# 🗓️ Volunteer Duty Roster

> **Candidate Name:** MEET JAIN  
> **Roll Number:** 23102C0068  
> **Repository:** [https://github.com/meetjain6091/volunteer-duty-roster](https://github.com/meetjain6091/volunteer-duty-roster)  
> **Academic Topic:** Selenium Testing for a Volunteer Duty Roster  
> **Live Ports:** Manual Docker: `http://localhost:8080` | Jenkins CI/CD: `http://localhost:8081` | CD Deployment: `http://localhost:8082`

![Java](https://img.shields.io/badge/Java-17%20%2F%2021-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-green)
![Maven](https://img.shields.io/badge/Maven-3.9-blue)
![Selenium](https://img.shields.io/badge/Selenium-4.18.1-brightgreen)
![Docker](https://img.shields.io/badge/Docker-Verified-blue)
![Jenkins](https://img.shields.io/badge/Jenkins-Build%20%233%20PASS-brightgreen)

---

## 📋 Table of Contents

- [Problem Statement](#-problem-statement)
- [Features & Role-Based Workflow](#-features--role-based-workflow)
- [Technology Stack](#-technology-stack)
- [System Architecture](#-system-architecture)
- [Quick Start Helper Scripts](#-quick-start-helper-scripts)
- [Local Build & Run (PowerShell)](#-local-build--run-powershell)
- [Testing & Automation Evidence](#-testing--automation-evidence)
- [Docker Containerization](#-docker-containerization)
- [Jenkins CI/CD Pipeline (Build #3 Verified)](#-jenkinscicd-pipeline-build-3-verified)
- [Ansible Provisioning](#-ansible-provisioning)
- [Default Demo Accounts](#-default-demo-accounts)
- [Coursework Documentation Links](#-coursework-documentation-links)
- [Troubleshooting & Known Limitations](#-troubleshooting--known-limitations)

---

## 🎯 Problem Statement

Non-profit organizations, food drives, and community outreach initiatives frequently rely on ad-hoc phone calls and spreadsheets for volunteer coordination. This causes:
- **Double-booking** and scheduling clashes.
- **Lack of real-time visibility** into volunteer duty coverage.
- **No accountability lifecycle** (no-shows without notification).
- **Communication breakdowns** between coordinators and volunteers.
- **Zero audit trail** of volunteer hours.

This application provides a centralized, role-based duty roster management portal with an automated status lifecycle (`DRAFT` ➔ `ASSIGNED` ➔ `CONFIRMED` ➔ `COMPLETED` / `CANCELLED`).

---

## ✨ Features & Role-Based Workflow

| # | Feature | Target Role | Description |
|---|---------|-------------|-------------|
| 1 | **User Authentication & RBAC** | All Roles | Spring Security 6 with BCrypt password hashing |
| 2 | **Public Registration** | Volunteer | Self-registration portal for new volunteers |
| 3 | **Summary Dashboard** | All Roles | Real-time metric cards (Total, Draft, Assigned, Confirmed, Completed) |
| 4 | **Create Duty Record** | Coordinator, Admin | Form with title, date, time slots, venue, and volunteer selection |
| 5 | **View & Filter Duties** | All Roles | Interactive data table with status badges and quick action buttons |
| 6 | **Edit & Update Duty** | Coordinator, Admin | Modify timings, reassign personnel, update instructions |
| 7 | **Status Workflow Lifecycle**| All Roles | State transition buttons (`ASSIGNED` ➔ `CONFIRMED` ➔ `COMPLETED`) |
| 8 | **Multi-Criteria Search** | All Roles | Real-time query by keyword, status dropdown, and calendar date |

---

## 🛠️ Technology Stack

| Component | Technology | Version | Purpose |
|---|---|---|---|
| **Language** | Java | 17 (Build) / 21 (Jenkins) | Application logic & CI runtime |
| **Backend Framework** | Spring Boot | 3.2.5 | MVC Controllers, Services, Security |
| **Data Layer** | Spring Data JPA / Hibernate | 6.4 | ORM & Entity persistence |
| **Database** | H2 In-Memory (Dev/Test) | 2.2 | Zero-configuration database |
| **View Template** | Thymeleaf | 3.1 | Server-side HTML rendering |
| **Build Tool** | Apache Maven | 3.9.6 | Dependency management & packaging |
| **Testing** | JUnit 5 & Selenium WebDriver | 4.18.1 | Unit testing & 5-journey E2E automation |
| **Containerization** | Docker Desktop (WSL2) | 29.6.2 | Multi-stage image build & containers |
| **CI/CD Controller** | Jenkins LTS | 2.580.1 | 8-stage declarative Pipeline as Code |
| **Configuration Mgmt** | Ansible | 2.14+ (WSL) | Idempotent server provisioning |

---

## 🏗️ System Architecture

```
┌────────────────────────────────────────────────────────┐
│               Web Browser (Chrome / Edge)               │
│                   Thymeleaf HTML Views                 │
└───────────────────────────┬────────────────────────────┘
                            │ HTTP GET / POST
┌───────────────────────────▼────────────────────────────┐
│              Spring Boot Application Layer             │
│   ┌────────────────────────────────────────────────┐   │
│   │ Spring Security 6 (RBAC Filter Chain & BCrypt) │   │
│   └───────────────────────┬────────────────────────┘   │
│                           │                            │
│   ┌───────────────────────▼────────────────────────┐   │
│   │ Controllers (Auth, Dashboard, Duty, Volunteer) │   │
│   └───────────────────────┬────────────────────────┘   │
│                           │                            │
│   ┌───────────────────────▼────────────────────────┐   │
│   │ Business Service Layer (DutyService, UserSvc)  │   │
│   └───────────────────────┬────────────────────────┘   │
│                           │                            │
│   ┌───────────────────────▼────────────────────────┐   │
│   │ Persistence Layer (DutyRepository, UserRepo)   │   │
│   └───────────────────────┬────────────────────────┘   │
└───────────────────────────┼────────────────────────────┘
                            │ JPA / Hibernate
             ┌──────────────▼─────────────┐
             │    H2 In-Memory Database   │
             │ (jdbc:h2:mem:volunteerdb)  │
             └────────────────────────────┘
```

---

## ⚡ Quick Start Helper Scripts

To simplify demonstration and testing on Windows:

```powershell
# Check health of Docker, Jenkins, and both application ports:
powershell -ExecutionPolicy Bypass -File scripts\check-demo.ps1

# Start the demo container on port 8080:
powershell -ExecutionPolicy Bypass -File scripts\start-demo.ps1

# Stop all demo application containers cleanly:
powershell -ExecutionPolicy Bypass -File scripts\stop-demo.ps1
```

---

## 🔨 Local Build & Run (PowerShell)

### Compile
```powershell
mvn clean compile
```

### Run Tests
```powershell
mvn test
```

### Package Production WAR
```powershell
mvn package -DskipTests
```

### Run Standalone WAR (Port 8080)
```powershell
java -jar target/volunteer-duty-roster.war
```
*Application URL:* `http://localhost:8080/login`  
*H2 Console:* `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:volunteerdb`, User: `sa`, Password: *(empty)*)

---

## 🧪 Testing & Automation Evidence

The test suite contains **14 automated tests** with 100% pass rate:
- **5 End-to-End Selenium WebDriver Journeys:**
  1. `testJourney1_LoginAndDashboard`: Coordinator login and metric card aggregation.
  2. `testJourney2_CreateDutyRecord`: Duty creation with date/time injection and assignment.
  3. `testJourney3_StatusWorkflow`: Full lifecycle (`ASSIGNED` ➔ `CONFIRMED` ➔ `COMPLETED`).
  4. `testJourney4_SearchAndFilter`: Multi-criteria text and status filtering.
  5. `testJourney5_RegisterNewVolunteer`: Public self-registration and subsequent login.
- **5 Service Tests (`DutyServiceTest`):** CRUD logic, status transitions, statistical metrics.
- **4 User Tests (`UserServiceTest`):** Registration, password encryption, role queries.
- **Screenshots:** Automatically archived in `docs/screenshots/` and Jenkins artifacts.

---

## 🐳 Docker Containerization

### Build Image
```powershell
docker build -t meetjain6091/volunteer-duty-roster:1.0.0 .
```

### Run Container (Port 8080)
```powershell
docker run -d -p 8080:8080 --name vdr-app --restart unless-stopped meetjain6091/volunteer-duty-roster:1.0.0
```

### Container Lifecycle
```powershell
docker ps                                 # View active containers and health
docker logs -f vdr-app                    # Inspect logs
docker stop vdr-app                       # Stop container
docker start vdr-app                      # Restart container
docker rm -f vdr-app                      # Remove container
```

---

## 🔄 Jenkins CI/CD Pipeline (Build #3 Verified)

- **Jenkins Controller URL:** `http://localhost:8081`
- **Job Name:** `Volunteer-Duty-Roster`
- **Pipeline as Code:** [Jenkinsfile](file:///c:/Users/meetj/.gemini/antigravity-ide/scratch/volunteer-duty-roster/Jenkinsfile)
- **Verified Execution Proof:** **Build #3** passed all 8 stages with green status:
  1. `Checkout SCM`: Clones Git repository.
  2. `Compile & Static Analysis`: Runs `mvn clean compile`.
  3. `Unit & Selenium Testing`: Runs 14 tests; archives JUnit XML and screenshots.
  4. `Package War Artefact`: Packages `target/volunteer-duty-roster.war` (55.7 MB).
  5. `Docker Build & Tag`: Builds versioned container image.
  6. `Publish Docker Image`: Validates registry tagging.
  7. `Deploy to Target Environment`: Deploys container to port `8082`.
  8. `Health Check & Smoke Test`: Verifies HTTP 200 with auto-rollback safety net.

---

## 🔧 Ansible Provisioning

Ansible automation scripts are located under `ansible/`:
- `ansible/inventory.ini`: Target server configuration.
- `ansible/playbook.yml`: 14 idempotent provisioning tasks (packages, users, directories, services).
- `ansible/rollback.yml`: Automated rollback restoring previous stable release (`:0.9.0`) on failure.

---

## 🔑 Default Demo Accounts

> [!WARNING]
> These credentials are for **development and academic demonstration only**. Never use default accounts in production.

| Role | Username | Password | Access Level |
|---|---|---|---|
| **Administrator** | `admin` | `admin123` | Full access (Users, Duties, System) |
| **Coordinator** | `coordinator` | `coord123` | Duty scheduling & volunteer management |
| **Volunteer** | `volunteer1` | `vol123` | Duty view & confirmation |

---

## 📚 Coursework Documentation Links

- [Deliverables 1 to 14 Academic Report](file:///c:/Users/meetj/.gemini/antigravity-ide/scratch/volunteer-duty-roster/docs/DELIVERABLES_1_TO_14_REPORT.md)
- [Comprehensive Operations Runbook](file:///c:/Users/meetj/.gemini/antigravity-ide/scratch/volunteer-duty-roster/docs/DEPLOYMENT_RUNBOOK.md)
- [Teacher & Evaluator Demonstration Guide](file:///c:/Users/meetj/.gemini/antigravity-ide/scratch/volunteer-duty-roster/docs/TEACHER_DEMO_GUIDE.md)
- [Final Project Verification & Audit Report](file:///c:/Users/meetj/.gemini/antigravity-ide/scratch/volunteer-duty-roster/docs/FINAL_VERIFICATION_REPORT.md)
- [Branching Policy](file:///c:/Users/meetj/.gemini/antigravity-ide/scratch/volunteer-duty-roster/BRANCHING_POLICY.md)

---

## ⚠️ Troubleshooting & Known Limitations

1. **H2 In-Memory Database:** Data resides in volatile memory (`jdbc:h2:mem:volunteerdb`). Restarting a container reinitializes the sample dataset. For permanent enterprise persistence, attach external MySQL via environment variables.
2. **Port Conflict Management:**
   - Standalone / Manual Docker container uses Port **8080**.
   - Jenkins Controller runs on Port **8081**.
   - Jenkins Automated Deployment container uses Port **8082**.
3. **Chrome CDP 155 Warning:** Chrome 155 DevTools protocol warning in logs is informational and does not affect WebDriver HTTP execution or assertions.

---

*Authored by Meet Jain (Roll No: 23102C0068) — Verified & Evaluator Ready.*
