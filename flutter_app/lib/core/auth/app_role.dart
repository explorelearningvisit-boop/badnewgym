/// Roles supported by the first BAD GYM release scope.
/// Staff and trainer roles can be added when their workflows are approved.
enum AppRole { owner, member }

enum AppPermission {
  viewGymDashboard,
  manageGymSettings,
  viewAllMembers,
  manageMembers,
  viewOwnProfile,
  viewOwnAttendance,
  manageAttendance,
  viewOwnWorkouts,
  manageWorkouts,
  viewOwnPayments,
  managePayments,
  viewOwnBodyMeasurements,
  manageOwnBodyMeasurements,
  viewAllFinancialReports,
}

/// Client-side capability map for navigation and presentation only.
/// The backend must independently enforce every permission and row scope.
abstract final class RolePolicy {
  static const Set<AppPermission> _ownerPermissions = {
    AppPermission.viewGymDashboard,
    AppPermission.manageGymSettings,
    AppPermission.viewAllMembers,
    AppPermission.manageMembers,
    AppPermission.viewOwnProfile,
    AppPermission.viewOwnAttendance,
    AppPermission.manageAttendance,
    AppPermission.viewOwnWorkouts,
    AppPermission.manageWorkouts,
    AppPermission.viewOwnPayments,
    AppPermission.managePayments,
    AppPermission.viewOwnBodyMeasurements,
    AppPermission.manageOwnBodyMeasurements,
    AppPermission.viewAllFinancialReports,
  };

  static const Set<AppPermission> _memberPermissions = {
    AppPermission.viewOwnProfile,
    AppPermission.viewOwnAttendance,
    AppPermission.viewOwnWorkouts,
    AppPermission.viewOwnPayments,
    AppPermission.viewOwnBodyMeasurements,
    AppPermission.manageOwnBodyMeasurements,
  };

  static Set<AppPermission> permissionsFor(AppRole role) => switch (role) {
        AppRole.owner => _ownerPermissions,
        AppRole.member => _memberPermissions,
      };

  static bool allows(AppRole role, AppPermission permission) =>
      permissionsFor(role).contains(permission);
}
