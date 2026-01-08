package com.uniovi.sdi2425entrega121.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_UserView extends PO_NavView {

  /**
   * Rellena el formulario de cambio de contraseña con los valores introducidos como parámetro
   *
   * @param driver
   * @param currentPassword contraseña actual del usuario
   * @param newPassword nueva contraseña para el usuario
   * @param confirmPassword confirmación de la nueva contraseña
   */
  public static void fillChangePasswordForm(WebDriver driver, String currentPassword,
      String newPassword, String confirmPassword) {
    WebElement currentPasswordField = driver.findElement(By.name("currentPassword"));
    currentPasswordField.click();
    currentPasswordField.clear();
    currentPasswordField.sendKeys(currentPassword);
    WebElement newPasswordField = driver.findElement(By.name("password"));
    newPasswordField.click();
    newPasswordField.clear();
    newPasswordField.sendKeys(newPassword);
    WebElement confirmPasswordField = driver.findElement(By.name("passwordConfirm"));
    confirmPasswordField.click();
    confirmPasswordField.clear();
    confirmPasswordField.sendKeys(confirmPassword);
    // Pulsar el botón de submit
    By boton = By.className("btn");
    driver.findElement(boton).click();
  }
}
