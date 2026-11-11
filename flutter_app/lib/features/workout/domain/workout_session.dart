class WorkoutSession {
  const WorkoutSession({required this.date, required this.durationMinutes, required this.calories, required this.trainingVolumeKg, required this.focus});
  final DateTime date;
  final int durationMinutes;
  final int calories;
  final double trainingVolumeKg;
  final String focus;
}

class WorkoutSummary {
  const WorkoutSummary({required this.sessions, required this.weeklyTarget});
  final List<WorkoutSession> sessions;
  final int weeklyTarget;
  int get totalMinutes => sessions.fold(0, (sum, session) => sum + session.durationMinutes);
  int get totalCalories => sessions.fold(0, (sum, session) => sum + session.calories);
  double get totalVolumeKg => sessions.fold(0, (sum, session) => sum + session.trainingVolumeKg);
  int get activeDays => sessions.map((session) => DateTime(session.date.year, session.date.month, session.date.day)).toSet().length;
}
