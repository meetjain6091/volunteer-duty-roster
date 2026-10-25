@echo off
echo === Pushing Volunteer Duty Roster to GitHub ===
git remote set-url origin git@github.com:meetjain6091/volunteer-duty-roster.git

echo Pushing all branches...
git push --all origin

echo Pushing all tags...
git push --tags origin

echo === Push Complete! ===
pause
