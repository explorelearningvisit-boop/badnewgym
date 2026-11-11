enum PaymentState { paid, partial, due, overdue, refunded, waived }
enum PaymentMethod { upi, cash, card, bankTransfer }

class PaymentRecord {
  const PaymentRecord({required this.id, required this.paidAt, required this.amountMinor, required this.currencyCode, required this.method, required this.state, this.receiptNumber});
  final String id;
  final DateTime paidAt;
  final int amountMinor;
  final String currencyCode;
  final PaymentMethod method;
  final PaymentState state;
  final String? receiptNumber;
}

class PaymentSummary {
  const PaymentSummary({required this.records, required this.outstandingMinor, required this.nextRenewal, required this.planName});
  final List<PaymentRecord> records;
  final int outstandingMinor;
  final DateTime? nextRenewal;
  final String planName;
  int get netPaidMinor => records.where((r) => r.state == PaymentState.paid || r.state == PaymentState.partial).fold(0, (sum, r) => sum + r.amountMinor);
}
