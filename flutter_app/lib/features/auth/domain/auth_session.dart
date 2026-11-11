import '../../../core/auth/app_role.dart';

/// Authenticated identity context returned by a trusted auth provider.
/// This model contains identifiers only; secrets/tokens belong in secure storage.
class AuthSession {
  const AuthSession({
    required this.userId,
    required this.gymId,
    required this.role,
    required this.issuedAt,
    this.memberId,
  });

  final String userId;
  final String gymId;
  final String? memberId;
  final AppRole role;
  final DateTime issuedAt;

  bool get isMemberLinked => memberId != null && memberId!.isNotEmpty;
}

class AuthFailure implements Exception {
  const AuthFailure(this.code, this.message);

  final String code;
  final String message;

  @override
  String toString() => 'AuthFailure($code): $message';
}
