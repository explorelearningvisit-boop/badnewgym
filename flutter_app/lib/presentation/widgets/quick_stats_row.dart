import 'package:flutter/material.dart';
import 'package:bad_gym/core/models/member_intelligence_data.dart';
import 'package:bad_gym/core/theme/bad_gym_theme.dart';

enum QuickStatTab {
  attendance,
  workout,
  payment,
  body,
}

class QuickStatsRow extends StatelessWidget {
  final MemberIntelligenceData data;
  final QuickStatTab selectedTab;
  final ValueChanged<QuickStatTab>? onTabSelected;

  const QuickStatsRow({
    super.key,
    required this.data,
    this.selectedTab = QuickStatTab.attendance,
    this.onTabSelected,
  });

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Expanded(
          child: _buildStatCard(
            tab: QuickStatTab.attendance,
            icon: Icons.calendar_month_outlined,
            title: 'Attendance',
            value: '${data.attendanceRate}%',
          ),
        ),
        const SizedBox(width: 6),
        Expanded(
          child: _buildStatCard(
            tab: QuickStatTab.workout,
            icon: Icons.fitness_center,
            title: 'Workout',
            value: data.workoutTime,
          ),
        ),
        const SizedBox(width: 6),
        Expanded(
          child: _buildStatCard(
            tab: QuickStatTab.payment,
            icon: Icons.account_balance_wallet_outlined,
            title: 'Payment',
            value: data.paymentStatus,
            valueColor: BadGymColors.emerald,
          ),
        ),
        const SizedBox(width: 6),
        Expanded(
          child: _buildStatCard(
            tab: QuickStatTab.body,
            icon: Icons.accessibility_new_outlined,
            title: 'Body',
            value: data.bodyWeight,
          ),
        ),
      ],
    );
  }

  Widget _buildStatCard({
    required QuickStatTab tab,
    required IconData icon,
    required String title,
    required String value,
    Color? valueColor,
  }) {
    final isSelected = tab == selectedTab;

    if (isSelected) {
      return InkWell(
        onTap: () => onTabSelected?.call(tab),
        borderRadius: BorderRadius.circular(14),
        child: Container(
          padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 4),
          decoration: BoxDecoration(
            gradient: const LinearGradient(
              begin: Alignment.topCenter,
              end: Alignment.bottomCenter,
              colors: [
                Color(0xFFED5B54),
                Color(0xFFE0453E),
              ],
            ),
            borderRadius: BorderRadius.circular(14),
            boxShadow: [
              BoxShadow(
                color: const Color(0xFFE0453E).withValues(alpha: 0.35),
                blurRadius: 8,
                offset: const Offset(0, 3),
              ),
            ],
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Padding(
                padding: const EdgeInsets.only(left: 4.0),
                child: Icon(
                  icon,
                  color: Colors.white,
                  size: 16,
                ),
              ),
              const SizedBox(height: 3),
              Padding(
                padding: const EdgeInsets.only(left: 4.0),
                child: Text(
                  title,
                  style: const TextStyle(
                    fontFamily: BadGymTypography.fontBody,
                    fontSize: 9.5,
                    fontWeight: FontWeight.w500,
                    color: Colors.white,
                  ),
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),
              ),
              const SizedBox(height: 1),
              Padding(
                padding: const EdgeInsets.only(left: 4.0),
                child: Text(
                  value,
                  style: const TextStyle(
                    fontFamily: BadGymTypography.fontHeading,
                    fontSize: 14,
                    fontWeight: FontWeight.w900,
                    color: Colors.white,
                  ),
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),
              ),
            ],
          ),
        ),
      );
    }

    return InkWell(
      onTap: () => onTabSelected?.call(tab),
      borderRadius: BorderRadius.circular(14),
      child: Container(
        padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 4),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(14),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withValues(alpha: 0.04),
              blurRadius: 6,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Padding(
              padding: const EdgeInsets.only(left: 4.0),
              child: Icon(
                icon,
                color: BadGymColors.coralPrimary,
                size: 16,
              ),
            ),
            const SizedBox(height: 3),
            Padding(
              padding: const EdgeInsets.only(left: 4.0),
              child: Text(
                title,
                style: const TextStyle(
                  fontFamily: BadGymTypography.fontBody,
                  fontSize: 9.5,
                  fontWeight: FontWeight.w500,
                  color: BadGymColors.inkPrimary,
                ),
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
              ),
            ),
            const SizedBox(height: 1),
            Padding(
              padding: const EdgeInsets.only(left: 4.0),
              child: Text(
                value,
                style: TextStyle(
                  fontFamily: BadGymTypography.fontHeading,
                  fontSize: 13,
                  fontWeight: FontWeight.w800,
                  color: valueColor ?? BadGymColors.inkPrimary,
                ),
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
