package com.uniovi.sdi2425entrega121.services;

import com.uniovi.sdi2425entrega121.entities.User;
import com.uniovi.sdi2425entrega121.repositories.UsersRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


/**
 * Implementación personalizada de {@link UserDetailsService} para cargar usuarios
 * a partir del DNI, utilizado en la autenticación de Spring Security.
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

  /**
   * Repositorio para acceder a la información de los usuarios.
   */
  private final UsersRepository usersRepository;

  /**
   * Constructor con inyección del repositorio de usuarios.
   *
   * @param usersRepository Repositorio para acceso a usuarios
   */
  public UserDetailsServiceImpl(UsersRepository usersRepository) {
    this.usersRepository = usersRepository;
  }

  /**
   * Carga un usuario a partir del DNI para la autenticación.
   *
   * @param dni DNI del usuario que intenta autenticarse
   * @return objeto UserDetails con la información del usuario y sus roles
   * @throws UsernameNotFoundException si no se encuentra ningún usuario con el DNI dado
   */
  @Override
  public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
    User user = usersRepository.findByDni(dni);

    if (user == null) {
      throw new UsernameNotFoundException("Usuario no encontrado con DNI: " + dni);
    }

    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));

    return new org.springframework.security.core.userdetails.User(
            user.getDni(),
            user.getPassword(),
            grantedAuthorities
    );
  }
}