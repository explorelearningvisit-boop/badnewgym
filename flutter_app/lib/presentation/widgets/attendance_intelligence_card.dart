import 'package:flutter/material.dart';
import 'package:bad_gym/core/models/member_intelligence_data.dart';
import 'package:bad_gym/core/theme/bad_gym_theme.dart';
import 'package:bad_gym/presentation/widgets/attendance_calendar_grid.dart';
import 'package:bad_gym/presentation/widgets/attendance_donut_chart.dart';

class AttendanceIntelligenceCard extends StatelessWidget {
  final MemberIntelligenceData data;
  final VoidCallback? onMonthDropdownPressed;
  final VoidCallback? onInfoPressed;
  final VoidCallback? onScrollDownPressed;

  const AttendanceIntelligenceCard({
    super.key,
    required this.data,
    this.onMonthDropdownPressed,
    this.onInfoPressed,
    this.onScrollDownPressed,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 10),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(22),
        border: Border.all(
          color: BadGymColors.borderSubtle,
          width: 1.2,
        ),
        boxShadow: [
          BoxShadow(
            color: BadGymColors.purpleRail.withValues(alpha: 0.08),
            blurRadius: 14,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisSize: MainAxisSize.min,
        children: [
          // 1. Header: Icon Badge | Title | Info | Dropdown
          Row(
            children: [
              // Purple rounded square icon badge
              Container(
                width: 25,
                height: 25,
                decoration: BoxDecoration(
                  gradient: const LinearGradient(
                    colors: [
                      BadGymColors.purpleCardStart,
                      BadGymColors.purpleCardEnd,
                    ],
                  ),
                  borderRadius: BorderRadius.circular(8),
                ),
                alignment: Alignment.center,
                child: const Icon(
                  Icons.bar_chart_rounded,
                  color: Colors.white,
                  size: 15,
                ),
              ),
              const SizedBox(width: 5),

              // Title with Info Icon
              Expanded(
                child: Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Flexible(
                      child: FittedBox(
                        fit: BoxFit.scaleDown,
                        alignment: Alignment.centerLeft,
                        child: const Text(
                          'Attendance Intelligence',
                          style: TextStyle(
                            fontFamily: BadGymTypography.fontHeading,
                            fontWeight: FontWeight.w800,
                            fontSize: 12.5,
                            color: Color(0xFF1E0E38),
                            letterSpacing: -0.2,
                          ),
                        ),
                      ),
                    ),
                    const SizedBox(width: 3),
                    const Icon(
                      Icons.info_outline_rounded,
                      size: 13,
                      color: Color(0xFF9CA3AF),
                    ),
                  ],
                ),
              ),

              const SizedBox(width: 3),

              // Month selector pill: Oct 2026 ▼
              InkWell(
                onTap: onMonthDropdownPressed,
                borderRadius: BorderRadius.circular(14),
                child: Container(
                  padding: const EdgeInsets.symmetric(horizontal: 7, vertical: 3),
                  decoration: BoxDecoration(
                    color: const Color(0xFFFAF7FE),
                    borderRadius: BorderRadius.circular(14),
                    border: Border.all(
                      color: const Color(0xFFE5DEEE),
                      width: 1,
                    ),
                  ),
                  child: const Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Text(
                        'Oct 2026',
                        style: TextStyle(
                          fontFamily: BadGymTypography.fontHeading,
                          fontSize: 9.5,
                          fontWeight: FontWeight.w700,
                          color: Color(0xFF1E0E38),
                        ),
                      ),
                      SizedBox(width: 2),
                      Icon(
                        Icons.arrow_drop_down,
                        size: 13,
                        color: Color(0xFF1E0E38),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),

          const SizedBox(height: 10),

          // 2. Metrics Row: Donut Chart | Legend | Streaks
          AttendanceMetricsRow(data: data),

          const SizedBox(height: 10),

          // 3. Calendar Grid
          AttendanceCalendarGrid(days: data.calendarDays),

          const SizedBox(height: 4),

          // 4. Bottom Row with Floating Action Indicator
          Align(
            alignment: Alignment.centerRight,
            child: InkWell(
              onTap: onScrollDownPressed,
              borderRadius: BorderRadius.circular(16),
              child: Container(
                width: 28,
                height: 28,
                decoration: const BoxDecoration(
                  color: Color(0xFF0F021F),
                  shape: BoxShape.circle,
                ),
                alignment: Alignment.center,
                child: const Icon(
                  Icons.arrow_downward_rounded,
                  color: Colors.white,
                  size: 16,
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
