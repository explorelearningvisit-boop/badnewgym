import '../domain/workout_session.dart';

abstract interface class WorkoutRepository {
  Future<WorkoutSummary> getRecentSummary({required String memberId});
}

/// Preview fixture only. Production must use recorded workout sessions.
class DemoWorkoutRepository implements WorkoutRepository {
  @override
  Future<WorkoutSummary> getRecentSummary({required String memberId}) async {
    final now = DateTime.now();
    final sessions = <WorkoutSession>[
      WorkoutSession(date: now.subtract(const Duration(days: 1)), durationMinutes: 61, calories: 420, trainingVolumeKg: 8420, focus: 'Legs'),
      WorkoutSession(date: now.subtract(const Duration(days: 2)), durationMinutes: 48, calories: 310, trainingVolumeKg: 5120, focus: 'Chest'),
      WorkoutSession(date: now.subtract(const Duration(days: 4)), durationMinutes: 72, calories: 460, trainingVolumeKg: 7350, focus: 'Back'),
      WorkoutSession(date: now.subtract(const Duration(days: 6)), durationMinutes: 38, calories: 250, trainingVolumeKg: 3240, focus: 'Cardio'),
    ];
    return WorkoutSummary(sessions: sessions, weeklyTarget: 5);
  }
}
