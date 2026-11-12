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
        color: const Color(0xFFFAF7FE),
        borderRadius: BorderRadius.circular(16),
        border: Border.all(
          color: const Color(0xFFECE4F8),
          width: 1,
        ),
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          // Month navigation header: < October 2026 >
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              InkWell(
                onTap: onPreviousMonth,
                borderRadius: BorderRadius.circular(14),
                child: Container(
                  width: 26,
                  height: 26,
                  decoration: BoxDecoration(
                    color: const Color(0xFFEFE8F9),
                    borderRadius: BorderRadius.circular(13),
                  ),
                  child: const Icon(
                    Icons.chevron_left,
                    size: 18,
                    color: BadGymColors.inkPrimary,
                  ),
                ),
              ),
              const Text(
                'October 2026',
                style: TextStyle(
                  fontFamily: BadGymTypography.fontHeading,
                  fontWeight: FontWeight.w800,
                  fontSize: 14,
                  color: Color(0xFF1E0E38),
                ),
              ),
              InkWell(
                onTap: onNextMonth,
                borderRadius: BorderRadius.circular(14),
                child: Container(
                  width: 26,
                  height: 26,
                  decoration: BoxDecoration(
                    color: const Color(0xFFEFE8F9),
                    borderRadius: BorderRadius.circular(13),
                  ),
                  child: const Icon(
                    Icons.chevron_right,
                    size: 18,
                    color: BadGymColors.inkPrimary,
                  ),
                ),
              ),
            ],
          ),

          const SizedBox(height: 8),

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
    color: Color(0xFF374151),
  );

  Widget _buildCalendarRows() {
    final items = <Widget>[];

    // Empty slots for Mon, Tue, Wed before Oct 1 (Thu)
    items.add(const SizedBox());
    items.add(const SizedBox());
    items.add(const SizedBox());

    for (final dayItem in days) {
      items.add(_buildDayBadge(dayItem));
    }

    // Pad remaining slots to complete last row
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
      // Off/Rest day: soft lavender grey pill
      return Container(
        width: 23,
        height: 23,
        decoration: BoxDecoration(
          color: const Color(0xFFE5DEEE),
          borderRadius: BorderRadius.circular(7),
        ),
        alignment: Alignment.center,
        child: Text(
          '${item.day}',
          style: const TextStyle(
            fontFamily: BadGymTypography.fontHeading,
            fontSize: 9.5,
            fontWeight: FontWeight.w600,
            color: Color(0xFF4B5563),
          ),
        ),
      );
    }

    final Color badgeColor = switch (item.status) {
      DayAttendanceStatus.present => BadGymColors.neonLime,
      DayAttendanceStatus.late => BadGymColors.yellowLate,
      DayAttendanceStatus.absent => BadGymColors.redAbsent,
      DayAttendanceStatus.holiday => BadGymColors.purpleHoliday,
      DayAttendanceStatus.empty => const Color(0xFFE5DEEE),
    };

    if (item.isToday) {
      // Prominent green double concentric ring indicating today!
      return Container(
        width: 27,
        height: 27,
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          border: Border.all(
            color: BadGymColors.neonLime,
            width: 1.6,
          ),
          boxShadow: [
            BoxShadow(
              color: BadGymColors.neonLime.withValues(alpha: 0.5),
              blurRadius: 4,
            ),
          ],
        ),
        padding: const EdgeInsets.all(1.5),
        child: Container(
          decoration: const BoxDecoration(
            color: BadGymColors.neonLime,
            shape: BoxShape.circle,
          ),
          alignment: Alignment.center,
          child: Text(
            '${item.day}',
            style: const TextStyle(
              fontFamily: BadGymTypography.fontHeading,
              fontSize: 9.5,
              fontWeight: FontWeight.w900,
              color: Colors.white,
            ),
          ),
        ),
      );
    }

    return Container(
      width: 23,
      height: 23,
      decoration: BoxDecoration(
        color: badgeColor,
        shape: BoxShape.circle,
        boxShadow: [
          BoxShadow(
            color: badgeColor.withValues(alpha: 0.35),
            blurRadius: 3,
            offset: const Offset(0, 1),
          ),
        ],
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
