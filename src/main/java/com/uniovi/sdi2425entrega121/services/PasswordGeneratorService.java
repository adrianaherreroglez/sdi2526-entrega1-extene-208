package com.uniovi.sdi2425entrega121.services;

import java.security.SecureRandom;

/**
 * Servicio para la generación de contraseñas aleatorias seguras.
 */
public class PasswordGeneratorService {

  /**
   * Conjunto de caracteres permitidos para la generación de la contraseña.
   */
  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";

  /**
   * Genera una contraseña aleatoria segura de una longitud determinada.
   *
   * @param length longitud deseada para la contraseña generada
   * @return contraseña aleatoria generada con los caracteres permitidos
   */
  public static String generatePassword(int length) {
    SecureRandom random = new SecureRandom();
    StringBuilder password = new StringBuilder(length);

    // Construir la contraseña carácter a carácter
    for (int i = 0; i < length; i++) {
      int randomIndex = random.nextInt(CHARACTERS.length());
      password.append(CHARACTERS.charAt(randomIndex));
    }

    return password.toString();
  }
}