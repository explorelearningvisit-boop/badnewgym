import 'package:flutter/material.dart';
import 'package:bad_gym/core/models/member_intelligence_data.dart';
import 'package:bad_gym/core/theme/bad_gym_theme.dart';

class MemberProfileCard extends StatelessWidget {
  final MemberIntelligenceData data;
  final VoidCallback? onRenewPressed;

  const MemberProfileCard({
    super.key,
    required this.data,
    this.onRenewPressed,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        gradient: const LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [
            BadGymColors.darkCardStart,
            BadGymColors.darkCardMid,
            BadGymColors.darkCardEnd,
          ],
        ),
        borderRadius: BorderRadius.circular(20),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.18),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 12),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisSize: MainAxisSize.min,
        children: [
          // Top row: Avatar, Info, Action
          Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              // Avatar with gold border ring
              Container(
                width: 54,
                height: 54,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  border: Border.all(
                    color: const Color(0xFFE5A338),
                    width: 2.2,
                  ),
                ),
                child: ClipOval(
                  child: Image.asset(
                    'assets/images/avatar_ashwini.png',
                    fit: BoxFit.cover,
                  ),
                ),
              ),
              const SizedBox(width: 10),

              // Member Details
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Text(
                      data.name,
                      style: BadGymTypography.cardTitle.copyWith(
                        fontSize: 14.5,
                        letterSpacing: -0.2,
                      ),
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                    ),
                    const SizedBox(height: 2),
                    Text(
                      '${data.memberId} • ${data.age} Years • ${data.gender}',
                      style: TextStyle(
                        fontFamily: BadGymTypography.fontBody,
                        fontWeight: FontWeight.w500,
                        fontSize: 9.8,
                        color: BadGymColors.inkLight.withValues(alpha: 0.9),
                      ),
                    ),
                    const SizedBox(height: 5),

                    // Badges row: Active & Gold Plan
                    Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        // Active pill
                        Container(
                          padding: const EdgeInsets.symmetric(
                            horizontal: 7,
                            vertical: 2.5,
                          ),
                          decoration: BoxDecoration(
                            color: BadGymColors.emeraldActive,
                            borderRadius: BorderRadius.circular(12),
                          ),
                          child: const Row(
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              Icon(
                                Icons.check_circle,
                                size: 11,
                                color: Colors.white,
                              ),
                              SizedBox(width: 3),
                              Text(
                                'Active',
                                style: TextStyle(
                                  fontFamily: BadGymTypography.fontBody,
                                  fontSize: 9,
                                  fontWeight: FontWeight.w700,
                                  color: Colors.white,
                                ),
                              ),
                            ],
                          ),
                        ),
                        const SizedBox(width: 6),

                        // Gold Plan pill
                        Container(
                          padding: const EdgeInsets.symmetric(
                            horizontal: 7,
                            vertical: 2.5,
                          ),
                          decoration: BoxDecoration(
                            color: BadGymColors.gold,
                            borderRadius: BorderRadius.circular(12),
                          ),
                          child: const Row(
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              Icon(
                                Icons.workspace_premium,
                                size: 11,
                                color: BadGymColors.goldDark,
                              ),
                              SizedBox(width: 3),
                              Text(
                                'Gold Plan',
                                style: TextStyle(
                                  fontFamily: BadGymTypography.fontBody,
                                  fontSize: 9,
                                  fontWeight: FontWeight.w800,
                                  color: BadGymColors.goldDark,
                                ),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),

              // Hairline vertical divider
              Container(
                height: 42,
                width: 1,
                color: const Color(0xFF5A4644),
                margin: const EdgeInsets.symmetric(horizontal: 8),
              ),

              // Right pill button & renewal date
              Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisSize: MainAxisSize.min,
                children: [
                  InkWell(
                    onTap: onRenewPressed,
                    borderRadius: BorderRadius.circular(8),
                    child: Container(
                      padding: const EdgeInsets.symmetric(
                        horizontal: 8,
                        vertical: 4,
                      ),
                      decoration: BoxDecoration(
                        gradient: const LinearGradient(
                          colors: [
                            BadGymColors.coralPrimary,
                            BadGymColors.coralDark,
                          ],
                        ),
                        borderRadius: BorderRadius.circular(8),
                        boxShadow: [
                          BoxShadow(
                            color: BadGymColors.coralDark.withValues(alpha: 0.35),
                            blurRadius: 4,
                            offset: const Offset(0, 2),
                          ),
                        ],
                      ),
                      child: const Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          Icon(
                            Icons.calendar_month,
                            size: 11,
                            color: Colors.white,
                          ),
                          SizedBox(width: 3),
                          Text(
                            '8 Days Left',
                            style: TextStyle(
                              fontFamily: BadGymTypography.fontBody,
                              fontSize: 9.5,
                              fontWeight: FontWeight.w700,
                              color: Colors.white,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                  const SizedBox(height: 3),
                  Text(
                    'Renew by\n${data.renewDate}',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      fontFamily: BadGymTypography.fontBody,
                      fontWeight: FontWeight.w500,
                      fontSize: 8.8,
                      color: BadGymColors.inkLight.withValues(alpha: 0.85),
                      height: 1.15,
                    ),
                  ),
                ],
              ),
            ],
          ),

          const SizedBox(height: 10),

          // Progress text
          Text(
            'Membership: ${data.daysUsed} / ${data.totalDays} Days (${data.usedPercentage.toStringAsFixed(1)}% Used)',
            style: TextStyle(
              fontFamily: BadGymTypography.fontBody,
              fontWeight: FontWeight.w600,
              fontSize: 9.8,
              color: BadGymColors.inkLight.withValues(alpha: 0.95),
            ),
          ),
          const SizedBox(height: 5),

          // Horizontal Progress Bar
          LayoutBuilder(
            builder: (context, constraints) {
              final double progressWidth =
                  constraints.maxWidth * (data.usedPercentage / 100.0);
              return Container(
                height: 6,
                width: constraints.maxWidth,
                decoration: BoxDecoration(
                  color: const Color(0xFF4D3A39),
                  borderRadius: BorderRadius.circular(3),
                ),
                alignment: Alignment.centerLeft,
                child: Container(
                  height: 6,
                  width: progressWidth.clamp(0.0, constraints.maxWidth),
                  decoration: BoxDecoration(
                    gradient: const LinearGradient(
                      colors: [
                        BadGymColors.limeProgressStart,
                        BadGymColors.limeProgressEnd,
                      ],
                    ),
                    borderRadius: BorderRadius.circular(3),
                  ),
                ),
              );
            },
          ),
        ],
      ),
    );
  }
}
