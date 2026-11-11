import 'package:flutter/material.dart';

abstract final class BadGymColors {
  static const Color background = Color(0xFFFAF2F0);
  static const Color surfaceWhite = Colors.white;
  static const Color surfaceSoft = Color(0xFFFAF7F6);
  static const Color borderSubtle = Color(0xFFF3ECEB);

  // Dark navigation & profile cards
  static const Color darkRail = Color(0xFF3A2F2E);
  static const Color darkRailDivider = Color(0xFF524443);
  static const Color darkCardStart = Color(0xFF7A584F);
  static const Color darkCardMid = Color(0xFF4C3435);
  static const Color darkCardEnd = Color(0xFF382728);

  // Accents
  static const Color coralPrimary = Color(0xFFED5B54);
  static const Color coralDark = Color(0xFFE0453E);
  static const Color emerald = Color(0xFF16A34A);
  static const Color emeraldActive = Color(0xFF15A049);
  static const Color limeProgressStart = Color(0xFF6DE338);
  static const Color limeProgressEnd = Color(0xFF00D26A);
  static const Color gold = Color(0xFFF9B834);
  static const Color goldDark = Color(0xFF362208);
  static const Color yellowLate = Color(0xFFF59E0B);
  static const Color redAbsent = Color(0xFFEF4444);
  static const Color purpleHoliday = Color(0xFF8B5CF6);

  // Ink / Typography
  static const Color inkNavy = Color(0xFF0E152A);
  static const Color inkPrimary = Color(0xFF1A202C);
  static const Color inkSecondary = Color(0xFF4A5568);
  static const Color inkMuted = Color(0xFF718096);
  static const Color inkLight = Color(0xFFDDD6D5);
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
