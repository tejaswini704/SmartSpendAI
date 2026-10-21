package in.smartspend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Choose income or expense")
    @Enumerated(EnumType.STRING)
    private TransactionType type = TransactionType.EXPENSE;

    @NotBlank(message = "Category is required")
    @Size(max = 60)
    private String category;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Digits(integer = 12, fraction = 2)
    @Column(precision = 14, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull(message = "Date is required")
    private LocalDate transactionDate = LocalDate.now();

    @Size(max = 250)
    private String description;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod = "UPI";

    public Long getId() { return id; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
