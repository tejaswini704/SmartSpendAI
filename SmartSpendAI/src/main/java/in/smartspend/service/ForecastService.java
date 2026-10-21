package in.smartspend.service;

import in.smartspend.model.Transaction;
import in.smartspend.model.TransactionType;
import in.smartspend.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class ForecastService {
    private final TransactionRepository repository;
    public ForecastService(TransactionRepository repository) { this.repository = repository; }

    /**
     * Simple trend forecast based on the last six calendar months of expense totals.
     * This is an educational estimate, not a guarantee of future spending.
     */
    public BigDecimal predictNextMonthExpenses() {
        YearMonth current = YearMonth.now();
        List<Double> values = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            YearMonth month = current.minusMonths(i);
            BigDecimal total = repository.findByTypeAndTransactionDateBetween(
                TransactionType.EXPENSE, month.atDay(1), month.atEndOfMonth()
            ).stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            values.add(total.doubleValue());
        }
        if (values.stream().allMatch(v -> v == 0.0)) return BigDecimal.ZERO.setScale(2);
        int n = values.size();
        double xMean = (n - 1) / 2.0;
        double yMean = values.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double numerator = 0, denominator = 0;
        for (int i = 0; i < n; i++) {
            numerator += (i - xMean) * (values.get(i) - yMean);
            denominator += (i - xMean) * (i - xMean);
        }
        double slope = denominator == 0 ? 0 : numerator / denominator;
        double forecast = Math.max(0, yMean + slope * ((n - 1) - xMean + 1));
        return BigDecimal.valueOf(forecast).setScale(2, RoundingMode.HALF_UP);
    }

    public boolean hasExpenseHistory() {
        return repository.count() > 0;
    }
}
