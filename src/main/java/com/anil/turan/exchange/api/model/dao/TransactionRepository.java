package com.anil.turan.exchange.api.model.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import com.anil.turan.exchange.api.model.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByTransactionDate(Date transactionDate);
	Page<Transaction> findAllByTransactionDate(@DateTimeFormat(pattern="yyyy-MM-dd") Date transactionDate, Pageable pageRequest);
}
