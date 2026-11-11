import 'package:flutter_test/flutter_test.dart';
import 'package:bad_gym/core/auth/app_role.dart';

void main() {
  group('RolePolicy', () {
    test('owner can manage gym and operational records', () {
      expect(RolePolicy.allows(AppRole.owner, AppPermission.manageGymSettings), isTrue);
      expect(RolePolicy.allows(AppRole.owner, AppPermission.manageMembers), isTrue);
      expect(RolePolicy.allows(AppRole.owner, AppPermission.managePayments), isTrue);
      expect(RolePolicy.allows(AppRole.owner, AppPermission.viewAllFinancialReports), isTrue);
    });

    test('member can read own records but cannot manage gym or finances', () {
      expect(RolePolicy.allows(AppRole.member, AppPermission.viewOwnProfile), isTrue);
      expect(RolePolicy.allows(AppRole.member, AppPermission.viewOwnAttendance), isTrue);
      expect(RolePolicy.allows(AppRole.member, AppPermission.viewOwnPayments), isTrue);
      expect(RolePolicy.allows(AppRole.member, AppPermission.manageGymSettings), isFalse);
      expect(RolePolicy.allows(AppRole.member, AppPermission.managePayments), isFalse);
      expect(RolePolicy.allows(AppRole.member, AppPermission.viewAllFinancialReports), isFalse);
    });
  });
}
