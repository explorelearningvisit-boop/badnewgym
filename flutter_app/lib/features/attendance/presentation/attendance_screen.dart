import 'package:flutter/material.dart';

import '../data/attendance_repository.dart';
import '../domain/attendance_record.dart';

class AttendanceScreen extends StatefulWidget {
  const AttendanceScreen({super.key, required this.memberId});
  final String memberId;

  @override
  State<AttendanceScreen> createState() => _AttendanceScreenState();
}

class _AttendanceScreenState extends State<AttendanceScreen> {
  final AttendanceRepository _repository = DemoAttendanceRepository();
  late DateTime _month;
  late Future<AttendanceSummary> _summary;

  @override
  void initState() {
    super.initState();
    final now = DateTime.now();
    _month = DateTime(now.year, now.month);
    _summary = _load();
  }

  Future<AttendanceSummary> _load() => _repository.getMonthlySummary(memberId: widget.memberId, month: _month);

  void _changeMonth(int delta) {
    setState(() {
      _month = DateTime(_month.year, _month.month + delta);
      _summary = _load();
    });
  }

  @override
  Widget build(BuildContext context) => FutureBuilder<AttendanceSummary>(
        future: _summary,
        builder: (context, snapshot) {
          if (snapshot.hasError) return const Center(child: Text('Attendance could not be loaded.'));
          if (!snapshot.hasData) return const Center(child: CircularProgressIndicator());
          final data = snapshot.data!;
          return ListView(
            padding: const EdgeInsets.fromLTRB(12, 8, 12, 20),
            children: [
              Row(children: [
                const Icon(Icons.calendar_month_rounded, color: _green),
                const SizedBox(width: 8),
                const Expanded(child: Text('Attendance Intelligence', style: TextStyle(fontSize: 18, fontWeight: FontWeight.w900, color: _ink))),
                IconButton(onPressed: () => _changeMonth(-1), icon: const Icon(Icons.chevron_left)),
                Text(_monthLabel(_month), style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w800)),
                IconButton(onPressed: () => _changeMonth(1), icon: const Icon(Icons.chevron_right)),
              ]),
              const SizedBox(height: 8),
              _Hero(summary: data),
              const SizedBox(height: 10),
              _Calendar(month: _month, records: data.records),
              const SizedBox(height: 10),
              _Distribution(summary: data),
              const SizedBox(height: 10),
              const Text('Preview data only. Replace with verified attendance events before operational use.', style: TextStyle(fontSize: 10, color: _muted)),
            ],
          );
        },
      );
}

const _ink = Color(0xFF1D2450);
const _muted = Color(0xFF72758D);
const _green = Color(0xFF159B68);
const _amber = Color(0xFFE8A52C);
const _red = Color(0xFFE05261);
const _line = Color(0xFFE5E0F0);

BoxDecoration _card() => BoxDecoration(
  color: Colors.white,
  borderRadius: BorderRadius.circular(18),
  border: Border.all(color: _line),
  boxShadow: const [BoxShadow(color: Color(0x10512A8A), blurRadius: 9, offset: Offset(0, 3))],
);

class _Hero extends StatelessWidget {
  const _Hero({required this.summary});
  final AttendanceSummary summary;

  @override
  Widget build(BuildContext context) {
    final percent = (summary.attendanceRate * 100).round();
    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        gradient: const LinearGradient(colors: [Color(0xFFE8F8F0), Colors.white]),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: const Color(0xFFCDEBDD)),
      ),
      child: Row(children: [
        SizedBox(
          width: 84,
          height: 84,
          child: Stack(alignment: Alignment.center, children: [
            SizedBox.expand(child: CircularProgressIndicator(value: summary.attendanceRate, strokeWidth: 8, backgroundColor: const Color(0xFFD9EDE3), color: _green, strokeCap: StrokeCap.round)),
            Column(mainAxisSize: MainAxisSize.min, children: [
              Text('$percent%', style: const TextStyle(fontSize: 22, fontWeight: FontWeight.w900, color: Color(0xFF146B4B))),
              const Text('ATTENDANCE', style: TextStyle(fontSize: 7, fontWeight: FontWeight.w800, color: Color(0xFF52706A))),
            ]),
          ]),
        ),
        const SizedBox(width: 14),
        Expanded(child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
          Text('${summary.attendedCount} visits', style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w900, color: _ink)),
          Text('of ${summary.trackedCount} recorded days', style: const TextStyle(fontSize: 11, color: _muted)),
          const SizedBox(height: 9),
          Wrap(spacing: 9, runSpacing: 6, children: [
            _Count(label: 'Present', count: summary.presentCount, color: _green),
            _Count(label: 'Late', count: summary.lateCount, color: _amber),
            _Count(label: 'Absent', count: summary.absentCount, color: _red),
            _Count(label: 'Off', count: summary.holidayCount, color: const Color(0xFF9AA0B2)),
          ]),
        ])),
      ]),
    );
  }
}

class _Count extends StatelessWidget {
  const _Count({required this.label, required this.count, required this.color});
  final String label;
  final int count;
  final Color color;
  @override
  Widget build(BuildContext context) => Row(mainAxisSize: MainAxisSize.min, children: [
    Container(width: 7, height: 7, decoration: BoxDecoration(color: color, shape: BoxShape.circle)),
    const SizedBox(width: 4),
    Text('$label $count', style: const TextStyle(fontSize: 9, color: Color(0xFF52576D), fontWeight: FontWeight.w600)),
  ]);
}

class _Calendar extends StatelessWidget {
  const _Calendar({required this.month, required this.records});
  final DateTime month;
  final List<AttendanceRecord> records;

  @override
  Widget build(BuildContext context) {
    final offset = DateTime(month.year, month.month, 1).weekday % 7;
    final days = DateTime(month.year, month.month + 1, 0).day;
    final byDay = {for (final record in records) record.date.day: record.status};
    return Container(
      padding: const EdgeInsets.all(12),
      decoration: _card(),
      child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
        const Text('MONTHLY CALENDAR', style: TextStyle(fontSize: 11, fontWeight: FontWeight.w900, color: _ink, letterSpacing: .5)),
        const SizedBox(height: 10),
        const Row(children: [
          for (final day in ['S', 'M', 'T', 'W', 'T', 'F', 'S'])
            Expanded(child: Center(child: Text(day, style: TextStyle(fontSize: 10, fontWeight: FontWeight.w800, color: Color(0xFF9295A7))))),
        ]),
        const SizedBox(height: 6),
        GridView.builder(
          shrinkWrap: true,
          physics: const NeverScrollableScrollPhysics(),
          itemCount: offset + days,
          gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(crossAxisCount: 7, mainAxisSpacing: 5, crossAxisSpacing: 5, childAspectRatio: 1.35),
          itemBuilder: (context, index) {
            if (index < offset) return const SizedBox.shrink();
            final day = index - offset + 1;
            final status = byDay[day];
            final color = switch (status) {
              AttendanceStatus.present => _green,
              AttendanceStatus.late => _amber,
              AttendanceStatus.absent => _red,
              AttendanceStatus.holiday => const Color(0xFFE9EAF0),
              null => const Color(0xFFF5F4F8),
            };
            final foreground = status == AttendanceStatus.holiday || status == null ? const Color(0xFF9AA0B2) : Colors.white;
            return Container(
              alignment: Alignment.center,
              decoration: BoxDecoration(color: color, borderRadius: BorderRadius.circular(9)),
              child: Text('$day', style: TextStyle(fontSize: 11, fontWeight: FontWeight.w800, color: foreground)),
            );
          },
        ),
        const SizedBox(height: 10),
        const Wrap(spacing: 12, runSpacing: 6, children: [
          _Legend(label: 'Present', color: _green),
          _Legend(label: 'Late', color: _amber),
          _Legend(label: 'Absent', color: _red),
          _Legend(label: 'Sunday / holiday', color: Color(0xFFE9EAF0)),
        ]),
      ]),
    );
  }
}

class _Legend extends StatelessWidget {
  const _Legend({required this.label, required this.color});
  final String label;
  final Color color;
  @override
  Widget build(BuildContext context) => Row(mainAxisSize: MainAxisSize.min, children: [
    Container(width: 8, height: 8, decoration: BoxDecoration(color: color, shape: BoxShape.circle, border: Border.all(color: const Color(0xFFD4D5DE)))),
    const SizedBox(width: 4),
    Text(label, style: const TextStyle(fontSize: 9, color: _muted)),
  ]);
}

class _Distribution extends StatelessWidget {
  const _Distribution({required this.summary});
  final AttendanceSummary summary;
  @override
  Widget build(BuildContext context) => Container(
    padding: const EdgeInsets.all(13),
    decoration: _card(),
    child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
      const Text('MONTHLY DISTRIBUTION', style: TextStyle(fontSize: 11, fontWeight: FontWeight.w900, letterSpacing: .5, color: _ink)),
      const SizedBox(height: 11),
      ClipRRect(
        borderRadius: BorderRadius.circular(7),
        child: Row(children: [
          if (summary.presentCount > 0) Expanded(flex: summary.presentCount, child: Container(height: 12, color: _green)),
          if (summary.lateCount > 0) Expanded(flex: summary.lateCount, child: Container(height: 12, color: _amber)),
          if (summary.absentCount > 0) Expanded(flex: summary.absentCount, child: Container(height: 12, color: _red)),
          if (summary.holidayCount > 0) Expanded(flex: summary.holidayCount, child: Container(height: 12, color: const Color(0xFFE9EAF0))),
        ]),
      ),
      const SizedBox(height: 12),
      Row(children: [
        Expanded(child: _Metric(value: '${(summary.attendanceRate * 100).round()}%', label: 'Attendance')),
        Expanded(child: _Metric(value: '${summary.attendedCount}', label: 'Visits')),
        Expanded(child: _Metric(value: '${summary.absentCount}', label: 'Missed')),
        Expanded(child: _Metric(value: '${summary.targetDays}', label: 'Target days')),
      ]),
    ]),
  );
}

class _Metric extends StatelessWidget {
  const _Metric({required this.value, required this.label});
  final String value;
  final String label;
  @override
  Widget build(BuildContext context) => Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
    Text(value, style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w900, color: _ink)),
    Text(label, style: const TextStyle(fontSize: 9, color: _muted)),
  ]);
}

String _monthLabel(DateTime date) {
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  return '${months[date.month - 1]} ${date.year}';
}
