import 'dart:math' as math;
import 'package:flutter/material.dart';
import 'package:bad_gym/core/models/member_intelligence_data.dart';
import 'package:bad_gym/core/theme/bad_gym_theme.dart';

class DonutChartPainter extends CustomPainter {
  final double progress; // 0.0 to 1.0
  final Color strokeColor;
  final Color trackColor;
  final double strokeWidth;

  const DonutChartPainter({
    required this.progress,
    required this.strokeColor,
    required this.trackColor,
    this.strokeWidth = 6.0,
  });

  @override
  void paint(Canvas canvas, Size size) {
    final center = Offset(size.width / 2, size.height / 2);
    final radius = (math.min(size.width, size.height) - strokeWidth) / 2;

    // Background track
    final trackPaint = Paint()
      ..color = trackColor
      ..style = PaintingStyle.stroke
      ..strokeWidth = strokeWidth;

    canvas.drawCircle(center, radius, trackPaint);

    // Progress arc
    final progressPaint = Paint()
      ..color = strokeColor
      ..style = PaintingStyle.stroke
      ..strokeWidth = strokeWidth
      ..strokeCap = StrokeCap.round;

    // Start at top (-pi / 2)
    const startAngle = -math.pi / 2;
    final sweepAngle = 2 * math.pi * progress;

    canvas.drawArc(
      Rect.fromCircle(center: center, radius: radius),
      startAngle,
      sweepAngle,
      false,
      progressPaint,
    );
  }

  @override
  bool shouldRepaint(covariant DonutChartPainter oldDelegate) {
    return oldDelegate.progress != progress ||
        oldDelegate.strokeColor != strokeColor ||
        oldDelegate.trackColor != trackColor ||
        oldDelegate.strokeWidth != strokeWidth;
  }
}

class AttendanceMetricsRow extends StatelessWidget {
  final MemberIntelligenceData data;

  const AttendanceMetricsRow({
    super.key,
    required this.data,
  });

  @override
  Widget build(BuildContext context) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        // 1. Donut Chart with Neon Lime Arc
        SizedBox(
          width: 68,
          height: 68,
          child: CustomPaint(
            painter: const DonutChartPainter(
              progress: 0.81,
              strokeColor: BadGymColors.neonLime,
              trackColor: Color(0xFFF1EBFB),
              strokeWidth: 5.5,
            ),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text(
                  '${data.attendanceRate}%',
                  style: const TextStyle(
                    fontFamily: BadGymTypography.fontHeading,
                    fontWeight: FontWeight.w900,
                    fontSize: 15,
                    color: Color(0xFF111827),
                    height: 1.0,
                  ),
                ),
                const SizedBox(height: 2),
                const Text(
                  '25 / 31',
                  style: TextStyle(
                    fontFamily: BadGymTypography.fontBody,
                    fontWeight: FontWeight.w600,
                    fontSize: 9.5,
                    color: Color(0xFF6B7280),
                    height: 1.0,
                  ),
                ),
                const Text(
                  'Days',
                  style: TextStyle(
                    fontFamily: BadGymTypography.fontBody,
                    fontWeight: FontWeight.w500,
                    fontSize: 9,
                    color: Color(0xFF6B7280),
                    height: 1.0,
                  ),
                ),
              ],
            ),
          ),
        ),

        const SizedBox(width: 6),

        // 2. Legend Pill Container (Present 23, Late 2, Absent 2, Holiday 4)
        Expanded(
          flex: 10,
          child: Container(
            padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 4),
            decoration: BoxDecoration(
              color: const Color(0xFFF7F3FC),
              borderRadius: BorderRadius.circular(12),
              border: Border.all(
                color: const Color(0xFFEBE2F8),
                width: 1,
              ),
            ),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                _buildLegendRow(
                  color: BadGymColors.neonLime,
                  label: 'Present',
                  count: data.presentCount,
                ),
                const SizedBox(height: 2.5),
                _buildLegendRow(
                  color: BadGymColors.yellowLate,
                  label: 'Late',
                  count: data.lateCount,
                ),
                const SizedBox(height: 2.5),
                _buildLegendRow(
                  color: BadGymColors.redAbsent,
                  label: 'Absent',
                  count: data.absentCount,
                ),
                const SizedBox(height: 2.5),
                _buildLegendRow(
                  color: BadGymColors.purpleHoliday,
                  label: 'Holiday',
                  count: data.holidayCount,
                ),
              ],
            ),
          ),
        ),

        const SizedBox(width: 6),

        // 3. Streak Cards: Current Streak 5 Days, Best Streak 14 Days
        Expanded(
          flex: 9,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            mainAxisSize: MainAxisSize.min,
            children: [
              // Current Streak
              Row(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  const Icon(
                    Icons.local_fire_department_rounded,
                    color: Color(0xFFEA580C),
                    size: 18,
                  ),
                  const SizedBox(width: 3),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        const Text(
                          'Current Streak',
                          style: TextStyle(
                            fontFamily: BadGymTypography.fontBody,
                            fontSize: 8,
                            fontWeight: FontWeight.w500,
                            color: Color(0xFF6B7280),
                            height: 1.0,
                          ),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                        const SizedBox(height: 1),
                        Text(
                          data.currentStreak,
                          style: const TextStyle(
                            fontFamily: BadGymTypography.fontHeading,
                            fontSize: 12,
                            fontWeight: FontWeight.w900,
                            color: Color(0xFF111827),
                            height: 1.1,
                          ),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 5),

              // Best Streak
              Row(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  const Icon(
                    Icons.emoji_events_rounded,
                    color: Color(0xFFF59E0B),
                    size: 18,
                  ),
                  const SizedBox(width: 3),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        const Text(
                          'Best Streak',
                          style: TextStyle(
                            fontFamily: BadGymTypography.fontBody,
                            fontSize: 8,
                            fontWeight: FontWeight.w500,
                            color: Color(0xFF6B7280),
                            height: 1.0,
                          ),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                        const SizedBox(height: 1),
                        Text(
                          data.bestStreak,
                          style: const TextStyle(
                            fontFamily: BadGymTypography.fontHeading,
                            fontSize: 12,
                            fontWeight: FontWeight.w900,
                            color: Color(0xFF111827),
                            height: 1.1,
                          ),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ],
    );
  }

  Widget _buildLegendRow({
    required Color color,
    required String label,
    required int count,
  }) {
    return Row(
      children: [
        Container(
          width: 7,
          height: 7,
          decoration: BoxDecoration(
            color: color,
            shape: BoxShape.circle,
          ),
        ),
        const SizedBox(width: 4),
        Expanded(
          child: Text(
            label,
            style: const TextStyle(
              fontFamily: BadGymTypography.fontBody,
              fontSize: 9,
              fontWeight: FontWeight.w500,
              color: Color(0xFF4B5563),
              height: 1.0,
            ),
            maxLines: 1,
            overflow: TextOverflow.ellipsis,
          ),
        ),
        Text(
          '$count',
          style: const TextStyle(
            fontFamily: BadGymTypography.fontHeading,
            fontSize: 9.5,
            fontWeight: FontWeight.w800,
            color: Color(0xFF111827),
            height: 1.0,
          ),
        ),
      ],
    );
  }
}
