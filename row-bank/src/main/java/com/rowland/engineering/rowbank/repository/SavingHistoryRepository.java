package com.rowland.engineering.rowbank.repository;

import com.rowland.engineering.rowbank.model.SavingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingHistoryRepository extends JpaRepository<SavingHistory, Long> {
}
