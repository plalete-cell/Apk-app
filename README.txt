GLITCH LAB — native Android project

WHAT THIS BUILDS
- A real Android debug APK through GitHub Actions.
- A visible app with adjustable random wait interval (15 seconds minimum).
- Requires the user to explicitly grant Android's “Display over other apps” permission.
- Overlay shows for 12 seconds and has a visible STOP button.
- Sessions are scheduled only while the display is on; screen-off removes the overlay and pauses scheduling.
- No boot receiver, no hidden startup, no lock-screen overlay.

BUILD ON GITHUB
1. Upload ALL files and folders in this project to your GitHub repository (repository root), including .github/workflows/build-android.yml.
2. Commit the changes to the main branch.
3. Open the repository's Actions tab.
4. Select “Build GLITCH Lab Android APK”, then “Run workflow” if it hasn't run automatically.
5. Open the successful run and download the artifact “GLITCH-Lab-Android-APK”.
6. Extract it; app-debug.apk is the installable APK. Transfer it to your Android phone and install it. Android may ask to allow installs from that source.
7. Open GLITCH Lab, grant “Display over other apps”, then tap “Start random sessions”.

NOTES
- This is a prototype, not a Play Store release; it is an unsigned debug APK.
- Android manufacturers may limit overlays/background services. Keep the app visible/active and use the persistent notification or overlay STOP button to stop it.
- No service is automatically started after reboot.


Saving settings: The native Android app now remembers the minimum and maximum wait times on this device. The HTML version also saves timing, intensity, online-image/sound options, and custom text fragments in the browser's local storage. Images selected from the phone must be selected again after reloading the HTML page.
