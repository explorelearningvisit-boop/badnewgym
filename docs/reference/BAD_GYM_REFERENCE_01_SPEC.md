# BAD GYM — Reference 1/24 Visual Specification

Source image: user-supplied BAD GYM Member Card Design System, 1536×1024.

## Composition
The reference is a design-system board containing a large mobile member-card mockup plus card anatomy, widget-size examples, nested-widget example, resize/drag example, and responsive layouts. The large mobile mockup is the primary visual implementation reference.

## Primary mobile composition
The mock phone presents a 360dp-class card with system/status area, green BAD GYM app header, left vertical rail, content column, member identity block, current visit block, responsive widget grid, and bottom action row. The UI is bright, premium, rounded and light. No black content workspace.

## Left rail
Persistent icon-driven rail. Home is selected using a bright emerald/mint active surface. Alert indicators are small red dots. Menu labels: Home, Check-In, Check-Out, Payment, Plan, Attendance, Gym Time, Workout, Trainer / PT, Body / Progress, History, Insight, Offers, More. Rail remains usable on a 360dp phone.

## Member block
Top member block contains portrait, member name, ID, age, gender, Gold Plan (Monthly), days remaining, verification indicator, progress indicator, and right chevron. Example visual content is Aman Tripathi / BG305 / 23 yrs / Male / Gold Plan (Monthly) / 18 Days Left. Never hard-code these values in production.

## Current visit
Green live CHECK-IN event: lightning/event icon, CHECK-IN, Today, 9:25 AM, On Time, Active pill, 1h 50m, right chevron. It is tied to current event state.

## Home widgets
Reference shows Attendance: circular 68%, 19/28 with Present/Late/Absent/Holiday legend and day markers; Payment Due: ₹2,800, due in 3 days, Collect Payment; Plan: Gold (Monthly), 18 days left, progress; Trainer/PT: Rahul Trainer, PT Today, 6:00 PM; Gym Time: 18h 30m this month, bar visualization, +12%; Body Progress: 67 → 68 kg, +1 kg, line chart; bottom actions: Message, View Details, Member History, More Actions.
Actual values must come from app data when available.

## Design-system rules
Widget sizes: XS 1×1, S 2×1, M 2×2, L 3×2, XL/Hero 4×2. Payment Due is a nested container with status/title, amount, due date, and action. Responsive targets: small phone 320–360dp; normal phone 360–390dp; large phone 400–430dp; tablet 600–800dp; foldable 800dp+.

## Visual priorities
Clear member identity; current visit/event; urgent payment/action; attendance/plan; trainer and progress; bottom actions; persistent rail. Use light surfaces, emerald/mint primary accents, blue informational accents, red urgent accents, amber plan accents, and purple analytics accents. Do not restore the retired eight-theme card.
