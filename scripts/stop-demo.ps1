# Volunteer Duty Roster - Demo Environment Stopper
# Candidate: MEET JAIN (23102C0068)

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "   Volunteer Duty Roster - Stopping Demo Containers        " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

# 1. Stop Port 8080 Application Container
$app1 = docker ps -q --filter "name=vdr-app"
if ($app1) {
    Write-Host "Stopping container 'vdr-app' (Port 8080)..." -NoNewline
    docker stop vdr-app | Out-Null
    Write-Host " [STOPPED]" -ForegroundColor Green
} else {
    Write-Host "Container 'vdr-app' is not running." -ForegroundColor Yellow
}

# 2. Stop Port 8082 Application Container
$app2 = docker ps -q --filter "name=volunteer-duty-roster"
if ($app2) {
    Write-Host "Stopping container 'volunteer-duty-roster' (Port 8082)..." -NoNewline
    docker stop volunteer-duty-roster | Out-Null
    Write-Host " [STOPPED]" -ForegroundColor Green
} else {
    Write-Host "Container 'volunteer-duty-roster' is not running." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Note: Jenkins controller on port 8081 remains running." -ForegroundColor Cyan
Write-Host "All application demo containers are stopped cleanly." -ForegroundColor Green
