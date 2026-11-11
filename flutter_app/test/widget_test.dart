import 'package:flutter_test/flutter_test.dart';
import 'package:bad_gym/main.dart';

void main() {
  testWidgets('renders BAD GYM member overview', (tester) async {
    await tester.pumpWidget(const BadGymApp());
    expect(find.text('BAD GYM'), findsOneWidget);
    expect(find.text('Ashwani Kumar Singh'), findsOneWidget);
    expect(find.text('THE ROAD TO FITNESS SUCCESS'), findsOneWidget);
  });

  testWidgets('opens Attendance intelligence', (tester) async {
    await tester.pumpWidget(const BadGymApp());
    await tester.tap(find.text('Attendance').last);
    await tester.pumpAndSettle();
    expect(find.text('Attendance Intelligence'), findsOneWidget);
    expect(find.text('MONTHLY CALENDAR'), findsOneWidget);
  });

  testWidgets('opens Workout intelligence', (tester) async {
    await tester.pumpWidget(const BadGymApp());
    await tester.tap(find.text('Workout').last);
    await tester.pumpAndSettle();
    expect(find.text('Workout Intelligence'), findsOneWidget);
    expect(find.text('RECENT SESSIONS'), findsOneWidget);
  });

  testWidgets('opens Payment intelligence', (tester) async {
    await tester.pumpWidget(const BadGymApp());
    await tester.tap(find.text('Payment').last);
    await tester.pumpAndSettle();
    expect(find.text('Payment Intelligence'), findsOneWidget);
    expect(find.text('PAYMENT HISTORY'), findsOneWidget);
  });

  testWidgets('opens Body intelligence', (tester) async {
    await tester.pumpWidget(const BadGymApp());
    await tester.tap(find.text('Body').last);
    await tester.pumpAndSettle();
    expect(find.text('Body Intelligence'), findsOneWidget);
    expect(find.text('BODY MEASUREMENTS'), findsOneWidget);
  });
}

  testWidgets('swipes horizontally between intelligence modules', (tester) async {
    await tester.pumpWidget(const BadGymApp());
    await tester.drag(find.byKey(const ValueKey('overview')), const Offset(-320, 0));
    await tester.pumpAndSettle();
    expect(find.text('Attendance Intelligence'), findsOneWidget);
  });
