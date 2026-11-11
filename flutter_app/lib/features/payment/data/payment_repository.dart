import '../domain/payment_record.dart';

abstract interface class PaymentRepository {
  Future<PaymentSummary> getMemberPaymentSummary({required String memberId});
}

/// Preview fixture only; never use this repository for payment collection.
class DemoPaymentRepository implements PaymentRepository {
  @override
  Future<PaymentSummary> getMemberPaymentSummary({required String memberId}) async {
    final now = DateTime.now();
    return PaymentSummary(
      planName: 'Gold Plan',
      outstandingMinor: 0,
      nextRenewal: DateTime(now.year, now.month, now.day).add(const Duration(days: 8)),
      records: [
        PaymentRecord(id: 'DEMO-0012', paidAt: now.subtract(const Duration(days: 22)), amountMinor: 120000, currencyCode: 'INR', method: PaymentMethod.upi, state: PaymentState.paid, receiptNumber: 'BG-DEMO-0012'),
        PaymentRecord(id: 'DEMO-0011', paidAt: now.subtract(const Duration(days: 52)), amountMinor: 120000, currencyCode: 'INR', method: PaymentMethod.cash, state: PaymentState.paid, receiptNumber: 'BG-DEMO-0011'),
        PaymentRecord(id: 'DEMO-0010', paidAt: now.subtract(const Duration(days: 82)), amountMinor: 120000, currencyCode: 'INR', method: PaymentMethod.card, state: PaymentState.paid, receiptNumber: 'BG-DEMO-0010'),
      ],
    );
  }
}
