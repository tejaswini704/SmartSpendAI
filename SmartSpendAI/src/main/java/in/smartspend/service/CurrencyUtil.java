package in.smartspend.service;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Component("currencyUtil")
public class CurrencyUtil {
    public String format(BigDecimal amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(amount == null ? BigDecimal.ZERO : amount);
    }
}
