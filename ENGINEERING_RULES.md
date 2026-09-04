# LEM Workbench engineering rule: real behavior only

Production code follows a strict **implement first, expose second** policy.

1. **No fabricated measurements.** A number shown as a metric must come from an executed experiment or API call and be persisted with its experiment record.
2. **No product mocks.** Production builds contain no mock services, canned API responses, simulated experiment outcomes, or demo-only backends.
3. **No placeholder UI.** A screen, button, menu item, sweep, critic, trainer, exporter, or research action appears only after the underlying behavior exists end to end.
4. **No no-op controls.** Controls with empty callbacks or callbacks that merely announce future work are forbidden.
5. **Persist before interpretation.** Raw experimental output is stored before any LLM-generated interpretation or critique.
6. **Failure is data.** Real endpoint failures are persisted as failed instrument records; they are never silently replaced with fallback values.
7. **Synthetic scientific benchmarks are allowed only when they are the experiment.** Controlled synthetic datasets may be used to test a research hypothesis, but must be explicitly labelled synthetic. They may never impersonate application functionality or real-world measurements.
8. **Future ideas live in issues/docs, not in production UI.** Unbuilt research branches stay in the backlog until executable.
9. **CI enforces the rule.** Production source is scanned for common stub/no-op markers before tests and APK assembly.

The practical criterion is simple: if a user can trigger it in the APK, it must execute real code and produce a real, inspectable result.
