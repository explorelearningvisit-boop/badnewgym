import '../domain/body_measurement.dart';

abstract interface class BodyRepository {
  Future<BodySummary> getMeasurements({required String memberId});
}

/// Preview fixture only. Composition metrics are intentionally not inferred.
class DemoBodyRepository implements BodyRepository {
  @override
  Future<BodySummary> getMeasurements({required String memberId}) async {
    final now = DateTime.now();
    return BodySummary(
      goalWeightKg: 72,
      measurements: [
        BodyMeasurement(date: now.subtract(const Duration(days: 60)), weightKg: 68.4, waistCm: 84, chestCm: 96, armCm: 31),
        BodyMeasurement(date: now.subtract(const Duration(days: 30)), weightKg: 69.0, waistCm: 83.5, chestCm: 96.5, armCm: 31.5),
        BodyMeasurement(date: now, weightKg: 69.5, waistCm: 83, chestCm: 97, armCm: 32),
      ],
    );
  }
}
