import 'package:flutter/material.dart';

import 'features/attendance/presentation/attendance_screen.dart';
import 'features/body/presentation/body_screen.dart';
import 'features/payment/presentation/payment_screen.dart';
import 'features/workout/presentation/workout_screen.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const BadGymApp());
}

class BadGymApp extends StatelessWidget {
  const BadGymApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'BAD GYM · Member Intelligence',
      debugShowCheckedModeBanner: false,
      theme: BadGymTheme.light,
      home: const MemberHomePage(),
    );
  }
}

abstract final class BadGymTheme {
  static const purple = Color(0xFF8A28E2);
  static const purpleDeep = Color(0xFF5B16C8);
  static const purpleDark = Color(0xFF24134F);
  static const violet = Color(0xFFB34CFF);
  static const green = Color(0xFF19C85A);
  static const greenDeep = Color(0xFF07964C);
  static const blue = Color(0xFF168FE5);
  static const cyan = Color(0xFF06B6DA);
  static const amber = Color(0xFFFFB323);
  static const red = Color(0xFFEA4558);
  static const ink = Color(0xFF191638);
  static const muted = Color(0xFF77738E);
  static const canvas = Color(0xFFF1EDFF);
  static const surface = Color(0xFFFFFFFF);
  static const line = Color(0xFFE4DDF7);

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
          bodyMedium: TextStyle(fontSize: 12, color: muted),
          labelLarge: TextStyle(fontSize: 12, fontWeight: FontWeight.w700),
        ),
        snackBarTheme: const SnackBarThemeData(
          behavior: SnackBarBehavior.floating,
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
            const _TopBar(),
            Expanded(
              child: GestureDetector(
                behavior: HitTestBehavior.opaque,
                onHorizontalDragEnd: (details) {
                  final velocity = details.primaryVelocity ?? 0;
                  final currentIndex = MemberSection.values.indexOf(selected);
                  if (velocity < -260 && currentIndex < MemberSection.values.length - 1) {
                    select(MemberSection.values[currentIndex + 1]);
                  } else if (velocity > 260 && currentIndex > 0) {
                    select(MemberSection.values[currentIndex - 1]);
                  }
                },
                child: AnimatedSwitcher(
                  duration: const Duration(milliseconds: 220),
                  switchInCurve: Curves.easeOutCubic,
                  child: isOverview
                      ? _OverviewView(
                          key: const ValueKey('overview'),
                          onSelect: select,
                        )
                      : _DetailView(
                          key: ValueKey(selected),
                          section: selected,
                        ),
                ),
              ),
            ),
          ],
        ),
      ),
      bottomNavigationBar: _BottomNavigation(
        selected: selected,
        onSelect: select,
      ),
    );
  }
}

class _TopBar extends StatelessWidget {
  const _TopBar();

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(14, 7, 14, 8),
      child: Row(
        children: [
          Container(
            width: 39,
            height: 39,
            decoration: BoxDecoration(
              gradient: const LinearGradient(
                colors: [BadGymTheme.purple, BadGymTheme.purpleDeep],
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
              ),
              borderRadius: BorderRadius.circular(14),
              boxShadow: const [
                BoxShadow(
                  color: Color(0x338A28E2),
                  blurRadius: 9,
                  offset: Offset(0, 4),
                ),
              ],
            ),
            child: const Icon(Icons.fitness_center_rounded,
                color: Colors.white, size: 22),
          ),
          const SizedBox(width: 9),
          const Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'BAD GYM',
                  style: TextStyle(
                    fontSize: 19,
                    fontWeight: FontWeight.w900,
                    letterSpacing: -.5,
                    color: BadGymTheme.ink,
                    height: 1,
                  ),
                ),
                SizedBox(height: 3),
                Text(
                  'Member Intelligence',
                  style: TextStyle(
                    fontSize: 10,
                    color: BadGymTheme.purple,
                    fontWeight: FontWeight.w700,
                  ),
                ),
              ],
            ),
          ),
          _CircleAction(
            icon: Icons.qr_code_scanner_rounded,
            onTap: () => _showPreviewMessage(context, 'Check-in scanner preview'),
          ),
          const SizedBox(width: 7),
          _CircleAction(
            icon: Icons.notifications_none_rounded,
            dot: true,
            onTap: () => _showPreviewMessage(context, 'No new notifications in preview'),
          ),
          const SizedBox(width: 7),
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 9, vertical: 6),
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(13),
              border: Border.all(color: BadGymTheme.line),
              boxShadow: const [
                BoxShadow(color: Color(0x105B16C8), blurRadius: 7, offset: Offset(0, 2)),
              ],
            ),
            child: const Column(
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(Icons.emoji_events_rounded,
                        color: BadGymTheme.amber, size: 13),
                    SizedBox(width: 3),
                    Text('₹1,200',
                        style: TextStyle(
                            fontWeight: FontWeight.w900,
                            fontSize: 12,
                            color: BadGymTheme.ink)),
                  ],
                ),
                Text('16 Oct 2026',
                    style: TextStyle(fontSize: 8, color: BadGymTheme.muted)),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class _CircleAction extends StatelessWidget {
  const _CircleAction({
    required this.icon,
    required this.onTap,
    this.dot = false,
  });

  final IconData icon;
  final VoidCallback onTap;
  final bool dot;

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      customBorder: const CircleBorder(),
      child: SizedBox(
        width: 35,
        height: 35,
        child: Stack(
          children: [
            Container(
              decoration: BoxDecoration(
                color: Colors.white,
                shape: BoxShape.circle,
                border: Border.all(color: BadGymTheme.line),
              ),
              child: Center(
                child: Icon(icon, size: 18, color: BadGymTheme.purpleDeep),
              ),
            ),
            if (dot)
              Positioned(
                right: 1,
                top: 1,
                child: Container(
                  width: 8,
                  height: 8,
                  decoration: const BoxDecoration(
                    color: BadGymTheme.red,
                    shape: BoxShape.circle,
                  ),
                ),
              ),
          ],
        ),
      ),
    );
  }
}

void _showPreviewMessage(BuildContext context, String message) {
  ScaffoldMessenger.of(context)
    ..hideCurrentSnackBar()
    ..showSnackBar(SnackBar(content: Text(message)));
}

class _OverviewView extends StatelessWidget {
  const _OverviewView({super.key, required this.onSelect});

  final ValueChanged<MemberSection> onSelect;

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (context, constraints) {
        final compact = constraints.maxWidth <= 380;
        return ListView(
          padding: const EdgeInsets.fromLTRB(12, 1, 12, 16),
          children: [
            const _MemberIdentityCard(),
            const SizedBox(height: 11),
            _JourneyHeading(compact: compact),
            const SizedBox(height: 7),
            _FitnessJourney(onSelect: onSelect),
            const SizedBox(height: 9),
            const _InsightStrip(),
            const SizedBox(height: 8),
            const Text(
              'PREVIEW DATA · NOT CONNECTED TO LIVE GYM RECORDS',
              textAlign: TextAlign.center,
              style: TextStyle(
                fontSize: 8,
                letterSpacing: .45,
                fontWeight: FontWeight.w700,
                color: BadGymTheme.muted,
              ),
            ),
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
      padding: const EdgeInsets.fromLTRB(10, 10, 10, 9),
      decoration: BoxDecoration(
        gradient: const LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [Colors.white, Color(0xFFF8F4FF), Color(0xFFEDE7FF)],
        ),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: const Color(0xFFDCD0FA)),
        boxShadow: const [
          BoxShadow(
            color: Color(0x1C5521B8),
            blurRadius: 14,
            offset: Offset(0, 5),
          ),
          BoxShadow(
            color: Colors.white,
            blurRadius: 2,
            offset: Offset(-1, -1),
          ),
        ],
      ),
      child: Column(
        children: [
          Row(
            children: [
              Container(
                width: 58,
                height: 58,
                padding: const EdgeInsets.all(3),
                decoration: const BoxDecoration(
                  shape: BoxShape.circle,
                  gradient: LinearGradient(
                    colors: [BadGymTheme.violet, BadGymTheme.purpleDeep],
                  ),
                ),
                child: Container(
                  decoration: const BoxDecoration(
                    color: Color(0xFFE9D9FF),
                    shape: BoxShape.circle,
                  ),
                  child: const Icon(
                    Icons.person_rounded,
                    size: 37,
                    color: BadGymTheme.purpleDeep,
                  ),
                ),
              ),
              const SizedBox(width: 9),
              const Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'Ashwani Kumar Singh',
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                      style: TextStyle(
                        fontSize: 14,
                        fontWeight: FontWeight.w900,
                        color: BadGymTheme.ink,
                      ),
                    ),
                    SizedBox(height: 3),
                    Row(
                      children: [
                        Icon(Icons.badge_outlined,
                            size: 11, color: BadGymTheme.muted),
                        SizedBox(width: 3),
                        Text('BG215',
                            style: TextStyle(
                                fontSize: 9, color: BadGymTheme.muted)),
                        SizedBox(width: 7),
                        Icon(Icons.cake_outlined,
                            size: 11, color: BadGymTheme.muted),
                        SizedBox(width: 3),
                        Text('28 Yrs',
                            style: TextStyle(
                                fontSize: 9, color: BadGymTheme.muted)),
                        SizedBox(width: 7),
                        Icon(Icons.male_rounded,
                            size: 12, color: BadGymTheme.muted),
                        Text('Male',
                            style: TextStyle(
                                fontSize: 9, color: BadGymTheme.muted)),
                      ],
                    ),
                    SizedBox(height: 6),
                    Row(
                      children: [
                        _StatusPill(
                            text: 'Active',
                            color: BadGymTheme.greenDeep,
                            icon: Icons.check_circle_rounded),
                        SizedBox(width: 5),
                        _StatusPill(
                            text: 'Gold Plan',
                            color: BadGymTheme.purple,
                            icon: Icons.workspace_premium_rounded),
                      ],
                    ),
                  ],
                ),
              ),
              const SizedBox(width: 5),
              const Column(
                crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                  Text(
                    '₹1,200',
                    style: TextStyle(
                      fontSize: 15,
                      fontWeight: FontWeight.w900,
                      color: BadGymTheme.ink,
                    ),
                  ),
                  Text('/ 30d',
                      style: TextStyle(
                          fontSize: 9,
                          fontWeight: FontWeight.w600,
                          color: BadGymTheme.muted)),
                ],
              ),
            ],
          ),
          const SizedBox(height: 10),
          Container(height: 1, color: const Color(0xFFEAE3F8)),
          const SizedBox(height: 8),
          Row(
            children: [
              const Icon(Icons.layers_rounded,
                  size: 14, color: BadGymTheme.purple),
              const SizedBox(width: 5),
              const Text(
                'Membership: 22 / 30 Days',
                style: TextStyle(
                  fontSize: 10,
                  fontWeight: FontWeight.w800,
                  color: BadGymTheme.ink,
                ),
              ),
              const Spacer(),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 7, vertical: 3),
                decoration: BoxDecoration(
                  color: const Color(0xFFFFF0C9),
                  borderRadius: BorderRadius.circular(20),
                ),
                child: const Text(
                  '8 Days Left',
                  style: TextStyle(
                    fontSize: 9,
                    color: Color(0xFF9B6200),
                    fontWeight: FontWeight.w900,
                  ),
                ),
              ),
            ],
          ),
          const SizedBox(height: 6),
          ClipRRect(
            borderRadius: BorderRadius.circular(12),
            child: Stack(
              children: [
                Container(height: 9, color: const Color(0xFFE3DDF1)),
                FractionallySizedBox(
                  widthFactor: 22 / 30,
                  child: Container(
                    height: 9,
                    decoration: const BoxDecoration(
                      gradient: LinearGradient(
                        colors: [Color(0xFF16B94F), Color(0xFF7FEA43)],
                      ),
                      borderRadius: BorderRadius.all(Radius.circular(12)),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class _StatusPill extends StatelessWidget {
  const _StatusPill({
    required this.text,
    required this.color,
    required this.icon,
  });

  final String text;
  final Color color;
  final IconData icon;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 7, vertical: 4),
      decoration: BoxDecoration(
        color: color.withValues(alpha: .11),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: color.withValues(alpha: .18)),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 10, color: color),
          const SizedBox(width: 3),
          Text(
            text,
            style: TextStyle(
              fontSize: 8,
              height: 1,
              color: color,
              fontWeight: FontWeight.w900,
            ),
          ),
        ],
      ),
    );
  }
}

class _JourneyHeading extends StatelessWidget {
  const _JourneyHeading({required this.compact});

  final bool compact;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        const Expanded(
          child: Text(
            'THE ROAD TO FITNESS SUCCESS',
            style: TextStyle(
              fontSize: 12,
              fontWeight: FontWeight.w900,
              letterSpacing: .25,
              color: BadGymTheme.ink,
            ),
          ),
        ),
        Text(
          compact ? '4 modules' : 'Member overview',
          style: const TextStyle(
            fontSize: 9,
            color: BadGymTheme.purple,
            fontWeight: FontWeight.w700,
          ),
        ),
      ],
    );
  }
}

class _FitnessJourney extends StatelessWidget {
  const _FitnessJourney({required this.onSelect});

  final ValueChanged<MemberSection> onSelect;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
            _JourneyCard(
              title: 'Attendance Intelligence',
              subtitle: '25 / 31 Days',
              value: '81%',
              detail: '6 Days Streak',
              icon: Icons.calendar_month_rounded,
              color: BadGymTheme.purple,
              accent: BadGymTheme.green,
              metricIcon: Icons.local_fire_department_rounded,
              onTap: () => onSelect(MemberSection.attendance),
            ),
            const SizedBox(height: 9),
            _JourneyCard(
              title: 'Workout Intelligence',
              subtitle: 'Today · Legs',
              value: '61 min',
              detail: '420 kcal logged',
              icon: Icons.fitness_center_rounded,
              color: BadGymTheme.purple,
              accent: BadGymTheme.amber,
              metricIcon: Icons.local_fire_department_rounded,
              onTap: () => onSelect(MemberSection.workout),
            ),
            const SizedBox(height: 9),
            _JourneyCard(
              title: 'Payment Intelligence',
              subtitle: 'Next Renewal · 24 Oct 2026',
              value: 'Paid',
              detail: '₹1,200 / 30 days',
              icon: Icons.account_balance_wallet_rounded,
              color: BadGymTheme.purple,
              accent: BadGymTheme.greenDeep,
              metricIcon: Icons.verified_rounded,
              onTap: () => onSelect(MemberSection.payment),
            ),
            const SizedBox(height: 9),
            _JourneyCard(
              title: 'Body Intelligence',
              subtitle: 'Current measured weight',
              value: '69.5 kg',
              detail: 'Latest recorded value',
              icon: Icons.accessibility_new_rounded,
              color: BadGymTheme.purple,
              accent: BadGymTheme.cyan,
              metricIcon: Icons.monitor_weight_outlined,
              onTap: () => onSelect(MemberSection.body),
            ),
      ],
    );
  }
}

class _JourneyCard extends StatelessWidget {
  const _JourneyCard({
    required this.title,
    required this.subtitle,
    required this.value,
    required this.detail,
    required this.icon,
    required this.color,
    required this.accent,
    required this.metricIcon,
    required this.onTap,
  });

  final String title;
  final String subtitle;
  final String value;
  final String detail;
  final IconData icon;
  final Color color;
  final Color accent;
  final IconData metricIcon;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return Material(
      color: Colors.transparent,
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(19),
        child: Container(
          constraints: const BoxConstraints(minHeight: 82),
          padding: const EdgeInsets.fromLTRB(10, 9, 9, 9),
          decoration: BoxDecoration(
            gradient: const LinearGradient(
              begin: Alignment.topLeft,
              end: Alignment.bottomRight,
              colors: [Color(0xF7FFFFFF), Color(0xEFFFFFFF), Color(0xFFF4F0FF)],
            ),
            borderRadius: BorderRadius.circular(19),
            border: Border.all(color: Colors.white, width: 1.3),
            boxShadow: [
              BoxShadow(
                color: color.withValues(alpha: .12),
                blurRadius: 11,
                offset: const Offset(0, 4),
              ),
              const BoxShadow(
                color: Color(0x0D4B278B),
                blurRadius: 2,
                offset: Offset(0, 1),
              ),
            ],
          ),
          child: Row(
            children: [
              Container(
                width: 39,
                height: 39,
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    colors: [color.withValues(alpha: .16), accent.withValues(alpha: .10)],
                  ),
                  borderRadius: BorderRadius.circular(13),
                  border: Border.all(color: color.withValues(alpha: .12)),
                ),
                child: Icon(icon, color: color, size: 22),
              ),
              const SizedBox(width: 9),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      title,
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                      style: const TextStyle(
                        fontSize: 11,
                        fontWeight: FontWeight.w900,
                        color: BadGymTheme.ink,
                      ),
                    ),
                    const SizedBox(height: 3),
                    Text(
                      subtitle,
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                      style: const TextStyle(
                        fontSize: 9,
                        color: BadGymTheme.muted,
                      ),
                    ),
                    const SizedBox(height: 3),
                    Text(
                      value,
                      style: TextStyle(
                        fontSize: 18,
                        height: 1,
                        fontWeight: FontWeight.w900,
                        color: accent,
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(width: 5),
              Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                  Icon(metricIcon, size: 19, color: accent),
                  const SizedBox(height: 3),
                  Text(
                    detail,
                    textAlign: TextAlign.right,
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                    style: const TextStyle(
                      fontSize: 8,
                      color: BadGymTheme.muted,
                      fontWeight: FontWeight.w700,
                    ),
                  ),
                  const SizedBox(height: 3),
                  const Icon(Icons.chevron_right_rounded,
                      size: 17, color: BadGymTheme.purple),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _InsightStrip extends StatelessWidget {
  const _InsightStrip();

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 11, vertical: 10),
      decoration: BoxDecoration(
        gradient: const LinearGradient(
          colors: [Color(0xFFF4E8FF), Color(0xFFE7FBEF)],
        ),
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: const Color(0xFFDCD0F8)),
        boxShadow: const [
          BoxShadow(color: Color(0x125B16C8), blurRadius: 8, offset: Offset(0, 3)),
        ],
      ),
      child: const Row(
        children: [
          Icon(Icons.auto_awesome_rounded,
              color: BadGymTheme.purple, size: 21),
          SizedBox(width: 8),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'AI INSIGHT · PREVIEW',
                  style: TextStyle(
                    fontSize: 8,
                    fontWeight: FontWeight.w900,
                    letterSpacing: .45,
                    color: BadGymTheme.purple,
                  ),
                ),
                SizedBox(height: 2),
                Text(
                  'Great progress! Keep your 6-day streak going.',
                  style: TextStyle(
                    fontSize: 10,
                    height: 1.2,
                    fontWeight: FontWeight.w700,
                    color: BadGymTheme.ink,
                  ),
                ),
              ],
            ),
          ),
          Icon(Icons.arrow_forward_rounded,
              color: BadGymTheme.purple, size: 17),
        ],
      ),
    );
  }
}

class _DetailView extends StatelessWidget {
  const _DetailView({super.key, required this.section});

  final MemberSection section;

  @override
  Widget build(BuildContext context) {
    return switch (section) {
      MemberSection.attendance => const AttendanceScreen(memberId: 'BG215'),
      MemberSection.workout => const WorkoutScreen(memberId: 'BG215'),
      MemberSection.payment => const PaymentScreen(memberId: 'BG215'),
      MemberSection.body => const BodyScreen(memberId: 'BG215'),
      MemberSection.overview => const SizedBox.shrink(),
    };
  }
}

class _BottomNavigation extends StatelessWidget {
  const _BottomNavigation({
    required this.selected,
    required this.onSelect,
  });

  final MemberSection selected;
  final ValueChanged<MemberSection> onSelect;

  @override
  Widget build(BuildContext context) {
    const sections = [
      (MemberSection.overview, 'Overview', Icons.home_rounded),
      (MemberSection.attendance, 'Attendance', Icons.calendar_month_rounded),
      (MemberSection.workout, 'Workout', Icons.fitness_center_rounded),
      (MemberSection.payment, 'Payment', Icons.account_balance_wallet_rounded),
      (MemberSection.body, 'Body', Icons.person_outline_rounded),
    ];
    return Container(
      decoration: const BoxDecoration(
        color: Colors.white,
        border: Border(top: BorderSide(color: BadGymTheme.line)),
        boxShadow: [
          BoxShadow(
            color: Color(0x1A32106F),
            blurRadius: 14,
            offset: Offset(0, -4),
          ),
        ],
      ),
      child: SafeArea(
        top: false,
        child: Padding(
          padding: const EdgeInsets.fromLTRB(5, 6, 5, 5),
          child: Row(
            children: [
              for (final item in sections)
                Expanded(
                  child: _NavItem(
                    label: item.$2,
                    icon: item.$3,
                    selected: selected == item.$1,
                    onTap: () => onSelect(item.$1),
                  ),
                ),
            ],
          ),
        ),
      ),
    );
  }
}

class _NavItem extends StatelessWidget {
  const _NavItem({
    required this.label,
    required this.icon,
    required this.selected,
    required this.onTap,
  });

  final String label;
  final IconData icon;
  final bool selected;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(14),
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 180),
        margin: const EdgeInsets.symmetric(horizontal: 2),
        padding: const EdgeInsets.symmetric(vertical: 6, horizontal: 2),
        decoration: BoxDecoration(
          gradient: selected
              ? const LinearGradient(
                  colors: [BadGymTheme.purple, BadGymTheme.purpleDeep],
                )
              : null,
          borderRadius: BorderRadius.circular(13),
          boxShadow: selected
              ? const [
                  BoxShadow(
                    color: Color(0x338A28E2),
                    blurRadius: 8,
                    offset: Offset(0, 3),
                  ),
                ]
              : null,
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(
              icon,
              size: 19,
              color: selected ? Colors.white : BadGymTheme.purpleDark,
            ),
            const SizedBox(height: 3),
            FittedBox(
              fit: BoxFit.scaleDown,
              child: Text(
                label,
                maxLines: 1,
                style: TextStyle(
                  fontSize: 8,
                  fontWeight: selected ? FontWeight.w900 : FontWeight.w600,
                  color: selected ? Colors.white : BadGymTheme.ink,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
