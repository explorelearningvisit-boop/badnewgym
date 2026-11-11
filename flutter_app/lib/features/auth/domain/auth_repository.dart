import 'auth_session.dart';

/// Provider-neutral contract. Implement only after the real auth provider,
/// tenant model, token lifecycle and account recovery rules are confirmed.
abstract interface class AuthRepository {
  Future<AuthSession?> restoreSession();
  Future<AuthSession> signIn({required String identifier, required String password});
  Future<void> signOut();
}
