import 'package:flutter/material.dart';

import '../data/payment_repository.dart';
import '../domain/payment_record.dart';

class PaymentScreen extends StatefulWidget {
  const PaymentScreen({super.key, required this.memberId});
  final String memberId;
  @override
  State<PaymentScreen> createState() => _PaymentScreenState();
}

class _PaymentScreenState extends State<PaymentScreen> {
  late final Future<PaymentSummary> _summary;
  @override
  void initState() {
    super.initState();
    _summary = DemoPaymentRepository().getMemberPaymentSummary(memberId: widget.memberId);
  }

  @override
  Widget build(BuildContext context) => FutureBuilder<PaymentSummary>(
    future: _summary,
    builder: (context, snapshot) {
      if (snapshot.hasError) return const Center(child: Text('Payment history could not be loaded.'));
      if (!snapshot.hasData) return const Center(child: CircularProgressIndicator());
      final data = snapshot.data!;
      return ListView(
        padding: const EdgeInsets.fromLTRB(12, 8, 12, 20),
        children: [
          const Row(children: [
            Icon(Icons.account_balance_wallet_rounded, color: _blue),
            SizedBox(width: 8),
            Text('Payment Intelligence', style: TextStyle(fontSize: 18, fontWeight: FontWeight.w900, color: _ink)),
          ]),
          const SizedBox(height: 9),
          _PaymentHero(summary: data),
          const SizedBox(height: 10),
          _PaymentHistory(records: data.records),
          const SizedBox(height: 10),
          _PaymentMethods(records: data.records),
          const SizedBox(height: 10),
          const Text('Demo payment records only. This screen does not collect money or confirm a real transaction.', style: TextStyle(fontSize: 10, color: _muted, height: 1.35)),
        ],
      );
    },
  );
}

const _ink = Color(0xFF1D2450);
const _muted = Color(0xFF72758D);
const _blue = Color(0xFF269DD8);
const _green = Color(0xFF159B68);

BoxDecoration _card() => BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(18), border: Border.all(color: const Color(0xFFE5E0F0)), boxShadow: const [BoxShadow(color: Color(0x10512A8A), blurRadius: 9, offset: Offset(0, 3))]);

class _PaymentHero extends StatelessWidget {
  const _PaymentHero({required this.summary});
  final PaymentSummary summary;
  @override
  Widget build(BuildContext context) {
    final next = summary.nextRenewal;
    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(gradient: const LinearGradient(colors: [Color(0xFFE8F6FC), Colors.white]), borderRadius: BorderRadius.circular(20), border: Border.all(color: const Color(0xFFCDEAF7))),
      child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
        Row(children: [
          Container(width: 42, height: 42, decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(14)), child: const Icon(Icons.verified_rounded, color: _green, size: 25)),
          const SizedBox(width: 10),
          const Expanded(child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
            Text('PAID', style: TextStyle(fontSize: 17, fontWeight: FontWeight.w900, color: _green)),
            Text('Latest payment state · demo', style: TextStyle(fontSize: 10, color: _muted)),
          ])),
          Text(_money(summary.netPaidMinor), style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w900, color: _ink)),
        ]),
        const Divider(height: 22, color: Color(0xFFDCECF4)),
        Row(children: [
          Expanded(child: _Value(label: 'Membership', value: summary.planName)),
          Expanded(child: _Value(label: 'Outstanding', value: _money(summary.outstandingMinor))),
          Expanded(child: _Value(label: 'Next renewal', value: next == null ? '—' : _date(next))),
        ]),
      ]),
    );
  }
}

class _Value extends StatelessWidget {
  const _Value({required this.label, required this.value});
  final String label, value;
  @override
  Widget build(BuildContext context) => Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
    Text(label, style: const TextStyle(fontSize: 9, color: _muted)),
    const SizedBox(height: 3),
    Text(value, maxLines: 1, overflow: TextOverflow.ellipsis, style: const TextStyle(fontSize: 11, fontWeight: FontWeight.w900, color: _ink)),
  ]);
}

class _PaymentHistory extends StatelessWidget {
  const _PaymentHistory({required this.records});
  final List<PaymentRecord> records;
  @override
  Widget build(BuildContext context) => Container(
    padding: const EdgeInsets.all(13),
    decoration: _card(),
    child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
      const Text('PAYMENT HISTORY', style: TextStyle(fontSize: 11, fontWeight: FontWeight.w900, color: _ink, letterSpacing: .5)),
      const SizedBox(height: 6),
      for (final record in records)
        Padding(
          padding: const EdgeInsets.symmetric(vertical: 8),
          child: Row(children: [
            Container(width: 34, height: 34, decoration: BoxDecoration(color: const Color(0xFFE8F6FC), borderRadius: BorderRadius.circular(11)), child: const Icon(Icons.receipt_long_rounded, color: _blue, size: 18)),
            const SizedBox(width: 9),
            Expanded(child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
              Text(_methodName(record.method), style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w800, color: _ink)),
              Text('${_date(record.paidAt)} · ${record.receiptNumber ?? 'No receipt ref'}', style: const TextStyle(fontSize: 9, color: _muted)),
            ])),
            Column(crossAxisAlignment: CrossAxisAlignment.end, children: [
              Text(_money(record.amountMinor), style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w900, color: _ink)),
              Text(record.state.name.toUpperCase(), style: const TextStyle(fontSize: 8, fontWeight: FontWeight.w900, color: _green)),
            ]),
          ]),
        ),
    ]),
  );
}

class _PaymentMethods extends StatelessWidget {
  const _PaymentMethods({required this.records});
  final List<PaymentRecord> records;
  @override
  Widget build(BuildContext context) {
    final counts = <PaymentMethod, int>{};
    for (final record in records) {
      counts.update(record.method, (value) => value + 1, ifAbsent: () => 1);
    }
    return Container(
      padding: const EdgeInsets.all(13),
      decoration: _card(),
      child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
        const Text('PAYMENT METHOD MIX', style: TextStyle(fontSize: 11, fontWeight: FontWeight.w900, color: _ink, letterSpacing: .5)),
        const SizedBox(height: 10),
        for (final entry in counts.entries)
          Padding(
            padding: const EdgeInsets.only(bottom: 7),
            child: Row(children: [
              Expanded(child: Text(_methodName(entry.key), style: const TextStyle(fontSize: 11, color: _ink))),
              Text('${entry.value} transaction(s)', style: const TextStyle(fontSize: 10, color: _muted, fontWeight: FontWeight.w700)),
            ]),
          ),
      ]),
    );
  }
}

String _money(int minor) => '₹${(minor / 100).toStringAsFixed(2)}';
String _date(DateTime date) => '${date.day.toString().padLeft(2, '0')} ${_month(date.month)} ${date.year}';
String _month(int month) => const ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'][month - 1];
String _methodName(PaymentMethod method) => switch (method) { PaymentMethod.upi => 'UPI', PaymentMethod.cash => 'Cash', PaymentMethod.card => 'Card', PaymentMethod.bankTransfer => 'Bank transfer' };
