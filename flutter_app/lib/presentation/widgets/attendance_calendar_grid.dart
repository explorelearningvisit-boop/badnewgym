import 'package:flutter/material.dart';
import 'package:bad_gym/core/models/member_intelligence_data.dart';
import 'package:bad_gym/core/theme/bad_gym_theme.dart';

class AttendanceCalendarGrid extends StatelessWidget {
  final List<CalendarDayItem> days;
  final VoidCallback? onPreviousMonth;
  final VoidCallback? onNextMonth;

  const AttendanceCalendarGrid({
    super.key,
    required this.days,
    this.onPreviousMonth,
    this.onNextMonth,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 8),
      decoration: BoxDecoration(
        color: const Color(0xFFFAF8F7),
        borderRadius: BorderRadius.circular(14),
        border: Border.all(color: const Color(0xFFF0EBEA)),
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          // Month navigation header
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              InkWell(
                onTap: onPreviousMonth,
                borderRadius: BorderRadius.circular(12),
                child: Container(
                  width: 24,
                  height: 24,
                  decoration: BoxDecoration(
                    color: const Color(0xFFEBE5E4),
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: const Icon(
                    Icons.chevron_left,
                    size: 16,
                    color: BadGymColors.inkPrimary,
                  ),
                ),
              ),
              const Text(
                'October 2026',
                style: TextStyle(
                  fontFamily: BadGymTypography.fontHeading,
                  fontWeight: FontWeight.w800,
                  fontSize: 13.5,
                  color: BadGymColors.inkPrimary,
                ),
              ),
              InkWell(
                onTap: onNextMonth,
                borderRadius: BorderRadius.circular(12),
                child: Container(
                  width: 24,
                  height: 24,
                  decoration: BoxDecoration(
                    color: const Color(0xFFEBE5E4),
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: const Icon(
                    Icons.chevron_right,
                    size: 16,
                    color: BadGymColors.inkPrimary,
                  ),
                ),
              ),
            ],
          ),

          const SizedBox(height: 6),

          // Day of week labels: Mon Tue Wed Thu Fri Sat Sun
          const Row(
            children: [
              Expanded(child: Center(child: Text('Mon', style: _dayHeaderStyle))),
              Expanded(child: Center(child: Text('Tue', style: _dayHeaderStyle))),
              Expanded(child: Center(child: Text('Wed', style: _dayHeaderStyle))),
              Expanded(child: Center(child: Text('Thu', style: _dayHeaderStyle))),
              Expanded(child: Center(child: Text('Fri', style: _dayHeaderStyle))),
              Expanded(child: Center(child: Text('Sat', style: _dayHeaderStyle))),
              Expanded(child: Center(child: Text('Sun', style: _dayHeaderStyle))),
            ],
          ),

          const SizedBox(height: 6),

          // Grid rows
          _buildCalendarRows(),
        ],
      ),
    );
  }

  static const TextStyle _dayHeaderStyle = TextStyle(
    fontFamily: BadGymTypography.fontBody,
    fontSize: 9.5,
    fontWeight: FontWeight.w700,
    color: BadGymColors.inkPrimary,
  );

  Widget _buildCalendarRows() {
    // 31 days with 1st starting on Thursday (index 3).
    // So row 1 has 3 empty/pre slots (indices 0, 1, 2) and days 1, 2, 3, 4.
    final items = <Widget>[];

    // Empty spaces for Mon, Tue, Wed before Oct 1
    items.add(const SizedBox());
    items.add(const SizedBox());
    items.add(const SizedBox());

    for (final dayItem in days) {
      items.add(_buildDayBadge(dayItem));
    }

    // Pad remaining row slots to complete grid
    while (items.length % 7 != 0) {
      items.add(const SizedBox());
    }

    final rows = <Widget>[];
    for (int i = 0; i < items.length; i += 7) {
      rows.add(
        Padding(
          padding: const EdgeInsets.symmetric(vertical: 2.5),
          child: Row(
            children: [
              for (int j = 0; j < 7; j++)
                Expanded(
                  child: Center(child: items[i + j]),
                ),
            ],
          ),
        ),
      );
    }

    return Column(
      mainAxisSize: MainAxisSize.min,
      children: rows,
    );
  }

  Widget _buildDayBadge(CalendarDayItem item) {
    if (item.status == DayAttendanceStatus.empty) {
      // Light grey circle
      return Container(
        width: 22,
        height: 22,
        decoration: const BoxDecoration(
          color: Color(0xFFE5E7EB),
          shape: BoxShape.circle,
        ),
        alignment: Alignment.center,
        child: Text(
          '${item.day}',
          style: const TextStyle(
            fontFamily: BadGymTypography.fontHeading,
            fontSize: 9.5,
            fontWeight: FontWeight.w600,
            color: Color(0xFF6B7280),
          ),
        ),
      );
    }

    final Color badgeColor = switch (item.status) {
      DayAttendanceStatus.present => BadGymColors.emerald,
      DayAttendanceStatus.late => BadGymColors.yellowLate,
      DayAttendanceStatus.absent => BadGymColors.redAbsent,
      DayAttendanceStatus.holiday => BadGymColors.purpleHoliday,
      DayAttendanceStatus.empty => const Color(0xFFE5E7EB),
    };

    if (item.isToday) {
      // Prominent green double ring indicating today!
      return Container(
        width: 26,
        height: 26,
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          border: Border.all(
            color: BadGymColors.emerald,
            width: 1.5,
          ),
        ),
        padding: const EdgeInsets.all(1.5),
        child: Container(
          decoration: const BoxDecoration(
            color: BadGymColors.emerald,
            shape: BoxShape.circle,
          ),
          alignment: Alignment.center,
          child: Text(
            '${item.day}',
            style: const TextStyle(
              fontFamily: BadGymTypography.fontHeading,
              fontSize: 9,
              fontWeight: FontWeight.w800,
              color: Colors.white,
            ),
          ),
        ),
      );
    }

    return Container(
      width: 22,
      height: 22,
      decoration: BoxDecoration(
        color: badgeColor,
        shape: BoxShape.circle,
      ),
      alignment: Alignment.center,
      child: Text(
        '${item.day}',
        style: const TextStyle(
          fontFamily: BadGymTypography.fontHeading,
          fontSize: 9.5,
          fontWeight: FontWeight.w700,
          color: Colors.white,
        ),
      ),
    );
  }
}
