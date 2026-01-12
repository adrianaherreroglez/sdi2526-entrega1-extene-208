package com.uniovi.sdi2526entrega121.repositories;

import com.uniovi.sdi2526entrega121.entities.LogEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repositorio para la entidad {@link LogEntry}, que proporciona métodos CRUD básicos
 * y consultas específicas para gestionar entradas de logs.
 * Extiende {@link CrudRepository} para operaciones básicas de persistencia.
 */
public interface LoggerRepository extends CrudRepository<LogEntry, Long> {

  Page<LogEntry> findAll(Pageable pageable);

  @Query("SELECT l FROM LogEntry l WHERE l.logType = :logFilter")
  Page<LogEntry> findLogByType(String logFilter, Pageable pageable);

  @Modifying
  @Transactional
  @Query("DELETE FROM LogEntry l WHERE l.logType = :logFilter")
  void deleteLogsByType(String logFilter);

  @Query("SELECT COUNT(*) FROM LogEntry l WHERE l.logType = :logType")
  int countLogEntriesByLogType(String logType);
}
