import 'package:flutter/material.dart';
import 'package:bad_gym/core/theme/bad_gym_theme.dart';
import 'package:bad_gym/presentation/member_checkin_screen.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const BadGymApp());
}

class BadGymApp extends StatelessWidget {
  const BadGymApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'BAD GYM — Member Intelligence',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        scaffoldBackgroundColor: BadGymColors.background,
        fontFamily: BadGymTypography.fontBody,
        colorScheme: ColorScheme.fromSeed(
          seedColor: BadGymColors.coralPrimary,
          primary: BadGymColors.coralPrimary,
          surface: BadGymColors.background,
        ),
      ),
      home: const MemberCheckInScreen(),
    );
  }
}
