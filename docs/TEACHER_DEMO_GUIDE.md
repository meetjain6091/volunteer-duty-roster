# 🎓 Volunteer Duty Roster — Teacher & Evaluator Demonstration Guide

**Candidate Name:** MEET JAIN  
**Roll Number:** 23102C0068  
**Topic:** Selenium Testing for a Volunteer Duty Roster  
**Repository:** [https://github.com/meetjain6091/volunteer-duty-roster](https://github.com/meetjain6091/volunteer-duty-roster)  
**Evaluator Target Environments:**
- **Primary Manual Docker Container:** `http://localhost:8080` (Container: `vdr-app`)
- **Jenkins Controller Dashboard:** `http://localhost:8081` (Pipeline: `Volunteer-Duty-Roster`)
- **Jenkins-Deployed CD Container:** `http://localhost:8082` (Container: `volunteer-duty-roster`)

---

## 🎤 1. Evaluator Presentation Script (Verbal Pitch)

> *"Good morning / afternoon, respected evaluator. My name is Meet Jain (Roll Number: 23102C0068), and my project topic is 'Selenium Testing for a Volunteer Duty Roster'.*
>
> *Non-profit organizations frequently suffer from volunteer double-booking, lack of attendance accountability, and communication breakdowns due to ad-hoc spreadsheets. To solve this, I designed and implemented a full-stack Spring Boot web application with strict Role-Based Access Control and an automated status workflow lifecycle: Draft ➔ Assigned ➔ Confirmed ➔ Completed.*
>
> *Beyond core CRUD features, my primary engineering focus was on the complete DevOps and Quality Assurance lifecycle:
> 1. Full GitFlow branching with Pull Requests and conflict resolution on GitHub.
> 2. A 14-test automated test harness featuring 5 critical end-to-end Selenium WebDriver user journeys with automated screenshot capture.
> 3. Multi-stage Docker containerization running under an unprivileged user with automated container health checking.
> 4. An 8-stage declarative Jenkins Pipeline as Code on Java 21 that builds, tests, packages, tags Docker images, deploys with zero downtime, and verifies container health with automated rollback.*
>
> *Let me demonstrate the live system across the application, tests, Docker, and Jenkins."*

---

## 📋 2. Demo Credentials & Architecture Summary

| Role | Username | Password (Dev Demo) | Accessible Capabilities |
|---|---|---|---|
| **Administrator** | `admin` | `admin123` | User account management, duty creation/deletion, system settings |
| **Coordinator** | `coordinator` | `coord123` | Duty scheduling, assigning volunteers, status workflow advancement |
| **Volunteer** | `volunteer1` | `vol123` | View assigned duties, confirm attendance, view personal schedule |

> [!NOTE]
> The application uses an in-memory database (`jdbc:h2:mem:volunteerdb`) for rapid testing and zero-configuration demonstration. Data initializes with sample records on startup and resets on container restart.

---

## 🚀 3. Step-by-Step Live Demonstration Walkthrough

### Step 3.1: Pre-Demo Health Check (5 seconds)
Open PowerShell in the project directory and run the automated checker script:
```powershell
cd C:\Users\meetj\.gemini\antigravity-ide\scratch\volunteer-duty-roster
powershell -ExecutionPolicy Bypass -File scripts\check-demo.ps1
```
*Show the Evaluator:* Points 1 through 5 will display green `[ONLINE]`, `[HTTP 200 OK]`, and active container status.

---

### Step 3.2: Demonstrating the Live Web Application (Port 8080 or 8082)
Open your browser to: **`http://localhost:8080/login`** (or `http://localhost:8082/login`)

1. **Demonstrate Login & Dashboard (Journey 1):**
   - Enter `coordinator` / `coord123` ➔ Click **Sign In**.
   - Show the summary dashboard with metric cards: Total Duties (6), Draft (2), Assigned (2), Confirmed (1), Completed (1), Volunteers (3).
   - Show the upcoming duties calendar table with color-coded status badges.

2. **Demonstrate Create Duty Assignment (Journey 2):**
   - Click **+ Create Duty** on the sidebar.
   - Enter:
     * Title: `Community Food Distribution Drive`
     * Date: *(select tomorrow's date)*
     * Start / End Time: `09:00` to `13:00`
     * Location: `City Community Center`
     * Assign Volunteer: Select `Alice Johnson` (Status updates to `ASSIGNED`).
   - Click **Save Duty**.
   - Show the green success message: `"Duty created successfully!"` and verify it appears in the roster table.

3. **Demonstrate Role-Based Workflow Transition (Journey 3):**
   - Click on any duty row action to view details (`/duties/view/{id}`).
   - Click the green **Confirm Duty** button (Status dynamically transitions to `CONFIRMED`).
   - Click the blue **Mark Completed** button (Status dynamically updates to `COMPLETED` and locks editing).

4. **Demonstrate Search & Filter Engine (Journey 4):**
   - Click **Search** in the sidebar.
   - Search by keyword `Park` or filter by Status `Confirmed`.
   - Show filtered results updating instantly in the table.

5. **Demonstrate Public Volunteer Registration (Journey 5):**
   - Click **Logout**.
   - On the login card, click **Register here** (`/register`).
   - Register a new volunteer (e.g. `rohit_kumar` / password).
   - Immediately log in with the new credentials to show seamless RBAC provisioning.

---

### Step 3.3: Demonstrating Automated Selenium Testing in Maven (Terminal)
Open PowerShell to show the evaluator the test suite executing:
```powershell
mvn test
```
*Key Talking Points for Evaluator:*
1. **Headless Execution:** Selenium WebDriver 4 launches Google Chrome in headless mode (`--headless=new`) with `WebDriverManager` automatically resolving driver binaries.
2. **Execution Speed:** 14 tests (5 Selenium E2E journeys + 5 DutyService tests + 4 UserService tests) execute and pass in ~25 seconds with 0 failures.
3. **Screenshot Output:** Show the generated visual proofs in `target/screenshots/`:
   ```powershell
   Get-ChildItem target\screenshots\
   ```

---

### Step 3.4: Demonstrating Jenkins CI/CD Controller & Pipeline (Port 8081)
Open browser to: **`http://localhost:8081`**

1. **Show Job Dashboard:**
   - Click on the pipeline job **`Volunteer-Duty-Roster`**.
   - Point out **Build #3** (Green checkmark, duration: 4m 46s).

2. **Explain the 8 Automated Pipeline Stages:**
   - **Stage 1 (Checkout SCM):** Clones code from GitHub repository `meetjain6091/volunteer-duty-roster`.
   - **Stage 2 (Compile & Static Analysis):** Validates Maven dependencies and compiles Java classes.
   - **Stage 3 (Unit & Selenium Testing):** Runs the full test suite. Point out the **Test Result** tab showing 14 passed tests.
   - **Stage 4 (Package War Artefact):** Packages `target/volunteer-duty-roster.war` (55.7 MB) and archives it.
   - **Stage 5 (Docker Build & Tag):** Multi-stage build generates image tagged with `${DOCKER_TAG}` and `${BUILD_NUMBER}`.
   - **Stage 6 (Publish Docker Image):** Simulates/executes registry publication.
   - **Stage 7 (Deploy to Target Environment):** Launches fresh container `volunteer-duty-roster` on port `8082`.
   - **Stage 8 (Health Check & Smoke Test):** Performs HTTP check on `/login`. If unhealthy, triggers auto-rollback.

3. **Show Archived Test Artifacts in Jenkins:**
   - Click on **Build #3** ➔ Click **Artifacts**.
   - Show the 5 archived Selenium screenshot files (`Journey1_Dashboard...png`, `Journey2_CreateDuty...png`, etc.).
   - Click on any screenshot to view the rendered application interface captured inside Jenkins!

---

### Step 3.5: Demonstrating Docker Container Lifecycle
In PowerShell, execute:
```powershell
# 1. Show running containers and health status
docker ps --format "table {{.ID}}\t{{.Names}}\t{{.Status}}\t{{.Ports}}"

# 2. Inspect container logs
docker logs --tail 20 volunteer-duty-roster

# 3. Show Docker Image
docker images meetjain6091/volunteer-duty-roster
```
*Key Talking Points:*
- Built from multi-stage `Dockerfile`: Maven build stage compiles WAR; lightweight Temurin 17 JRE runs the container.
- Container runs as unprivileged user `rosteruser` (UID 1000) for security hardening.
- Built-in `HEALTHCHECK` runs `curl -f http://localhost:8080/login || exit 1` every 30 seconds.

---

### Step 3.6: Demonstrating Ansible Configuration Management (Code & Architecture)
Open `ansible/playbook.yml` and explain:
1. **Target Prerequisites:** Configures system packages (`curl`, `git`, `docker.io`, `ufw`), system groups (`rostergroup`), service users (`rosteruser` with `/usr/sbin/nologin`), and directories (`/opt/volunteer-duty-roster/{logs,config,backups}`).
2. **Idempotency:** Tasks use declarative state (`state: present`, `state: directory`, `state: started`). Re-running the playbook produces `changed=0, failed=0`.
3. **Rollback Recovery (`ansible/rollback.yml`):** If health check fails on a new release, it stops the unstable container and automatically restarts `:0.9.0` (previous stable release) in under 15 seconds.

---

## 🛠️ 4. Quick Troubleshooting During Demo

| Issue | Cause | Quick Fix Command |
|---|---|---|
| Port 8080 or 8082 gives "Connection Refused" | Docker container stopped | Run `powershell -ExecutionPolicy Bypass -File scripts\start-demo.ps1` |
| Jenkins URL `http://localhost:8081` not opening | Jenkins process closed | Run: `& "C:\Program Files\Microsoft\jdk-21.0.12.101-hotspot\bin\java.exe" -jar "C:\Users\meetj\tools\jenkins\jenkins.war" --httpPort=8081` |
| Chrome version warning in test console | Chrome CDP 155 advisory | Purely informational warning; tests execute and pass completely via standard WebDriver HTTP calls. |
| Port collision error during Jenkins build | Port 8080 already taken | Set pipeline parameter `APP_PORT=8082` before clicking Build. |

---

## 🏁 5. Demonstration Shutdown Command
When the demonstration is complete:
```powershell
powershell -ExecutionPolicy Bypass -File scripts\stop-demo.ps1
```
*(Leaves Jenkins controller online while cleanly stopping the application demo containers).*
