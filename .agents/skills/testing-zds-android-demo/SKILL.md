---
name: testing-zds-android-demo
description: Runtime navigation and resource-packaging checks for the ZDS Android demo on an Android emulator.
---

# ZDS Android demo runtime testing

## Devin Secrets Needed
None for the local demo. Publishing is a separate authenticated workflow.

## Run and inspect
- Use JDK 17 and the installed Android SDK; consult the repo blueprint for SDK, Gradle mirror, and local publishing-property setup.
- Build with `./gradlew assembleDebug`; install `app/build/outputs/apk/debug/app-debug.apk` using `adb install -r`.
- Launch `adb shell am start -W -n com.zebra.zdsDemo/.MainActivity`.
- Use the visible emulator window for recorded navigation. Allow drawer/fragment transitions to finish before clicking another target.
- The hamburger drawer scroll position persists between destinations. Scroll to reveal later entries instead of assuming menu coordinates remain fixed.
- Confirm current drawer entries against `app/src/main/res/menu/navigation_drawer.xml`. Theme switching uses the moon icon in the app toolbar.
- Dialog, dropdown, snackbar, and banner pages have action buttons that must be clicked to see the actual component. Snackbar lasts roughly one second; capture about 400 ms after clicking.
- App resources may not all be referenced by demo screens. Check archive entries using `unzip -l` and resource-table names using `aapt2 dump resources` rather than claiming unused icons were visually rendered.

## Crash evidence
Before launch, obtain a full device timestamp using `adb shell date` (ensure any format argument with spaces is quoted for the remote shell). Collect `adb logcat -d -T 'MM-DD HH:MM:SS.000'` afterward without clearing logs. Check FATAL EXCEPTION/AndroidRuntime messages against the app package and timestamp: unrelated emulator applications may have historical crashes. Verify `adb shell pidof com.zebra.zdsDemo` and the resumed activity in `adb shell dumpsys activity activities`.
