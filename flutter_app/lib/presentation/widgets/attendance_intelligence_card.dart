import 'package:flutter/material.dart';
import 'package:bad_gym/core/models/member_intelligence_data.dart';
import 'package:bad_gym/core/theme/bad_gym_theme.dart';
import 'package:bad_gym/presentation/widgets/attendance_calendar_grid.dart';
import 'package:bad_gym/presentation/widgets/attendance_donut_chart.dart';
import 'package:bad_gym/presentation/widgets/attendance_trend_chart.dart';

class AttendanceIntelligenceCard extends StatelessWidget {
  final MemberIntelligenceData data;
  final VoidCallback? onMonthDropdownPressed;

  const AttendanceIntelligenceCard({
    super.key,
    required this.data,
    this.onMonthDropdownPressed,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 10),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(18),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.05),
            blurRadius: 10,
            offset: const Offset(0, 3),
          ),
        ],
        border: Border.all(
          color: BadGymColors.borderSubtle,
          width: 1,
        ),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisSize: MainAxisSize.min,
        children: [
          // 1. Card Header
          Row(
            children: [
              const Icon(
                Icons.calendar_month_outlined,
                color: BadGymColors.coralPrimary,
                size: 16,
              ),
              const SizedBox(width: 5),
              const Expanded(
                child: Text(
                  'Attendance Intelligence',
                  style: TextStyle(
                    fontFamily: BadGymTypography.fontHeading,
                    fontWeight: FontWeight.w800,
                    fontSize: 12.5,
                    color: BadGymColors.inkPrimary,
                  ),
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),
              ),
              const SizedBox(width: 4),
              // Month dropdown pill
              InkWell(
                onTap: onMonthDropdownPressed,
                borderRadius: BorderRadius.circular(12),
                child: Container(
                  padding: const EdgeInsets.symmetric(horizontal: 7, vertical: 2.5),
                  decoration: BoxDecoration(
                    color: const Color(0xFFF9F7F6),
                    borderRadius: BorderRadius.circular(12),
                    border: Border.all(color: const Color(0xFFEBE3E2)),
                  ),
                  child: const Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Text(
                        'Oct 2026',
                        style: TextStyle(
                          fontFamily: BadGymTypography.fontBody,
                          fontSize: 9.5,
                          fontWeight: FontWeight.w600,
                          color: BadGymColors.inkPrimary,
                        ),
                      ),
                      SizedBox(width: 2),
                      Icon(
                        Icons.arrow_drop_down,
                        size: 13,
                        color: BadGymColors.inkPrimary,
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),

          const SizedBox(height: 8),

          // 2. Metrics row (Donut + Legend + Streaks)
          AttendanceMetricsRow(data: data),

          const SizedBox(height: 8),

          // 3. Calendar Grid
          AttendanceCalendarGrid(days: data.calendarDays),

          const SizedBox(height: 10),

          // 4. Attendance Trend Chart
          AttendanceTrendChart(points: data.trendPoints),
        ],
      ),
    );
  }
}
