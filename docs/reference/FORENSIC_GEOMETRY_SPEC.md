# BAD GYM — Member Intelligence Forensic Geometry Specification

Authoritative measurable baseline: **Natural Fresh** reference state.

## 1. Compositional Anatomy & Proportions

| Component / Slot | Relative Proportion | Target Dimensions (DP) | Corner Radius | Spacing / Padding | Alignment & Hierarchy |
|---|---|---|---|---|---|
| **Outer Card Shell** | 100% W / H | Fill viewport (aspect ~0.70) | `24.dp` | Outer: `8.dp` H, `6.dp` V | Rounded elevated surface container |
| **Integrated Navigation Rail** | Left column (~16% W) | `56.dp` – `60.dp` width, fill H | `14.dp` (selected) | Item gap: `6.dp` | Vertical rail with Home, Attend, Plan, Pay, Trainer, Workout, More |
| **Card Header** | Top span | Height: `36.dp` | `10.dp` buttons, `Circle` avatar | Horizontal gap: `7.dp` | Back button, BAD GYM brand pill + subtitle, notification, admin avatar |
| **Event & Time Header** | Full content width | Height: `32.dp` | `14.dp` pill | Padding: `12.dp` H, `6.dp` V | Left: `+ CHECK-IN` pill; Right: `07:15 AM` / `Just now` |
| **Hero Member Section** | Two-column hero | Height: `100.dp` | Photo: `14.dp` | Gap: `12.dp` | Left: Rectangular portrait `82.dp` × `94.dp`; Right: Name + Verified badge + ID + Motto |
| **Membership & Status Bands** | 2 equal columns | Height: `44.dp` each (Weight 1:1) | `12.dp` | Gap: `8.dp` | Left: Gold Plan band (Gold icon, Plan name + duration); Right: ACTIVE band (Green check, ACTIVE + Days left) |
| **Decision Metrics Grid** | 3 equal columns | Height: `82.dp` each (Weight 1:1:1) | `12.dp` | Gap: `6.dp` | 1: Attendance ring (`visits/target` + circular ring); 2: Payment status (Due amount + overdue/status); 3: Workouts (6-bar progression chart) |
| **Quick Shortcuts / Signals** | Full width | Height: `36.dp` – `48.dp` | `10.dp` | Gap: `6.dp` | Subordinate intelligence cards (Trainer, Workout, Services summary) |
| **Primary Contextual CTA** | Full width | Height: `46.dp` – `48.dp` | `14.dp` | Padding: `12.dp` | High-contrast actionable button (e.g. `PROCEED TO WORKOUT`, `PAY NOW`) |
| **Decorative Botanical Framing** | Fixed anchor points | Header: `30.dp`; Bottom: `110.dp` | N/A | Absolute anchors | Top-right leaf pair; Bottom-left and bottom-right botanical clusters |
| **Decorative Footer Motto** | Full width center | Height: `20.dp` | N/A | Vertical margin: `6.dp` | Centered text: *"Small Steps Big Results"* + Brand tag |
| **Debug Runtime Identity** | Bottom-right / overlay | Height: Auto | `4.dp` | Padding: `4.dp` H, `2.dp` V | `MI-V5 BUILD <sha> DEBUG` (Debug builds only) |

## 2. Layer Z-Order
0. **Layer 0**: Background surface (`colors.background` / soft gradient)
1. **Layer 1**: Ambient glow / theme decorative background vector/textures
2. **Layer 2**: Shell card boundary (`colors.surface` with `colors.border` and `24.dp` corner clip)
3. **Layer 3**: Background botanical / theme ornament overlays (anchored bottom-left, bottom-right, top-right)
4. **Layer 4**: Integrated Navigation Rail (left fixed column)
5. **Layer 5**: Scrollable Content Column (Header, Event, Hero, Membership, Metrics, CTA, Footer)
6. **Layer 6**: Interactive touches, micro-animations, ripple effects
7. **Layer 7**: Debug Runtime Identity Overlay (Debug build variant only)

## 3. Responsive Breakpoint Adaptation
- **Compact (<= 360dp)**: Rail width `52.dp`, portrait `72.dp` × `84.dp`, metric text `11.sp`, card padding `6.dp`.
- **Medium (375dp – 400dp - Baseline)**: Rail width `58.dp`, portrait `82.dp` × `94.dp`, metric text `13.sp`, card padding `8.dp`.
- **Expanded (>= 412dp)**: Rail width `64.dp`, portrait `90.dp` × `104.dp`, metric text `14.sp`, card padding `12.dp`.
