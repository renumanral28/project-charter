package com.rewards.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rewards.entity.Transaction;

@Repository
@Transactional
public interface TransactionRepository extends CrudRepository<Transaction,Long> {
    public List<Transaction> findAllByCustomerId(String customerId);


    @Query("SELECT t FROM Transaction t WHERE t.customerId = :customerId AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findAllByCustomerIdAndTransactionDateBetween(@Param("customerId") String customerId,
                                                                   @Param("startDate") Timestamp startDate,
                                                                   @Param("endDate") Timestamp endDate);

}
