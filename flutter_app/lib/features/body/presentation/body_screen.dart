import 'package:flutter/material.dart';

import '../data/body_repository.dart';
import '../domain/body_measurement.dart';

class BodyScreen extends StatefulWidget {
  const BodyScreen({super.key, required this.memberId});
  final String memberId;
  @override
  State<BodyScreen> createState() => _BodyScreenState();
}

class _BodyScreenState extends State<BodyScreen> {
  late final Future<BodySummary> _summary;
  @override
  void initState() {
    super.initState();
    _summary = DemoBodyRepository().getMeasurements(memberId: widget.memberId);
  }

  @override
  Widget build(BuildContext context) => FutureBuilder<BodySummary>(
    future: _summary,
    builder: (context, snapshot) {
      if (snapshot.hasError) return const Center(child: Text('Body measurements could not be loaded.'));
      if (!snapshot.hasData) return const Center(child: CircularProgressIndicator());
      final data = snapshot.data!;
      final latest = data.latest;
      if (latest == null) return const Center(child: Text('No body measurements recorded yet.'));
      return ListView(
        padding: const EdgeInsets.fromLTRB(12, 8, 12, 20),
        children: [
          const Row(children: [
            Icon(Icons.accessibility_new_rounded, color: _purple),
            SizedBox(width: 8),
            Text('Body Intelligence', style: TextStyle(fontSize: 18, fontWeight: FontWeight.w900, color: _ink)),
          ]),
          const SizedBox(height: 9),
          _BodyHero(latest: latest, goal: data.goalWeightKg),
          const SizedBox(height: 10),
          _MeasurementGrid(latest: latest),
          const SizedBox(height: 10),
          _HistoryCard(measurements: data.measurements),
          const SizedBox(height: 10),
          const Text('Only dated, member-recorded measurements are shown. Body fat, skeletal muscle and health metrics are not inferred.', style: TextStyle(fontSize: 10, color: _muted, height: 1.35)),
        ],
      );
    },
  );
}

const _ink = Color(0xFF1D2450);
const _muted = Color(0xFF72758D);
const _purple = Color(0xFF7628E8);
const _green = Color(0xFF159B68);

BoxDecoration _card() => BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(18), border: Border.all(color: const Color(0xFFE5E0F0)), boxShadow: const [BoxShadow(color: Color(0x10512A8A), blurRadius: 9, offset: Offset(0, 3))]);

class _BodyHero extends StatelessWidget {
  const _BodyHero({required this.latest, required this.goal});
  final BodyMeasurement latest;
  final double? goal;
  @override
  Widget build(BuildContext context) {
    final weight = latest.weightKg;
    final difference = weight == null || goal == null ? null : goal! - weight;
    return Container(
      padding: const EdgeInsets.all(15),
      decoration: BoxDecoration(gradient: const LinearGradient(colors: [Color(0xFFF1EAFE), Colors.white]), borderRadius: BorderRadius.circular(20), border: Border.all(color: const Color(0xFFE3D5FA))),
      child: Row(children: [
        Container(width: 58, height: 58, decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(18)), child: const Icon(Icons.monitor_weight_outlined, color: _purple, size: 30)),
        const SizedBox(width: 12),
        Expanded(child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
          const Text('LATEST BODY WEIGHT', style: TextStyle(fontSize: 9, fontWeight: FontWeight.w900, color: _muted, letterSpacing: .5)),
          Text(weight == null ? 'Not recorded' : '${weight.toStringAsFixed(1)} kg', style: const TextStyle(fontSize: 26, fontWeight: FontWeight.w900, color: _ink)),
          Text('Measured ${_date(latest.date)}', style: const TextStyle(fontSize: 10, color: _muted)),
        ])),
        if (difference != null)
          Column(crossAxisAlignment: CrossAxisAlignment.end, children: [
            const Text('GOAL', style: TextStyle(fontSize: 9, color: _muted, fontWeight: FontWeight.w800)),
            Text('${goal!.toStringAsFixed(1)} kg', style: const TextStyle(fontSize: 13, fontWeight: FontWeight.w900, color: _purple)),
            Text(difference >= 0 ? '${difference.toStringAsFixed(1)} kg to go' : 'Goal passed', style: const TextStyle(fontSize: 9, color: _muted)),
          ]),
      ]),
    );
  }
}

class _MeasurementGrid extends StatelessWidget {
  const _MeasurementGrid({required this.latest});
  final BodyMeasurement latest;
  @override
  Widget build(BuildContext context) {
    final items = [
      ('Waist', latest.waistCm, 'cm'),
      ('Chest', latest.chestCm, 'cm'),
      ('Arm', latest.armCm, 'cm'),
    ];
    return Container(
      padding: const EdgeInsets.all(13),
      decoration: _card(),
      child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
        const Text('BODY MEASUREMENTS', style: TextStyle(fontSize: 11, fontWeight: FontWeight.w900, color: _ink, letterSpacing: .5)),
        const SizedBox(height: 10),
        Row(children: [
          for (final item in items)
            Expanded(child: Container(
              margin: const EdgeInsets.only(right: 6),
              padding: const EdgeInsets.symmetric(vertical: 11, horizontal: 8),
              decoration: BoxDecoration(color: const Color(0xFFF7F4FD), borderRadius: BorderRadius.circular(13)),
              child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
                Text(item.$1, style: const TextStyle(fontSize: 10, color: _muted)),
                const SizedBox(height: 4),
                Text(item.$2 == null ? '—' : '${item.$2!.toStringAsFixed(1)} ${item.$3}', style: const TextStyle(fontSize: 14, fontWeight: FontWeight.w900, color: _ink)),
              ]),
            )),
        ]),
      ]),
    );
  }
}

class _HistoryCard extends StatelessWidget {
  const _HistoryCard({required this.measurements});
  final List<BodyMeasurement> measurements;
  @override
  Widget build(BuildContext context) => Container(
    padding: const EdgeInsets.all(13),
    decoration: _card(),
    child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
      const Text('MEASUREMENT HISTORY', style: TextStyle(fontSize: 11, fontWeight: FontWeight.w900, color: _ink, letterSpacing: .5)),
      const SizedBox(height: 7),
      for (final item in measurements.reversed)
        Padding(
          padding: const EdgeInsets.symmetric(vertical: 7),
          child: Row(children: [
            const Icon(Icons.event_note_rounded, size: 17, color: _purple),
            const SizedBox(width: 8),
            Expanded(child: Text(_date(item.date), style: const TextStyle(fontSize: 10, color: _muted))),
            Text(item.weightKg == null ? '—' : '${item.weightKg!.toStringAsFixed(1)} kg', style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w900, color: _ink)),
          ]),
        ),
    ]),
  );
}

String _date(DateTime date) => '${date.day.toString().padLeft(2, '0')}/${date.month.toString().padLeft(2, '0')}/${date.year}';
