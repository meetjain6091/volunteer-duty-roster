# Volunteer Duty Roster - Demo Environment Starter
# Candidate: MEET JAIN (23102C0068)

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "   Volunteer Duty Roster - Starting Demo Services          " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

# 1. Verify Docker Daemon
Write-Host ""
Write-Host "[1/3] Verifying Docker Desktop..." -NoNewline
try {
    $null = docker info 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host " [ONLINE]" -ForegroundColor Green
    } else {
        Write-Host " [OFFLINE] Please start Docker Desktop first!" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host " [ERROR] Docker command failed." -ForegroundColor Red
    exit 1
}

# 2. Check and Start Primary Application Container (vdr-app on port 8080)
Write-Host "[2/3] Checking Container 'vdr-app' (Port 8080)..."
$running = docker ps -q --filter "name=vdr-app"
$exists = docker ps -aq --filter "name=vdr-app"

if ($running) {
    Write-Host "  * Container 'vdr-app' is already running on port 8080." -ForegroundColor Green
} elseif ($exists) {
    Write-Host "  * Starting existing stopped container 'vdr-app'..." -ForegroundColor Yellow
    docker start vdr-app | Out-Null
    Start-Sleep -Seconds 5
} else {
    Write-Host "  * Launching new container 'vdr-app' on port 8080..." -ForegroundColor Yellow
    docker run -d -p 8080:8080 --name vdr-app --restart unless-stopped meetjain6091/volunteer-duty-roster:1.0.0 | Out-Null
    Start-Sleep -Seconds 5
}

# 3. Check Jenkins Deployment Container (volunteer-duty-roster on port 8082)
Write-Host "[3/3] Checking Jenkins Deployment Container 'volunteer-duty-roster' (Port 8082)..."
$vdrRunning = docker ps -q --filter "name=volunteer-duty-roster"
if ($vdrRunning) {
    Write-Host "  * Jenkins deployed container is active on port 8082." -ForegroundColor Green
} else {
    Write-Host "  * Jenkins container on 8082 is stopped. (Can be started via Jenkins Build or 'docker start volunteer-duty-roster')" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Checking service health:"
powershell -ExecutionPolicy Bypass -File "$PSScriptRoot\check-demo.ps1"
