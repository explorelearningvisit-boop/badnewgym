import 'package:flutter/material.dart';

import 'features/attendance/presentation/attendance_screen.dart';
import 'features/workout/presentation/workout_screen.dart';
import 'features/payment/presentation/payment_screen.dart';
import 'features/body/presentation/body_screen.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const BadGymApp());
}

class BadGymApp extends StatelessWidget {
  const BadGymApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'BAD GYM',
      debugShowCheckedModeBanner: false,
      theme: BadGymTheme.light,
      home: const MemberHomePage(),
    );
  }
}

abstract final class BadGymTheme {
  static const purple = Color(0xFF7628E8);
  static const purpleDark = Color(0xFF35136E);
  static const green = Color(0xFF159B68);
  static const blue = Color(0xFF269DD8);
  static const amber = Color(0xFFE8A52C);
  static const red = Color(0xFFE05261);
  static const ink = Color(0xFF1D2450);
  static const muted = Color(0xFF72758D);
  static const canvas = Color(0xFFF4F1FC);
  static const surface = Color(0xFFFFFFFF);

  static ThemeData get light => ThemeData(
        useMaterial3: true,
        scaffoldBackgroundColor: canvas,
        colorScheme: ColorScheme.fromSeed(
          seedColor: purple,
          brightness: Brightness.light,
          surface: surface,
        ),
        textTheme: const TextTheme(
          headlineSmall: TextStyle(fontSize: 22, fontWeight: FontWeight.w800, color: ink),
          titleLarge: TextStyle(fontSize: 18, fontWeight: FontWeight.w800, color: ink),
          titleMedium: TextStyle(fontSize: 15, fontWeight: FontWeight.w700, color: ink),
          bodyLarge: TextStyle(fontSize: 14, color: ink),
          bodyMedium: TextStyle(fontSize: 13, color: muted),
          labelLarge: TextStyle(fontSize: 13, fontWeight: FontWeight.w700),
        ),
        appBarTheme: const AppBarTheme(
          backgroundColor: canvas,
          foregroundColor: ink,
          elevation: 0,
          centerTitle: false,
        ),
      );
}

enum MemberSection { overview, attendance, workout, payment, body }

class MemberHomePage extends StatefulWidget {
  const MemberHomePage({super.key});

  @override
  State<MemberHomePage> createState() => _MemberHomePageState();
}

class _MemberHomePageState extends State<MemberHomePage> {
  MemberSection selected = MemberSection.overview;

  void select(MemberSection section) => setState(() => selected = section);

  @override
  Widget build(BuildContext context) {
    final isOverview = selected == MemberSection.overview;
    return Scaffold(
      body: SafeArea(
        bottom: false,
        child: Column(
          children: [
            _TopBar(),
            Expanded(
              child: AnimatedSwitcher(
                duration: const Duration(milliseconds: 220),
                child: isOverview
                    ? _OverviewView(key: const ValueKey('overview'), onSelect: select)
                    : _DetailView(key: ValueKey(selected), section: selected),
              ),
            ),
          ],
        ),
      ),
      bottomNavigationBar: _BottomNavigation(selected: selected, onSelect: select),
    );
  }
}

class _TopBar extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(14, 7, 14, 8),
      child: Row(
        children: [
          Container(
            width: 38,
            height: 38,
            decoration: BoxDecoration(
              gradient: const LinearGradient(colors: [BadGymTheme.purple, Color(0xFFB14CF5)]),
              borderRadius: BorderRadius.circular(13),
            ),
            child: const Icon(Icons.fitness_center, color: Colors.white, size: 21),
          ),
          const SizedBox(width: 9),
          const Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('BAD GYM', style: TextStyle(fontSize: 19, fontWeight: FontWeight.w900, color: BadGymTheme.ink, height: 1)),
                SizedBox(height: 3),
                Text('Member Intelligence', style: TextStyle(fontSize: 11, color: BadGymTheme.purple, fontWeight: FontWeight.w600)),
              ],
            ),
          ),
          _CircleAction(icon: Icons.qr_code_scanner_rounded),
          const SizedBox(width: 7),
          _CircleAction(icon: Icons.notifications_none_rounded, dot: true),
          const SizedBox(width: 7),
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 7),
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(15),
              border: Border.all(color: const Color(0xFFE8DFFB)),
            ),
            child: const Column(
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                Text('₹1,200', style: TextStyle(fontWeight: FontWeight.w800, fontSize: 13, color: BadGymTheme.ink)),
                Text('16 Oct', style: TextStyle(fontSize: 10, color: BadGymTheme.muted)),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class _CircleAction extends StatelessWidget {
  const _CircleAction({required this.icon, this.dot = false});
  final IconData icon;
  final bool dot;

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        Container(
          width: 36,
          height: 36,
          decoration: BoxDecoration(color: Colors.white, shape: BoxShape.circle, border: Border.all(color: const Color(0xFFE9E2F7))),
          child: Icon(icon, size: 19, color: BadGymTheme.purpleDark),
        ),
        if (dot)
          Positioned(right: 0, top: 0, child: Container(width: 8, height: 8, decoration: const BoxDecoration(color: BadGymTheme.red, shape: BoxShape.circle))),
      ],
    );
  }
}

class _OverviewView extends StatelessWidget {
  const _OverviewView({super.key, required this.onSelect});
  final ValueChanged<MemberSection> onSelect;

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (context, constraints) {
        final compact = constraints.maxWidth < 390;
        return ListView(
          padding: const EdgeInsets.fromLTRB(12, 2, 12, 18),
          children: [
            const _MemberIdentityCard(),
            const SizedBox(height: 9),
            _SummaryRow(onSelect: onSelect),
            const SizedBox(height: 10),
            _JourneyHeader(compact: compact),
            const SizedBox(height: 7),
            _JourneyModule(
              title: 'Attendance',
              subtitle: '25 of 31 days tracked',
              primary: '81%',
              secondary: '6 day streak',
              icon: Icons.calendar_month_rounded,
              color: BadGymTheme.green,
              illustration: const _CalendarMini(),
              onTap: () => onSelect(MemberSection.attendance),
            ),
            _JourneyModule(
              title: 'Workout',
              subtitle: 'Today · Legs',
              primary: '61 min',
              secondary: '420 kcal logged',
              icon: Icons.fitness_center_rounded,
              color: BadGymTheme.purple,
              illustration: const _WorkoutMini(),
              onTap: () => onSelect(MemberSection.workout),
            ),
            _JourneyModule(
              title: 'Payment',
              subtitle: 'Gold Plan · ₹1,200 / 30 days',
              primary: 'Paid',
              secondary: 'Next renewal · 24 Oct',
              icon: Icons.account_balance_wallet_rounded,
              color: BadGymTheme.blue,
              illustration: const _PaymentMini(),
              onTap: () => onSelect(MemberSection.payment),
            ),
            _JourneyModule(
              title: 'Body',
              subtitle: 'Current weight',
              primary: '69.5 kg',
              secondary: 'BMI 22.6 · sample',
              icon: Icons.accessibility_new_rounded,
              color: const Color(0xFF8A4DE8),
              illustration: const _BodyMini(),
              onTap: () => onSelect(MemberSection.body),
            ),
            const SizedBox(height: 9),
            const _InsightStrip(),
            const SizedBox(height: 8),
            const Text('Demo data only · Connect the verified gym records before production use.',
                textAlign: TextAlign.center, style: TextStyle(fontSize: 10, color: BadGymTheme.muted)),
          ],
        );
      },
    );
  }
}

class _MemberIdentityCard extends StatelessWidget {
  const _MemberIdentityCard();

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(11),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: const Color(0xFFE6DDF8)),
        boxShadow: const [BoxShadow(color: Color(0x1A5B32A0), blurRadius: 15, offset: Offset(0, 6))],
      ),
      child: Column(
        children: [
          Row(
            children: [
              Container(
                width: 54,
                height: 54,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  gradient: const LinearGradient(colors: [Color(0xFFB78BFF), Color(0xFF6325CF)]),
                  border: Border.all(color: Colors.white, width: 3),
                ),
                child: const Icon(Icons.person_rounded, size: 34, color: Colors.white),
              ),
              const SizedBox(width: 10),
              const Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text('Ashwani Kumar Singh', maxLines: 1, overflow: TextOverflow.ellipsis,
                        style: TextStyle(fontSize: 16, fontWeight: FontWeight.w800, color: BadGymTheme.ink)),
                    SizedBox(height: 3),
                    Text('BG215  ·  28 yrs  ·  Male', style: TextStyle(fontSize: 11, color: BadGymTheme.muted)),
                    SizedBox(height: 5),
                    Row(children: [
                      _StatusPill(text: 'ACTIVE', color: BadGymTheme.green),
                      SizedBox(width: 5),
                      _StatusPill(text: 'GOLD PLAN', color: BadGymTheme.purple),
                    ]),
                  ],
                ),
              ),
              const Column(
                crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                  Text('₹1,200', style: TextStyle(fontSize: 16, fontWeight: FontWeight.w900, color: BadGymTheme.ink)),
                  Text('/ 30 days', style: TextStyle(fontSize: 10, color: BadGymTheme.muted)),
                ],
              ),
            ],
          ),
          const SizedBox(height: 10),
          Row(
            children: [
              const Text('Membership', style: TextStyle(fontSize: 11, fontWeight: FontWeight.w700, color: BadGymTheme.ink)),
              const Spacer(),
              const Text('22 / 30 days', style: TextStyle(fontSize: 11, color: BadGymTheme.muted)),
            ],
          ),
          const SizedBox(height: 6),
          ClipRRect(
            borderRadius: BorderRadius.circular(10),
            child: const LinearProgressIndicator(value: 22 / 30, minHeight: 8, backgroundColor: Color(0xFFE9E4F2), color: BadGymTheme.green),
          ),
          const SizedBox(height: 4),
          const Align(alignment: Alignment.centerRight, child: Text('8 days left', style: TextStyle(fontSize: 10, color: BadGymTheme.amber, fontWeight: FontWeight.w700))),
        ],
      ),
    );
  }
}

class _StatusPill extends StatelessWidget {
  const _StatusPill({required this.text, required this.color});
  final String text;
  final Color color;
  @override
  Widget build(BuildContext context) => Container(
        padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
        decoration: BoxDecoration(color: color.withValues(alpha: .12), borderRadius: BorderRadius.circular(20)),
        child: Text(text, style: TextStyle(fontSize: 9, color: color, fontWeight: FontWeight.w900)),
      );
}

class _SummaryRow extends StatelessWidget {
  const _SummaryRow({required this.onSelect});
  final ValueChanged<MemberSection> onSelect;
  @override
  Widget build(BuildContext context) {
    const items = [
      (MemberSection.attendance, 'ATTN', '81%', Icons.calendar_month, BadGymTheme.green),
      (MemberSection.workout, 'WORKOUT', '61m', Icons.fitness_center, BadGymTheme.purple),
      (MemberSection.payment, 'PAYMENT', 'Paid', Icons.wallet, BadGymTheme.blue),
      (MemberSection.body, 'BODY', '69.5kg', Icons.accessibility_new, BadGymTheme.purple),
    ];
    return Row(
      children: [
        for (var i = 0; i < items.length; i++) ...[
          Expanded(
            child: InkWell(
              borderRadius: BorderRadius.circular(13),
              onTap: () => onSelect(items[i].$1),
              child: Container(
                padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 4),
                decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(13), border: Border.all(color: const Color(0xFFE6DFF1))),
                child: Column(
                  children: [
                    Icon(items[i].$4, color: items[i].$5, size: 15),
                    const SizedBox(height: 3),
                    Text(items[i].$2, style: const TextStyle(fontSize: 8, fontWeight: FontWeight.w800, color: BadGymTheme.muted)),
                    const SizedBox(height: 2),
                    FittedBox(child: Text(items[i].$3, style: TextStyle(fontSize: 12, fontWeight: FontWeight.w900, color: items[i].$5))),
                  ],
                ),
              ),
            ),
          ),
          if (i != items.length - 1) const SizedBox(width: 5),
        ],
      ],
    );
  }
}

class _JourneyHeader extends StatelessWidget {
  const _JourneyHeader({required this.compact});
  final bool compact;
  @override
  Widget build(BuildContext context) => Row(
        children: [
          const Expanded(child: Text('MEMBER JOURNEY', style: TextStyle(fontSize: 13, fontWeight: FontWeight.w900, letterSpacing: .5, color: BadGymTheme.ink))),
          Text(compact ? 'Tap a module for details' : 'Live member overview', style: const TextStyle(fontSize: 10, color: BadGymTheme.muted)),
        ],
      );
}

class _JourneyModule extends StatelessWidget {
  const _JourneyModule({
    required this.title, required this.subtitle, required this.primary, required this.secondary,
    required this.icon, required this.color, required this.illustration, required this.onTap,
  });
  final String title, subtitle, primary, secondary;
  final IconData icon;
  final Color color;
  final Widget illustration;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 7),
      child: Material(
        color: Colors.white,
        borderRadius: BorderRadius.circular(18),
        child: InkWell(
          borderRadius: BorderRadius.circular(18),
          onTap: onTap,
          child: Container(
            constraints: const BoxConstraints(minHeight: 88),
            padding: const EdgeInsets.fromLTRB(10, 9, 10, 9),
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(18),
              border: Border.all(color: color.withValues(alpha: .18)),
              boxShadow: const [BoxShadow(color: Color(0x10512A8A), blurRadius: 9, offset: Offset(0, 3))],
            ),
            child: Row(
              children: [
                Container(
                  width: 42, height: 42,
                  decoration: BoxDecoration(
                    gradient: LinearGradient(begin: Alignment.topLeft, end: Alignment.bottomRight, colors: [color.withValues(alpha: .16), color.withValues(alpha: .06)]),
                    borderRadius: BorderRadius.circular(14),
                  ),
                  child: Icon(icon, color: color, size: 23),
                ),
                const SizedBox(width: 9),
                Expanded(
                  flex: 5,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(title, style: const TextStyle(fontSize: 14, fontWeight: FontWeight.w900, color: BadGymTheme.ink)),
                      const SizedBox(height: 2),
                      Text(subtitle, maxLines: 1, overflow: TextOverflow.ellipsis, style: const TextStyle(fontSize: 10, color: BadGymTheme.muted)),
                      const SizedBox(height: 5),
                      Text(primary, style: TextStyle(fontSize: 18, fontWeight: FontWeight.w900, color: color)),
                      Text(secondary, maxLines: 1, overflow: TextOverflow.ellipsis, style: const TextStyle(fontSize: 10, color: BadGymTheme.muted)),
                    ],
                  ),
                ),
                const SizedBox(width: 4),
                SizedBox(width: 76, height: 62, child: illustration),
                const Icon(Icons.chevron_right_rounded, color: BadGymTheme.muted, size: 18),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class _CalendarMini extends StatelessWidget {
  const _CalendarMini();
  @override
  Widget build(BuildContext context) => Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Text('OCT 2026', style: TextStyle(fontSize: 8, fontWeight: FontWeight.w800, color: BadGymTheme.muted)),
          const SizedBox(height: 4),
          for (var row = 0; row < 3; row++)
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: List.generate(7, (col) {
                final n = row * 7 + col + 1;
                final present = [1, 2, 5, 6, 7, 8, 9, 12, 13, 14, 15, 16, 19, 20, 21, 23, 26, 27, 29, 30, 31].contains(n);
                final absent = [17, 24].contains(n);
                final c = present ? BadGymTheme.green : absent ? BadGymTheme.red : const Color(0xFFE9E5F1);
                return Container(width: 7, height: 7, margin: const EdgeInsets.all(2), decoration: BoxDecoration(color: c, shape: BoxShape.circle));
              }),
            ),
        ],
      );
}

class _WorkoutMini extends StatelessWidget {
  const _WorkoutMini();
  @override
  Widget build(BuildContext context) {
    const heights = [18.0, 30.0, 22.0, 40.0, 26.0, 34.0, 16.0];
    return Row(
      crossAxisAlignment: CrossAxisAlignment.end,
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: [for (final h in heights) Container(width: 7, height: h, decoration: BoxDecoration(color: BadGymTheme.purple, borderRadius: BorderRadius.circular(5)))],
    );
  }
}

class _PaymentMini extends StatelessWidget {
  const _PaymentMini();
  @override
  Widget build(BuildContext context) => Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Icon(Icons.verified_rounded, color: BadGymTheme.green, size: 28),
          const SizedBox(height: 3),
          Container(padding: const EdgeInsets.symmetric(horizontal: 9, vertical: 3), decoration: BoxDecoration(color: BadGymTheme.green.withValues(alpha: .12), borderRadius: BorderRadius.circular(20)), child: const Text('PAID', style: TextStyle(fontSize: 9, fontWeight: FontWeight.w900, color: BadGymTheme.green))),
        ],
      );
}

class _BodyMini extends StatelessWidget {
  const _BodyMini();
  @override
  Widget build(BuildContext context) => Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Icon(Icons.accessibility_new_rounded, size: 34, color: BadGymTheme.purple),
          Container(width: 50, height: 4, decoration: BoxDecoration(color: BadGymTheme.green, borderRadius: BorderRadius.circular(5))),
          const SizedBox(height: 3),
          const Text('69.5 kg', style: TextStyle(fontSize: 9, fontWeight: FontWeight.w800, color: BadGymTheme.ink)),
        ],
      );
}

class _InsightStrip extends StatelessWidget {
  const _InsightStrip();
  @override
  Widget build(BuildContext context) => Container(
        padding: const EdgeInsets.all(11),
        decoration: BoxDecoration(
          gradient: const LinearGradient(colors: [Color(0xFFF1E8FF), Color(0xFFE6F7F1)]),
          borderRadius: BorderRadius.circular(16),
          border: Border.all(color: const Color(0xFFE5D9FB)),
        ),
        child: const Row(
          children: [
            Icon(Icons.auto_awesome_rounded, color: BadGymTheme.purple, size: 22),
            SizedBox(width: 9),
            Expanded(child: Text('Keep your 6-day attendance streak going. Demo insight — connect real member records before enabling AI recommendations.',
                style: TextStyle(fontSize: 11, height: 1.3, color: BadGymTheme.ink, fontWeight: FontWeight.w600))),
          ],
        ),
      );
}

class _DetailView extends StatelessWidget {
  const _DetailView({super.key, required this.section});
  final MemberSection section;

  @override
  Widget build(BuildContext context) {
    if (section == MemberSection.attendance) {
      return const AttendanceScreen(memberId: 'BG215');
    }
    if (section == MemberSection.workout) {
      return const WorkoutScreen(memberId: 'BG215');
    }
    if (section == MemberSection.payment) {
      return const PaymentScreen(memberId: 'BG215');
    }
    if (section == MemberSection.body) {
      return const BodyScreen(memberId: 'BG215');
    }
    final details = switch (section) {
      MemberSection.attendance => _DetailData('Attendance Intelligence', Icons.calendar_month, BadGymTheme.green, '81%', '25 / 31 days', ['Present 23', 'Late 2', 'Absent 2', 'Holiday 4'], '6 day streak'),
      MemberSection.workout => _DetailData('Workout Intelligence', Icons.fitness_center, BadGymTheme.purple, '61 min', 'Today · Legs', ['Weekly sessions 4 / 5', 'Logged volume 8,420 kg', 'Peak session 88 min', 'Monthly total 19.4 hr'], '420 kcal logged'),
      MemberSection.payment => _DetailData('Payment Intelligence', Icons.account_balance_wallet, BadGymTheme.blue, 'PAID', '₹1,200 · Gold Plan', ['Next renewal 24 Oct 2026', 'Lifetime paid ₹14,400*', 'Outstanding ₹0*', 'Payment cycles 12*'], 'Payment recorded · demo'),
      MemberSection.body => _DetailData('Body Intelligence', Icons.accessibility_new, BadGymTheme.purple, '69.5 kg', 'Current weight', ['BMI 22.6*', 'Target 72.0 kg*', 'Chest / waist / arm measurements', 'Trend available from dated records'], 'Body data must be member-measured'),
      MemberSection.overview => _DetailData('Member Overview', Icons.dashboard, BadGymTheme.purple, '79', 'Fitness score · demo', ['Attendance 81%', 'Workout consistency 61%', 'Payment status Paid', 'Membership 8 days left'], 'Demo data only'),
    };
    return ListView(
      key: ValueKey(section),
      padding: const EdgeInsets.fromLTRB(12, 6, 12, 20),
      children: [
        Row(
          children: [
            Icon(details.icon, color: details.color, size: 22),
            const SizedBox(width: 8),
            Expanded(child: Text(details.title, style: Theme.of(context).textTheme.titleLarge)),
            IconButton(onPressed: () => Navigator.maybePop(context), icon: const Icon(Icons.more_horiz)),
          ],
        ),
        Container(
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            gradient: LinearGradient(colors: [details.color.withValues(alpha: .12), Colors.white]),
            borderRadius: BorderRadius.circular(20),
            border: Border.all(color: details.color.withValues(alpha: .2)),
          ),
          child: Row(
            children: [
              Expanded(
                child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
                  Text(details.primary, style: TextStyle(fontSize: 30, fontWeight: FontWeight.w900, color: details.color)),
                  Text(details.secondary, style: const TextStyle(fontSize: 13, color: BadGymTheme.ink)),
                  const SizedBox(height: 6),
                  Text(details.footer, style: const TextStyle(fontSize: 11, color: BadGymTheme.muted)),
                ]),
              ),
              SizedBox(width: 90, height: 90, child: _DetailVisual(color: details.color, icon: details.icon)),
            ],
          ),
        ),
        const SizedBox(height: 12),
        const Text('KEY INFORMATION', style: TextStyle(fontSize: 11, fontWeight: FontWeight.w900, letterSpacing: .6, color: BadGymTheme.muted)),
        const SizedBox(height: 7),
        for (final item in details.items) _InfoLine(text: item, color: details.color),
        const SizedBox(height: 10),
        const Text('This prototype uses clearly labelled sample values. Connect the verified backend records before operational use.',
            style: TextStyle(fontSize: 11, height: 1.35, color: BadGymTheme.muted)),
      ],
    );
  }
}

class _DetailData {
  const _DetailData(this.title, this.icon, this.color, this.primary, this.secondary, this.items, this.footer);
  final String title, primary, secondary, footer;
  final IconData icon;
  final Color color;
  final List<String> items;
}

class _DetailVisual extends StatelessWidget {
  const _DetailVisual({required this.color, required this.icon});
  final Color color;
  final IconData icon;
  @override
  Widget build(BuildContext context) => Container(
        decoration: BoxDecoration(shape: BoxShape.circle, color: Colors.white, border: Border.all(color: color.withValues(alpha: .2), width: 7), boxShadow: [BoxShadow(color: color.withValues(alpha: .15), blurRadius: 12)]),
        child: Icon(icon, color: color, size: 38),
      );
}

class _InfoLine extends StatelessWidget {
  const _InfoLine({required this.text, required this.color});
  final String text;
  final Color color;
  @override
  Widget build(BuildContext context) => Container(
        margin: const EdgeInsets.only(bottom: 7),
        padding: const EdgeInsets.symmetric(horizontal: 13, vertical: 13),
        decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(14), border: Border.all(color: const Color(0xFFEAE5F3))),
        child: Row(children: [
          Icon(Icons.check_circle_rounded, color: color, size: 17),
          const SizedBox(width: 9),
          Expanded(child: Text(text, style: const TextStyle(fontSize: 13, fontWeight: FontWeight.w600, color: BadGymTheme.ink))),
        ]),
      );
}

class _BottomNavigation extends StatelessWidget {
  const _BottomNavigation({required this.selected, required this.onSelect});
  final MemberSection selected;
  final ValueChanged<MemberSection> onSelect;

  @override
  Widget build(BuildContext context) {
    const sections = [
      (MemberSection.overview, 'Overview', Icons.dashboard_rounded),
      (MemberSection.attendance, 'Attendance', Icons.calendar_month_rounded),
      (MemberSection.workout, 'Workout', Icons.fitness_center_rounded),
      (MemberSection.payment, 'Payment', Icons.account_balance_wallet_rounded),
      (MemberSection.body, 'Body', Icons.accessibility_new_rounded),
    ];
    return Container(
      decoration: const BoxDecoration(color: Colors.white, boxShadow: [BoxShadow(color: Color(0x14000000), blurRadius: 12, offset: Offset(0, -3))]),
      child: SafeArea(
        top: false,
        child: Padding(
          padding: const EdgeInsets.fromLTRB(4, 7, 4, 5),
          child: Row(
            children: [
              for (final item in sections)
                Expanded(
                  child: InkWell(
                    borderRadius: BorderRadius.circular(13),
                    onTap: () => onSelect(item.$1),
                    child: Padding(
                      padding: const EdgeInsets.symmetric(vertical: 5, horizontal: 1),
                      child: Column(mainAxisSize: MainAxisSize.min, children: [
                        Icon(item.$3, size: 20, color: selected == item.$1 ? BadGymTheme.purple : BadGymTheme.muted),
                        const SizedBox(height: 3),
                        FittedBox(child: Text(item.$2, style: TextStyle(fontSize: 9, fontWeight: selected == item.$1 ? FontWeight.w900 : FontWeight.w500, color: selected == item.$1 ? BadGymTheme.purple : BadGymTheme.muted))),
                      ]),
                    ),
                  ),
                ),
            ],
          ),
        ),
      ),
    );
  }
}
