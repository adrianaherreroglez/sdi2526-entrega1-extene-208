package com.uniovi.sdi2425entrega121.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_RouteView extends PO_NavView{

/**
 * Selecciona una matricula del selector para crear un trayecto
 *
 * @param driver
 * @param pos : La posición de la matricula en la lista
 */
public static void selectPlateForNewRoute(WebDriver driver, int pos){

    WebElement plateSelector = driver.findElement(By.id("matricula"));
    plateSelector.click();

    List<WebElement> options = driver.findElements(By.xpath("//select[@id='matricula']/option"));
    if(pos >= 0 && pos < options.size()){
        options.get(pos).click();
    } else {
        options.get(options.size()-1).click();
    }
}

    public static void fillEndRouteForm(WebDriver driver, String odometer, String observations){
        WebElement odometerFinalValue = driver.findElement(By.id("odometerFinalValue"));
        odometerFinalValue.click();
        odometerFinalValue.clear();
        odometerFinalValue.sendKeys(odometer);
        WebElement observationsInput = driver.findElement(By.id("observations"));
        observationsInput.click();
        observationsInput.clear();
        observationsInput.sendKeys(observations);
        //Pulsar el boton de enviar.
        By boton = By.className("btn");
        driver.findElement(boton).click();
    }


}
