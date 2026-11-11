import 'package:flutter/material.dart';
import 'package:bad_gym/core/models/member_intelligence_data.dart';
import 'package:bad_gym/core/theme/bad_gym_theme.dart';

class AttendanceTrendChart extends StatelessWidget {
  final List<TrendDataPoint> points;

  const AttendanceTrendChart({
    super.key,
    required this.points,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisSize: MainAxisSize.min,
      children: [
        // Header
        Row(
          children: [
            Container(
              padding: const EdgeInsets.all(4),
              decoration: BoxDecoration(
                color: BadGymColors.coralPrimary,
                borderRadius: BorderRadius.circular(6),
              ),
              child: const Icon(
                Icons.trending_up,
                size: 14,
                color: Colors.white,
              ),
            ),
            const SizedBox(width: 6),
            const Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(
                  'Attendance Trend',
                  style: TextStyle(
                    fontFamily: BadGymTypography.fontHeading,
                    fontWeight: FontWeight.w800,
                    fontSize: 12.5,
                    color: BadGymColors.inkPrimary,
                    height: 1.1,
                  ),
                ),
                Text(
                  'Daily Attendance – Oct 2026',
                  style: TextStyle(
                    fontFamily: BadGymTypography.fontBody,
                    fontWeight: FontWeight.w500,
                    fontSize: 9,
                    color: BadGymColors.inkMuted,
                    height: 1.1,
                  ),
                ),
              ],
            ),
          ],
        ),

        const SizedBox(height: 8),

        // Chart Canvas with axes
        SizedBox(
          height: 110,
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              // Y-Axis labels
              const Column(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                  Text('100%', style: _axisStyle),
                  Text('75%', style: _axisStyle),
                  Text('50%', style: _axisStyle),
                  Text('25%', style: _axisStyle),
                  Text('0%', style: _axisStyle),
                  SizedBox(height: 14), // space for X-axis baseline
                ],
              ),
              const SizedBox(width: 4),

              // Canvas + X-Axis labels
              Expanded(
                child: Column(
                  children: [
                    // Spline canvas with guidelines
                    Expanded(
                      child: Stack(
                        clipBehavior: Clip.none,
                        children: [
                          CustomPaint(
                            size: Size.infinite,
                            painter: _TrendChartPainter(points: points),
                          ),
                          // Callout Badges
                          _buildCallout(
                            leftFraction: 0.16,
                            topOffset: 0,
                            text: '97% (3 Oct)',
                            isGreen: true,
                          ),
                          _buildCallout(
                            leftFraction: 0.52,
                            topOffset: 48,
                            text: '58% (17 Oct)',
                            isGreen: false,
                          ),
                          _buildCallout(
                            leftFraction: 0.88,
                            topOffset: 8,
                            text: '81% (31 Oct)',
                            isGreen: false,
                          ),
                        ],
                      ),
                    ),

                    // X-Axis labels row
                    const SizedBox(height: 4),
                    const Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text('1', style: _axisStyle),
                        Text('5', style: _axisStyle),
                        Text('10', style: _axisStyle),
                        Text('15', style: _axisStyle),
                        Text('20', style: _axisStyle),
                        Text('25', style: _axisStyle),
                        Text('31', style: _axisStyle),
                      ],
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),

        const SizedBox(height: 8),

        // Bottom stats row
        Container(
          padding: const EdgeInsets.symmetric(vertical: 6, horizontal: 8),
          decoration: BoxDecoration(
            color: const Color(0xFFFAF8F7),
            borderRadius: BorderRadius.circular(10),
          ),
          child: Row(
            children: [
              // Maximum
              const Expanded(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(
                          Icons.arrow_upward,
                          size: 13,
                          color: BadGymColors.emerald,
                        ),
                        SizedBox(width: 2),
                        Text(
                          '97%',
                          style: TextStyle(
                            fontFamily: BadGymTypography.fontHeading,
                            fontSize: 12,
                            fontWeight: FontWeight.w800,
                            color: BadGymColors.inkPrimary,
                          ),
                        ),
                      ],
                    ),
                    Text(
                      'Maximum',
                      style: TextStyle(
                        fontFamily: BadGymTypography.fontBody,
                        fontSize: 8.5,
                        fontWeight: FontWeight.w500,
                        color: BadGymColors.inkMuted,
                      ),
                    ),
                  ],
                ),
              ),
              Container(width: 1, height: 20, color: const Color(0xFFE5DFDE)),

              // Average
              const Expanded(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(
                          Icons.bar_chart,
                          size: 13,
                          color: Color(0xFF3B82F6),
                        ),
                        SizedBox(width: 2),
                        Text(
                          '81%',
                          style: TextStyle(
                            fontFamily: BadGymTypography.fontHeading,
                            fontSize: 12,
                            fontWeight: FontWeight.w800,
                            color: BadGymColors.inkPrimary,
                          ),
                        ),
                      ],
                    ),
                    Text(
                      'Average',
                      style: TextStyle(
                        fontFamily: BadGymTypography.fontBody,
                        fontSize: 8.5,
                        fontWeight: FontWeight.w500,
                        color: BadGymColors.inkMuted,
                      ),
                    ),
                  ],
                ),
              ),
              Container(width: 1, height: 20, color: const Color(0xFFE5DFDE)),

              // Minimum
              const Expanded(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(
                          Icons.arrow_downward,
                          size: 13,
                          color: BadGymColors.coralPrimary,
                        ),
                        SizedBox(width: 2),
                        Text(
                          '58%',
                          style: TextStyle(
                            fontFamily: BadGymTypography.fontHeading,
                            fontSize: 12,
                            fontWeight: FontWeight.w800,
                            color: BadGymColors.inkPrimary,
                          ),
                        ),
                      ],
                    ),
                    Text(
                      'Minimum',
                      style: TextStyle(
                        fontFamily: BadGymTypography.fontBody,
                        fontSize: 8.5,
                        fontWeight: FontWeight.w500,
                        color: BadGymColors.inkMuted,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }

  static const TextStyle _axisStyle = TextStyle(
    fontFamily: BadGymTypography.fontBody,
    fontSize: 8.5,
    fontWeight: FontWeight.w600,
    color: BadGymColors.inkPrimary,
  );

  Widget _buildCallout({
    required double leftFraction,
    required double topOffset,
    required String text,
    required bool isGreen,
  }) {
    return Positioned(
      left: leftFraction * 200,
      top: topOffset,
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 5, vertical: 2),
        decoration: BoxDecoration(
          color: isGreen ? BadGymColors.emerald : BadGymColors.coralPrimary,
          borderRadius: BorderRadius.circular(6),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withValues(alpha: 0.12),
              blurRadius: 4,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Text(
          text,
          style: const TextStyle(
            fontFamily: BadGymTypography.fontBody,
            fontSize: 7.8,
            fontWeight: FontWeight.w800,
            color: Colors.white,
          ),
        ),
      ),
    );
  }
}

class _TrendChartPainter extends CustomPainter {
  final List<TrendDataPoint> points;

  const _TrendChartPainter({required this.points});

  @override
  void paint(Canvas canvas, Size size) {
    if (size.width <= 0 || size.height <= 0) return;

    // 1. Draw horizontal guidelines (for 100%, 75%, 50%, 25%, 0%)
    final gridPaint = Paint()
      ..color = const Color(0xFFF1EAE8)
      ..strokeWidth = 1.0;

    for (int i = 0; i < 5; i++) {
      final y = size.height * (i / 4.0);
      canvas.drawLine(Offset(0, y), Offset(size.width, y), gridPaint);
    }

    if (points.isEmpty) return;

    // 2. Map data points to pixel coordinates
    // days range 1 to 31, percentage ranges 0 to 100
    final offsets = <Offset>[];
    for (final pt in points) {
      final x = ((pt.day - 1) / 30.0) * size.width;
      final y = size.height * (1.0 - (pt.percentage / 100.0));
      offsets.add(Offset(x, y));
    }

    // 3. Build smooth bezier path
    final path = Path();
    path.moveTo(offsets[0].dx, offsets[0].dy);

    for (int i = 0; i < offsets.length - 1; i++) {
      final p0 = offsets[i];
      final p1 = offsets[i + 1];
      final controlX1 = p0.dx + (p1.dx - p0.dx) / 2;
      final controlY1 = p0.dy;
      final controlX2 = p0.dx + (p1.dx - p0.dx) / 2;
      final controlY2 = p1.dy;

      path.cubicTo(controlX1, controlY1, controlX2, controlY2, p1.dx, p1.dy);
    }

    // 4. Fill gradient area under curve
    final fillPath = Path.from(path)
      ..lineTo(size.width, size.height)
      ..lineTo(0, size.height)
      ..close();

    final fillPaint = Paint()
      ..shader = LinearGradient(
        begin: Alignment.topCenter,
        end: Alignment.bottomCenter,
        colors: [
          BadGymColors.coralPrimary.withValues(alpha: 0.28),
          BadGymColors.coralPrimary.withValues(alpha: 0.02),
        ],
      ).createShader(Rect.fromLTWH(0, 0, size.width, size.height));

    canvas.drawPath(fillPath, fillPaint);

    // 5. Draw spline line
    final linePaint = Paint()
      ..color = BadGymColors.coralPrimary
      ..strokeWidth = 2.2
      ..style = PaintingStyle.stroke
      ..strokeCap = StrokeCap.round
      ..strokeJoin = StrokeJoin.round;

    canvas.drawPath(path, linePaint);

    // 6. Draw dots on data points
    final dotFillPaint = Paint()..color = Colors.white;
    final dotBorderPaint = Paint()
      ..color = BadGymColors.coralPrimary
      ..strokeWidth = 2.0
      ..style = PaintingStyle.stroke;

    for (final offset in offsets) {
      canvas.drawCircle(offset, 3.0, dotFillPaint);
      canvas.drawCircle(offset, 3.0, dotBorderPaint);
    }
  }

  @override
  bool shouldRepaint(covariant _TrendChartPainter oldDelegate) => true;
}
