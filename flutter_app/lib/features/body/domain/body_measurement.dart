class BodyMeasurement {
  const BodyMeasurement({required this.date, this.weightKg, this.waistCm, this.chestCm, this.armCm});
  final DateTime date;
  final double? weightKg;
  final double? waistCm;
  final double? chestCm;
  final double? armCm;
}

class BodySummary {
  const BodySummary({required this.measurements, this.goalWeightKg});
  final List<BodyMeasurement> measurements;
  final double? goalWeightKg;
  BodyMeasurement? get latest => measurements.isEmpty ? null : measurements.last;
  BodyMeasurement? get first => measurements.isEmpty ? null : measurements.first;
}
