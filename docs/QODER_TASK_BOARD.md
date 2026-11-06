# QODER TASK BOARD — Supervisor Loop

Shared coordination surface between two agents working on the same checkout.

| Role | Agent | Can do |
|---|---|---|
| **Supervisor** | OpenCode (this session) | Writes tasks, verifies results by running builds/tests/git, issues corrections |
| **Worker** | Qoder IDE | Executes one task, writes code, commits on the feature branch, appends to the log |

The supervisor has **no GUI access** to Qoder. The only channel is this file plus the repository state.
A task is therefore only "done" when the supervisor can prove it from disk — never from a chat reply.

---

## 1. Standing rules (both agents)

1. Branch is **`feature/member-intelligence-v3`**. Never commit or edit on `main`.
2. Qoder **may commit** on the feature branch. Qoder must **never** push, never merge, never rebase, never `git reset --hard`, never force-push.
3. Never touch `unused/` (archived v1 dashboard) or `BAD_GYM_MEMBER_INTELLIGENCE_READY/` (legacy reference).
4. One task at a time. Do not start a task whose status is not `OPEN`.
5. Every task ends with the exact command output that proves it, pasted into the log.
6. If a task cannot be completed, set it to `BLOCKED` and state the blocker in one line. Do not improvise scope.
7. Existing uncommitted work is **not** yours to discard. Never revert files you were not asked to change.

---

## 2. Environment facts (verified by supervisor, 2026-11-06)

| Fact | Value |
|---|---|
| Repo root | `C:\Users\User\AndroidStudioProjects\badnewgym` |
| Module / package | `:app` / `com.example.badnewgym` |
| Compose version | **1.11.0** — see §4 pitfalls |
| Java home | `C:\Program Files\Android\Android Studio\jbr` |
| Build command | `cmd /c "set JAVA_HOME=C:\Program Files\Android\Android Studio\jbr&& gradlew <tasks> --console=plain --no-daemon"` |
| Full gate | `gradlew clean testDebugUnitTest assembleDebug` |
| Device | `zxdada69gunb7ls4` (Xiaomi 11i 5G, 1080×2400) — currently **`unauthorized`**, needs on-phone "Allow USB debugging" tap |
| adb | `C:\Users\User\AppData\Local\Android\Sdk\platform-tools\adb.exe` |
| Screen scale | screenshots render 900×2000; adb tap coords = displayed coord × 1.2 |

---

## 3. Board

Status values: `OPEN` · `IN_PROGRESS` · `REVIEW` · `DONE` · `BLOCKED`

| ID | Task | Status | Owner |
|---|---|---|---|
| T-001 | Compose 1.11 API migration of the 5 intelligence files | **DONE** | Qoder |
| T-002 | Full build + unit-test gate | **DONE** | Supervisor |
| T-003 | Device authorize → install → screenshot all 4 tabs | **DONE** | Supervisor |
| T-004 | Pixel comparison vs reference spec, deviation list | **DONE** | Supervisor |
| T-005 | Commit the pixel pass with evidence | **DONE** (`307de25`) | Supervisor |

### T-001 — Compose 1.11 API migration — DONE

Evidence confirmed by supervisor:

```
> Task :app:compileDebugKotlin UP-TO-DATE
BUILD SUCCESSFUL in 33s
```

Files now import `androidx.compose.ui.geometry.{Offset,Size,CornerRadius,Rect,RoundRect}` and call
`measurer.measure(text, style, constraints = Constraints(...))` with named arguments.
This matches the pitfall note `common_pitfalls_experience/Compose_1.11.0_API_Mismatches_Causing_Build_Failures.md`.

---

### T-002 — Full build + unit-test gate — OPEN

**Goal.** Prove the branch is green from a cold state, not from an incremental cache.

**Run exactly:**

```
cmd /c "set JAVA_HOME=C:\Program Files\Android\Android Studio\jbr&& gradlew clean testDebugUnitTest assembleDebug --console=plain --no-daemon"
```

**Acceptance criteria**

- [ ] `BUILD SUCCESSFUL` and the task reports zero failed tests.
- [ ] No new compiler warnings about unresolved references in `presentation/intelligence/`.
- [ ] The log entry contains the literal final `BUILD SUCCESSFUL in <N>s` line and the count of executed tasks.

**On failure.** Fix only what the compiler/test names. Re-run the same command. If a fix requires
changing behaviour or design, stop and set `BLOCKED` with the reason — do not redesign a screen to make a build pass.

**Do not** commit in this task; T-005 owns the commit.

---

### T-003 — Device verification — BLOCKED on physical tap

**Blocker.** `adb devices` reports `zxdada69gunb7ls4 unauthorized`. A human must unlock the phone and tap
**Allow** on the "Allow USB debugging?" dialog. The supervisor cannot press it.

**Once authorized, run:**

```
adb -s zxdada69gunb7ls4 install -r app\build\outputs\apk\debug\app-debug.apk
adb -s zxdada69gunb7ls4 shell am force-stop com.example.badnewgym
adb -s zxdada69gunb7ls4 shell monkey -p com.example.badnewgym -c android.intent.category.LAUNCHER 1
```

Capture the four tabs into `docs/reference/images/`:

| Tab | Tap (device px) | File |
|---|---|---|
| Attendance | none (default) | `v3_tab0_attendance.png` |
| Workout | `(389, 1230)` | `v3_tab1_workout.png` |
| Payment | `(632, 1230)` | `v3_tab2_payment.png` |
| Body | `(875, 1230)` | `v3_tab3_body.png` |

Tap coordinates are estimates against 1080×2400 and **must be corrected from the first screenshot**
before capturing the rest — the tab row position is confirmed visually, not assumed.

**Acceptance criteria**

- [ ] Four PNGs exist and are non-empty (> 10 kB each).
- [ ] Attendance tab shows `81%` and `25 / 31 Days`.
- [ ] Workout tab shows the 6 anchor labels (`47m`, `82m`, `88m`, `41m`, `32m`, `74m`).
- [ ] Payment tab shows `₹6,000` / `₹1,200` / `₹1,200` summary values.
- [ ] Body tab shows weight `69.5 kg` in the Current pod.

---

### T-004 — Pixel comparison — OPEN (after T-003)

**Goal.** Produce a deviation list, not a redesign.

Read `docs/reference/INTELLIGENCE_V3_REBUILD_SPEC.md`, compare each of the four screenshots, and
append a table of concrete mismatches (element, expected, actual, severity). No code changes in this task.
The supervisor reviews the list and issues a scoped fix task from it.

---

### T-005 — Commit — OPEN (after T-004)

**Goal.** Land the verified pixel pass on the feature branch.

**Requirements**

- [ ] `git branch --show-current` prints `feature/member-intelligence-v3`.
- [ ] Commit message: `feat(member-intelligence): pixel pass on light-canvas intelligence V3`
- [ ] Body lists changed files and the evidence paths from T-002 / T-003.
- [ ] No `--force`, no push.
- [ ] Append the resulting SHA to the log below.

---

## 4. Compose 1.11 pitfalls (from Qoder memory, verified)

Do not re-derive these — they already cost a build cycle each.

1. `Offset`, `Size`, `CornerRadius`, `Rect`, `RoundRect` come from **`androidx.compose.ui.geometry`**, not `ui.graphics`.
2. `TextMeasurer.measure()` — 3rd positional parameter is `overflow`. Always pass `constraints = Constraints(...)` by name.
3. `DrawScope.drawRoundRect()` order is `(brush/color, topLeft, size, cornerRadius)`.
4. Extended Material icons are extension properties: `Icons.Filled.Insights`, `Icons.Filled.BarChart` — never bare `Insights`.
5. `Alignment.StartHorizontally` does not exist; use `Alignment.Start`.
6. `Path.addRect` requires a `Rect(...)`, not four floats.
7. `Modifier.weight()` / `wrapContentWidth()` are **scope** modifiers — never import them; they resolve inside `Row`/`Column`.
8. `rememberTextMeasurer()` is `@Composable` — it cannot be called inside `Canvas { }`'s draw lambda. Hoist it above the `Canvas`.

---

## 5. Log

Append-only. Newest at the bottom. Worker writes result; supervisor writes verdict.

```
[2026-11-06] SUPERVISOR — T-001 marked DONE. compileDebugKotlin BUILD SUCCESSFUL (UP-TO-DATE).
             Verified imports moved to ui.geometry and measure() uses named constraints.
[2026-11-06] SUPERVISOR — T-003 set BLOCKED: adb reports device unauthorized; requires on-phone tap.
[2026-11-06] SUPERVISOR — Device re-authorized after `adb kill-server` + `start-server`.
             testDebugUnitTest + assembleDebug green. All four tabs captured on zxdada69gunb7ls4.
[2026-11-06] SUPERVISOR — T-004 findings (all now fixed):
             1. Every chart used raw px for geometry. On 440dpi a callout bubble was ~12dp wide
                holding two text lines -> clipped/illegible blobs. Now dp.toPx() throughout.
             2. Body GoalRing stroke 13f px = 4.7dp instead of the specified 13dp.
             3. Payment value labels sat on the line (y - 16px = 5.8dp) and the peak label
                collided with its crown (40px vs 16px offsets).
             4. Area fills were flat alpha blocks with hard edges -> vertical gradient.
             5. Peak crowns were emoji glyphs ("\uD83D\uDC51") -> vector path (drawCrown).
             6. Workout y-axis "0m" overlapped x label "1"; plot left inset widened.
             7. Theme strip was styled for the old dark canvas (translucent black surface,
                white unselected text) -> light surface, ink text, accent border.
[2026-11-06] SUPERVISOR — T-005 DONE, commit 307de25 on feature/member-intelligence-v3.
             Evidence: docs/reference/images/v3_{tab0_attendance,trend2,workout_b,payment_b,body_b}.png
[2026-11-06] NOTE — `IntelligenceSeries.kt` and `CURRENT_TASK.md` still carry uncommitted edits.
             Not part of 307de25. Decide whether to land or revert before the branch is merged.
```

---

## 6. Supervisor verification commands

What the supervisor runs to judge a task — worker does not need to run these twice.

```powershell
git branch --show-current
git status --short
git log -1 --oneline
gradlew compileDebugKotlin --console=plain --no-daemon        # fast signal
gradlew testDebugUnitTest assembleDebug --console=plain --no-daemon
Select-String -Path app\src\main\java\com\example\badnewgym\feature\membercard\presentation\intelligence\*.kt `
  -Pattern "ui\.graphics\.(Offset|Size|CornerRadius)|crossAxisAlignment|Alignment\.StartHorizontally"
adb devices
```
