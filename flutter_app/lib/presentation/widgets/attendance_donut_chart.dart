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
        // 1. Donut Chart
        SizedBox(
          width: 74,
          height: 74,
          child: CustomPaint(
            painter: DonutChartPainter(
              progress: data.attendanceRate / 100.0,
              strokeColor: BadGymColors.emerald,
              trackColor: const Color(0xFFF0EFEB),
              strokeWidth: 6.0,
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
                    color: BadGymColors.inkPrimary,
                    height: 1.0,
                  ),
                ),
                const SizedBox(height: 2),
                const Text(
                  '25 / 31',
                  style: TextStyle(
                    fontFamily: BadGymTypography.fontBody,
                    fontWeight: FontWeight.w600,
                    fontSize: 9,
                    color: BadGymColors.inkSecondary,
                    height: 1.0,
                  ),
                ),
                const SizedBox(height: 1),
                const Text(
                  'Days',
                  style: TextStyle(
                    fontFamily: BadGymTypography.fontBody,
                    fontWeight: FontWeight.w500,
                    fontSize: 8,
                    color: BadGymColors.inkMuted,
                    height: 1.0,
                  ),
                ),
              ],
            ),
          ),
        ),

        const SizedBox(width: 6),

        // 2. Legend Box
        Expanded(
          flex: 4,
          child: Container(
            padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 5),
            decoration: BoxDecoration(
              color: const Color(0xFFFAF7F6),
              borderRadius: BorderRadius.circular(10),
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                _buildLegendItem(
                  color: BadGymColors.emerald,
                  label: 'Present',
                  count: data.presentCount,
                ),
                const SizedBox(height: 2.5),
                _buildLegendItem(
                  color: BadGymColors.yellowLate,
                  label: 'Late',
                  count: data.lateCount,
                ),
                const SizedBox(height: 2.5),
                _buildLegendItem(
                  color: BadGymColors.redAbsent,
                  label: 'Absent',
                  count: data.absentCount,
                ),
                const SizedBox(height: 2.5),
                _buildLegendItem(
                  color: BadGymColors.purpleHoliday,
                  label: 'Holiday',
                  count: data.holidayCount,
                ),
              ],
            ),
          ),
        ),

        const SizedBox(width: 6),

        // 3. Streaks Box
        Expanded(
          flex: 4,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              _buildStreakCard(
                imageAsset: 'assets/images/fire_icon.png',
                fallbackIcon: Icons.local_fire_department,
                iconColor: const Color(0xFFFF5722),
                value: data.currentStreak,
                subtitle: 'Current Streak',
              ),
              const SizedBox(height: 5),
              _buildStreakCard(
                imageAsset: 'assets/images/trophy_icon.png',
                fallbackIcon: Icons.emoji_events,
                iconColor: const Color(0xFFFBBF24),
                value: data.bestStreak,
                subtitle: 'Best Streak',
              ),
            ],
          ),
        ),
      ],
    );
  }

  Widget _buildLegendItem({
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
              fontWeight: FontWeight.w600,
              color: BadGymColors.inkPrimary,
            ),
          ),
        ),
        Text(
          '$count',
          style: const TextStyle(
            fontFamily: BadGymTypography.fontHeading,
            fontSize: 9.5,
            fontWeight: FontWeight.w800,
            color: BadGymColors.inkPrimary,
          ),
        ),
      ],
    );
  }

  Widget _buildStreakCard({
    required String imageAsset,
    required IconData fallbackIcon,
    required Color iconColor,
    required String value,
    required String subtitle,
  }) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Image.asset(
          imageAsset,
          width: 18,
          height: 18,
          errorBuilder: (_, __, ___) => Icon(
            fallbackIcon,
            color: iconColor,
            size: 18,
          ),
        ),
        const SizedBox(width: 5),
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              value,
              style: const TextStyle(
                fontFamily: BadGymTypography.fontHeading,
                fontSize: 11,
                fontWeight: FontWeight.w800,
                color: BadGymColors.inkPrimary,
                height: 1.1,
              ),
            ),
            Text(
              subtitle,
              style: const TextStyle(
                fontFamily: BadGymTypography.fontBody,
                fontSize: 8,
                fontWeight: FontWeight.w500,
                color: BadGymColors.inkMuted,
                height: 1.1,
              ),
            ),
          ],
        ),
      ],
    );
  }
}
