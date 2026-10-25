# Push script for Volunteer Duty Roster
Write-Host "=== Pushing Volunteer Duty Roster to GitHub ===" -ForegroundColor Cyan
git remote set-url origin git@github.com:meetjain6091/volunteer-duty-roster.git

Write-Host "Pushing all branches..." -ForegroundColor Yellow
git push --all origin

Write-Host "Pushing all tags..." -ForegroundColor Yellow
git push --tags origin

Write-Host "=== Push Complete! Verification: ===" -ForegroundColor Green
git branch -r
