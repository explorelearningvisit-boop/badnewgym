enum AttendanceStatus { present, late, absent, holiday }

class AttendanceRecord {
  const AttendanceRecord({required this.date, required this.status, this.checkIn, this.checkOut, this.minutesTrained});
  final DateTime date;
  final AttendanceStatus status;
  final DateTime? checkIn;
  final DateTime? checkOut;
  final int? minutesTrained;
}

class AttendanceSummary {
  const AttendanceSummary({required this.records, required this.targetDays});
  final List<AttendanceRecord> records;
  final int targetDays;
  int get presentCount => records.where((r) => r.status == AttendanceStatus.present).length;
  int get lateCount => records.where((r) => r.status == AttendanceStatus.late).length;
  int get absentCount => records.where((r) => r.status == AttendanceStatus.absent).length;
  int get holidayCount => records.where((r) => r.status == AttendanceStatus.holiday).length;
  int get attendedCount => presentCount + lateCount;
  int get trackedCount => records.length;
  double get attendanceRate => trackedCount == 0 ? 0 : attendedCount / trackedCount;
}
