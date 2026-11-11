import 'package:flutter/material.dart';
import 'package:bad_gym/core/models/member_intelligence_data.dart';
import 'package:bad_gym/core/theme/bad_gym_theme.dart';
import 'package:bad_gym/presentation/widgets/attendance_intelligence_card.dart';
import 'package:bad_gym/presentation/widgets/member_profile_card.dart';
import 'package:bad_gym/presentation/widgets/promo_banner_widget.dart';
import 'package:bad_gym/presentation/widgets/quick_stats_row.dart';
import 'package:bad_gym/presentation/widgets/top_header_bar.dart';
import 'package:bad_gym/presentation/widgets/vertical_navigation_rail.dart';

class MemberCheckInScreen extends StatefulWidget {
  final MemberIntelligenceData? initialData;

  const MemberCheckInScreen({
    super.key,
    this.initialData,
  });

  @override
  State<MemberCheckInScreen> createState() => _MemberCheckInScreenState();
}

class _MemberCheckInScreenState extends State<MemberCheckInScreen> {
  late final MemberIntelligenceData _data;
  RailTab _selectedRailTab = RailTab.attendance;
  QuickStatTab _selectedStatTab = QuickStatTab.attendance;

  @override
  void initState() {
    super.initState();
    _data = widget.initialData ?? MemberIntelligenceData.ashwaniKumarSingh;
  }

  void _onRailTabSelected(RailTab tab) {
    setState(() {
      _selectedRailTab = tab;
      switch (tab) {
        case RailTab.attendance:
          _selectedStatTab = QuickStatTab.attendance;
          break;
        case RailTab.workout:
          _selectedStatTab = QuickStatTab.workout;
          break;
        case RailTab.payment:
          _selectedStatTab = QuickStatTab.payment;
          break;
        case RailTab.body:
          _selectedStatTab = QuickStatTab.body;
          break;
        default:
          break;
      }
    });
  }

  void _onStatTabSelected(QuickStatTab tab) {
    setState(() {
      _selectedStatTab = tab;
      switch (tab) {
        case QuickStatTab.attendance:
          _selectedRailTab = RailTab.attendance;
          break;
        case QuickStatTab.workout:
          _selectedRailTab = RailTab.workout;
          break;
        case QuickStatTab.payment:
          _selectedRailTab = RailTab.payment;
          break;
        case QuickStatTab.body:
          _selectedRailTab = RailTab.body;
          break;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: BadGymColors.background,
      body: SafeArea(
        child: Column(
          children: [
            // Top App Bar
            TopHeaderBar(
              onMenuPressed: () {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('Menu tapped')),
                );
              },
              onNotificationPressed: () {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('Notifications (1 unread)')),
                );
              },
              onDatePressed: () {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('Date: Thu, 16 Oct 2026')),
                );
              },
            ),

            // Scrollable Content
            Expanded(
              child: SingleChildScrollView(
                physics: const BouncingScrollPhysics(),
                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                child: Column(
                  children: [
                    // Member Profile Card
                    MemberProfileCard(
                      data: _data,
                      onRenewPressed: () {
                        ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(content: Text('Plan Renewal - 8 Days Remaining')),
                        );
                      },
                    ),

                    const SizedBox(height: 6),

                    // Main 2-column layout: Left Rail + Right Content
                    IntrinsicHeight(
                      child: Row(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: [
                          // Left Vertical Navigation Rail
                          VerticalNavigationRail(
                            selectedTab: _selectedRailTab,
                            onTabSelected: _onRailTabSelected,
                          ),

                          const SizedBox(width: 6),

                          // Right Column: Quick Stats + Attendance Intelligence
                          Expanded(
                            child: Column(
                              children: [
                                // 4 Quick Stat Cards
                                QuickStatsRow(
                                  data: _data,
                                  selectedTab: _selectedStatTab,
                                  onTabSelected: _onStatTabSelected,
                                ),

                                const SizedBox(height: 6),

                                // Attendance Intelligence Card
                                AttendanceIntelligenceCard(
                                  data: _data,
                                  onMonthDropdownPressed: () {
                                    ScaffoldMessenger.of(context).showSnackBar(
                                      const SnackBar(content: Text('October 2026 selected')),
                                    );
                                  },
                                ),
                              ],
                            ),
                          ),
                        ],
                      ),
                    ),

                    const SizedBox(height: 8),

                    // Bottom Promotional Banner
                    PromoBannerWidget(
                      onTap: () {
                        ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(content: Text('Muscle Building Program tapped')),
                        );
                      },
                    ),

                    const SizedBox(height: 10),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
