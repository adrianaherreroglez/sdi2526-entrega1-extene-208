package com.uniovi.sdi2526entrega121.repositories;


import com.uniovi.sdi2526entrega121.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repositorio para la entidad {@link Employee}, que proporciona operaciones CRUD básicas
 * y consultas personalizadas para la gestión de empleados.
 * Extiende {@link CrudRepository} para operaciones básicas de persistencia.
 */
public interface EmployeesRepository extends CrudRepository<Employee, Long> {
  Employee findByDni(String dni);
  Page<Employee> findAll(Pageable pageable);

  @Query("SELECT COUNT(e) FROM Employee e")
  int countAll();
}
