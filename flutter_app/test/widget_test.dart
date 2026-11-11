import 'package:flutter_test/flutter_test.dart';
import 'package:bad_gym/main.dart';
import 'package:bad_gym/presentation/widgets/vertical_navigation_rail.dart';

void main() {
  testWidgets('renders BAD GYM Member Intelligence screen with all core components', (tester) async {
    await tester.pumpWidget(const BadGymApp());
    await tester.pumpAndSettle();

    // Verify Header
    expect(find.text('BAD GYM'), findsOneWidget);
    expect(find.text('Member Intelligence'), findsOneWidget);
    expect(find.text('Thu, 16 Oct 2026'), findsOneWidget);

    // Verify Member Profile Card
    expect(find.text('Ashwani Kumar Singh'), findsOneWidget);
    expect(find.textContaining('BG215'), findsOneWidget);
    expect(find.text('Active'), findsOneWidget);
    expect(find.text('Gold Plan'), findsOneWidget);
    expect(find.text('8 Days Left'), findsOneWidget);
    expect(find.textContaining('73.3% Used'), findsOneWidget);

    // Verify Quick Stats
    expect(find.text('81%'), findsWidgets);
    expect(find.text('58 min'), findsOneWidget);
    expect(find.text('Paid'), findsOneWidget);
    expect(find.text('69.5 kg'), findsOneWidget);

    // Verify Attendance Intelligence Card
    expect(find.text('Attendance Intelligence'), findsOneWidget);
    expect(find.text('October 2026'), findsWidgets);
    expect(find.text('Present'), findsOneWidget);
    expect(find.text('Late'), findsOneWidget);
    expect(find.text('Absent'), findsOneWidget);
    expect(find.text('Holiday'), findsOneWidget);
    expect(find.text('6 Days'), findsOneWidget);
    expect(find.text('14 Days'), findsOneWidget);

    // Verify Attendance Trend Chart
    expect(find.text('Attendance Trend'), findsOneWidget);
    expect(find.text('Maximum'), findsOneWidget);
    expect(find.text('Average'), findsOneWidget);
    expect(find.text('Minimum'), findsOneWidget);
  });

  testWidgets('taps rail navigation tab', (tester) async {
    await tester.pumpWidget(const BadGymApp());
    await tester.pumpAndSettle();

    // Tap Workout rail item
    await tester.tap(find.text('Workout').first);
    await tester.pumpAndSettle();

    // Verify state updated
    expect(find.byType(VerticalNavigationRail), findsOneWidget);
  });
}
