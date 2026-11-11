import 'package:flutter/material.dart';
import 'package:bad_gym/core/theme/bad_gym_theme.dart';

enum RailTab {
  overview,
  attendance,
  workout,
  body,
  payment,
  goals,
  reports,
  offers,
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
      width: 62,
      padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 3),
      decoration: BoxDecoration(
        color: BadGymColors.darkRail,
        borderRadius: BorderRadius.circular(22),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.12),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          _buildRailItem(
            tab: RailTab.overview,
            icon: Icons.home_outlined,
            label: 'Overview',
          ),
          const SizedBox(height: 5),
          _buildRailItem(
            tab: RailTab.attendance,
            icon: Icons.event_available,
            label: 'Attendance',
          ),
          const SizedBox(height: 5),
          _buildRailItem(
            tab: RailTab.workout,
            icon: Icons.fitness_center,
            label: 'Workout',
          ),
          const SizedBox(height: 5),
          _buildRailItem(
            tab: RailTab.body,
            icon: Icons.person_outline,
            label: 'Body',
          ),
          const SizedBox(height: 5),
          _buildRailItem(
            tab: RailTab.payment,
            icon: Icons.account_balance_wallet_outlined,
            label: 'Payment',
          ),
          const SizedBox(height: 5),
          // Divider
          Container(
            height: 1,
            width: 34,
            color: BadGymColors.darkRailDivider,
            margin: const EdgeInsets.symmetric(vertical: 4),
          ),
          const SizedBox(height: 4),
          _buildRailItem(
            tab: RailTab.goals,
            icon: Icons.track_changes,
            label: 'Goals',
          ),
          const SizedBox(height: 5),
          _buildRailItem(
            tab: RailTab.reports,
            icon: Icons.bar_chart,
            label: 'Reports',
          ),
          const SizedBox(height: 5),
          _buildRailItem(
            tab: RailTab.offers,
            icon: Icons.local_offer_outlined,
            label: 'Offers',
            hasBadge: true,
          ),
        ],
      ),
    );
  }

  Widget _buildRailItem({
    required RailTab tab,
    required IconData icon,
    required String label,
    bool hasBadge = false,
  }) {
    final isSelected = tab == selectedTab;

    if (isSelected) {
      return InkWell(
        onTap: () => onTabSelected?.call(tab),
        borderRadius: BorderRadius.circular(14),
        child: Container(
          width: 56,
          padding: const EdgeInsets.symmetric(vertical: 6, horizontal: 2),
          decoration: BoxDecoration(
            gradient: const LinearGradient(
              begin: Alignment.topCenter,
              end: Alignment.bottomCenter,
              colors: [
                Color(0xFFF0645B),
                Color(0xFFE54A41),
              ],
            ),
            borderRadius: BorderRadius.circular(14),
            boxShadow: [
              BoxShadow(
                color: const Color(0xFFE54A41).withValues(alpha: 0.4),
                blurRadius: 8,
                offset: const Offset(0, 3),
              ),
            ],
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Icon(
                icon,
                color: Colors.white,
                size: 19,
              ),
              const SizedBox(height: 2),
              FittedBox(
                fit: BoxFit.scaleDown,
                child: Text(
                  label,
                  style: const TextStyle(
                    fontFamily: BadGymTypography.fontBody,
                    fontSize: 8.5,
                    fontWeight: FontWeight.w700,
                    color: Colors.white,
                    letterSpacing: -0.2,
                  ),
                ),
              ),
            ],
          ),
        ),
      );
    }

    return InkWell(
      onTap: () => onTabSelected?.call(tab),
      borderRadius: BorderRadius.circular(10),
      child: Container(
        width: 56,
        padding: const EdgeInsets.symmetric(vertical: 5, horizontal: 2),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Stack(
              clipBehavior: Clip.none,
              children: [
                Icon(
                  icon,
                  color: BadGymColors.inkLight,
                  size: 19,
                ),
                if (hasBadge)
                  Positioned(
                    top: -2,
                    right: -3,
                    child: Container(
                      width: 6,
                      height: 6,
                      decoration: const BoxDecoration(
                        color: BadGymColors.coralPrimary,
                        shape: BoxShape.circle,
                      ),
                    ),
                  ),
              ],
            ),
            const SizedBox(height: 2),
            FittedBox(
              fit: BoxFit.scaleDown,
              child: Text(
                label,
                style: TextStyle(
                  fontFamily: BadGymTypography.fontBody,
                  fontSize: 8.5,
                  fontWeight: FontWeight.w500,
                  color: BadGymColors.inkLight.withValues(alpha: 0.9),
                  letterSpacing: -0.2,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
