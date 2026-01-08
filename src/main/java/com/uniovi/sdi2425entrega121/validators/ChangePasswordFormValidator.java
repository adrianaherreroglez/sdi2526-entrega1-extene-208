package com.uniovi.sdi2425entrega121.validators;

import com.uniovi.sdi2425entrega121.dtos.UserDTO;
import com.uniovi.sdi2425entrega121.services.UsersService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ChangePasswordFormValidator implements Validator {

  private final UsersService usersService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public ChangePasswordFormValidator(UsersService usersService,
      BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.usersService = usersService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return UserDTO.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
  }

  public void validate(String userDni, UserDTO user, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "currentPassword", "Error.empty");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Error.empty");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "Error.empty");

    // Recogemos la contraseña real del usuario actual (cifrada)
    String usersRealCurrentPassword = usersService.getUserByDni(userDni).getPassword();

    // Comprobamos que coincida la contraseña actual introducida por el usuario con la
    // contraseña actual real del usuario
    if (!bCryptPasswordEncoder.matches(user.getCurrentPassword(), usersRealCurrentPassword)) {
      errors.rejectValue("currentPassword", "Error.change.password.not.match");
    }
    if (!user.getPassword().equals(user.getPasswordConfirm())) {
      errors.rejectValue("passwordConfirm", "Error.password.confirm.not.match");
    }
    if (!isStrongPassword(user.getPassword())) {
      errors.rejectValue("password", "Error.password.not.strong");
    }
  }

  private boolean isStrongPassword(String password) {
    // Verificar que la longitud sea mayor a 12
    if (password.length() < 12)
      return false;

    // Verificar que tenga al menos una letra mayúscula
    if (!password.matches(".*[A-Z].*"))
      return false;

    // Verificar que tenga al menos una letra minúscula
    if (!password.matches(".*[a-z].*"))
      return false;

    // Verificar que tenga al menos un número
    if (!password.matches(".*\\d.*"))
      return false;

    // Verificar que tenga al menos un carácter especial
    if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*"))
      return false;

    // Si pasa todas las verificaciones, la contraseña es válida
    return true;
  }
}
