import '../domain/attendance_record.dart';

abstract interface class AttendanceRepository {
  Future<AttendanceSummary> getMonthlySummary({required String memberId, required DateTime month});
}

/// Preview-only data source. Replace with an authenticated backend repository.
class DemoAttendanceRepository implements AttendanceRepository {
  @override
  Future<AttendanceSummary> getMonthlySummary({required String memberId, required DateTime month}) async {
    final daysInMonth = DateTime(month.year, month.month + 1, 0).day;
    final records = <AttendanceRecord>[];
    for (var day = 1; day <= daysInMonth; day++) {
      final date = DateTime(month.year, month.month, day);
      if (date.weekday == DateTime.sunday) {
        records.add(AttendanceRecord(date: date, status: AttendanceStatus.holiday));
      } else if ([3, 11, 18, 25].contains(day)) {
        records.add(AttendanceRecord(date: date, status: AttendanceStatus.absent));
      } else if ([7, 16].contains(day)) {
        records.add(AttendanceRecord(date: date, status: AttendanceStatus.late));
      } else if (date.isBefore(DateTime.now()) || day <= 10) {
        records.add(AttendanceRecord(date: date, status: AttendanceStatus.present));
      }
    }
    return AttendanceSummary(records: records, targetDays: 26);
  }
}
