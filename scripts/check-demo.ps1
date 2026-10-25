# Volunteer Duty Roster - Demo Environment Health & Status Checker
# Candidate: MEET JAIN (23102C0068)

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "   Volunteer Duty Roster - Demo Verification Checker       " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

# 1. Check Docker Daemon
Write-Host ""
Write-Host "[1/5] Checking Docker Daemon..." -NoNewline
try {
    $dockerInfo = docker info 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host " [ONLINE]" -ForegroundColor Green
    } else {
        Write-Host " [OFFLINE] (Start Docker Desktop)" -ForegroundColor Red
    }
} catch {
    Write-Host " [NOT INSTALLED]" -ForegroundColor Red
}

# 2. Check Running Containers
Write-Host "[2/5] Checking Active Containers..."
$containers = docker ps --format "{{.Names}} (Port: {{.Ports}}, Status: {{.Status}})" 2>$null
if ($containers) {
    $containers | ForEach-Object { Write-Host "  * $_" -ForegroundColor Green }
} else {
    Write-Host "  No containers currently active." -ForegroundColor Yellow
}

# 3. Check Application Endpoint (Port 8080 - Manual Container)
Write-Host "[3/5] Checking Port 8080 (Manual Container: vdr-app)..." -NoNewline
try {
    $res8080 = Invoke-WebRequest -Uri "http://localhost:8080/login" -UseBasicParsing -TimeoutSec 3
    if ($res8080.StatusCode -eq 200) {
        Write-Host " [HTTP 200 OK]" -ForegroundColor Green
    } else {
        Write-Host " [HTTP $($res8080.StatusCode)]" -ForegroundColor Yellow
    }
} catch {
    Write-Host " [NOT REACHABLE]" -ForegroundColor Yellow
}

# 4. Check Application Endpoint (Port 8082 - Jenkins Deployment)
Write-Host "[4/5] Checking Port 8082 (Jenkins Deployment)..." -NoNewline
try {
    $res8082 = Invoke-WebRequest -Uri "http://localhost:8082/login" -UseBasicParsing -TimeoutSec 3
    if ($res8082.StatusCode -eq 200) {
        Write-Host " [HTTP 200 OK]" -ForegroundColor Green
    } else {
        Write-Host " [HTTP $($res8082.StatusCode)]" -ForegroundColor Yellow
    }
} catch {
    Write-Host " [NOT REACHABLE]" -ForegroundColor Yellow
}

# 5. Check Jenkins Controller (Port 8081)
Write-Host "[5/5] Checking Port 8081 (Jenkins Controller)..." -NoNewline
try {
    $res8081 = Invoke-WebRequest -Uri "http://localhost:8081/login" -UseBasicParsing -TimeoutSec 3
    if ($res8081.StatusCode -eq 200) {
        Write-Host " [ONLINE - HTTP 200]" -ForegroundColor Green
    } else {
        Write-Host " [HTTP $($res8081.StatusCode)]" -ForegroundColor Yellow
    }
} catch {
    Write-Host " [OFFLINE]" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "   Verification Complete! Ready for Demo.                 " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan
