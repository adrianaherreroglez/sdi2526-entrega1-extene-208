package com.uniovi.sdi2526entrega121.repositories;

import com.uniovi.sdi2526entrega121.entities.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Repositorio para la entidad {@link User}, que extiende {@link CrudRepository}
 * para proporcionar operaciones CRUD básicas y consultas personalizadas.
 */
public interface UsersRepository extends CrudRepository<User, Long> {
  User findByDni(String dni);
}
