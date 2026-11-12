import 'package:flutter/material.dart';
import 'package:bad_gym/core/models/member_intelligence_data.dart';
import 'package:bad_gym/core/theme/bad_gym_theme.dart';

class TopHeaderBar extends StatelessWidget {
  final MemberIntelligenceData data;
  final VoidCallback? onNotificationPressed;
  final VoidCallback? onMorePressed;

  const TopHeaderBar({
    super.key,
    required this.data,
    this.onNotificationPressed,
    this.onMorePressed,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 10.0, vertical: 6.0),
      padding: const EdgeInsets.symmetric(horizontal: 8.0, vertical: 6.0),
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
          color: BadGymColors.neonVioletBorder.withValues(alpha: 0.65),
          width: 1.5,
        ),
        boxShadow: [
          BoxShadow(
            color: BadGymColors.neonViolet.withValues(alpha: 0.22),
            blurRadius: 10,
            offset: const Offset(0, 3),
          ),
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.3),
            blurRadius: 6,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          // 1. Member Profile Avatar with Neon Ring
          Container(
            width: 42,
            height: 42,
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              border: Border.all(
                color: BadGymColors.neonViolet,
                width: 1.8,
              ),
              boxShadow: [
                BoxShadow(
                  color: BadGymColors.neonViolet.withValues(alpha: 0.4),
                  blurRadius: 6,
                ),
              ],
            ),
            child: ClipOval(
              child: Image.asset(
                'assets/images/avatar_ashwini.png',
                fit: BoxFit.cover,
              ),
            ),
          ),
          const SizedBox(width: 8),

          // 2. Center Capsule: Checked In + Time & Date (Scaling smoothly)
          Expanded(
            child: Container(
              padding: const EdgeInsets.symmetric(horizontal: 8.0, vertical: 4.0),
              decoration: BoxDecoration(
                color: BadGymColors.purpleInnerGlass.withValues(alpha: 0.85),
                borderRadius: BorderRadius.circular(18),
                border: Border.all(
                  color: BadGymColors.neonViolet.withValues(alpha: 0.25),
                  width: 1,
                ),
              ),
              child: FittedBox(
                fit: BoxFit.scaleDown,
                alignment: Alignment.center,
                child: Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    // Checked In neon green pill
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 7.5, vertical: 3.5),
                      decoration: BoxDecoration(
                        gradient: const LinearGradient(
                          colors: [
                            Color(0xFF22C55E),
                            Color(0xFF16A34A),
                          ],
                        ),
                        borderRadius: BorderRadius.circular(14),
                        boxShadow: [
                          BoxShadow(
                            color: const Color(0xFF22C55E).withValues(alpha: 0.45),
                            blurRadius: 6,
                          ),
                        ],
                      ),
                      child: const Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          Icon(
                            Icons.check_circle_rounded,
                            size: 13,
                            color: Colors.white,
                          ),
                          SizedBox(width: 3.5),
                          Text(
                            'Checked In',
                            style: TextStyle(
                              fontFamily: BadGymTypography.fontHeading,
                              fontWeight: FontWeight.w800,
                              fontSize: 10.5,
                              color: Colors.white,
                              letterSpacing: -0.2,
                            ),
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(width: 8),

                    // Thin translucent divider
                    Container(
                      width: 1,
                      height: 24,
                      color: Colors.white.withValues(alpha: 0.2),
                    ),
                    const SizedBox(width: 8),

                    // Time and Date column
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Text(
                          data.checkInTime,
                          style: const TextStyle(
                            fontFamily: BadGymTypography.fontHeading,
                            fontWeight: FontWeight.w900,
                            fontSize: 14.5,
                            color: Colors.white,
                            height: 1.1,
                            letterSpacing: 0.2,
                          ),
                        ),
                        Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            const Icon(
                              Icons.calendar_month,
                              size: 9.5,
                              color: BadGymColors.inkLight,
                            ),
                            const SizedBox(width: 2.5),
                            Text(
                              data.checkInDate,
                              style: const TextStyle(
                                fontFamily: BadGymTypography.fontBody,
                                fontWeight: FontWeight.w500,
                                fontSize: 9.5,
                                color: BadGymColors.inkLight,
                                height: 1.1,
                              ),
                            ),
                          ],
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ),
          const SizedBox(width: 6),

          // 3. Notification Bell with Red Badge
          InkWell(
            onTap: onNotificationPressed,
            borderRadius: BorderRadius.circular(16),
            child: SizedBox(
              width: 34,
              height: 34,
              child: Stack(
                clipBehavior: Clip.none,
                children: [
                  const Center(
                    child: Icon(
                      Icons.notifications,
                      color: Colors.white,
                      size: 23,
                    ),
                  ),
                  Positioned(
                    top: 1,
                    right: 1,
                    child: Container(
                      padding: const EdgeInsets.all(2.5),
                      decoration: BoxDecoration(
                        color: BadGymColors.redAbsent,
                        shape: BoxShape.circle,
                        border: Border.all(
                          color: BadGymColors.purpleCardMid,
                          width: 1.5,
                        ),
                        boxShadow: [
                          BoxShadow(
                            color: BadGymColors.redAbsent.withValues(alpha: 0.5),
                            blurRadius: 4,
                          ),
                        ],
                      ),
                      constraints: const BoxConstraints(
                        minWidth: 15,
                        minHeight: 15,
                      ),
                      alignment: Alignment.center,
                      child: Text(
                        '${data.notificationCount}',
                        style: const TextStyle(
                          fontFamily: BadGymTypography.fontHeading,
                          color: Colors.white,
                          fontSize: 8,
                          fontWeight: FontWeight.w900,
                          height: 1,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),

          // 4. Three Vertical Dots
          InkWell(
            onTap: onMorePressed,
            borderRadius: BorderRadius.circular(14),
            child: const Padding(
              padding: EdgeInsets.symmetric(horizontal: 2.0),
              child: Icon(
                Icons.more_vert,
                color: Colors.white,
                size: 21,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
