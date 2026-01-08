package com.uniovi.sdi2425entrega121.pageobjects;

import com.uniovi.sdi2425entrega121.util.SeleniumUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_LogInView extends PO_NavView {

  /**
   * Rellena el formulario de login con los datos del usuario introducidos
   *
   * @param driver
   * @param inputUsername nombre del usuario con el que hacer login
   * @param inputPassword password introducida por el usuario como input (sin cifrar)
   */
  static public void fillLoginForm(WebDriver driver, String inputUsername, String inputPassword) {
    WebElement username = driver.findElement(By.name("username"));
    username.click();
    username.clear();
    username.sendKeys(inputUsername);
    WebElement password = driver.findElement(By.name("password"));
    password.click();
    password.clear();
    password.sendKeys(inputPassword);
    //Pulsar el boton de enviar.
    By boton = By.className("btn");
    driver.findElement(boton).click();
  }

  /**
   * Ejecuta el proceso de login completo con los datos introducidos como parámetro
   *
   * @param driver
   * @param inputUsername nombre de usuario con el que hacer login
   * @param inputPassword password introducida por el usuario como input (sin cifrar)
   * @param checkText texto a comprobar una vez hecho el login
   * @return lista de elementos qeu contienen el texto a comprobar una vez hecho login
   */
  public static List<WebElement> login(WebDriver driver, String inputUsername, String inputPassword, String checkText) {
    //Vamos al formulario de logueo.
    PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
    //Rellenamos el formulario
    fillLoginForm(driver, inputUsername, inputPassword);
    //Comprobamos que entramos en la pagina privada
    return PO_View.checkElementBy(driver, "text", checkText);
  }



  public static void logout(WebDriver driver, int language) {
    //Ahora nos desconectamos y comprobamos que aparece el menú de registro
    String loginText = PO_HomeView.getP().getString("login.message", language);
    PO_NavView.clickOption(driver, "/logout", "text", loginText);
  }
}
