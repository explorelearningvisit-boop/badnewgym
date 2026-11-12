import 'package:flutter/material.dart';
import 'package:bad_gym/core/models/member_intelligence_data.dart';
import 'package:bad_gym/core/theme/bad_gym_theme.dart';

class MemberProfileCard extends StatelessWidget {
  final MemberIntelligenceData data;
  final VoidCallback? onRenewPressed;
  final VoidCallback? onPaymentDetailsPressed;

  const MemberProfileCard({
    super.key,
    required this.data,
    this.onRenewPressed,
    this.onPaymentDetailsPressed,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 10.0, vertical: 4.0),
      decoration: BoxDecoration(
        gradient: const LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [
            BadGymColors.purpleCardStart,
            BadGymColors.purpleCardMid,
            BadGymColors.purpleCardEnd,
          ],
        ),
        borderRadius: BorderRadius.circular(22),
        border: Border.all(
          color: BadGymColors.neonVioletBorder.withValues(alpha: 0.6),
          width: 1.3,
        ),
        boxShadow: [
          BoxShadow(
            color: BadGymColors.neonViolet.withValues(alpha: 0.22),
            blurRadius: 14,
            offset: const Offset(0, 4),
          ),
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.35),
            blurRadius: 8,
            offset: const Offset(0, 3),
          ),
        ],
      ),
      padding: const EdgeInsets.fromLTRB(8, 8, 8, 8),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisSize: MainAxisSize.min,
        children: [
          // 1. Upper Section: Athlete | Check-In Info | Divider | Payment & Shaker
          Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              // Athlete Cutout Image (Cleanly integrated without harsh inner box)
              SizedBox(
                width: 82,
                height: 82,
                child: ClipRRect(
                  borderRadius: const BorderRadius.only(
                    topLeft: Radius.circular(16),
                    bottomLeft: Radius.circular(12),
                  ),
                  child: Image.asset(
                    'assets/images/vip_athlete_hi.png',
                    fit: BoxFit.cover,
                    alignment: Alignment.topCenter,
                    errorBuilder: (context, error, stackTrace) {
                      return Image.asset(
                        'assets/images/avatar_ashwini.png',
                        fit: BoxFit.cover,
                      );
                    },
                  ),
                ),
              ),
              const SizedBox(width: 6),

              // Center: Checked In Details
              Expanded(
                flex: 11,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    // Checked In Neon Capsule
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 7, vertical: 2.5),
                      decoration: BoxDecoration(
                        gradient: const LinearGradient(
                          colors: [
                            Color(0xFF22C55E),
                            Color(0xFF16A34A),
                          ],
                        ),
                        borderRadius: BorderRadius.circular(12),
                        boxShadow: [
                          BoxShadow(
                            color: const Color(0xFF22C55E).withValues(alpha: 0.4),
                            blurRadius: 4,
                          ),
                        ],
                      ),
                      child: const Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          Icon(
                            Icons.check_circle_rounded,
                            size: 11,
                            color: Colors.white,
                          ),
                          SizedBox(width: 3),
                          Text(
                            'Checked In',
                            style: TextStyle(
                              fontFamily: BadGymTypography.fontHeading,
                              fontWeight: FontWeight.w800,
                              fontSize: 9.5,
                              color: Colors.white,
                            ),
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(height: 2),

                    // Big Bold Time
                    Text(
                      data.checkInTime,
                      style: const TextStyle(
                        fontFamily: BadGymTypography.fontHeading,
                        fontWeight: FontWeight.w900,
                        fontSize: 20,
                        color: Colors.white,
                        letterSpacing: 0.2,
                        height: 1.1,
                      ),
                    ),
                    const SizedBox(height: 2),

                    // Date Row
                    Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        const Icon(
                          Icons.calendar_month,
                          size: 10,
                          color: BadGymColors.inkLight,
                        ),
                        const SizedBox(width: 3),
                        Flexible(
                          child: Text(
                            data.checkInDate,
                            style: const TextStyle(
                              fontFamily: BadGymTypography.fontBody,
                              fontWeight: FontWeight.w500,
                              fontSize: 9.5,
                              color: BadGymColors.inkLight,
                            ),
                            maxLines: 1,
                            overflow: TextOverflow.ellipsis,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 1),

                    // Location Row
                    Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        const Icon(
                          Icons.location_on,
                          size: 10,
                          color: BadGymColors.inkLight,
                        ),
                        const SizedBox(width: 3),
                        Flexible(
                          child: Text(
                            data.checkInLocation,
                            style: const TextStyle(
                              fontFamily: BadGymTypography.fontBody,
                              fontWeight: FontWeight.w500,
                              fontSize: 9.5,
                              color: BadGymColors.inkLight,
                            ),
                            maxLines: 1,
                            overflow: TextOverflow.ellipsis,
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),

              // Vertical Divider Line
              Container(
                width: 1,
                height: 52,
                color: Colors.white.withValues(alpha: 0.2),
                margin: const EdgeInsets.symmetric(horizontal: 4),
              ),

              // Right: Payment & Shaker
              Expanded(
                flex: 13,
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          // Payment header with 3-dots
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              const Row(
                                mainAxisSize: MainAxisSize.min,
                                children: [
                                  Icon(
                                    Icons.account_balance_wallet_outlined,
                                    size: 11,
                                    color: BadGymColors.inkLight,
                                  ),
                                  SizedBox(width: 3),
                                  Text(
                                    'Payment',
                                    style: TextStyle(
                                      fontFamily: BadGymTypography.fontBody,
                                      fontWeight: FontWeight.w600,
                                      fontSize: 10,
                                      color: BadGymColors.inkLight,
                                    ),
                                  ),
                                ],
                              ),
                              Container(
                                width: 18,
                                height: 18,
                                decoration: BoxDecoration(
                                  color: Colors.white.withValues(alpha: 0.12),
                                  shape: BoxShape.circle,
                                ),
                                alignment: Alignment.center,
                                child: const Icon(
                                  Icons.more_vert,
                                  size: 12,
                                  color: Colors.white,
                                ),
                              ),
                            ],
                          ),
                          const SizedBox(height: 2),

                          // Paid Green Capsule
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                            decoration: BoxDecoration(
                              color: BadGymColors.emerald,
                              borderRadius: BorderRadius.circular(10),
                            ),
                            child: const Row(
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                Icon(
                                  Icons.check_circle_rounded,
                                  size: 9.5,
                                  color: Colors.white,
                                ),
                                SizedBox(width: 2.5),
                                Text(
                                  'Paid',
                                  style: TextStyle(
                                    fontFamily: BadGymTypography.fontHeading,
                                    fontWeight: FontWeight.w800,
                                    fontSize: 9,
                                    color: Colors.white,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 1),

                          // Amount
                          Text(
                            data.paymentAmount,
                            style: const TextStyle(
                              fontFamily: BadGymTypography.fontHeading,
                              fontWeight: FontWeight.w900,
                              fontSize: 17,
                              color: Colors.white,
                              height: 1.1,
                            ),
                          ),
                          const SizedBox(height: 1),

                          // Next Due
                          FittedBox(
                            fit: BoxFit.scaleDown,
                            alignment: Alignment.centerLeft,
                            child: Text(
                              'Next Due: ${data.paymentNextDue}',
                              style: TextStyle(
                                fontFamily: BadGymTypography.fontBody,
                                fontWeight: FontWeight.w500,
                                fontSize: 8.5,
                                color: BadGymColors.inkLight.withValues(alpha: 0.9),
                                letterSpacing: -0.2,
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),

                    // Purple 3D Shaker Bottle
                    Container(
                      width: 24,
                      height: 44,
                      margin: const EdgeInsets.only(left: 3),
                      child: Image.asset(
                        'assets/images/vip_shaker_hi.png',
                        fit: BoxFit.contain,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),

          const SizedBox(height: 6),

          // 2. Lower Section: Nested Dark Glass Container with Member Details & Expiry
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 8),
            decoration: BoxDecoration(
              color: BadGymColors.purpleInnerGlass.withValues(alpha: 0.92),
              borderRadius: BorderRadius.circular(16),
              border: Border.all(
                color: BadGymColors.neonViolet.withValues(alpha: 0.25),
                width: 1,
              ),
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                // Member Name + Badges
                Row(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Expanded(
                      child: Text(
                        data.name,
                        style: const TextStyle(
                          fontFamily: BadGymTypography.fontHeading,
                          fontWeight: FontWeight.w800,
                          fontSize: 13.5,
                          color: Colors.white,
                          letterSpacing: -0.2,
                        ),
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                      ),
                    ),
                    const SizedBox(width: 4),

                    // Active badge
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                      decoration: BoxDecoration(
                        color: BadGymColors.emerald,
                        borderRadius: BorderRadius.circular(10),
                      ),
                      child: const Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          Icon(
                            Icons.check_circle_rounded,
                            size: 9.5,
                            color: Colors.white,
                          ),
                          SizedBox(width: 2.5),
                          Text(
                            'Active',
                            style: TextStyle(
                              fontFamily: BadGymTypography.fontHeading,
                              fontWeight: FontWeight.w800,
                              fontSize: 9,
                              color: Colors.white,
                            ),
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(width: 4),

                    // Gold Plan badge
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                      decoration: BoxDecoration(
                        gradient: const LinearGradient(
                          colors: [
                            BadGymColors.gold,
                            BadGymColors.goldDark,
                          ],
                        ),
                        borderRadius: BorderRadius.circular(10),
                      ),
                      child: const Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          Icon(
                            Icons.workspace_premium,
                            size: 10,
                            color: Color(0xFF1E0E00),
                          ),
                          SizedBox(width: 2.5),
                          Text(
                            'Gold Plan',
                            style: TextStyle(
                              fontFamily: BadGymTypography.fontHeading,
                              fontWeight: FontWeight.w800,
                              fontSize: 9,
                              color: Color(0xFF1E0E00),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 3),

                // Member ID, Age, Gender
                Row(
                  children: [
                    const Icon(
                      Icons.badge_outlined,
                      size: 11,
                      color: BadGymColors.inkLight,
                    ),
                    const SizedBox(width: 3),
                    Flexible(
                      child: Text(
                        '${data.memberId}  |  ${data.age} Years  |  ♂ ${data.gender}',
                        style: const TextStyle(
                          fontFamily: BadGymTypography.fontBody,
                          fontWeight: FontWeight.w500,
                          fontSize: 9.5,
                          color: BadGymColors.inkLight,
                        ),
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 6),

                // Progress Bar and 8 Days Left Badge
                Row(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    // Left Progress
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          Text(
                            'Membership: ${data.daysUsed} / ${data.totalDays} Days (${data.usedPercentage.toStringAsFixed(1)}% Used)',
                            style: const TextStyle(
                              fontFamily: BadGymTypography.fontBody,
                              fontWeight: FontWeight.w500,
                              fontSize: 8.8,
                              color: BadGymColors.inkLight,
                            ),
                            maxLines: 1,
                            overflow: TextOverflow.ellipsis,
                          ),
                          const SizedBox(height: 4),
                          // Progress Bar Track
                          Container(
                            height: 6.5,
                            width: double.infinity,
                            decoration: BoxDecoration(
                              color: const Color(0xFF381B57),
                              borderRadius: BorderRadius.circular(4),
                            ),
                            child: FractionallySizedBox(
                              alignment: Alignment.centerLeft,
                              widthFactor: (data.usedPercentage / 100.0).clamp(0.0, 1.0),
                              child: Container(
                                decoration: BoxDecoration(
                                  gradient: const LinearGradient(
                                    colors: [
                                      BadGymColors.neonLime,
                                      BadGymColors.neonLimeGlow,
                                    ],
                                  ),
                                  borderRadius: BorderRadius.circular(4),
                                  boxShadow: [
                                    BoxShadow(
                                      color: BadGymColors.neonLime.withValues(alpha: 0.6),
                                      blurRadius: 4,
                                    ),
                                  ],
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(width: 8),

                    // Right Floating Pearl Card (8 Days Left)
                    InkWell(
                      onTap: onRenewPressed,
                      borderRadius: BorderRadius.circular(10),
                      child: Container(
                        padding: const EdgeInsets.symmetric(horizontal: 7, vertical: 3.5),
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.circular(10),
                          boxShadow: [
                            BoxShadow(
                              color: Colors.black.withValues(alpha: 0.15),
                              blurRadius: 5,
                              offset: const Offset(0, 2),
                            ),
                          ],
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.center,
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            Row(
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                Container(
                                  padding: const EdgeInsets.all(2),
                                  decoration: BoxDecoration(
                                    color: BadGymColors.redAbsent.withValues(alpha: 0.12),
                                    shape: BoxShape.circle,
                                  ),
                                  child: const Icon(
                                    Icons.alarm,
                                    size: 11,
                                    color: BadGymColors.redAbsent,
                                  ),
                                ),
                                const SizedBox(width: 3),
                                Text(
                                  '${data.daysLeft} Days Left',
                                  style: const TextStyle(
                                    fontFamily: BadGymTypography.fontHeading,
                                    fontWeight: FontWeight.w900,
                                    fontSize: 10,
                                    color: BadGymColors.redAbsent,
                                  ),
                                ),
                              ],
                            ),
                            const SizedBox(height: 1),
                            Text(
                              'Expires on: ${data.renewDate}',
                              style: const TextStyle(
                                fontFamily: BadGymTypography.fontBody,
                                fontWeight: FontWeight.w600,
                                fontSize: 8,
                                color: Color(0xFF4B5563),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
