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
  final ScrollController _scrollController = ScrollController();
  RailTab _selectedRailTab = RailTab.intelligence;
  QuickStatTab _selectedStatTab = QuickStatTab.attendance;

  @override
  void initState() {
    super.initState();
    _data = widget.initialData ?? MemberIntelligenceData.ashwaniKumarSingh;
  }

  @override
  void dispose() {
    _scrollController.dispose();
    super.dispose();
  }

  void _onRailTabSelected(RailTab tab) {
    setState(() {
      _selectedRailTab = tab;
      switch (tab) {
        case RailTab.intelligence:
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
          _selectedRailTab = RailTab.intelligence;
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

  void _scrollDown() {
    if (_scrollController.hasClients) {
      _scrollController.animateTo(
        _scrollController.offset + 260,
        duration: const Duration(milliseconds: 350),
        curve: Curves.easeInOut,
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: BadGymColors.background,
      body: SafeArea(
        child: Column(
          children: [
            // 1. Sleek Royal Purple Top Header Bar (Matching Image 2)
            TopHeaderBar(
              data: _data,
              onNotificationPressed: () {
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text('${_data.notificationCount} unread gym notifications'),
                    backgroundColor: BadGymColors.purpleCardStart,
                  ),
                );
              },
              onMorePressed: () {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(
                    content: Text('Member options tapped'),
                    backgroundColor: BadGymColors.purpleCardStart,
                  ),
                );
              },
            ),

            // 2. Scrollable Body
            Expanded(
              child: SingleChildScrollView(
                controller: _scrollController,
                physics: const BouncingScrollPhysics(),
                padding: const EdgeInsets.fromLTRB(4, 2, 4, 16),
                child: Column(
                  children: [
                    // Member VIP Check-In Pass Card (Matching Image 1)
                    MemberProfileCard(
                      data: _data,
                      onRenewPressed: () {
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            content: Text(
                              'Renew Membership: ${_data.daysLeft} Days Remaining (Expires ${_data.renewDate})',
                            ),
                            backgroundColor: BadGymColors.purpleCardStart,
                          ),
                        );
                      },
                      onPaymentDetailsPressed: () {
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            content: Text(
                              'Payment Status: ${_data.paymentStatus} (${_data.paymentAmount})',
                            ),
                            backgroundColor: BadGymColors.purpleCardStart,
                          ),
                        );
                      },
                    ),

                    const SizedBox(height: 8),

                    // 4 Quick Stats Row Spanning Full Width (Matching Image 3 Top)
                    QuickStatsRow(
                      data: _data,
                      selectedTab: _selectedStatTab,
                      onTabSelected: _onStatTabSelected,
                    ),

                    const SizedBox(height: 8),

                    // Two-Column Section: Left Rail + Right Attendance Card (Matching Image 3)
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 10.0),
                      child: Row(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          // Left Vertical Navigation Rail
                          VerticalNavigationRail(
                            selectedTab: _selectedRailTab,
                            onTabSelected: _onRailTabSelected,
                          ),

                          const SizedBox(width: 8),

                          // Right Attendance Intelligence Card
                          Expanded(
                            child: AttendanceIntelligenceCard(
                              data: _data,
                              onMonthDropdownPressed: () {
                                ScaffoldMessenger.of(context).showSnackBar(
                                  const SnackBar(
                                    content: Text('October 2026 selected'),
                                    backgroundColor: BadGymColors.purpleCardStart,
                                  ),
                                );
                              },
                              onInfoPressed: () {
                                ScaffoldMessenger.of(context).showSnackBar(
                                  const SnackBar(
                                    content: Text('Attendance Intelligence Calculation: 25 / 31 Days (81%)'),
                                    backgroundColor: BadGymColors.purpleCardStart,
                                  ),
                                );
                              },
                              onScrollDownPressed: _scrollDown,
                            ),
                          ),
                        ],
                      ),
                    ),

                    const SizedBox(height: 12),

                    // Bottom Promotional Banner (Muscle Building Program)
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 10.0),
                      child: PromoBannerWidget(
                        onTap: () {
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(
                              content: Text('Start Muscle Building Plan'),
                              backgroundColor: BadGymColors.purpleCardStart,
                            ),
                          );
                        },
                      ),
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
