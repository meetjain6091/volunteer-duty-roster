# 🌿 Git Branching Policy & Collaboration Guidelines

**Project:** Volunteer Duty Roster  
**Author:** Meet Jain (Roll No: 23102C0068)  
**Repository:** https://github.com/meetjain6091/volunteer-duty-roster  

---

## 1. Branch Strategy (GitFlow Architecture)

The Volunteer Duty Roster repository enforces a strict GitFlow branching standard across all development phases:

```
main          ─────────────────────────● (v1.0.0 Release Tag)
                ▲                     ▲
                │                     │ Merge Release
develop       ──┴──●─────────●────────┴────────
                    ▲       ▲
     Merge Feature1 │       │ Merge Feature2
                    │       │
feature/US-01 ──────┴────── │
                            │
feature/US-02 ──────────────┴ (Simulated Merge Conflict & Resolution)
```

### Branch Roles

| Branch | Protection Level | Purpose |
|---|---|---|
| `main` | Protected | Production-ready, tagged releases only (`v1.0.0`). Direct pushes forbidden. Requires approved PR and clean CI build. |
| `develop` | Protected | Integration branch for completed feature branches. All automated unit and Selenium tests must pass. |
| `feature/<id>-<desc>` | Unprotected | Ephemeral branches for user story development, branched from `develop` and merged via Pull Request. |
| `bugfix/<id>-<desc>` | Unprotected | Defect remediation branches branched from `develop`. |
| `release/v<semver>` | Staged | Release stabilization, version bump, and release notes baseline. |

---

## 2. Branch Naming Conventions

All branch names must be lowercase and adhere to the regular expression:
`^(feature|bugfix|release|hotfix)\/([A-Z]+-[0-9]+|[a-z0-9-]+)$`

**Valid Examples:**
- `feature/US-01-duty-crud`
- `feature/US-02-status-workflow`
- `feature/US-06-summary-dashboard`
- `bugfix/BUG-101-localtime-spel-conversion`
- `release/v1.0.0`

---

## 3. Commit Message Standards (Conventional Commits)

Format: `<type>(<scope>): <subject>`

**Allowed Types:**
- `feat`: New user-facing capability or API endpoint
- `fix`: Bug fix in business logic, view, or database query
- `test`: Adding or updating Selenium or JUnit tests
- `docs`: README, SRS, architecture, or DevOps documentation updates
- `ci`: Jenkinsfile, GitHub Actions, or Docker container configuration
- `refactor`: Code restructuring without changing observable behavior

**Examples:**
- `feat(duties): implement duty creation and volunteer assignment workflow`
- `feat(dashboard): add aggregated statistics and upcoming duties table`
- `test(selenium): add 5 critical user journey end-to-end tests with screenshots`
- `ci(jenkins): configure parameterized pipeline with test gate and container deploy`

---

## 4. Pull Request & Code Review Process

1. **Branch Out:** Always branch from the latest `develop` branch:
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/US-01-duty-crud
   ```
2. **Commit Frequently:** Write clean, modular commits with meaningful commit messages.
3. **Pull Request Checklist:**
   - [x] Code compiles cleanly with `mvn clean compile`
   - [x] Unit and Selenium test suites pass 100% locally (`mvn test`)
   - [x] PR description references the corresponding Backlog User Story ID
   - [x] At least 1 code reviewer approval obtained
4. **Merge Strategy:** Squash and merge or rebase merge to keep a clean commit history.

---

## 5. Merge Conflict Creation & Resolution Protocol (Deliverable 6)

To demonstrate merge conflict management for academic evaluation:
1. Feature Branch A modifies the status transitions in `DutyStatus.java`.
2. Feature Branch B concurrently modifies the display naming and status order in `DutyStatus.java`.
3. When merging Feature Branch B into `develop` after Branch A is merged, Git triggers a merge conflict:
   ```
   Auto-merging src/main/java/com/volunteer/dutyroster/model/DutyStatus.java
   CONFLICT (content): Merge conflict in src/main/java/com/volunteer/dutyroster/model/DutyStatus.java
   Automatic merge failed; fix conflicts and then commit the result.
   ```
4. **Resolution:** The conflict markers (`<<<<<<<`, `=======`, `>>>>>>>`) are inspected, harmonized to retain both enum improvements, tested with `mvn compile`, and committed with:
   `git commit -m "fix(merge): resolve concurrent status workflow conflict between branches"`
5. Tagged release `v1.0.0` is cut from `main`.

## Current Release Baseline
Current Baseline: Release v1.0.0-RELEASE (Harmonized Status Workflow & DevOps CI/CD Baseline)
- **Roll No:** 23102C0068
- **Name:** Meet Jain
- **Resolved Date:** October 25, 2026
- **Status:** Release-Ready Source Baseline Verified Across 14 Unit and Selenium E2E Tests

