package com.uniovi.sdi2425entrega121.services;

import com.uniovi.sdi2425entrega121.entities.Employee;
import com.uniovi.sdi2425entrega121.entities.User;
import com.uniovi.sdi2425entrega121.repositories.UsersRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * Servicio para la gestión de usuarios en el sistema.
 * Proporciona métodos para obtener, crear, editar y eliminar usuarios,
 * así como funcionalidades específicas relacionadas con empleados.
 */
@Service
public class UsersService {

  private final UsersRepository usersRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final RolesService rolesService;

  /**
   * Constructor con inyección de dependencias.
   *
   * @param usersRepository       Repositorio para acceso a datos de usuarios
   * @param bCryptPasswordEncoder Codificador para encriptar contraseñas
   * @param rolesService          Servicio para obtener roles disponibles
   */
  public UsersService(UsersRepository usersRepository,
                      BCryptPasswordEncoder bCryptPasswordEncoder,
                      RolesService rolesService) {
    this.usersRepository = usersRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.rolesService = rolesService;
  }

  /**
   * Obtiene un usuario por su ID.
   *
   * @param id Identificador del usuario
   * @return usuario encontrado
   * @throws java.util.NoSuchElementException si no existe el usuario
   */
  public User getUser(Long id) {
    return usersRepository.findById(id).get();
  }

  /**
   * Añade un nuevo usuario al sistema. La contraseña se encripta antes de guardar.
   *
   * @param user usuario a añadir
   */
  public void addUser(User user) {
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    usersRepository.save(user);
  }

  /**
   * Busca un usuario por su DNI.
   *
   * @param dni DNI del usuario
   * @return usuario encontrado o null si no existe
   */
  public User getUserByDni(String dni) {
    return usersRepository.findByDni(dni);
  }

  /**
   * Crea un usuario nuevo asociado a un empleado registrado en el sistema.
   * Se genera una contraseña aleatoria y se devuelve la versión sin encriptar.
   *
   * @param employee empleado asociado al nuevo usuario
   * @return contraseña generada para el nuevo usuario (en texto plano)
   */
  public String addUserByEmployee(Employee employee) {
    User user = new User();
    user.setDni(employee.getDni());
    user.setRole(rolesService.getRoles()[0]); // ROLE_STANDARD por defecto
    String plainPassword = PasswordGeneratorService.generatePassword(12);
    String password = bCryptPasswordEncoder.encode(plainPassword);
    user.setPassword(password);
    user.setPasswordConfirm(password);
    user.setEmployee(employee);
    employee.setUser(user);
    usersRepository.save(user);
    return plainPassword;
  }

  /**
   * Edita el usuario cuyo ID es recibido, actualizando sus datos a partir del empleado y asignando un nuevo rol.
   *
   * @param id       ID del usuario a editar
   * @param employee empleado con la nueva información
   * @param role     nuevo rol a asignar
   */
  public void editUserFromEmployee(long id, Employee employee, String role) {
    User user = getUser(id);
    user.setDni(employee.getDni());

    // Asigna el rol solo si coincide con alguno definido en rolesService
    for (String r : rolesService.getRoles()) {
      if (r.equals(role)) {
        user.setRole(r);
        break;
      }
    }
    usersRepository.save(user);
  }

  /**
   * Cambia la contraseña del usuario con el ID indicado por la nueva contraseña dada.
   * La contraseña se almacena encriptada.
   *
   * @param id       ID del usuario
   * @param password nueva contraseña en texto plano
   */
  public void changePassword(long id, String password) {
    User user = getUser(id);
    String encryptedPassword = bCryptPasswordEncoder.encode(password);
    user.setPassword(encryptedPassword);
    user.setPasswordConfirm(encryptedPassword);
    usersRepository.save(user);
  }
}