package in.smartspend.controller;

import in.smartspend.model.Transaction;
import in.smartspend.model.TransactionType;
import in.smartspend.repository.TransactionRepository;
import in.smartspend.service.ForecastService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
public class DashboardController {
    private final TransactionRepository repository;
    private final ForecastService forecastService;

    public DashboardController(TransactionRepository repository, ForecastService forecastService) {
        this.repository = repository;
        this.forecastService = forecastService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        YearMonth month = YearMonth.now();
        List<Transaction> all = repository.findAllByOrderByTransactionDateDescIdDesc();
        BigDecimal income = sum(repository.findByTypeAndTransactionDateBetween(
            TransactionType.INCOME, month.atDay(1), month.atEndOfMonth()));
        BigDecimal expenses = sum(repository.findByTypeAndTransactionDateBetween(
            TransactionType.EXPENSE, month.atDay(1), month.atEndOfMonth()));
        model.addAttribute("transactions", all);
        model.addAttribute("transactionForm", new Transaction());
        model.addAttribute("income", income);
        model.addAttribute("expenses", expenses);
        model.addAttribute("balance", income.subtract(expenses));
        model.addAttribute("prediction", forecastService.predictNextMonthExpenses());
        model.addAttribute("currentMonth", month.toString());
        model.addAttribute("expenseLabels", all.stream().filter(t -> t.getType() == TransactionType.EXPENSE)
            .map(Transaction::getCategory).distinct().limit(8).toList());
        model.addAttribute("expenseValues", all.stream().filter(t -> t.getType() == TransactionType.EXPENSE)
            .collect(java.util.stream.Collectors.groupingBy(Transaction::getCategory,
                java.util.stream.Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)))
            .entrySet().stream().limit(8).map(e -> e.getValue().doubleValue()).toList());
        return "dashboard";
    }

    @PostMapping("/transactions")
    public String addTransaction(@Valid @ModelAttribute("transactionForm") Transaction transaction,
                                 BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please complete all required fields with a valid amount.");
            return "redirect:/";
        }
        if (transaction.getTransactionDate().isAfter(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("error", "Future transaction dates are not allowed in this starter version.");
            return "redirect:/";
        }
        repository.save(transaction);
        redirectAttributes.addFlashAttribute("success", "Transaction saved successfully.");
        return "redirect:/";
    }

    @PostMapping("/transactions/{id}/delete")
    public String deleteTransaction(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Transaction deleted.");
        }
        return "redirect:/";
    }

    private BigDecimal sum(List<Transaction> transactions) {
        return transactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
