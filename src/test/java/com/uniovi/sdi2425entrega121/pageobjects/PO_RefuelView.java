package com.uniovi.sdi2425entrega121.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_RefuelView extends PO_NavView{

    /**
     * Rellena el formulario de añadir un repostaje
     *
     * @param driver
     * @param stationNameInput
     * @param pricePerUnitInput
     * @param quantityInput
     * @param isTankFullInput
     * @param odometerInput
     * @param observationsInput
     */
    public static void fillAddRefuelForm(WebDriver driver, String stationNameInput, double pricePerUnitInput,
                                         double quantityInput, boolean isTankFullInput, double odometerInput,
                                         String observationsInput){
        WebElement stationNameField = driver.findElement(By.name("stationName"));
        stationNameField.click();
        stationNameField.clear();
        stationNameField.sendKeys(stationNameInput);
        WebElement pricePerUnitField = driver.findElement(By.name("pricePerUnit"));
        pricePerUnitField.click();
        pricePerUnitField.clear();
        pricePerUnitField.sendKeys(String.valueOf(pricePerUnitInput));
        WebElement quantityField = driver.findElement(By.name("quantity"));
        quantityField.click();
        quantityField.clear();
        quantityField.sendKeys(String.valueOf(quantityInput));
        WebElement isTankFullField = driver.findElement(By.name("isTankFull"));
        if(!isTankFullField.isSelected() && isTankFullInput){
            isTankFullField.click();
        }
        WebElement odometerField = driver.findElement(By.name("odometerRefuelValue"));
        odometerField.click();
        odometerField.clear();
        odometerField.sendKeys(String.valueOf(odometerInput));
        if(!observationsInput.isEmpty() && !observationsInput.isBlank()){
            WebElement observationsField = driver.findElement(By.name("observations"));
            observationsField.click();
            observationsField.clear();
            observationsField.sendKeys(observationsInput);
        }
        By boton = By.className("btn");
        driver.findElement(boton).click();
    }

    public static void fillAddRefuelFormEmptyValues(WebDriver driver, String stationNameInput, double pricePerUnitInput,
                                         double quantityInput, boolean isTankFullInput, double odometerInput,
                                         String observationsInput){
        WebElement stationNameField = driver.findElement(By.name("stationName"));
        stationNameField.click();
        stationNameField.clear();
        WebElement pricePerUnitField = driver.findElement(By.name("pricePerUnit"));
        pricePerUnitField.click();
        pricePerUnitField.clear();
        WebElement quantityField = driver.findElement(By.name("quantity"));
        quantityField.click();
        quantityField.clear();
        WebElement isTankFullField = driver.findElement(By.name("isTankFull"));
        if(!isTankFullField.isSelected() && isTankFullInput){
            isTankFullField.click();
        }
        WebElement odometerField = driver.findElement(By.name("odometer"));
        odometerField.click();
        odometerField.clear();
        if(!observationsInput.isEmpty() && !observationsInput.isBlank()){
            WebElement observationsField = driver.findElement(By.name("observations"));
            observationsField.click();
            observationsField.clear();
            observationsField.sendKeys(observationsInput);
        }
        By boton = By.className("btn");
        driver.findElement(boton).click();
    }
}
