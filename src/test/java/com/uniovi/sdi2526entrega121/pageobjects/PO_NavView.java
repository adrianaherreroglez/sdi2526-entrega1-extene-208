package com.uniovi.sdi2526entrega121.pageobjects;

import com.uniovi.sdi2526entrega121.util.SeleniumUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_NavView extends PO_View {

  /**
   * Clic en una de las opciones principales (a href) y comprueba que se vaya a la vista con el
   * elemento de tipo type con el texto Destino
   *
   * @param driver:     apuntando al navegador abierto actualmente.
   * @param textOption: Texto de la opción principal.
   * @param criterio:   "id" or "class" or "text" or "@attribute" or "free". Si el valor de criterio
   *                    es free es una expresion xpath completa.
   * @param targetText: texto correspondiente a la búsqueda de la página destino.
   */
  public static void clickOption(WebDriver driver, String textOption, String criterio,
      String targetText) {
    //CLickamos en la opción de registro y esperamos a que se cargue el enlace de Registro.
    List<WebElement> elements =
        SeleniumUtils.waitLoadElementsBy(driver, "@href", textOption, getTimeout());
    //Tiene que haber un sólo elemento.
    Assertions.assertEquals(1, elements.size());
    //Ahora lo clickamos
    elements.get(0).click();
    //Esperamos a que sea visible un elemento concreto
    elements = SeleniumUtils.waitLoadElementsBy(driver, criterio, targetText, getTimeout());
    //Tiene que haber un sólo elemento.
    Assertions.assertEquals(1, elements.size());
  }

  /**
   * Selecciona el enlace de idioma correspondiente al texto textLanguage
   *
   * @param driver:       apuntando al navegador abierto actualmente.
   * @param textLanguage: el texto que aparece en el enlace de idioma ("English" o "Spanish" o
   *                      "Italian")
   */
  public static void changeLanguage(WebDriver driver, String textLanguage) {
    //clickamos la opción Idioma.
    List<WebElement> languageButton =
        SeleniumUtils.waitLoadElementsBy(driver, "id", "btnLanguage", getTimeout());
    languageButton.get(0).click();
    //Esperamos a que aparezca el menú de opciones.
    SeleniumUtils.waitLoadElementsBy(driver, "id", "languageDropdownMenuButton", getTimeout());
    //CLickamos la opción Inglés partiendo de la opción Español
    List<WebElement> Selectedlanguage =
        SeleniumUtils.waitLoadElementsBy(driver, "id", textLanguage, getTimeout());
    Selectedlanguage.get(0).click();
  }

  /**
   * Selecciona la sección del nav a la que acceder introduciéndole el XPath del elemento dropdown
   * (el apartado del nav, p.e Empleados) y el Xpath del elemento sección concreto (p.e Añadir
   * Empleado)
   *
   * @param driver
   * @param xPathNavSection
   * @param xPathConcreteSection
   */
  public static void accessSection(WebDriver driver, String xPathNavSection,
      String xPathConcreteSection) {
    List<WebElement> elements = PO_View.checkElementBy(driver, "free", xPathNavSection);
    elements.get(0).click();
    //Esperamos a que aparezca la opción: //a[contains(@href, 'mark/add')]
    elements = PO_View.checkElementBy(driver, "free", xPathConcreteSection);
    //Pinchamos en xPathConcreteSection.
    elements.get(0).click();
  }

  /**
   * Comprueba si el usuario actual puede visualizar una sección concreta del menú cuyo nombre es
   * introducido como parámetro en el apartado con la id introducida como parámetro
   *
   * @param driver
   * @param sectionId id de la sección (dropdown) en la que se encontraría la sección concreta
   * @param concreteSectionName
   * @return true si el usuario dispone de dicha sección en el menú de navegación, false en caso
   * contrario
   */
  public static boolean checkIfHasSection(WebDriver driver, String sectionId,
      String concreteSectionName) {
    // Pulsamos en el menú desplegable de los empleados
    PO_View.checkElementBy(driver, "id", sectionId).get(0).click();
    List<WebElement> result2 = PO_View.checkElementBy(driver, "text", concreteSectionName);
    // Comprobamos que tenga el apartado de Añadir Empleado
    return concreteSectionName.equals(result2.get(0).getText());
  }
}
