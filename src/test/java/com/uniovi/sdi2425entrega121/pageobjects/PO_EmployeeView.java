package com.uniovi.sdi2425entrega121.pageobjects;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class PO_EmployeeView extends PO_NavView {

  /**
   * Rellena el formulario de añadir un empleado
   *
   * @param driver
   * @param dniInput      del nuevo empleado
   * @param nameInput     del nuevo empleado
   * @param lastNameInput del nuevo empleado
   */
  public static void fillAddEmployeeForm(WebDriver driver, String dniInput, String nameInput,
      String lastNameInput) {
    WebElement dniField = driver.findElement(By.name("dni"));
    dniField.click();
    dniField.clear();
    dniField.sendKeys(dniInput);
    WebElement nameField = driver.findElement(By.name("name"));
    nameField.click();
    nameField.clear();
    nameField.sendKeys(nameInput);
    WebElement lastNameField = driver.findElement(By.name("surname"));
    lastNameField.click();
    lastNameField.clear();
    lastNameField.sendKeys(lastNameInput);
    //Pulsar el boton de submit
    By boton = By.className("btn");
    driver.findElement(boton).click();
  }

  /**
   * Rellena el formulario de edición de un empleado introduciendo los datos recibidos como
   * parámetro
   *
   * @param driver
   * @param dniInput dni nuevo del usuario a editar
   * @param nameInput nombre nuevo del usuario a editar
   * @param lastNameInput apellidos nuevos del usuario a editar
   * @param roleInput rol nuevo del usuario a editar
   */
  public static void fillEditEmployeeForm(WebDriver driver, String dniInput, String nameInput,
      String lastNameInput, String roleInput) {

    WebElement dniField = driver.findElement(By.name("dni"));
    dniField.click();
    dniField.clear();
    dniField.sendKeys(dniInput);
    WebElement nameField = driver.findElement(By.name("name"));
    nameField.click();
    nameField.clear();
    nameField.sendKeys(nameInput);
    WebElement lastNameField = driver.findElement(By.name("surname"));
    lastNameField.click();
    lastNameField.clear();
    lastNameField.sendKeys(lastNameInput);
    // Seleccionamos el rol
    WebElement roleField = driver.findElement(By.name("role"));
    Select select = new Select(roleField);
    select.selectByVisibleText(roleInput);
    // Comprobamos que se haya seleccionado dicha opción
    Assertions.assertEquals(roleInput, select.getFirstSelectedOption().getText());
    //Pulsar el boton de submit
    By boton = By.className("btn");
    driver.findElement(boton).click();
  }
}
