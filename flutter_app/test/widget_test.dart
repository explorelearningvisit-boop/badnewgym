import 'package:flutter_test/flutter_test.dart';
import 'package:bad_gym/main.dart';

void main() {
  testWidgets('renders BAD GYM member overview', (tester) async {
    await tester.pumpWidget(const BadGymApp());
    expect(find.text('BAD GYM'), findsOneWidget);
    expect(find.text('Ashwani Kumar Singh'), findsOneWidget);
    expect(find.text('MEMBER JOURNEY'), findsOneWidget);
  });

  testWidgets('opens attendance detail from bottom navigation', (tester) async {
    await tester.pumpWidget(const BadGymApp());
    await tester.tap(find.text('Attendance').last);
    await tester.pumpAndSettle();
    expect(find.text('Attendance Intelligence'), findsOneWidget);
  });
}
