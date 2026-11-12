import 'package:flutter/material.dart';

abstract final class BadGymColors {
  // Screen Background: Soft luxury lavender / pearl
  static const Color background = Color(0xFFF4EEFD);
  static const Color surfaceWhite = Colors.white;
  static const Color surfaceSoft = Color(0xFFF8F5FE);
  static const Color borderSubtle = Color(0xFFE9E0F8);

  // Royal Purple Surfaces & Cards
  static const Color purpleDark = Color(0xFF1A0234);
  static const Color purpleRail = Color(0xFF1E033A);
  static const Color purpleRailDivider = Color(0xFF3B1563);
  static const Color purpleCardStart = Color(0xFF380B61);
  static const Color purpleCardMid = Color(0xFF250645);
  static const Color purpleCardEnd = Color(0xFF160032);
  static const Color purpleInnerGlass = Color(0xFF130125);

  // Neon Glow Accents
  static const Color neonViolet = Color(0xFFA855F7);
  static const Color neonVioletGlow = Color(0x66A855F7);
  static const Color neonVioletBorder = Color(0xFFB066FE);
  static const Color neonLime = Color(0xFF22C55E);
  static const Color neonLimeGlow = Color(0xFF4ADE80);

  // Status & Badges
  static const Color emerald = Color(0xFF22C55E);
  static const Color emeraldActive = Color(0xFF16A34A);
  static const Color limeProgressStart = Color(0xFF22C55E);
  static const Color limeProgressEnd = Color(0xFF4ADE80);
  static const Color gold = Color(0xFFF59E0B);
  static const Color goldDark = Color(0xFFD97706);
  static const Color yellowLate = Color(0xFFF59E0B);
  static const Color redAbsent = Color(0xFFEF4444);
  static const Color purpleHoliday = Color(0xFFA855F7);
  static const Color cyanBody = Color(0xFF06B6D4);
  static const Color coralPrimary = Color(0xFFEF4444);

  // Dark navigation & legacy aliases
  static const Color darkRail = Color(0xFF1E033A);
  static const Color darkRailDivider = Color(0xFF3B1563);
  static const Color darkCardStart = Color(0xFF380B61);
  static const Color darkCardMid = Color(0xFF250645);
  static const Color darkCardEnd = Color(0xFF160032);

  // Typography & Inks
  static const Color inkNavy = Color(0xFF140228);
  static const Color inkPrimary = Color(0xFF1E1035);
  static const Color inkSecondary = Color(0xFF4B3D60);
  static const Color inkMuted = Color(0xFF7A6B90);
  static const Color inkLight = Color(0xFFDDD6FE);
  static const Color inkLavender = Color(0xFFC4B5FD);
}

abstract final class BadGymTypography {
  static const String fontHeading = 'Poppins';
  static const String fontBody = 'Poppins';

  static TextStyle titleLarge = const TextStyle(
    fontFamily: fontHeading,
    fontWeight: FontWeight.w900,
    fontSize: 18,
    color: BadGymColors.inkNavy,
    letterSpacing: 0.3,
  );

  static TextStyle titleMedium = const TextStyle(
    fontFamily: fontHeading,
    fontWeight: FontWeight.w800,
    fontSize: 14.5,
    color: BadGymColors.inkPrimary,
  );

  static TextStyle cardTitle = const TextStyle(
    fontFamily: fontHeading,
    fontWeight: FontWeight.w700,
    fontSize: 14.5,
    color: Colors.white,
  );

  static TextStyle bodySmall = const TextStyle(
    fontFamily: fontBody,
    fontWeight: FontWeight.w500,
    fontSize: 10,
    color: BadGymColors.inkMuted,
  );

  static TextStyle caption = const TextStyle(
    fontFamily: fontBody,
    fontWeight: FontWeight.w600,
    fontSize: 9,
    color: BadGymColors.inkLight,
  );
}
