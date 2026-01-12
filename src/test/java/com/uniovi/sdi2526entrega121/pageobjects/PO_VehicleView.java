package com.uniovi.sdi2526entrega121.pageobjects;

import com.uniovi.sdi2526entrega121.entities.Vehicle;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class PO_VehicleView extends PO_NavView {

    /**
     * Rellena el formulario de añadir un vehículo
     */
    public static void fillAddVehicleForm(WebDriver driver, String plateInput, String modelInput,
                                          String brandInput, String chassisNumberInput, String fuelTypeInput) {
        WebElement plateField = driver.findElement(By.name("plate"));
        plateField.click();
        plateField.clear();
        plateField.sendKeys(plateInput);

        WebElement modelField = driver.findElement(By.name("model"));
        modelField.click();
        modelField.clear();
        modelField.sendKeys(modelInput);

        WebElement brandField = driver.findElement(By.name("brand"));
        brandField.click();
        brandField.clear();
        brandField.sendKeys(brandInput);

        WebElement chassisNumberField = driver.findElement(By.name("chassisNumber"));
        chassisNumberField.click();
        chassisNumberField.clear();
        chassisNumberField.sendKeys(chassisNumberInput);

        // Seleccionar tipo de combustible del desplegable
        Select fuelTypeDropdown = new Select(driver.findElement(By.name("fuelType")));
        fuelTypeDropdown.selectByVisibleText(fuelTypeInput);

        // Hacer clic en el botón de envío
        By submitButton = By.className("btn");
        driver.findElement(submitButton).click();
    }

    public static void selectAllVehicles(WebDriver driver) {
        List<WebElement> checkboxes = driver.findElements(By.name("vehicleCheckbox"));
        for (WebElement checkbox : checkboxes) {
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }
    }

    public static void deleteSelectedVehicles(WebDriver driver) {
        WebElement deleteButton = driver.findElement(By.id("deleteVehiclesButton"));
        deleteButton.click();
        driver.switchTo().alert().accept();
    }

    public static boolean deleteNonExistingVehicle(WebDriver driver, String plate) {
        try {
            WebElement vehicleRow = driver.findElement(By.xpath("//tr[td[text()='" + plate + "']]/td/input[@type='checkbox']"));
            vehicleRow.click();
            deleteSelectedVehicles(driver);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static void fillFormAddVehicle(WebDriver driver, String platep, String modelp, String brand, String chassisNumber, Vehicle.FuelType type) {
        //Rellenemos los campos
        WebElement plate = driver.findElement(By.name("plate"));
        plate.clear();
        plate.sendKeys(platep);
        WebElement model = driver.findElement(By.name("model"));
        model.clear();
        model.sendKeys(modelp);
        WebElement brandField = driver.findElement(By.name("brand"));
        brandField.click();
        brandField.clear();
        brandField.sendKeys(brand);
        WebElement chassisNumberField = driver.findElement(By.name("chassisNumber"));
        chassisNumberField.click();
        chassisNumberField.clear();
        chassisNumberField.sendKeys(chassisNumber);
        Select fuelTypeDropdown = new Select(driver.findElement(By.name("fuelType")));
        fuelTypeDropdown.selectByVisibleText(type.toString());

        By boton = By.className("btn");
        driver.findElement(boton).click();


    }


    public static void clickDeleteButton(WebDriver driver) {
        WebElement deleteButton = driver.findElement(By.id("deleteSelectedVehicles"));
        deleteButton.click();
    }

    public static List<WebElement> getVehicleCheckboxes(WebDriver driver) {
        return driver.findElements(By.cssSelector(".vehicle-checkbox"));
    }



}
