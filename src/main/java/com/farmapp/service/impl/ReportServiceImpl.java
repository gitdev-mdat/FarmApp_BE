        package com.farmapp.service.impl;

        import com.farmapp.dto.response.Report.EventDTO;
        import com.farmapp.dto.response.Report.ReportTransactionResponseDTO;
        import com.farmapp.dto.response.Report.SummaryDTO;
        import com.farmapp.enums.SellType;
        import com.farmapp.enums.TransactionType;
        import com.farmapp.helper.MoneyUtil;
        import com.farmapp.model.*;
        import com.farmapp.repository.*;
        import com.farmapp.service.interfaces.ReportService;
        import lombok.RequiredArgsConstructor;
        import org.springframework.stereotype.Service;

        import java.math.BigDecimal;
        import java.time.LocalDate;
        import java.time.format.DateTimeFormatter;
        import java.util.ArrayList;
        import java.util.Comparator;
        import java.util.List;
        import java.util.Objects;
        import java.util.stream.Collectors;

        @Service
        @RequiredArgsConstructor
        public class ReportServiceImpl implements ReportService {
            private final DepositRepository depositRepository;
            private final CloseRepository closeRepository;
            private final SellRepository sellRepository;
            private final DeliveryRepository deliveryRepository;
            private final PaymentRepository paymentRepository;

            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


            @Override
            public ReportTransactionResponseDTO getTransactionReport(Integer farmerId, Integer seasonId) {
                // 1. Lấy dữ liệu
                List<Deposit> deposits = depositRepository.findByFarmerIdAndSeasonIdAndIsActiveIsTrue(farmerId, seasonId);
                List<Close> closes = closeRepository.findByFarmerIdAndSeasonIdAndIsActiveIsTrue(farmerId, seasonId);
                List<Sell> sells = sellRepository.findByFarmerIdAndSeasonIdAndIsActiveIsTrue(farmerId, seasonId);
                List<Delivery> deliveries = deliveryRepository.findByFarmerIdAndSeasonIdAndIsActiveIsTrueAndDeliveredAtNotNull(farmerId, seasonId);
                List<Payment> payments = paymentRepository.findByFarmerIdAndSeasonIdAndIsActiveIsTrueAndPaymentDateNotNull(farmerId, seasonId);

                // 2. Mapping sự kiện
                List<EventDTO> events = buildEventList(deposits, closes, sells, deliveries, payments);

                // 3. Tính tổng hợp
                List<SummaryDTO> summary = buildSummary(deposits, closes, sells, deliveries, payments);

                // 4. Trả kết quả
                return new ReportTransactionResponseDTO(events, summary);
            }

            private List<EventDTO> buildEventList(List<Deposit> deposits,
                                                  List<Close> closes,
                                                  List<Sell> sells,
                                                  List<Delivery> deliveries,
                                                  List<Payment> payments) {

                List<EventDTO> events = new ArrayList<>();

                for (Deposit d : deposits) {
                    if (d == null || d.getCreatedAt() == null || d.getProduct() == null) continue;
                    events.add(new EventDTO(
                            d.getCreatedAt().format(formatter),
                            "Ký gửi",
                            String.format("Đã ký gửi %s kg %s (Mã đơn: %s)",
                                    d.getTotalQuantity(),
                                    d.getProduct().getName(),
                                    d.getId())
                    ));
                }

                for (Close c : closes) {
                    if (c == null || c.getCreatedAt() == null || c.getProduct() == null) continue;
                    events.add(new EventDTO(
                            c.getCreatedAt().format(formatter),
                            "Chốt giá",
                            String.format("Đã chốt giá %s/kg cho %s kg %s (Mã đơn: %s)",
                                    MoneyUtil.format(c.getPrice()),
                                    c.getTotalQuantity(),
                                    c.getProduct().getName(),
                                    c.getId())
                    ));
                }

                for (Sell s : sells) {
                    if (s == null || s.getCreatedAt() == null || s.getProduct() == null) continue;
                    String depositInfo = s.getDeposit() != null ? s.getDeposit().getId() : "bán trực tiếp";

                    events.add(new EventDTO(
                            s.getCreatedAt().format(formatter),
                            s.getSellType().getDisplayName() ,
                            String.format("Bán %s kg %s giá %s/kg từ đơn %s",
                                    s.getTotalQuantity(),
                                    s.getProduct().getName(),
                                    MoneyUtil.format(s.getPrice()),
                                    depositInfo)
                    ));
                }

                for (Delivery d : deliveries) {
                    if (d == null || d.getProduct() == null) continue;
                    String date = d.getDeliveredAt() != null ? d.getDeliveredAt().format(formatter) : "Không rõ ngày giao";
                    String closeId = (d.getClose() != null) ? d.getClose().getId() : "N/A";

                    events.add(new EventDTO(
                            date,
                            "Giao hàng",
                            String.format("Đã giao %s kg %s (Giao cho đơn chốt %s)",
                                    d.getDeliveredQuantity(),
                                    d.getProduct().getName(),
                                    closeId)
                    ));
                }

                for (Payment p : payments) {
                    if (p == null || p.getProduct() == null) continue;
                    String quantity = "?";
                    String productName = p.getProduct().getName();

                    if (p.getTransactionType() == TransactionType.CLOSE) {
                        quantity = closeRepository.findById(p.getTransactionId())
                                .map(Close::getTotalQuantity)
                                .map(String::valueOf)
                                .orElse("?");
                    } else if (p.getTransactionType() == TransactionType.SELL_FROM_DEPOSIT
                            || p.getTransactionType() == TransactionType.SELL_DIRECT) {
                        quantity = sellRepository.findById(p.getTransactionId())
                                .map(Sell::getTotalQuantity)
                                .map(String::valueOf)
                                .orElse("?");
                    }

                    String paymentDateStr = (p.getPaymentDate() != null)
                            ? p.getPaymentDate().format(formatter)
                            : "Chưa thanh toán";

                    events.add(new EventDTO(
                            paymentDateStr,
                            "Thanh toán",
                            String.format("Đã được thanh toán %s cho %s kg %s",
                                    MoneyUtil.format(p.getPaymentAmount()),
                                    quantity,
                                    productName)
                    ));
                }

                // Sắp xếp theo ngày hợp lệ, đẩy ngày không parse được xuống cuối
                return events.stream()
                        .sorted(Comparator.comparing(
                                e -> parseDateOrNull(e.getDate()),
                                Comparator.nullsLast(Comparator.naturalOrder())
                        ))
                        .collect(Collectors.toList());
            }

            private LocalDate parseDateOrNull(String dateStr) {
                try {
                    return LocalDate.parse(dateStr, formatter);
                } catch (Exception e) {
                    return null;
                }
            }


            private List<SummaryDTO> buildSummary(List<Deposit> deposits,
                                                  List<Close> closes,
                                                  List<Sell> sells,
                                                  List<Delivery> deliveries,
                                                  List<Payment> payments) {

                float totalDeposit = deposits.stream()
                        .map(Deposit::getTotalQuantity)
                        .filter(Objects::nonNull)
                        .reduce(0f, Float::sum);

                float totalClosed = closes.stream()
                        .map(Close::getTotalQuantity)
                        .filter(Objects::nonNull)
                        .reduce(0f, Float::sum);

                float totalSold = sells.stream()
                        .map(Sell::getTotalQuantity)
                        .filter(Objects::nonNull)
                        .reduce(0f, Float::sum);
                float totalSoldFromDeposit = sells.stream()
                        .filter(s -> s.getSellType() == SellType.FROM_DEPOSIT && s.getTotalQuantity() != null)
                        .map(Sell::getTotalQuantity)
                        .reduce(0f, Float::sum);

                float totalDelivered = deliveries.stream()
                        .map(Delivery::getDeliveredQuantity)
                        .filter(Objects::nonNull)
                        .reduce(0f, Float::sum);

                int totalPaid = payments.stream()
                        .map(Payment::getPaymentAmount)
                        .filter(Objects::nonNull)
                        .mapToInt(Integer::intValue)
                        .sum();

                int totalDue = (int) Math.round(
                        sells.stream()
                                .filter(s -> s.getPrice() != null && s.getTotalQuantity() != null)
                                .mapToDouble(s -> s.getPrice() * s.getTotalQuantity())
                                .sum()
                                +
                                closes.stream()
                                        .filter(c -> c.getPrice() != null && c.getTotalQuantity() != null)
                                        .mapToDouble(c -> c.getPrice() * c.getTotalQuantity())
                                        .sum()
                );

                float remainingStock = totalDeposit - totalSoldFromDeposit;
                int remainingMoney = totalDue - totalPaid;

                List<SummaryDTO> summary = new ArrayList<>();
                summary.add(new SummaryDTO("Tổng ký gửi", totalDeposit + " kg", totalClosed > 0 ? "Đã chốt giá" : "Chưa chốt giá"));
                summary.add(new SummaryDTO("Đã chốt giá", totalClosed + " kg", "Hoàn tất"));
                summary.add(new SummaryDTO("Đã bán", totalSold + " kg", remainingStock > 0 ? "Còn " + remainingStock + "kg chưa bán" : "Đã bán hết"));
                summary.add(new SummaryDTO("Đã giao", totalDelivered + " kg", totalDelivered < totalSold ? "Chưa giao đủ" : "Hoàn tất"));
                summary.add(new SummaryDTO("Đã thanh toán", MoneyUtil.format(totalPaid) , remainingMoney == 0 ? "Đã thanh toán đủ" : "Còn thiếu"));
                summary.add(new SummaryDTO("Số tiền còn lại phải thanh toán", MoneyUtil.format(remainingMoney) , remainingMoney == 0 ? "Đã thanh toán đủ" : "Còn thiếu"));
                summary.add(new SummaryDTO("Tồn kho còn lại", remainingStock + " kg", remainingStock > 0 ? "Chưa bán/chưa giao" : "Hết hàng"));

                return summary;
            }

        }

