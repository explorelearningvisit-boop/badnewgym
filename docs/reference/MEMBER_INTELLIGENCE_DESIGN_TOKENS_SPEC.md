# BAD GYM — Member Intelligence Dashboard
## Design Tokens & UI/UX Styling Specification

---

### 1. Color Palette Tokens

The design uses a luminous, high-contrast palette combining an ethereal mint-aurora background with four vivid jewel-tone bento accents.

#### 1.1 Canvas & Background Gradients
```kotlin
// Ethereal Mint-Aurora Canvas
val CanvasBackgroundGradient = Brush.verticalGradient(
    listOf(
        Color(0xFFE3F8ED), // Luminous Mint top
        Color(0xFFDCF6EA), // Fresh Seafoam
        Color(0xFFE8FAF1), // Pale Aqua
        Color(0xFFF3FCF7), // Soft White-Mint bottom
        Color(0xFFE2F5EC)  // Base footer tone
    )
)

// Background Atmosphere Glow Orbs
val AuroraCyanGlow = Color(0x3338EF7D)
val AuroraEmeraldGlow = Color(0x2611998E)
val AuroraGoldGlow = Color(0x1AFBBF24)
```

#### 1.2 Bento Card Accent Gradients
Each bento card features a multi-stop gradient with high luminosity and vibrancy:

```kotlin
// 1. Attendance Bento (Emerald / Mint)
val AttendanceCardGradient = Brush.verticalGradient(
    listOf(
        Color(0xFF00E676), // Neon Spring Green
        Color(0xFF10B981), // Emerald
        Color(0xFF059669)  // Deep Forest Mint
    )
)
val AttendanceBorderGlow = Color(0x66A7F3D0)
val AttendanceChartBar = Color(0xFFE6FFFA)

// 2. Payment Bento (Amber / Flame Orange)
val PaymentCardGradient = Brush.verticalGradient(
    listOf(
        Color(0xFFFFA000), // Vibrant Amber
        Color(0xFFF59E0B), // Golden Honey
        Color(0xFFEA580C)  // Flame Orange
    )
)
val PaymentBorderGlow = Color(0x66FDE68A)
val PaymentWarningPill = Color(0xE6DC2626) // Deep Coral Alert

// 3. Workout Bento (Cyan / Electric Sky Blue)
val WorkoutCardGradient = Brush.verticalGradient(
    listOf(
        Color(0xFF00E5FF), // Electric Cyan
        Color(0xFF0284C7), // Sky Blue
        Color(0xFF1D4ED8)  // Royal Cobalt
    )
)
val WorkoutBorderGlow = Color(0x66BAE6FD)
val WorkoutChartBar = Color(0xFFE0F2FE)

// 4. Body Progress Bento (Fuchsia / Vivid Violet)
val BodyProgressCardGradient = Brush.verticalGradient(
    listOf(
        Color(0xFFF43F5E), // Rose Magenta
        Color(0xFFD946EF), // Vivid Fuchsia
        Color(0xFF8B5CF6)  // Radiant Violet
    )
)
val BodyProgressBorderGlow = Color(0x66F5D0FE)
val BodyProgressChartLine = Color(0xFFFDE047)
```

#### 1.3 Glassmorphism & Specular Reflection Tokens
```kotlin
// Card Surface & Borders
val GlassSurfaceWhite = Color(0xCCFFFFFF) // 80% opacity white
val GlassSurfaceUltra = Color(0xEEFFFFFF) // 93% opacity white
val GlassBorderHighlight = Color(0x80FFFFFF) // 50% white specular stroke
val GlassBorderGlow = Color(0x4034D399) // 25% emerald rim glow

// Specular Reflection Brushes
val GlassSpecularGlare = Brush.verticalGradient(
    listOf(
        Color(0x99FFFFFF), // Crisp top highlight
        Color(0x20FFFFFF),
        Color.Transparent
    )
)

val CardInnerBevel = Brush.linearGradient(
    listOf(
        Color(0x80FFFFFF),
        Color.Transparent
    )
)

// Drop Shadows
val CardShadowEmerald = Color(0x1A065F46)
val DockShadow = Color(0x260F172A)
```

#### 1.4 Typography & Text Colors
```kotlin
val TextPrimaryDark = Color(0xFF0F172A)    // Deep Slate Navy (900)
val TextBrandHeader = Color(0xFF0A3F2C)    // BAD GYM Deep Forest Green
val TextSecondarySlate = Color(0xFF334155) // Slate (700)
val TextMutedGrey = Color(0xFF64748B)      // Muted Slate (500)
val TextPureWhite = Color(0xFFFFFFFF)      // Pure White (for bento cards)
val TextTranslucentWhite = Color(0xCCFFFFFF) // 80% White
val TextPositiveGreen = Color(0xFF10B981)  // Positive Metric Indicator
val TextNegativeRed = Color(0xFFEF4444)    // Negative Metric Indicator
val TextWarningAmber = Color(0xFFF59E0B)   // Warning Text
```

---

### 2. Geometry & Spacing Grid

| Token Name | Value | Purpose |
| :--- | :--- | :--- |
| `ScreenPaddingHorizontal` | `16.dp` | Left and right safe margins of the screen |
| `ScreenPaddingTop` | `8.dp` | Top spacing below status bar |
| `BentoCornerRadius` | `24.dp` | Corner rounding for the 4 bento cards |
| `OverviewCardRadius` | `28.dp` | Corner rounding for the Detailed Overview card |
| `PillCornerRadius` | `50.dp` | Fully rounded pill buttons and badges |
| `SquircleCornerRadius` | `12.dp` | Small icon container rounding |
| `AvatarDiameter` | `96.dp` | Centerpiece circular profile image |
| `AvatarRingStroke` | `3.5.dp` | Glowing halo around avatar |
| `BentoCardHeight` | `132.dp` | Standardized height for all 4 bento cards |
| `BottomDockHeight` | `64.dp` | Floating bottom navigation capsule height |
| `MinTouchTarget` | `48.dp` | Accessibility standard for clickable controls |

---

### 3. Typography Scale (Inter / Roboto Display)

| Style Name | Font Size | Font Weight | Line Height | Letter Spacing | Typical Usage |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `BrandHeader` | `20.sp` | `ExtraBold` (800) | `24.sp` | `0.5.sp` | `"BAD GYM"` App Title |
| `BrandSubHeader` | `12.sp` | `Medium` (500) | `16.sp` | `0.2.sp` | `"Member Intelligence"` |
| `HeroMemberName` | `20.sp` | `Bold` (700) | `24.sp` | `0.sp` | `"Ashwini Singh"` |
| `HeroMemberId` | `13.sp` | `Medium` (500) | `18.sp` | `0.sp` | `"BG305 \| Prime Plan"` |
| `BentoBigValue` | `26.sp` | `Black` (900) | `30.sp` | `-0.5.sp` | `"85%"`, `"₹2,800"`, `"47 min"`, `"69.5 kg"` |
| `BentoCardTitle` | `12.sp` | `SemiBold` (600) | `16.sp` | `0.2.sp` | `"Attendance"`, `"Payment"` |
| `BentoSubLabel` | `11.sp` | `Normal` (400) | `14.sp` | `0.sp` | `"18/21 Days"`, `"Due Amount"` |
| `BadgeText` | `10.sp` | `Bold` (700) | `12.sp` | `0.2.sp` | `"Active"`, `"+12%"`, `"! 25 Days Left"` |
| `OverviewHeader` | `16.sp` | `Bold` (700) | `20.sp` | `0.sp` | `"Detailed Overview"` |
| `NavLabel` | `11.sp` | `SemiBold` (600) | `14.sp` | `0.sp` | Bottom Nav labels (`Home`, `Progress`) |

---

### 4. Iconography & Graphical Embellishments

1. **Squircle Frosted Icon Carriers**:
   - Size: `32dp x 32dp` with `10dp` rounded corners.
   - Background: `Color(0x33FFFFFF)` (20% white translucency).
   - Border: `1dp` solid white highlight.
   - Tint: Pure white icon glyph.
2. **Gold Crown Badge**:
   - Location: Centerpiece avatar ring (top-right) and name title.
   - Asset: Vector glyph with metallic amber-gold gradient `#F59E0B` to `#FBBF24`.
3. **Pill Badges**:
   - **Active Status**: Solid Emerald Green `#10B981` with white checkmark.
   - **Expiry Alert**: Frosted white with coral red text `#E11D48` and subtle gold border.
   - **Trend Indicators**: Translucent pill with green `↑` or red `↓` indicator.
4. **Dual Chart Engine**:
   - **Vertical Bars**: Linear gradient from cyan (`#00E5FF`) to emerald (`#10B981`), `6dp` bar width, `3dp` top corner radius.
   - **Trend Spline**: `2.5dp` curved bezier line with `#00E676` neon stroke.
   - **Floating Tooltip**: Pill tooltip in `#059669` displaying primary metric and secondary ratio.
