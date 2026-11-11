enum MembershipState { active, due, overdue, frozen, cancelled }

class MemberProfile {
  const MemberProfile({required this.id, required this.fullName, required this.age, required this.planName, required this.planPriceMinor, required this.currencyCode, required this.membershipDaysElapsed, required this.membershipDurationDays, required this.state});
  final String id;
  final String fullName;
  final int age;
  final String planName;
  final int planPriceMinor;
  final String currencyCode;
  final int membershipDaysElapsed;
  final int membershipDurationDays;
  final MembershipState state;
  int get daysRemaining => (membershipDurationDays - membershipDaysElapsed).clamp(0, membershipDurationDays);
  double get progress => membershipDurationDays <= 0 ? 0 : (membershipDaysElapsed / membershipDurationDays).clamp(0, 1);
}
