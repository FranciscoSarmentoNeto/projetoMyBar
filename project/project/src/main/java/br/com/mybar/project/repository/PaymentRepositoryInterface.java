package br.com.mybar.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.mybar.project.model.Payment;

import java.util.List;

public interface PaymentRepositoryInterface extends JpaRepository<Payment, Long> {
    List<Payment> findByConta_Id(Long contaId);
}
