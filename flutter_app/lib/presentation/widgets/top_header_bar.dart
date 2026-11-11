import 'package:flutter/material.dart';
import 'package:bad_gym/core/theme/bad_gym_theme.dart';

class TopHeaderBar extends StatelessWidget {
  final VoidCallback? onMenuPressed;
  final VoidCallback? onNotificationPressed;
  final VoidCallback? onDatePressed;

  const TopHeaderBar({
    super.key,
    this.onMenuPressed,
    this.onNotificationPressed,
    this.onDatePressed,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 10.0, vertical: 6.0),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          // 1. Hamburger menu button
          InkWell(
            onTap: onMenuPressed,
            borderRadius: BorderRadius.circular(10),
            child: Container(
              width: 36,
              height: 36,
              decoration: BoxDecoration(
                color: const Color(0xFF473B3A),
                borderRadius: BorderRadius.circular(10),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withValues(alpha: 0.08),
                    blurRadius: 4,
                    offset: const Offset(0, 2),
                  ),
                ],
              ),
              child: const Icon(
                Icons.menu,
                color: Colors.white,
                size: 19,
              ),
            ),
          ),
          const SizedBox(width: 7),

          // 2. 3D Dumbbell logo
          Image.asset(
            'assets/images/logo_dumbbell.png',
            width: 30,
            height: 24,
            fit: BoxFit.contain,
          ),
          const SizedBox(width: 7),

          // 3. BAD GYM title & subtitle
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(
                'BAD GYM',
                style: BadGymTypography.titleLarge.copyWith(
                  fontSize: 16,
                  height: 1.1,
                ),
              ),
              const Text(
                'Member Intelligence',
                style: TextStyle(
                  fontFamily: BadGymTypography.fontHeading,
                  fontWeight: FontWeight.w600,
                  fontSize: 10,
                  color: BadGymColors.inkSecondary,
                  letterSpacing: -0.2,
                ),
              ),
            ],
          ),

          const Spacer(),

          // 4. Notification icon button with red badge
          InkWell(
            onTap: onNotificationPressed,
            borderRadius: BorderRadius.circular(12),
            child: Container(
              width: 36,
              height: 36,
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(12),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withValues(alpha: 0.05),
                    blurRadius: 6,
                    offset: const Offset(0, 2),
                  ),
                ],
              ),
              child: Stack(
                alignment: Alignment.center,
                children: [
                  const Icon(
                    Icons.notifications,
                    color: Color(0xFF332726),
                    size: 19,
                  ),
                  Positioned(
                    top: 4,
                    right: 5,
                    child: Container(
                      width: 14,
                      height: 14,
                      decoration: const BoxDecoration(
                        color: BadGymColors.coralPrimary,
                        shape: BoxShape.circle,
                      ),
                      alignment: Alignment.center,
                      child: const Text(
                        '1',
                        style: TextStyle(
                          color: Colors.white,
                          fontSize: 8.5,
                          fontWeight: FontWeight.w800,
                          height: 1.0,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
          const SizedBox(width: 6),

          // 5. Date pill capsule
          InkWell(
            onTap: onDatePressed,
            borderRadius: BorderRadius.circular(20),
            child: Container(
              height: 36,
              padding: const EdgeInsets.symmetric(horizontal: 10),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(20),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withValues(alpha: 0.05),
                    blurRadius: 6,
                    offset: const Offset(0, 2),
                  ),
                ],
              ),
              child: const Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Icon(
                    Icons.calendar_month_outlined,
                    color: BadGymColors.coralPrimary,
                    size: 15,
                  ),
                  SizedBox(width: 5),
                  Text(
                    'Thu, 16 Oct 2026',
                    style: TextStyle(
                      fontFamily: BadGymTypography.fontBody,
                      fontWeight: FontWeight.w700,
                      fontSize: 10.5,
                      color: BadGymColors.inkPrimary,
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
