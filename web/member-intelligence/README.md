BAD GYM Member Intelligence — Web Reference Surface

This folder mirrors the Android V3 visual system as a lightweight web implementation for rapid visual review.

The web surface contains the same eight themes:
1 Natural Fresh
2 Futuristic Neon
3 Minimal Dark
4 Glassmorphism
5 Premium 3D
6 Vibrant Gradient
7 Gym Beast Mode
8 Purple Royal

The implementation is data-driven. Theme changes alter the visual token set while the member intelligence content remains the same.

The reference composition is:
BAD GYM header
left navigation rail
event chip + exact timestamp
member identity
membership badge
active status
three metrics
intelligence signal
contextual CTA
detail panels

This is a visual companion/reference, not the production Android business backend. Production Android remains Jetpack Compose/Kotlin with repository/domain separation.

For production integration, replace the demo member object in app.js with API data and keep the theme token contract unchanged.
