# LEM Workbench

Android research workbench for the LEM program: a small, inspectable instrument for configuring experiments, calling real model endpoints, persisting raw outcomes and keeping the research path auditable.

This repository is the **workbench**, not a claim that the full LEM architecture is implemented. The production UI follows an implement-first rule: if a control is visible, it must execute real end-to-end behavior. Planned research branches belong in notes/issues until they are executable.

## Current state

Implemented in the current Android app:

- runtime Gemini API-key storage;
- live model discovery;
- real embedding endpoint checks;
- real generation endpoint checks;
- persisted experiment configurations and results using Room;
- an experiment/ledger data model intended to keep raw measurements separate from interpretation;
- CI that runs tests, builds the debug APK and rejects common placeholder/no-op markers in production source.

The built-in smoke tests verify that the configured model endpoints actually work. They are **instrument checks**, not evidence for a LEM research hypothesis.

## Engineering rule: real behavior only

`ENGINEERING_RULES.md` is binding for this repository. In particular:

- no fabricated measurements;
- no demo backends masquerading as production behavior;
- no placeholder or no-op controls;
- persist raw output before model-generated interpretation;
- endpoint failure is stored as failure, never replaced with a plausible fallback value;
- synthetic data is allowed only when the synthetic dataset is explicitly the experiment.

`REAL_ONLY_CHANGELOG.md` records the cleanup that removed exposed unfinished behavior and hardened the actual Gemini path.

## Build

The project is an Android/Gradle application. CI uses JDK 17 and Gradle 9.3.1.

```bash
gradle testDebugUnitTest
gradle assembleDebug
```

The debug APK is produced under:

```text
app/build/outputs/apk/debug/app-debug.apk
```

A Gemini key can be entered at runtime; production code does not require a key to be committed to the repository.

## Repository layout

```text
app/src/main/java/com/example/
  api/          model discovery, API-key storage and Gemini transport
  data/         Room entities, DAOs and repository layer
  research/     executable experiment/instrument runners
  ui/           Android UI
  viewmodel/    application/research state

.github/workflows/android.yml   CI and APK build
ENGINEERING_RULES.md            production-behavior contract
REAL_ONLY_CHANGELOG.md          latest truthfulness/hardening pass
```

## Scope

LEM as a research program is broader than this Android shell. This repository is deliberately useful before the entire research stack exists: it provides a place where a real experiment can be configured, executed, persisted and inspected without turning future ideas into fake product surface.

## Licensing

This project is **source-available**, not OSI open-source. Noncommercial use is licensed under the PolyForm Noncommercial License 1.0.0; see `LICENSE`.

Commercial use requires a separate written license; see `COMMERCIAL_LICENSE.md`.
