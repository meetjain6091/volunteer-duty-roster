# 📖 Volunteer Duty Roster — Comprehensive Operations & Deployment Runbook

**Candidate Name:** MEET JAIN  
**Roll Number:** 23102C0068  
**Topic:** Selenium Testing for a Volunteer Duty Roster  
**Repository:** [https://github.com/meetjain6091/volunteer-duty-roster](https://github.com/meetjain6091/volunteer-duty-roster)  
**Host Environment:** Windows 11 64-bit / Docker Desktop / Jenkins LTS 2.580.1 / Java 17 & 21  

---

## 📋 1. Project Prerequisites

| Component | Required Version | Verified Host Setup | Executable Path / Location | Purpose |
|---|---|---|---|---|
| **Operating System** | Windows 10/11 / Linux | Windows 11 Pro 64-bit | Host OS | Workstation & Runtime |
| **Java Development Kit** | JDK 17 LTS | Oracle JDK 17.0.12 LTS | `C:\Program Files\Java\jdk-17` | Backend Compilation & Execution |
| **Jenkins Runtime JDK** | JDK 21 LTS | Microsoft OpenJDK 21.0.12 | `C:\Program Files\Microsoft\jdk-21.0.12.101-hotspot` | Jenkins 2.580.1 Controller |
| **Build Tool** | Apache Maven 3.8+ | Apache Maven 3.9.6 | `C:\Users\meetj\tools\apache-maven-3.9.6` | Build, Dependencies & Test Runner |
| **Web Browser** | Google Chrome 120+ | Chrome 155.0.8059.86 | Standard Windows Path | Selenium Headless End-to-End Testing |
| **Version Control** | Git 2.40+ | Git 2.44+ with SSH Key | `C:\Program Files\Git\cmd\git.exe` | SCM & GitHub Synchronization |
| **Container Engine** | Docker 24+ | Docker Desktop 29.6.2 (WSL2) | `C:\Users\meetj\AppData\Local\Programs\DockerDesktop` | Production Containerization |
| **CI/CD Controller** | Jenkins LTS 2.440+ | Jenkins 2.580.1 WAR | `C:\Users\meetj\tools\jenkins\jenkins.war` | Continuous Integration & Pipeline Automation |
| **Config Management** | Ansible 2.14+ | Ansible under WSL Ubuntu | WSL 2 Environment | Automated Node Provisioning & Idempotency |

---

## ⚡ 2. Automated Demonstration Helper Scripts

The project includes pre-configured PowerShell helper scripts under `scripts/`:

```powershell
# 1. Check health of Docker, Jenkins, and both application ports:
powershell -ExecutionPolicy Bypass -File scripts\check-demo.ps1

# 2. Start the demo container on port 8080:
powershell -ExecutionPolicy Bypass -File scripts\start-demo.ps1

# 3. Stop demo containers cleanly:
powershell -ExecutionPolicy Bypass -File scripts\stop-demo.ps1
```

---

## 💻 3. Local Development & Testing Commands (PowerShell)

### 3.1 Clean Compilation
```powershell
cd C:\Users\meetj\.gemini\antigravity-ide\scratch\volunteer-duty-roster
mvn clean compile
```

### 3.2 Run Test Suite (14 Tests)
```powershell
mvn test
```
*Expected Result:*
```text
Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS (Total time: ~25s)
```

### 3.3 Package Production Archive
```powershell
mvn package -DskipTests
```
*Generated Artifact:* `target/volunteer-duty-roster.war` (55.7 MB).

### 3.4 Run Application Standalone
```powershell
java -jar target/volunteer-duty-roster.war
```
*Endpoint:* `http://localhost:8080/login`

---

## 🐳 4. Docker Setup and Container Deployment

### 4.1 Build Docker Image
```powershell
docker build -t meetjain6091/volunteer-duty-roster:1.0.0 -t meetjain6091/volunteer-duty-roster:latest .
```

### 4.2 Run Container (Port 8080)
```powershell
docker run -d -p 8080:8080 --name vdr-app --restart unless-stopped meetjain6091/volunteer-duty-roster:1.0.0
```

### 4.3 Verify Container Status & Logs
```powershell
docker ps --format "table {{.ID}}\t{{.Names}}\t{{.Status}}\t{{.Ports}}"
docker logs -f vdr-app
```

### 4.4 Automated Health Check
```powershell
docker inspect --format='{{.State.Health.Status}}' vdr-app
Invoke-WebRequest http://localhost:8080/login -UseBasicParsing
```

---

## 🔄 5. Jenkins Controller & Pipeline Execution (Build #3 Verified)

### 5.1 Starting Jenkins Controller on Port 8081
To prevent collision with port 8080, Jenkins runs on port 8081 using Java 21:
```powershell
& "C:\Program Files\Microsoft\jdk-21.0.12.101-hotspot\bin\java.exe" -jar "C:\Users\meetj\tools\jenkins\jenkins.war" --httpPort=8081
```
*Jenkins URL:* `http://localhost:8081`

### 5.2 Pipeline Job Configuration
1. Job Name: `Volunteer-Duty-Roster`
2. Type: **Pipeline script from SCM**
3. SCM: **Git**
4. Repository URL: `https://github.com/meetjain6091/volunteer-duty-roster.git`
5. Branch Specifier: `*/main`
6. Script Path: `Jenkinsfile`

### 5.3 Executing Pipeline Build
Click **Build with Parameters**:
- `ENVIRONMENT`: `production`
- `DOCKER_TAG`: `1.0.0`
- `RUN_SELENIUM`: `true`
- `APP_PORT`: `8082` *(prevents port collision with container `vdr-app` on 8080)*

### 5.4 Verified Stages in Build #3 (Total Duration: 4m 46s)
1. `Checkout SCM`: Successfully cloned repository from GitHub.
2. `Compile & Static Analysis`: `mvn clean compile` succeeded.
3. `Unit & Selenium Testing`: 14 tests executed and passed (JUnit XML published).
4. `Package War Artefact`: `target/volunteer-duty-roster.war` packaged and archived.
5. `Docker Build & Tag`: Built image tagged with `1.0.0` and `#3`.
6. `Publish Docker Image`: Tag validation passed.
7. `Deploy to Target Environment`: Deployed container `volunteer-duty-roster` on port `8082`.
8. `Health Check & Smoke Test`: Verified HTTP 200 on `http://localhost:8082/login`.

---

## 🛡️ 6. Port Management & Collision Prevention

To ensure smooth multi-service operation on the Windows workstation:

| Service / Container | Host Port | Internal Container Port | URL |
|---|---|---|---|
| **Manual Docker Container (`vdr-app`)** | `8080` | `8080` | `http://localhost:8080` |
| **Jenkins Controller** | `8081` | N/A (Host Process) | `http://localhost:8081` |
| **Jenkins Automated Deployment Container (`volunteer-duty-roster`)** | `8082` | `8080` | `http://localhost:8082` |

> [!IMPORTANT]
> If you wish to deploy the Jenkins container to port 8080 instead of 8082, first stop `vdr-app`:
> ```powershell
> docker stop vdr-app
> ```

---

## 🔧 7. Ansible Configuration Management via WSL

### 7.1 Setup WSL Ubuntu Control Node
```powershell
wsl --install -d Ubuntu
```

### 7.2 Run Playbook Syntax Checks in Ubuntu
```bash
sudo apt update && sudo apt install -y ansible
cd /mnt/c/Users/meetj/.gemini/antigravity-ide/scratch/volunteer-duty-roster/ansible

# Syntax check
ansible-playbook -i inventory.ini playbook.yml --syntax-check
ansible-playbook -i inventory.ini rollback.yml --syntax-check
```

### 7.3 Idempotency Execution
```bash
# First Run
ansible-playbook -i inventory.ini playbook.yml

# Second Run (Idempotency check: changed=0, failed=0)
ansible-playbook -i inventory.ini playbook.yml
```

---

## ⚠️ 8. Troubleshooting & Common Fixes

| Issue | Diagnosis | Resolution |
|---|---|---|
| **HTTP 500 on application start** | Database lock or stale process | Stop competing Java processes: `Stop-Process -Name java -Force` |
| **Docker port allocation error** | Port already in use | Use alternate port: `docker run -p 8082:8080 ...` or stop container on port 8080 |
| **Jenkins pipeline fails at deploy stage** | Docker socket permission or name collision | Jenkinsfile uses `docker stop volunteer-duty-roster \|\| true` to ensure idempotency |
| **Data disappears after container restart** | H2 in-memory volatility | Expected behavior with `jdbc:h2:mem:volunteerdb`. For persistent storage, connect external MySQL |

---

*Authored by Meet Jain (Roll No: 23102C0068) — Verified & Evaluator Ready.*
