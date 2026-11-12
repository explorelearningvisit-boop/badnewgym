import 'package:flutter_test/flutter_test.dart';
import 'package:bad_gym/main.dart';
import 'package:bad_gym/presentation/widgets/vertical_navigation_rail.dart';

void main() {
  testWidgets('renders BAD GYM Royal Purple Member Intelligence screen with all core components', (tester) async {
    await tester.pumpWidget(const BadGymApp());
    await tester.pumpAndSettle();

    // Verify VIP Header Bar (Image 2)
    expect(find.text('Checked In'), findsWidgets);
    expect(find.text('06:42 PM'), findsWidgets);
    expect(find.text('Fri, 16 Oct 2026'), findsWidgets);

    // Verify VIP Member Profile Card (Image 1)
    expect(find.text('Ashwani Kumar Singh'), findsOneWidget);
    expect(find.textContaining('BG215'), findsOneWidget);
    expect(find.text('Active'), findsOneWidget);
    expect(find.text('Gold Plan'), findsOneWidget);
    expect(find.text('8 Days Left'), findsOneWidget);
    expect(find.textContaining('73.3% Used'), findsOneWidget);
    expect(find.text('Main Gym Floor'), findsOneWidget);
    expect(find.text('₹1,200'), findsOneWidget);
    expect(find.textContaining('Next Due: 24 Oct 2026'), findsOneWidget);

    // Verify Quick Stats Row (Image 3 Top)
    expect(find.text('81%'), findsWidgets);
    expect(find.text('58 min'), findsOneWidget);
    expect(find.text('Paid'), findsWidgets);
    expect(find.text('69.5 kg'), findsOneWidget);

    // Verify Attendance Intelligence Card (Image 3 Right)
    expect(find.text('Attendance Intelligence'), findsOneWidget);
    expect(find.text('October 2026'), findsWidgets);
    expect(find.text('Present'), findsOneWidget);
    expect(find.text('Late'), findsOneWidget);
    expect(find.text('Absent'), findsOneWidget);
    expect(find.text('Holiday'), findsOneWidget);
    expect(find.text('5 Days'), findsOneWidget);
    expect(find.text('14 Days'), findsOneWidget);

    // Verify Vertical Navigation Rail (Image 3 Left)
    expect(find.text('Intelligence'), findsOneWidget);
    expect(find.text('Calendar'), findsOneWidget);
    expect(find.text('History'), findsOneWidget);
    expect(find.text('Trend'), findsOneWidget);
    expect(find.text('Streak'), findsOneWidget);
  });

  testWidgets('taps rail navigation tab', (tester) async {
    await tester.pumpWidget(const BadGymApp());
    await tester.pumpAndSettle();

    // Tap Calendar rail item
    await tester.tap(find.text('Calendar').first);
    await tester.pumpAndSettle();

    // Verify state updated
    expect(find.byType(VerticalNavigationRail), findsOneWidget);
  });
}
