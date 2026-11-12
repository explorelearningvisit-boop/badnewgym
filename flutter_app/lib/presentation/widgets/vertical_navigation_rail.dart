import 'package:flutter/material.dart';
import 'package:bad_gym/core/theme/bad_gym_theme.dart';

enum RailTab {
  intelligence,
  calendar,
  history,
  trend,
  streak,
  analysis,
  goals,
  reports,
  offers,
  more,
  // backwards compatible aliases
  overview,
  attendance,
  workout,
  body,
  payment,
}

class VerticalNavigationRail extends StatelessWidget {
  final RailTab selectedTab;
  final ValueChanged<RailTab>? onTabSelected;

  const VerticalNavigationRail({
    super.key,
    required this.selectedTab,
    this.onTabSelected,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 66,
      padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 3),
      decoration: BoxDecoration(
        gradient: const LinearGradient(
          begin: Alignment.topCenter,
          end: Alignment.bottomCenter,
          colors: [
            BadGymColors.purpleRail,
            Color(0xFF16012C),
          ],
        ),
        borderRadius: BorderRadius.circular(22),
        border: Border.all(
          color: BadGymColors.neonVioletBorder.withValues(alpha: 0.35),
          width: 1,
        ),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.25),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
          BoxShadow(
            color: BadGymColors.neonViolet.withValues(alpha: 0.15),
            blurRadius: 6,
          ),
        ],
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          // 1. Intelligence (Active)
          _buildRailItem(
            tab: RailTab.intelligence,
            icon: Icons.bar_chart_rounded,
            label: 'Intelligence',
          ),
          const SizedBox(height: 5),

          // 2. Calendar
          _buildRailItem(
            tab: RailTab.calendar,
            icon: Icons.calendar_month_outlined,
            label: 'Calendar',
          ),
          const SizedBox(height: 5),

          // 3. History
          _buildRailItem(
            tab: RailTab.history,
            icon: Icons.history_rounded,
            label: 'History',
          ),
          const SizedBox(height: 5),

          // 4. Trend
          _buildRailItem(
            tab: RailTab.trend,
            icon: Icons.trending_up_rounded,
            label: 'Trend',
          ),
          const SizedBox(height: 5),

          // 5. Streak
          _buildRailItem(
            tab: RailTab.streak,
            icon: Icons.local_fire_department_rounded,
            label: 'Streak',
          ),
          const SizedBox(height: 5),

          // 6. Analysis
          _buildRailItem(
            tab: RailTab.analysis,
            icon: Icons.pie_chart_outline_rounded,
            label: 'Analysis',
          ),
          const SizedBox(height: 5),

          // Thin Divider
          Container(
            height: 1,
            width: 40,
            color: BadGymColors.purpleRailDivider,
            margin: const EdgeInsets.symmetric(vertical: 4),
          ),
          const SizedBox(height: 4),

          // 7. Goals
          _buildRailItem(
            tab: RailTab.goals,
            icon: Icons.track_changes_rounded,
            label: 'Goals',
          ),
          const SizedBox(height: 5),

          // 8. Reports
          _buildRailItem(
            tab: RailTab.reports,
            icon: Icons.poll_outlined,
            label: 'Reports',
          ),
          const SizedBox(height: 5),

          // 9. Offers
          _buildRailItem(
            tab: RailTab.offers,
            icon: Icons.local_offer_outlined,
            label: 'Offers',
          ),
          const SizedBox(height: 5),

          // 10. More
          _buildRailItem(
            tab: RailTab.more,
            icon: Icons.more_horiz_rounded,
            label: 'More',
          ),
        ],
      ),
    );
  }

  Widget _buildRailItem({
    required RailTab tab,
    required IconData icon,
    required String label,
  }) {
    final bool isSelected = tab == selectedTab ||
        (tab == RailTab.intelligence &&
            (selectedTab == RailTab.intelligence ||
                selectedTab == RailTab.attendance ||
                selectedTab == RailTab.overview));

    if (isSelected) {
      // Active glowing violet capsule
      return Container(
        width: 60,
        padding: const EdgeInsets.symmetric(vertical: 5, horizontal: 1),
        decoration: BoxDecoration(
          gradient: const LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [
              Color(0xFF9333EA),
              Color(0xFF7C3AED),
            ],
          ),
          borderRadius: BorderRadius.circular(14),
          border: Border.all(
            color: BadGymColors.neonVioletBorder,
            width: 1.5,
          ),
          boxShadow: [
            BoxShadow(
              color: BadGymColors.neonViolet.withValues(alpha: 0.6),
              blurRadius: 8,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(
              icon,
              size: 19,
              color: Colors.white,
            ),
            const SizedBox(height: 2),
            FittedBox(
              fit: BoxFit.scaleDown,
              child: Text(
                label,
                style: const TextStyle(
                  fontFamily: BadGymTypography.fontHeading,
                  fontWeight: FontWeight.w800,
                  fontSize: 8.5,
                  color: Colors.white,
                  letterSpacing: -0.3,
                  height: 1.0,
                ),
              ),
            ),
          ],
        ),
      );
    }

    // Unselected Rail Item
    return InkWell(
      onTap: () => onTabSelected?.call(tab),
      borderRadius: BorderRadius.circular(12),
      child: Container(
        width: 60,
        padding: const EdgeInsets.symmetric(vertical: 4, horizontal: 1),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(
              icon,
              size: 18,
              color: BadGymColors.inkLight.withValues(alpha: 0.85),
            ),
            const SizedBox(height: 2),
            FittedBox(
              fit: BoxFit.scaleDown,
              child: Text(
                label,
                style: TextStyle(
                  fontFamily: BadGymTypography.fontBody,
                  fontWeight: FontWeight.w600,
                  fontSize: 8.5,
                  color: BadGymColors.inkLight.withValues(alpha: 0.85),
                  letterSpacing: -0.3,
                  height: 1.0,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
