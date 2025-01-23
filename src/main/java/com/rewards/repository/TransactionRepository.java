package com.rewards.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rewards.entity.Transaction;

@Repository
@Transactional
public interface TransactionRepository extends CrudRepository<Transaction,Long> {
    public List<Transaction> findAllByCustomerId(String customerId);

    public List<Transaction> findAllByCustomerIdAndTransactionDateBetween(String customerId, Timestamp from,Timestamp to);

   }
