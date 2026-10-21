package in.smartspend.repository;

import in.smartspend.model.Transaction;
import in.smartspend.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByOrderByTransactionDateDescIdDesc();
    List<Transaction> findByTypeAndTransactionDateBetween(TransactionType type, LocalDate start, LocalDate end);
}
