import 'package:flutter/material.dart';

import '../data/workout_repository.dart';
import '../domain/workout_session.dart';

class WorkoutScreen extends StatefulWidget {
  const WorkoutScreen({super.key, required this.memberId});
  final String memberId;
  @override
  State<WorkoutScreen> createState() => _WorkoutScreenState();
}

class _WorkoutScreenState extends State<WorkoutScreen> {
  late final Future<WorkoutSummary> _summary;
  @override
  void initState() {
    super.initState();
    _summary = DemoWorkoutRepository().getRecentSummary(memberId: widget.memberId);
  }

  @override
  Widget build(BuildContext context) => FutureBuilder<WorkoutSummary>(
    future: _summary,
    builder: (context, snapshot) {
      if (snapshot.hasError) return const Center(child: Text('Workout data could not be loaded.'));
      if (!snapshot.hasData) return const Center(child: CircularProgressIndicator());
      final data = snapshot.data!;
      return ListView(
        padding: const EdgeInsets.fromLTRB(12, 8, 12, 20),
        children: [
          const Row(children: [
            Icon(Icons.fitness_center_rounded, color: _purple),
            SizedBox(width: 8),
            Text('Workout Intelligence', style: TextStyle(fontSize: 18, fontWeight: FontWeight.w900, color: _ink)),
          ]),
          const SizedBox(height: 9),
          _WeeklyHero(summary: data),
          const SizedBox(height: 10),
          _SessionList(sessions: data.sessions),
          const SizedBox(height: 10),
          _VolumeCard(summary: data),
          const SizedBox(height: 10),
          const Text('Preview data only. Workout metrics must be derived from saved exercise and session records.', style: TextStyle(fontSize: 10, color: _muted, height: 1.35)),
        ],
      );
    },
  );
}

const _ink = Color(0xFF1D2450);
const _muted = Color(0xFF72758D);
const _purple = Color(0xFF7628E8);
const _blue = Color(0xFF269DD8);

BoxDecoration _card() => BoxDecoration(
  color: Colors.white,
  borderRadius: BorderRadius.circular(18),
  border: Border.all(color: const Color(0xFFE5E0F0)),
  boxShadow: const [BoxShadow(color: Color(0x10512A8A), blurRadius: 9, offset: Offset(0, 3))],
);

class _WeeklyHero extends StatelessWidget {
  const _WeeklyHero({required this.summary});
  final WorkoutSummary summary;
  @override
  Widget build(BuildContext context) {
    final maxMinutes = summary.sessions.fold<int>(1, (max, session) => session.durationMinutes > max ? session.durationMinutes : max);
    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        gradient: const LinearGradient(colors: [Color(0xFFF1EAFE), Colors.white]),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: const Color(0xFFE3D5FA)),
      ),
      child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
        Row(children: [
          const Expanded(child: Text('LAST 7 DAYS', style: TextStyle(fontSize: 11, fontWeight: FontWeight.w900, color: _ink, letterSpacing: .5))),
          Text('${summary.sessions.length} / ${summary.weeklyTarget} sessions', style: const TextStyle(fontSize: 10, fontWeight: FontWeight.w800, color: _purple)),
        ]),
        const SizedBox(height: 12),
        SizedBox(
          height: 106,
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.end,
            children: [
              for (var i = 0; i < 7; i++)
                Expanded(child: _DayBar(
                  label: _dayLabel(DateTime.now().subtract(Duration(days: 6 - i))),
                  minutes: _minutesForDay(summary.sessions, DateTime.now().subtract(Duration(days: 6 - i))),
                  maxMinutes: maxMinutes,
                )),
            ],
          ),
        ),
        const SizedBox(height: 12),
        Row(children: [
          Expanded(child: _Metric(value: '${summary.totalMinutes} min', label: 'Training time')),
          Expanded(child: _Metric(value: '${summary.totalCalories} kcal', label: 'Logged calories')),
          Expanded(child: _Metric(value: '${summary.activeDays}', label: 'Active days')),
        ]),
      ]),
    );
  }
}

class _DayBar extends StatelessWidget {
  const _DayBar({required this.label, required this.minutes, required this.maxMinutes});
  final String label;
  final int minutes;
  final int maxMinutes;
  @override
  Widget build(BuildContext context) => Padding(
    padding: const EdgeInsets.symmetric(horizontal: 3),
    child: Column(mainAxisAlignment: MainAxisAlignment.end, children: [
      Text(minutes == 0 ? '–' : '$minutes', style: const TextStyle(fontSize: 9, fontWeight: FontWeight.w800, color: _ink)),
      const SizedBox(height: 4),
      Container(
        height: minutes == 0 ? 4 : 62 * minutes / maxMinutes,
        decoration: BoxDecoration(
          color: minutes == 0 ? const Color(0xFFE7E1F0) : _purple,
          borderRadius: BorderRadius.circular(6),
        ),
      ),
      const SizedBox(height: 5),
      Text(label, style: const TextStyle(fontSize: 9, color: _muted)),
    ]),
  );
}

class _SessionList extends StatelessWidget {
  const _SessionList({required this.sessions});
  final List<WorkoutSession> sessions;
  @override
  Widget build(BuildContext context) => Container(
    padding: const EdgeInsets.all(13),
    decoration: _card(),
    child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
      const Text('RECENT SESSIONS', style: TextStyle(fontSize: 11, fontWeight: FontWeight.w900, color: _ink, letterSpacing: .5)),
      const SizedBox(height: 7),
      for (final session in sessions)
        Padding(
          padding: const EdgeInsets.symmetric(vertical: 7),
          child: Row(children: [
            Container(width: 34, height: 34, decoration: BoxDecoration(color: const Color(0xFFF1EAFE), borderRadius: BorderRadius.circular(11)), child: const Icon(Icons.fitness_center, color: _purple, size: 18)),
            const SizedBox(width: 9),
            Expanded(child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
              Text(session.focus, style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w800, color: _ink)),
              Text(_dateLabel(session.date), style: const TextStyle(fontSize: 9, color: _muted)),
            ])),
            Text('${session.durationMinutes} min', style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w900, color: _purple)),
          ]),
        ),
    ]),
  );
}

class _VolumeCard extends StatelessWidget {
  const _VolumeCard({required this.summary});
  final WorkoutSummary summary;
  @override
  Widget build(BuildContext context) => Container(
    padding: const EdgeInsets.all(13),
    decoration: _card(),
    child: Row(children: [
      Container(width: 44, height: 44, decoration: BoxDecoration(color: const Color(0xFFE8F6FC), borderRadius: BorderRadius.circular(14)), child: const Icon(Icons.monitor_weight_outlined, color: _blue)),
      const SizedBox(width: 10),
      const Expanded(child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
        Text('Logged training volume', style: TextStyle(fontSize: 12, fontWeight: FontWeight.w800, color: _ink)),
        Text('Sum of recorded sets × reps × load', style: TextStyle(fontSize: 9, color: _muted)),
      ])),
      Text('${summary.totalVolumeKg.toStringAsFixed(0)} kg', style: const TextStyle(fontSize: 15, fontWeight: FontWeight.w900, color: _blue)),
    ]),
  );
}

class _Metric extends StatelessWidget {
  const _Metric({required this.value, required this.label});
  final String value;
  final String label;
  @override
  Widget build(BuildContext context) => Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
    Text(value, style: const TextStyle(fontSize: 15, fontWeight: FontWeight.w900, color: _ink)),
    Text(label, style: const TextStyle(fontSize: 9, color: _muted)),
  ]);
}

int _minutesForDay(List<WorkoutSession> sessions, DateTime date) {
  for (final session in sessions) {
    if (session.date.year == date.year && session.date.month == date.month && session.date.day == date.day) return session.durationMinutes;
  }
  return 0;
}

String _dayLabel(DateTime date) {
  const labels = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
  return labels[date.weekday - 1];
}

String _dateLabel(DateTime date) => '${date.day.toString().padLeft(2, '0')}/${date.month.toString().padLeft(2, '0')}/${date.year}';
