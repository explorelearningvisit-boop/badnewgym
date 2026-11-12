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
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 10.0, vertical: 4.0),
      child: Row(
        children: [
          Expanded(
            child: _buildStatCard(
              tab: QuickStatTab.attendance,
              icon: Icons.calendar_month_outlined,
              iconColor: Colors.white,
              title: 'Attendance',
              value: '${data.attendanceRate}%',
            ),
          ),
          const SizedBox(width: 5),
          Expanded(
            child: _buildStatCard(
              tab: QuickStatTab.workout,
              icon: Icons.fitness_center,
              iconColor: BadGymColors.neonViolet,
              title: 'Workout',
              value: data.workoutTime,
            ),
          ),
          const SizedBox(width: 5),
          Expanded(
            child: _buildStatCard(
              tab: QuickStatTab.payment,
              icon: Icons.account_balance_wallet_rounded,
              iconColor: BadGymColors.gold,
              title: 'Payment',
              value: data.paymentStatus,
              valueColor: BadGymColors.emerald,
            ),
          ),
          const SizedBox(width: 5),
          Expanded(
            child: _buildStatCard(
              tab: QuickStatTab.body,
              icon: Icons.person,
              iconColor: BadGymColors.cyanBody,
              title: 'Body',
              value: data.bodyWeight,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildStatCard({
    required QuickStatTab tab,
    required IconData icon,
    Color? iconColor,
    required String title,
    required String value,
    Color? valueColor,
  }) {
    final isSelected = tab == selectedTab;

    if (isSelected) {
      // Selected Tab: Deep Royal Purple with Neon Violet Glow Border
      return InkWell(
        onTap: () => onTabSelected?.call(tab),
        borderRadius: BorderRadius.circular(16),
        child: Container(
          padding: const EdgeInsets.symmetric(vertical: 7, horizontal: 3),
          decoration: BoxDecoration(
            gradient: const LinearGradient(
              begin: Alignment.topCenter,
              end: Alignment.bottomCenter,
              colors: [
                BadGymColors.purpleCardStart,
                BadGymColors.purpleCardEnd,
              ],
            ),
            borderRadius: BorderRadius.circular(16),
            border: Border.all(
              color: BadGymColors.neonViolet,
              width: 1.6,
            ),
            boxShadow: [
              BoxShadow(
                color: BadGymColors.neonViolet.withValues(alpha: 0.5),
                blurRadius: 8,
                offset: const Offset(0, 2),
              ),
              BoxShadow(
                color: Colors.black.withValues(alpha: 0.25),
                blurRadius: 5,
                offset: const Offset(0, 2),
              ),
            ],
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              FittedBox(
                fit: BoxFit.scaleDown,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(
                      icon,
                      size: 12,
                      color: Colors.white,
                    ),
                    const SizedBox(width: 2.5),
                    Text(
                      title,
                      style: const TextStyle(
                        fontFamily: BadGymTypography.fontBody,
                        fontWeight: FontWeight.w600,
                        fontSize: 9.5,
                        color: BadGymColors.inkLight,
                        letterSpacing: -0.2,
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 2),
              FittedBox(
                fit: BoxFit.scaleDown,
                child: Text(
                  value,
                  style: const TextStyle(
                    fontFamily: BadGymTypography.fontHeading,
                    fontWeight: FontWeight.w900,
                    fontSize: 14,
                    color: Colors.white,
                    height: 1.0,
                  ),
                ),
              ),
            ],
          ),
        ),
      );
    }

    // Unselected Tab: Pearl White Glass with Soft Lavender Border
    return InkWell(
      onTap: () => onTabSelected?.call(tab),
      borderRadius: BorderRadius.circular(16),
      child: Container(
        padding: const EdgeInsets.symmetric(vertical: 7, horizontal: 3),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(16),
          border: Border.all(
            color: BadGymColors.borderSubtle,
            width: 1.2,
          ),
          boxShadow: [
            BoxShadow(
              color: BadGymColors.purpleRail.withValues(alpha: 0.05),
              blurRadius: 5,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            FittedBox(
              fit: BoxFit.scaleDown,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                mainAxisSize: MainAxisSize.min,
                children: [
                  Icon(
                    icon,
                    size: 12,
                    color: iconColor ?? BadGymColors.inkMuted,
                  ),
                  const SizedBox(width: 2.5),
                  Text(
                    title,
                    style: const TextStyle(
                      fontFamily: BadGymTypography.fontBody,
                      fontWeight: FontWeight.w600,
                      fontSize: 9.5,
                      color: Color(0xFF4B5563),
                      letterSpacing: -0.2,
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 2),
            FittedBox(
              fit: BoxFit.scaleDown,
              child: Text(
                value,
                style: TextStyle(
                  fontFamily: BadGymTypography.fontHeading,
                  fontWeight: FontWeight.w800,
                  fontSize: 13.5,
                  color: valueColor ?? const Color(0xFF1F2937),
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
