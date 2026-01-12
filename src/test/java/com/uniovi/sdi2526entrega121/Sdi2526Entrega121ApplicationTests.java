package com.uniovi.sdi2526entrega121;

import com.uniovi.sdi2526entrega121.entities.Employee;
import com.uniovi.sdi2526entrega121.entities.LogEntry;
import com.uniovi.sdi2526entrega121.entities.Vehicle;
import com.uniovi.sdi2526entrega121.pageobjects.*;
import com.uniovi.sdi2526entrega121.services.*;
import com.uniovi.sdi2526entrega121.util.SeleniumUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class Sdi2526Entrega121ApplicationTests {

  @Autowired
  EmployeesService employeesService;
  @Autowired
  RolesService rolesService;
  @Autowired
  LoggerService loggerService;
  @Autowired
  private VehiclesService vehiclesService;
  @Autowired
  RoutesService routesService;
  @Autowired
  RefuelService refuelService;

  //Paths
  static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
  static String Geckodriver = "C:\\Users\\adria\\Downloads\\PL-SDI-Sesión6-material\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";

  //Común a Windows y a MACOSX
  static WebDriver driver = getDriver(PathFirefox, Geckodriver);
  static String URL = "http://localhost:8080";

  public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
    System.setProperty("webdriver.firefox.bin", PathFirefox);
    System.setProperty("webdriver.gecko.driver", Geckodriver);
    driver = new FirefoxDriver();
    return driver;
  }

  @BeforeEach
  public void setUp() {
    driver.navigate().to(URL);
    PO_NavView.changeLanguage(driver, "Spanish");
  }

  //Después de cada prueba se borran las cookies del navegador
  @AfterEach
  public void tearDown() {
    driver.manage().deleteAllCookies();
  }

  //Antes de la primera prueba
  @BeforeAll
  static public void begin() {
  }

  //Al finalizar la última prueba
  @AfterAll
  static public void end() {
    //Cerramos el navegador al finalizar las pruebas
    driver.quit();
  }

  /**
   * Prueba de LogIn con Rol ADMINISTRADOR
   */
  @Test
  @Order(1)
  public void Prueba1() {
    String checkText = "Empleados";
    String dni = "12345678Z";
    String password = "@Dm1n1str@D0r";

    List<WebElement> result = PO_LogInView.login(driver, dni, password, checkText);
    //Comprobamos que entramos en la sección de lista de empleados
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Comprobamos que es admin si tiene el menú de añadir empleado
    String sectionName =
        PO_View.getP().getString("navbar.add.employees", PO_Properties.getSPANISH());
    boolean isAdmin = PO_NavView.checkIfHasSection(driver, "employeesDropDown", sectionName);
    Assertions.assertTrue(isAdmin);
  }

  /**
   * Prueba de LogIn con Rol ESTANDAR
   */
  @Test
  @Order(2)
  public void Prueba2() {
    String checkText = "Trayectos";
    String dni = "10000001S";
    String password = "Us3r@1-PASSW";

    List<WebElement> result = PO_LogInView.login(driver, dni, password, checkText);
    //Comprobamos que entramos en la sección Trayectos del usuario y nos nuestra el texto a buscar
    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /**
   * Prueba de LogIn datos inválidos (dni y contraseña vacíos)
   */
  @Test
  @Order(3)
  public void Prueba3() {
    // Obtenemos el mensaje correspondiente a la sesión de login en español
    String checkText = PO_HomeView.getP().getString("login.message", PO_Properties.getSPANISH());
    String dni = "";
    String password = "";

    List<WebElement> result = PO_LogInView.login(driver, dni, password, checkText);
    //Comprobamos que seguimos en la sección de login
    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /**
   * Prueba de LogIn datos inválidos (dni exitente y contraseña incorrecta)
   */
  @Test
  @Order(4)
  public void Prueba4() {
    // Obtenemos el mensaje correspondiente al error de login en español
    String checkText = PO_View.getP().getString("login.error", PO_Properties.getSPANISH());
    String dni = "10000001S";
    String password = "1234";

    List<WebElement> result = PO_LogInView.login(driver, dni, password, checkText);
    //Comprobamos que sale el mensaje de error de inicio de sesión
    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /**
   * Prueba de logout en la que se comprueba que se muestra el mensaje de confirmación de logout y
   * que se accede a la pantalla de login
   */
  @Test
  @Order(5)
  public void Prueba5() {
    String checkText = "Trayectos";
    String dni = "10000001S";
    String password = "Us3r@1-PASSW";

    // Iniciamos sesión con usuuario estándar
    List<WebElement> result = PO_LogInView.login(driver, dni, password, checkText);
    //Comprobamos que entramos en la sección home y nos nuestra el texto a buscar
    Assertions.assertEquals(checkText, result.get(0).getText());
    PO_LogInView.logout(driver, PO_Properties.getSPANISH());

    // Comprobamos que se encuentra el mensaje de confirmación de logout
    checkText = PO_View.getP().getString("logout.confirm.message", PO_Properties.getSPANISH());
    result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Comprobamos que nos encontramos en la pantalla de login
    checkText = PO_View.getP().getString("login.message", PO_Properties.getSPANISH());
    result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

  }

  /**
   * Prueba de para comprobar que el botón de logout no está visible si el usuario no está
   * autenticado
   */
  @Test
  @Order(6)
  public void Prueba6() {
    String checkText = "Trayectos";
    String dni = "10000001S";
    String password = "Us3r@1-PASSW";

    // Iniciamos sesión con usuario estándar
    List<WebElement> result = PO_LogInView.login(driver, dni, password, checkText);
    //Comprobamos que entramos en la sección home y nos nuestra el texto a buscar
    Assertions.assertEquals(checkText, result.get(0).getText());
    // Cerramos sesión
    PO_LogInView.logout(driver, PO_Properties.getSPANISH());

    // Comprobamos que no se encuentra el botón de logout
    checkText = PO_View.getP().getString("logout.message", PO_Properties.getSPANISH());
    SeleniumUtils.textIsNotPresentOnPage(driver, checkText);
  }

  /**
   * Test de registro de un empleado nuevo con datos válidos
   */
  @Test
  @Order(7)
  public void Prueba7() {
    String checkText = "Empleados";
    String dni = "12345678Z";
    String password = "@Dm1n1str@D0r";

    PO_LogInView.login(driver, dni, password, checkText);

    // Accedemos a la sección de añadir empleado
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[2]/a",
        "/html/body/nav/div/ul[1]/li[2]/div/a");
    String dniInput = "12345678X";
    String nameInput = "Rodrigo";
    String lastNameInput = "José";

    // Rellenamos el formulario de añadir empleado
    PO_EmployeeView.fillAddEmployeeForm(driver, dniInput, nameInput, lastNameInput);

    // Comprobamos que se esté mostrando la ventana de mostrar la nueva contraseña generada
    checkText =
        PO_View.getP().getString("show.employee.password.message", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Guardamos la nueva contraseña
    String newPassword =
        PO_View.checkElementBy(driver, "id", "newEmployeePassword").get(0).getText();

    // Cerramos sesión para iniciar sesión con el nuevo usuario
    PO_LogInView.logout(driver, PO_Properties.getSPANISH());

    // Iniciamos sesión con los datos del nuevo usuario
    PO_LogInView.fillLoginForm(driver, dniInput, newPassword);
    // Comprobamos que accedemos al home con el nuevo usuario
    checkText = "Trayectos";
    result = PO_View.checkElementBy(driver, "text", checkText);
    //Comprobamos que entramos en la sección home y nos nuestra el texto a buscar
    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /**
   * Test de registro de un empleado con datos inválidos (datos vacíos). Comprobamos que no se
   * registre el empleado y que salgan los mensajes de validación
   */
  @Test
  @Order(8)
  public void Prueba8() {
    String checkText = "Empleados";
    String dni = "12345678Z";
    String password = "@Dm1n1str@D0r";

    PO_LogInView.login(driver, dni, password, checkText);

    // Accedemos a la sección de añadir empleado
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[2]/a",
        "/html/body/nav/div/ul[1]/li[2]/div/a");

    String dniInput = "";
    String nameInput = "";
    String lastNameInput = "";

    // Rellenamos el formulario de añadir empleado con datos vacíos
    PO_EmployeeView.fillAddEmployeeForm(driver, dniInput, nameInput, lastNameInput);

    checkText = PO_View.getP().getString("Error.empty", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);

    // Comprobamos que se muestra el mensaje de error 3 veces (para los 3 campos)
    Assertions.assertEquals(3, result.size());
    // Para este primero cogemos unicamente el primer mensaje que muestra pues, al ser el dni,
    // muestra tambien el mensaje que indica que debe tener exactamente 9 de longitud
    Assertions.assertEquals(checkText, result.get(0).getText().split("\n")[0]);
    Assertions.assertEquals(checkText, result.get(1).getText());
    Assertions.assertEquals(checkText, result.get(2).getText());
  }

  /**
   * Test de registro de un empleado con datos inválidos (mal formato de dni). Comprobamos que no se
   * registre el empleado y que salgan los mensajes de validación
   */
  @Test
  @Order(9)
  public void Prueba9() {
    String checkText = "Empleados";
    String dni = "12345678Z";
    String password = "@Dm1n1str@D0r";

    PO_LogInView.login(driver, dni, password, checkText);

    // Accedemos a la sección de añadir empleado
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[2]/a",
        "/html/body/nav/div/ul[1]/li[2]/div/a");

    // Dni incorrecto por tener 8 de longitud y no 9 y no tener en el último carácter una letra
    String dniInput = "12345678";
    String nameInput = "Gabriel";
    String lastNameInput = "Jesús";

    // Rellenamos el formulario de añadir empleado con dni incorrecto
    PO_EmployeeView.fillAddEmployeeForm(driver, dniInput, nameInput, lastNameInput);

    String checkTextLength =
        PO_View.getP().getString("Error.add.employee.dniLength", PO_Properties.getSPANISH());

    String checkTextLastChar =
        PO_View.getP().getString("Error.add.employee.dniLastChar", PO_Properties.getSPANISH());

    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkTextLength);

    // Comprobamos que se muestra un único span de mensajes de error validación
    Assertions.assertEquals(1, result.size());
    // Comprobamos que el span tiene dos mensajes de error y que son los esperados
    String lengthValidationMsg = result.get(0).getText().split("\n")[0];
    String lastCharValidationMsg = result.get(0).getText().split("\n")[1];
    Assertions.assertEquals(checkTextLength, lengthValidationMsg);
    Assertions.assertEquals(checkTextLastChar, lastCharValidationMsg);
  }

  /**
   * Test de registro de un empleado con datos inválidos (dni ya registrado). Comprobamos que no se
   * registre el empleado y que salgan los mensajes de validación
   */
  @Test
  @Order(10)
  public void Prueba10() {
    String checkText = "Empleados";
    String dni = "12345678Z";
    String password = "@Dm1n1str@D0r";

    PO_LogInView.login(driver, dni, password, checkText);

    // Accedemos a la sección de añadir empleado
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[2]/a",
        "/html/body/nav/div/ul[1]/li[2]/div/a");

    // Dni incorrecto por tener 8 de longitud y no 9 y no tener en el último carácter una letra
    String dniInput = "12345678Z"; // DNI del propio admin
    String nameInput = "Gabriel";
    String lastNameInput = "Jesús";

    // Rellenamos el formulario de añadir empleado con dni ya registrado
    PO_EmployeeView.fillAddEmployeeForm(driver, dniInput, nameInput, lastNameInput);

    checkText =
        PO_View.getP().getString("Error.add.employee.dni.duplicate", PO_Properties.getSPANISH());

    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);

    // Comprobamos que se muestra un único span de mensajes de error validación
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /*
  PRUEBAS DEL EJERCICIO 4
   */


  // Registro de un vehículo con datos válidos
  @Test
  @Order(11)
  public void Prueba11VehiculoCorrecto() {
    PO_LogInView.login(driver,"12345678Z","@Dm1n1str@D0r","Empleados");
    //Pinchamos en la opción de menú de Vehículos
    PO_View.checkView(driver,"free","//*[@id=\"navbarDropdown\"]",0);
    //Esperamos a que aparezca la opción de añadir vehiculo: //*[@id="myNavbar"]/ul[1]/li[2]/div/a[2]
    PO_View.checkView(driver,"free","//a[contains(@href, 'vehicle/add')]",0);
    //Ahora vamos a rellenar el formulario con valores correctos
    String plate = "0123BCV";
    String model = "4x4";
    String brand = "Toyota";
    String chassisNumber = "KHGCM82633A901239";
    Vehicle.FuelType type = Vehicle.FuelType.HIBRIDO;

    PO_VehicleView.fillFormAddVehicle(driver, plate, model,brand,chassisNumber,type);
    //Comprobamos que aparece el vehículo  en la página
    String checkText = PO_VehicleView.getP().getString("list.vehicle.system", PO_Properties.getSPANISH());
    List<WebElement> elements = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, elements.get(0).getText());
  }


  // Registro de un vehículo con datos vacios
  @Test
  @Order(12)
  public void Prueba12VehiculoDatosVacios() {
    PO_LogInView.login(driver,"12345678Z","@Dm1n1str@D0r","Empleados");
    //Pinchamos en la opción de menú de Vehículos
    PO_View.checkView(driver,"free","//*[@id=\"navbarDropdown\"]",0);
    //Esperamos a que aparezca la opción de añadir vehiculo: //*[@id="myNavbar"]/ul[1]/li[2]/div/a[2]
    PO_View.checkView(driver,"free","//a[contains(@href, 'vehicle/add')]",0);
    //Ahora vamos a rellenar el formulario con valores vacios
    String plate = "";
    String model = "";
    String brand = "";
    String chassisNumber = "";
    Vehicle.FuelType type = Vehicle.FuelType.HIBRIDO;

    PO_VehicleView.fillFormAddVehicle(driver, plate, model,brand,chassisNumber,type);
    List<WebElement> result = PO_VehicleView.checkElementByKey(driver, "Error.empty", PO_Properties.getSPANISH());
    String checkText = PO_HomeView.getP().getString("Error.empty", PO_Properties.getSPANISH());
    Assertions.assertEquals(checkText, result.get(0).getText().split("\n")[0]);
    Assertions.assertEquals(checkText, result.get(1).getText().split("\n")[0]);
    Assertions.assertEquals(checkText, result.get(2).getText());
    Assertions.assertEquals(checkText, result.get(3).getText());

  }

  // Registro de un vehículo con matricula invalida
  @Test
  @Order(13)
  public void Prueba13MatriculaInvalida() {
    PO_LogInView.login(driver,"12345678Z","@Dm1n1str@D0r","Empleados");
    //Pinchamos en la opción de menú de Vehículos
    PO_View.checkView(driver,"free","//*[@id=\"navbarDropdown\"]",0);
    //Esperamos a que aparezca la opción de añadir vehiculo: //*[@id="myNavbar"]/ul[1]/li[2]/div/a[2]
    PO_View.checkView(driver,"free","//a[contains(@href, 'vehicle/add')]",0);
    //Ahora vamos a rellenar el formulario con valor de matricula incrrecta
    String plate = "000";
    String model = "4x4";
    String brand = "Toyota";
    String chassisNumber = "KHGCM82633A901239";
    Vehicle.FuelType type = Vehicle.FuelType.HIBRIDO;

    PO_VehicleView.fillFormAddVehicle(driver, plate, model,brand,chassisNumber,type);
    List<WebElement> result = PO_VehicleView.checkElementByKey(driver, "Error.vehicle.plate.invalidFormat", PO_Properties.getSPANISH());
    String checkText = PO_HomeView.getP().getString("Error.vehicle.plate.invalidFormat", PO_Properties.getSPANISH());
    Assertions.assertEquals(checkText, result.get(0).getText());

  }

  // Registro de un vehículo con bastidor inválido, menos de 17 caracteres
  @Test
  @Order(14)
  public void Prueba14BastidorInvalido() {
    PO_LogInView.login(driver,"12345678Z","@Dm1n1str@D0r","Empleados");
    //Pinchamos en la opción de menú de Vehículos
    PO_View.checkView(driver,"free","//*[@id=\"navbarDropdown\"]",0);
    //Esperamos a que aparezca la opción de añadir vehiculo: //*[@id="myNavbar"]/ul[1]/li[2]/div/a[2]
    PO_View.checkView(driver,"free","//a[contains(@href, 'vehicle/add')]",0);
    //Ahora vamos a rellenar el formulario con valor del bastidor incrrecto
    String plate = "0123BCV";
    String model = "4x4";
    String brand = "Toyota";
    String chassisNumber = "123";
    Vehicle.FuelType type = Vehicle.FuelType.HIBRIDO;

    PO_VehicleView.fillFormAddVehicle(driver, plate, model,brand,chassisNumber,type);
    List<WebElement> result = PO_VehicleView.checkElementByKey(driver, "Error.vehicle.chassisNumber.length", PO_Properties.getSPANISH());
    String checkText = PO_HomeView.getP().getString("Error.vehicle.chassisNumber.length", PO_Properties.getSPANISH());
    Assertions.assertEquals(checkText, result.get(0).getText());

  }

  // Registro de un vehículo con matrícula existente
  @Test
  @Order(15)
  public void Prueba15MatriculaExistente() {
    PO_LogInView.login(driver,"12345678Z","@Dm1n1str@D0r","Empleados");
    //Pinchamos en la opción de menú de Vehículos
    PO_View.checkView(driver,"free","//*[@id=\"navbarDropdown\"]",0);
    //Esperamos a que aparezca la opción de añadir vehiculo: //*[@id="myNavbar"]/ul[1]/li[2]/div/a[2]
    PO_View.checkView(driver,"free","//a[contains(@href, 'vehicle/add')]",0);
    //Ahora vamos a rellenar el formulario con valor de la matrícula repetida
    String plate = "9101GHJ";
    String model = "4x4";
    String brand = "Toyota";
    String chassisNumber = "BHGCM82633A123456";
    Vehicle.FuelType type = Vehicle.FuelType.HIBRIDO;

    PO_VehicleView.fillFormAddVehicle(driver, plate, model,brand,chassisNumber,type);


    List<WebElement> result = PO_VehicleView.checkElementByKey(driver, "Error.vehicle.plate.duplicate",
        PO_Properties.getSPANISH() );
    //Comprobamos el error de matricula repetida.
    String checkText = PO_HomeView.getP().getString("Error.vehicle.plate.duplicate",
        PO_Properties.getSPANISH());
    Assertions.assertEquals(checkText , result.get(0).getText());

  }

  // Registro de un vehículo con bastidor existente
  @Test
  @Order(16)
  public void Prueba16BastidorExistente() {
    PO_LogInView.login(driver,"12345678Z","@Dm1n1str@D0r","Empleados");
    //Pinchamos en la opción de menú de Vehículos
    PO_View.checkView(driver,"free","//*[@id=\"navbarDropdown\"]",0);
    //Esperamos a que aparezca la opción de añadir vehiculo: //*[@id="myNavbar"]/ul[1]/li[2]/div/a[2]
    PO_View.checkView(driver,"free","//a[contains(@href, 'vehicle/add')]",0);
    //Ahora vamos a rellenar el formulario con valor del bastidor repetida
    String plate = "9101GHW";
    String model = "4x4";
    String brand = "Toyota";
    String chassisNumber = "5HGCM82633A901234";
    Vehicle.FuelType type = Vehicle.FuelType.HIBRIDO;

    PO_VehicleView.fillFormAddVehicle(driver, plate, model,brand,chassisNumber,type);


    List<WebElement> result = PO_VehicleView.checkElementByKey(driver, "Error.vehicle.chassisNumber.duplicate",
        PO_Properties.getSPANISH() );
    //Comprobamos el error de bastidor repetido.
    String checkText = PO_HomeView.getP().getString("Error.vehicle.chassisNumber.duplicate",
        PO_Properties.getSPANISH());
    Assertions.assertEquals(checkText , result.get(0).getText());

  }

  /**
   * Test de lista de empleados. Se accede a la lista de empleados y se comprueba que están todos
   * los empleados del sistema
   */
  @Test
  @Order(17)
  public void Prueba17() {
    String checkText = "Empleados";
    String dni = "12345678Z";
    String password = "@Dm1n1str@D0r";

    PO_LogInView.login(driver, dni, password, checkText);

    // Accedemos a la sección de lista de empleados
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[2]/a",
        "/html/body/nav/div/ul[1]/li[2]/div/a[2]");

    // Comprobamos que efectivamente estamos en dicha sección
    checkText = PO_View.getP().getString("employees.list.message", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Comprobamos la paginación
    List<String> allEmployees = new ArrayList<>();

    boolean hasNextPage = true;

    // Bucle para contar el número de empleados totales página por página
    while (hasNextPage) {
      // Obtenemos los empleados en la página actual
      WebElement table = PO_View.checkElementBy(driver, "id", "employeesTable").get(0);
      // Obtenemos todas las filas de la tabla
      List<WebElement> rows = table.findElements(By.tagName("tr"));

      // Recorremos todas las filas menos la primera porque es la cabecera de la tabla
      for (int i = 1; i < rows.size(); i++) {
        allEmployees.add(rows.get(i).getText());
      }

      // Intentamos avanzar a la siguiente página si existe
      // Utilizamos findElements del driver en vez de checkElementBy pues
      // llegará un punto (última iteración) que no existirá dicho elemento
      // y no queremos que el test espere por él hasta encontrarlo o fallar
      List<WebElement> nextPageButton = driver.findElements(By.id("nextPage"));
      if (!nextPageButton.isEmpty() && nextPageButton.get(0).isDisplayed()) {
        nextPageButton.get(0).click();
      } else {
        hasNextPage = false;
      }
    }

    // Validamos que todos los empleados esperados están en la lista
    int totalEmployees = employeesService.getNumberOfEmployees();

    Assertions.assertEquals(totalEmployees, allEmployees.size());
  }

  /**
   * Prueba de edición de empleado. Se inicia sesión como admin, se edita un empleado y se comprueba
   * que dicho empleado ha sido modificado en base de datos. Además, se entra con la sesión del
   * empleado para comprobar si se han llevado a cabo los cambios
   */
  @Test
  @Order(18)
  public void Prueba18() {
    String checkText = "Empleados";
    String dni = "12345678Z";
    String password = "@Dm1n1str@D0r";
    PO_LogInView.login(driver, dni, password, checkText);

    // Accedemos a la lista de empleados
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[2]/a",
        "/html/body/nav/div/ul[1]/li[2]/div/a[2]");

    // Accedemos a editar el segundo empleado
    PO_View.checkElementBy(driver, "text",
        PO_View.getP().getString("modify", PO_Properties.getSPANISH())).get(1).click();

    String currentUrl = driver.getCurrentUrl();
    // Obtenemos el id del empleado de la URL
    String idString = currentUrl.substring(currentUrl.lastIndexOf("/") + 1);
    Long id = Long.parseLong(idString);

    String newDNI = "12345678W";
    String newName = "TestName";
    String newSurname = "TestSurname";
    // Ponemos rol de admin
    String newRole = rolesService.getRoles()[1];

    PO_EmployeeView.fillEditEmployeeForm(driver, newDNI, newName, newSurname, newRole);
    // Comprobamos que estamos en el apartado de lista
    checkText = PO_View.getP().getString("employees.list.message", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());
    // Comprobamos que el empleado tiene ahora esos datos
    Employee emp = employeesService.getEmployee(id)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

    Assertions.assertEquals(newDNI, emp.getDni());
    Assertions.assertEquals(newName, emp.getName());
    Assertions.assertEquals(newSurname, emp.getSurname());
    Assertions.assertEquals(newRole, employeesService.getEmployeeRole(id));

    // Cerramos sesión e iniciamos sesión con el nuevo usuario comprobando que efectivamente es admin
    PO_LogInView.logout(driver, PO_Properties.getSPANISH());
    // La contraseña no debería haber cambiado así que tiene que ser "Us3r@1-PASSW"
    PO_LogInView.login(driver, newDNI, "Us3r@1-PASSW", "Trayectos");

    // Comprobamos que es admin si tiene el menú de añadir empleado
    String sectionName =
        PO_View.getP().getString("navbar.add.employees", PO_Properties.getSPANISH());
    boolean isAdmin = PO_NavView.checkIfHasSection(driver, "employeesDropDown", sectionName);
    Assertions.assertTrue(isAdmin);
  }

  /**
   * Prueba de edición de empleado. Se inicia sesión como admin, se edita un empleado con un DNI ya
   * registrado en el sistema y con nombre y apellidos vacíos. Se comprueba entonces que el empleado
   * no ha sido modificado y que salen los correspondientes mensajes de error
   */
  @Test
  @Order(19)
  public void Prueba19() {
    String checkText = "Empleados";
    String dni = "12345678Z";
    String password = "@Dm1n1str@D0r";
    PO_LogInView.login(driver, dni, password, checkText);

    // Accedemos a la lista de empleados
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[2]/a",
        "/html/body/nav/div/ul[1]/li[2]/div/a[2]");

    // Accedemos a editar el segundo empleado
    PO_View.checkElementBy(driver, "text",
        PO_View.getP().getString("modify", PO_Properties.getSPANISH())).get(1).click();

    String currentUrl = driver.getCurrentUrl();
    // Obtenemos el id del empleado de la URL
    String idString = currentUrl.substring(currentUrl.lastIndexOf("/") + 1);
    Long id = Long.parseLong(idString);

    Employee originalEmp = employeesService.getEmployee(id)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

    String originalDNI = originalEmp.getDni();
    String originalName = originalEmp.getName();
    String originalSurname = originalEmp.getSurname();
    String originalRole = employeesService.getEmployeeRole(id);

    String newDNI = "12345678Z";
    String newName = "";
    String newSurname = "";
    // Ponemos rol de admin
    String newRole = rolesService.getRoles()[1];

    PO_EmployeeView.fillEditEmployeeForm(driver, newDNI, newName, newSurname, newRole);

    // Comprobamos que estamos en el apartado de editar usuario
    checkText = PO_View.getP().getString("edit.employee", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Comprobamos que el empleado mantiene sus datos originales
    Employee emp = employeesService.getEmployee(id)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

    Assertions.assertEquals(originalDNI, emp.getDni());
    Assertions.assertEquals(originalName, emp.getName());
    Assertions.assertEquals(originalSurname, emp.getSurname());
    Assertions.assertEquals(originalRole, employeesService.getEmployeeRole(id));

    // Comprobamos que salen los mensajes de error
    String duplicateError =
        PO_View.getP().getString("Error.add.employee.dni.duplicate", PO_Properties.getSPANISH());
    String emptyError = PO_View.getP().getString("Error.empty", PO_Properties.getSPANISH());

    List<WebElement> duplicateErrorsSpan = PO_View.checkElementBy(driver, "text", duplicateError);
    List<WebElement> emptyErrorsSpan = PO_View.checkElementBy(driver, "text", emptyError);

    // Comprobamos que se muestra un único span de mensajes de error de duplicado
    Assertions.assertEquals(1, duplicateErrorsSpan.size());
    Assertions.assertEquals(duplicateError, duplicateErrorsSpan.get(0).getText());
    // Comprobamos que se muestran ds span de mensajes de error por valor vacío
    Assertions.assertEquals(2, emptyErrorsSpan.size());
    Assertions.assertEquals(emptyError, emptyErrorsSpan.get(0).getText());
    Assertions.assertEquals(emptyError, emptyErrorsSpan.get(1).getText());
  }

  /*
  PRUEBAS DEL EJERCICIO 7
   */

  // Listado de vehículos
  @Test
  @Order(20)
  public void Prueba20ListadoVehiculos() {
    PO_LogInView.login(driver,"12345678Z","@Dm1n1str@D0r","Empleados");
    //Pinchamos en la opción de menú de Vehículos
    PO_View.checkView(driver,"free","//*[@id=\"navbarDropdown\"]",0);
    //Esperamos a que aparezca la opción de lista de vehiculos
    PO_View.checkView(driver,"free","//a[contains(@href, 'vehicle/list')]",0);

    //Comprobamos que entramos en la lista de vehículos
    List<WebElement> result = PO_VehicleView.checkElementByKey(driver, "list.vehicle.view",
        PO_Properties.getSPANISH() );
    String checkText = PO_HomeView.getP().getString("list.vehicle.view",
        PO_Properties.getSPANISH());
    Assertions.assertEquals(checkText, result.get(0).getText());

  }

  /*
  PRUEBAS DEL EJERCICIO 8
   */

  // Borrar primer vehículo de la lista, actualizar la lista y ver que desaparece
  @Test
  @Order(21)
  public void Prueba21BorrarPrimerVehiculo() {
    PO_LogInView.login(driver, "12345678Z", "@Dm1n1str@D0r", "Empleados");

    //Pinchamos en la opción de menú de Vehículos
    PO_View.checkView(driver,"free","//*[@id=\"navbarDropdown\"]",0);
    //Esperamos a que aparezca la opción de añadir vehiculo: //*[@id="myNavbar"]/ul[1]/li[2]/div/a[2]
    PO_View.checkView(driver,"free","//a[contains(@href, 'vehicle/add')]",0);
    //Ahora vamos a rellenar el formulario con valores correctos
    String plate = "0123BCQ";
    String model = "4x4";
    String brand = "Toyota";
    String chassisNumber = "KHGCM82633A901230";
    Vehicle.FuelType type = Vehicle.FuelType.HIBRIDO;

    PO_VehicleView.fillFormAddVehicle(driver, plate, model,brand,chassisNumber,type);

    // Ir a la lista de vehículos
    PO_View.checkView(driver, "free", "//*[@id=\"navbarDropdown\"]", 0);
    PO_View.checkView(driver, "free", "//a[contains(@href, 'vehicle/list')]", 0);


    // Obtener la lista de vehículos antes de eliminar
    List<WebElement> checkboxes = PO_VehicleView.getVehicleCheckboxes(driver);
    int index = 0;

    // Obtener el ID del vehículo a eliminar
    Long vehicleId = Long.parseLong(checkboxes.get(index).getAttribute("value"));

    // Seleccionar el primer vehículo de la lista
    checkboxes.get(index).click();

    // Pulsar el botón de eliminar
    PO_VehicleView.clickDeleteButton(driver);

    // Verificar que el vehículo desaparece de la lista consultando el repositorio
    Assertions.assertTrue(vehiclesService.isVehicleInRepository(vehicleId).isEmpty());

  }

  // Borrar el último vehículo de la lista, actualizar la lista y ver que desaparece
  @Test
  @Order(22)
  public void Prueba22BorrarUltimoVehiculo() {
    PO_LogInView.login(driver, "12345678Z", "@Dm1n1str@D0r", "Empleados");

    // Ir a la lista de vehículos
    PO_View.checkView(driver, "free", "//*[@id=\"navbarDropdown\"]", 0);
    PO_View.checkView(driver, "free", "//a[contains(@href, 'vehicle/list')]", 0);

    // Obtener la lista de vehículos antes de eliminar
    List<WebElement> checkboxes = PO_VehicleView.getVehicleCheckboxes(driver);
    int index = checkboxes.size() - 1;

    // Obtener el ID del vehículo a eliminar
    Long vehicleId = Long.parseLong(checkboxes.get(index).getAttribute("value"));

    // Seleccionar el último vehículo de la lista
    checkboxes.get(index).click();

    // Pulsar el botón de eliminar
    PO_VehicleView.clickDeleteButton(driver);

    // Verificar que el vehículo desaparece de la lista consultando el repositorio
    Assertions.assertTrue(vehiclesService.isVehicleInRepository(vehicleId).isEmpty());


  }

  // Borrar tres vehículos y ver que se actualiza la vista y se han borrado
  @Test
  @Order(23)
  public void Prueba23BorrarTresVehiculos() {
    PO_LogInView.login(driver, "12345678Z", "@Dm1n1str@D0r", "Empleados");

    // AÑADIR 3 VEHÍCULOS
    //PRIMER VEHÍCULO
    //Pinchamos en la opción de menú de Vehículos
    PO_View.checkView(driver,"free","//*[@id=\"navbarDropdown\"]",0);
    //Esperamos a que aparezca la opción de añadir vehiculo: //*[@id="myNavbar"]/ul[1]/li[2]/div/a[2]
    PO_View.checkView(driver,"free","//a[contains(@href, 'vehicle/add')]",0);

    //Ahora vamos a rellenar el formulario con valores correctos
    String plate = "1123BCQ";
    String model = "4x4";
    String brand = "Toyota";
    String chassisNumber = "KHGCM82633A901211";
    Vehicle.FuelType type = Vehicle.FuelType.HIBRIDO;

    PO_VehicleView.fillFormAddVehicle(driver, plate, model,brand,chassisNumber,type);

    //SEGUNDO VEHÍCULO
    //Pinchamos en la opción de menú de Vehículos
    PO_View.checkView(driver,"free","//*[@id=\"navbarDropdown\"]",0);
    //Esperamos a que aparezca la opción de añadir vehiculo: //*[@id="myNavbar"]/ul[1]/li[2]/div/a[2]
    PO_View.checkView(driver,"free","//a[contains(@href, 'vehicle/add')]",0);
    //Ahora vamos a rellenar el formulario con valores correctos
    String plate1 = "0123BRY";
    String model1 = "4x4";
    String brand1 = "Toyota";
    String chassisNumber1 = "KHGCM82633A901237";
    Vehicle.FuelType type1 = Vehicle.FuelType.HIBRIDO;

    PO_VehicleView.fillFormAddVehicle(driver, plate1, model1,brand1,chassisNumber1,type1);

    //TERCER VEHÍCULO
    //Pinchamos en la opción de menú de Vehículos
    PO_View.checkView(driver,"free","//*[@id=\"navbarDropdown\"]",0);
    //Esperamos a que aparezca la opción de añadir vehiculo: //*[@id="myNavbar"]/ul[1]/li[2]/div/a[2]
    PO_View.checkView(driver,"free","//a[contains(@href, 'vehicle/add')]",0);
    //Ahora vamos a rellenar el formulario con valores correctos
    String plate2 = "0123BHL";
    String model2 = "4x4";
    String brand2 = "Toyota";
    String chassisNumber2 = "KHGCM82633A901231";
    Vehicle.FuelType type2 = Vehicle.FuelType.HIBRIDO;

    PO_VehicleView.fillFormAddVehicle(driver, plate2, model2,brand2,chassisNumber2,type2);


    // Ir a la lista de vehículos
    PO_View.checkView(driver, "free", "//*[@id=\"navbarDropdown\"]", 0);
    PO_View.checkView(driver, "free", "//a[contains(@href, 'vehicle/list')]", 0);

    // Obtener la lista de vehículos antes de eliminar
    List<WebElement> checkboxes = PO_VehicleView.getVehicleCheckboxes(driver);
    int index = 0;
    int index1 = 1;
    int index2 = 2;

    // Obtener el ID del vehículo a eliminar
    Long vehicleId = Long.parseLong(checkboxes.get(index).getAttribute("value"));
    Long vehicleId1 = Long.parseLong(checkboxes.get(index1).getAttribute("value"));
    Long vehicleId2 = Long.parseLong(checkboxes.get(index2).getAttribute("value"));

    // Seleccionar el último vehículo de la lista
    checkboxes.get(index).click();
    checkboxes.get(index1).click();
    checkboxes.get(index2).click();

    // Pulsar el botón de eliminar
    PO_VehicleView.clickDeleteButton(driver);

    // Verificar que el vehículo desaparece de la lista consultando el repositorio
    Assertions.assertTrue(vehiclesService.isVehicleInRepository(vehicleId).isEmpty());
    Assertions.assertTrue(vehiclesService.isVehicleInRepository(vehicleId1).isEmpty());
    Assertions.assertTrue(vehiclesService.isVehicleInRepository(vehicleId2).isEmpty());

  }

  /**
   * Mostrar el listado de trayectos y comprobar que se muestran todos los que tienen al empleado
   * actual asignado como conductor.
   */
  @Test
  @Order(24)
  public void Prueba24() {
    String dni = "10000001L";
    String password = "Us3r@12-PASSW";
    PO_LogInView.login(driver, dni, password, "Trayectos");

    // Accede al menú de "Mis trayectos"
    PO_NavView.accessSection(driver,"//*[@id=\"routesDropdown\"]",
            "/html/body/nav/div/ul[1]/li[4]/div/a[2]");

    // Verificamos que estamos en la vista correcta
    String checkText = PO_View.getP().getString("route.list", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Obtenemos todas las rutas del usuario paginando
    List<String> allRouteIds = new ArrayList<>();
    int pageCount = 0;

    boolean hasNextPage = true;
    while (hasNextPage) {
      WebElement table = PO_View.checkElementBy(driver, "id", "routesForEmployeeTable").get(0);
      List<WebElement> rows = table.findElements(By.tagName("tr"));

      // Comprobamos que hay filas (la primera es el encabezado)
      Assertions.assertTrue(rows.size() > 1);

      // Validamos que haya 5 elementos por página como máximo
      int routesThisPage = rows.size() - 1;
      Assertions.assertTrue(routesThisPage <= 5);

      // Recorremos las rutas (omitimos el encabezado)
      for (int i = 1; i < rows.size(); i++) {
        List<WebElement> cols = rows.get(i).findElements(By.tagName("td"));
        Assertions.assertEquals(7, cols.size());

        String routeId = cols.get(0).getText();

        // Validaciones básicas de contenido
        Assertions.assertFalse(routeId.isEmpty());

        allRouteIds.add(routeId);
      }

      pageCount++;

      // Intentar avanzar de página si hay botón "nextPage"
      List<WebElement> nextButton = driver.findElements(By.id("nextPage"));
      if (!nextButton.isEmpty() && nextButton.get(0).isDisplayed()) {
        nextButton.get(0).click();
      } else {
        hasNextPage = false;
      }
    }

    // Verificamos que se mostraron al menos 3 páginas
    Assertions.assertTrue(pageCount >= 3, "Se esperaban al menos 3 páginas de trayectos.");

    // Verificamos que hay trayectos registrados para ese usuario
    Employee employee = employeesService.getEmployeeByDni(dni);
    int expectedRoutes = routesService.getNumberOfRoutesForEmployee(employee);

    Assertions.assertEquals(expectedRoutes, allRouteIds.size());
  }


  /**
   * Prueba de inicio de trayecto válido
   */
  @Test
  @Order(25)
  public void Prueba25(){
    String dni = "10000001S";
    String password = "Us3r@1-PASSW";
    PO_LogInView.login(driver, dni, password, "Trayectos");

    Employee employee = employeesService.getEmployeeByDni(dni);

    PO_NavView.accessSection(driver,"//*[@id=\"routesDropdown\"]",
            "/html/body/nav/div/ul[1]/li[4]/div/a[1]");


    int routesBefore = routesService.getNumberOfRoutesForEmployee(employee);

    int pos = vehiclesService.getNumberOfVehicles()-1;

    // Seleccionamos el último vehículo de la lista y pulsamos el botón de iniciar ruta
    PO_RouteView.selectPlateForNewRoute(driver, pos);
    PO_View.checkElementBy(driver, "free", "/html/body/div/form/div[2]/div/button").get(0)
            .click();

    List<String> allRouteIds = new ArrayList<>();
    boolean hasNextPage = true;

    while (hasNextPage) {
      WebElement table = PO_View.checkElementBy(driver, "id", "routesForEmployeeTable").get(0);
      List<WebElement> rows = table.findElements(By.tagName("tr"));

      for (int i = 1; i < rows.size(); i++) {
        List<WebElement> cols = rows.get(i).findElements(By.tagName("td"));
        allRouteIds.add(cols.get(0).getText());
      }

      List<WebElement> nextButton = driver.findElements(By.id("nextPage"));
      if (!nextButton.isEmpty() && nextButton.get(0).isDisplayed()) {
        nextButton.get(0).click();
      } else {
        hasNextPage = false;
      }
    }

    // Nos aseguramos de que estamos en la página correcta
    String checkText = PO_RouteView.getP().getString("route.list.p1", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Nos aseguramos de que se añadió exactamente 1 ruta
    Assertions.assertEquals(routesBefore + 1, allRouteIds.size());


  }

  /**
   * Prueba de inicio de un trayecto no válido, el empleado tiene otro trayecto en curso
   */
  @Test
  @Order(26)
  public void Prueba26(){
    String checkText = "Trayectos";
    String dni = "10000001N";
    String password = "Us3r@14-PASSW";
    PO_LogInView.login(driver, dni, password, checkText);

    PO_NavView.accessSection(driver,"/html/body/nav/div/ul[1]/li[4]/a",
        "/html/body/nav/div/ul[1]/li[4]/div/a[1]");


    PO_RouteView.selectPlateForNewRoute(driver, 0);
    PO_View.checkElementBy(driver, "free", "/html/body/div/form/div[2]/div/button").get(0)
            .click();

    checkText = PO_RouteView.getP().getString("Error.route.alreadyOnRoute", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /**
   * Prueba de inicio de trayecto no válido, el vehículo el vehículo para el que se quiere iniciar el trayecto no está
   * disponible
   */
  @Test
  @Order(27)
  public void Prueba27(){
    // Seleccionamos empleado sin rutas activas
    String checkText = "Trayectos";
    String dni = "10000001P";
    String password = "Us3r@15-PASSW";
    PO_LogInView.login(driver, dni, password, checkText);

    PO_NavView.accessSection(driver,"/html/body/nav/div/ul[1]/li[4]/a",
        "/html/body/nav/div/ul[1]/li[4]/div/a[1]");

    int pos = 1;


    // Seleccionamos un vehículo
    PO_RouteView.selectPlateForNewRoute(driver, pos);
    PO_View.checkElementBy(driver, "free", "/html/body/div/form/div[2]/div/button").get(0)
            .click();

    // Volvemos a Iniciar trayecto
    PO_NavView.accessSection(driver,"/html/body/nav/div/ul[1]/li[4]/a",
            "/html/body/nav/div/ul[1]/li[4]/div/a[1]");

    PO_RouteView.selectPlateForNewRoute(driver, pos);
    PO_View.checkElementBy(driver, "free", "/html/body/div/form/div[2]/div/button").get(0)
            .click();

    checkText = PO_RouteView.getP().getString("Error.route.notFreeVehicle", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertTrue(result.get(0).getText().contains(checkText));

  }

  /**
   * Test para realizar un repostaje válido. Se accede al menu para añadir repostaje
   */
  @Test
  @Order(28)
  public void Prueba28() {
    String checkText = "Trayectos";
    String dni =  "10000001K";
    String password = "Us3r@11-PASSW";
    PO_LogInView.login(driver, dni, password, checkText);

    // Accedemos al formulario para registrar los repostajes
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[3]/a",
        "/html/body/nav/div/ul[1]/li[3]/div/a[1]");

    String stationName = "Shell Fuenlabrada";
    double pricePerUnit = 13.5;
    double quantity = 12.5;
    boolean isTankFull = true;
    double odometer = 200;
    String observations = "The clerk was a jerk";

    // Rellenamos el formulario
    PO_RefuelView.fillAddRefuelForm(driver, stationName, pricePerUnit, quantity, isTankFull, odometer, observations);

    checkText = PO_RefuelView.getP().getString("refuel.list.title1", PO_Properties.getSPANISH());
    checkText += " 7890RST " + PO_RefuelView.getP().getString("refuel.list.title2",
        PO_Properties.getSPANISH());
    List<WebElement> result = PO_RefuelView.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /**
   * Prueba de registro de repostaje inválido, no existe ningún trayecto activo.
   */
  @Test
  @Order(29)
  public void Prueba29() {

    String checkText = "Trayectos";
    String dni =  "10000001G";
    String password = "Us3r@7-PASSW";
    PO_LogInView.login(driver, dni, password, checkText);

    // Accedemos al formulario para registrar los repostajes
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[3]/a",
        "/html/body/nav/div/ul[1]/li[3]/div/a[1]");

    String stationName = "Shell Fuenlabrada";
    double pricePerUnit = 13.5;
    double quantity = 12.5;
    boolean isTankFull = true;
    double odometer = 300;
    String observations = "The clerk was a jerk";

    // Rellenamos el formulario
    PO_RefuelView.fillAddRefuelForm(driver, stationName, pricePerUnit, quantity, isTankFull, odometer, observations);
    checkText = PO_RefuelView.getP().getString("Error.refuel.employee.route.notActive",
        PO_Properties.getSPANISH());
    List<WebElement> result = PO_RefuelView.checkElementBy(driver,"text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /**
   * Prueba de registro de repostaje inválido, campos de texto vacios
   */
  @Test
  @Order(30)
  public void Prueba30() {

    String checkText = "Trayectos";
    String dni =  "10000001L";
    String password = "Us3r@12-PASSW";
    PO_LogInView.login(driver, dni, password, checkText);

    // Accedemos al formulario para registrar los repostajes
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[3]/a",
        "/html/body/nav/div/ul[1]/li[3]/div/a[1]");

    String stationName = "";
    double pricePerUnit = 0;
    double quantity = 0;
    boolean isTankFull = true;
    double odometer = 0;
    String observations = "The clerk was a jerk";

    // Rellenamos el formulario
    PO_RefuelView.fillAddRefuelForm(driver, stationName, pricePerUnit, quantity, isTankFull, odometer, observations);
    List<WebElement> result = PO_RefuelView.checkElementByKey(driver, "Error.empty",
        PO_Properties.getSPANISH());
    checkText = PO_RefuelView.getP().getString("Error.empty",
        PO_Properties.getSPANISH());
    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /**
   * Prueba de registro de repostaje inválido, campos numéricos negativos.
   */
  @Test
  @Order(31)
  public void Prueba31() {
    // Iniciamos sesión con un usuario con el rol de Empleado
    PO_LogInView.login(driver, "10000001S", "Us3r@1-PASSW", "Trayectos");

    // Accedemos al formulario para registrar los repostajes
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[3]/a",
        "/html/body/nav/div/ul[1]/li[3]/div/a[1]");

    String stationName = "Shell Fuenlabrada";
    double pricePerUnit = -20;
    double quantity = -10;
    boolean isTankFull = true;
    double odometer = 400;
    String observations = "The clerk was a jerk";

    // Rellenamos el formulario
    PO_RefuelView.fillAddRefuelForm(driver, stationName, pricePerUnit, quantity, isTankFull, odometer, observations);

    String expectedErrorQuantity = PO_RefuelView.getP().getString("Error.refuel.quantity.negative",
        PO_Properties.getSPANISH());

    String expectedErrorPricePerUnit = PO_RefuelView.getP().getString("Error.refuel.pricePerUnit.negative",
        PO_Properties.getSPANISH());

    List<WebElement> errorMessages = PO_RefuelView.checkElementByKey(driver, "Error.refuel.quantity.negative",
        PO_Properties.getSPANISH());
    errorMessages.addAll(PO_RefuelView.checkElementByKey(driver, "Error.refuel.pricePerUnit.negative",
        PO_Properties.getSPANISH()));

    List<String> errorTexts = errorMessages.stream().map(WebElement::getText).toList();

    Assertions.assertTrue(errorTexts.contains(expectedErrorQuantity), "Quantity error message not found");
    Assertions.assertTrue(errorTexts.contains(expectedErrorPricePerUnit), "Price per unit error message not found");
  }

  /**
   * Prueba de registro de repostaje inválido, valor del odometraje menor que el del inicio del trayecto.
   */
  @Test
  @Order(32)
  public void Prueba32() {
    // Iniciamos sesión con un usuario con el rol de Empleado
    String checkText = "Trayectos";
    String dni =  "10000001K";
    String password = "Us3r@11-PASSW";
    PO_LogInView.login(driver, dni, password, checkText);

    // Accedemos al formulario para registrar los repostajes
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[3]/a",
            "/html/body/nav/div/ul[1]/li[3]/div/a[1]");

    String stationName = "Shell Fuenlabrada";
    double pricePerUnit = 13.5;
    double quantity = 12.5;
    boolean isTankFull = true;
    double odometer = 0;
    String observations = "The clerk was a jerk";

    // Rellenamos el formulario
    PO_RefuelView.fillAddRefuelForm(driver, stationName, pricePerUnit, quantity, isTankFull, odometer, observations);
    checkText = PO_RefuelView.getP().getString("Error.refuel.odometer.higherOnStart",
            PO_Properties.getSPANISH());
    List<WebElement> result = PO_RefuelView.checkElementBy(driver, "text",  checkText);

    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /**
   * Prueba de registro de fin de trayecto válido
   */
  @Test
  @Order(33)
  public void Prueba33() {
    // Usuario sin ruta activa
    String dni =  "10000001S";
    String password = "Us3r@1-PASSW";
    PO_LogInView.login(driver, dni, password, "Trayectos");

    PO_NavView.accessSection(driver,"//*[@id=\"routesDropdown\"]",
            "/html/body/nav/div/ul[1]/li[4]/div/a[1]");



    // Seleccionamos el primer vehículo y pulsamos el botón de iniciar ruta
    PO_RouteView.selectPlateForNewRoute(driver, 0);
    PO_View.checkElementBy(driver, "free", "/html/body/div/form/div[2]/div/button").get(0)
            .click();


    // Accedemos al apartado de mis trayectos
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[4]/a", "/html/body/nav/div/ul[1]/li[4]/div/a[2]");



    // Intentar avanzar de página si hay botón "nextPage"
    boolean hasNextPage = true;
    while (hasNextPage) {
      WebElement table = PO_View.checkElementBy(driver, "id", "routesForEmployeeTable").get(0);
      List<WebElement> rows = table.findElements(By.tagName("tr"));

      // Comprobamos que hay filas (la primera es el encabezado)
      Assertions.assertTrue(rows.size() > 1);

      // Validamos que haya 5 elementos por página como máximo
      int routesThisPage = rows.size() - 1;
      Assertions.assertTrue(routesThisPage <= 5);



      // Intentar avanzar de página si hay botón "nextPage"
      List<WebElement> nextButton = driver.findElements(By.id("nextPage"));
      if (!nextButton.isEmpty() && nextButton.get(0).isDisplayed()) {
        nextButton.get(0).click();
      } else {
        hasNextPage = false;
      }
    }

    // Seleccionamos el botón de finalizar trayecto
    PO_View.checkElementBy(driver, "free", "/html/body/div/div[1]/table/tbody/tr/td[7]/a").get(0)
            .click();



    String buttonText = PO_RefuelView.getP().getString("route.end.title",
            PO_Properties.getSPANISH());
    PO_View.checkElementBy(driver, "text", buttonText).get(0).click();

    // Rellenamos el formulario con datos válidos
    String odometerValue = "235000.0";
    PO_RouteView.fillEndRouteForm(driver, odometerValue, "");

    Assertions.assertNull(routesService.getActiveRouteByEmployeeDni(dni));
  }

  /**
   * Prueba de registro de fin de trayecto inválido. Odómetro vacío
   */
  @Test
  @Order(34)
  public void Prueba34() {

    // Usuario sin ruta activa
    String dni =  "10000001S";
    String password = "Us3r@1-PASSW";
    PO_LogInView.login(driver, dni, password, "Trayectos");

    PO_NavView.accessSection(driver,"//*[@id=\"routesDropdown\"]",
            "/html/body/nav/div/ul[1]/li[4]/div/a[1]");



    // Seleccionamos el primer vehículo y pulsamos el botón de iniciar ruta
    PO_RouteView.selectPlateForNewRoute(driver, 0);
    PO_View.checkElementBy(driver, "free", "/html/body/div/form/div[2]/div/button").get(0)
            .click();


    // Accedemos al apartado de mis trayectos
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[4]/a", "/html/body/nav/div/ul[1]/li[4]/div/a[2]");



    // Intentar avanzar de página si hay botón "nextPage"
    boolean hasNextPage = true;
    while (hasNextPage) {
      WebElement table = PO_View.checkElementBy(driver, "id", "routesForEmployeeTable").get(0);
      List<WebElement> rows = table.findElements(By.tagName("tr"));

      // Comprobamos que hay filas (la primera es el encabezado)
      Assertions.assertTrue(rows.size() > 1);

      // Validamos que haya 5 elementos por página como máximo
      int routesThisPage = rows.size() - 1;
      Assertions.assertTrue(routesThisPage <= 5);

      // Recorremos las rutas (omitimos el encabezado)



      // Intentar avanzar de página si hay botón "nextPage"
      List<WebElement> nextButton = driver.findElements(By.id("nextPage"));
      if (!nextButton.isEmpty() && nextButton.get(0).isDisplayed()) {
        nextButton.get(0).click();
      } else {
        hasNextPage = false;
      }
    }

    // Seleccionamos el botón de finalizar trayecto
    PO_View.checkElementBy(driver, "free", "/html/body/div/div[1]/table/tbody/tr/td[7]/a").get(0)
            .click();



    String buttonText = PO_RefuelView.getP().getString("route.end.title",
            PO_Properties.getSPANISH());
    PO_View.checkElementBy(driver, "text", buttonText).get(0).click();

    // Rellenamos el formulario con datos inválidos
    PO_RouteView.fillEndRouteForm(driver, "", "");

    String checkText = PO_HomeView.getP().getString("Error.empty", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);

    // Comprobamos que sale el error y que no ha finalizado el trayecto
    Assertions.assertEquals(checkText, result.get(0).getText());
    Assertions.assertNotNull(routesService.getActiveRouteByEmployeeDni(dni));
  }

  /**
   * Prueba de registro de fin de trayecto inválido. Odómetro negativo
   */
  @Test
  @Order(35)
  public void Prueba35() {
    // Usuario sin ruta activa
    String dni =  "10000001S";
    String password = "Us3r@1-PASSW";
    PO_LogInView.login(driver, dni, password, "Trayectos");

    PO_NavView.accessSection(driver,"//*[@id=\"routesDropdown\"]",
            "/html/body/nav/div/ul[1]/li[4]/div/a[1]");



    // Seleccionamos el primer vehículo y pulsamos el botón de iniciar ruta
    PO_RouteView.selectPlateForNewRoute(driver, 0);
    PO_View.checkElementBy(driver, "free", "/html/body/div/form/div[2]/div/button").get(0)
            .click();


    // Accedemos al apartado de mis trayectos
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[4]/a", "/html/body/nav/div/ul[1]/li[4]/div/a[2]");



    // Intentar avanzar de página si hay botón "nextPage"
    boolean hasNextPage = true;
    while (hasNextPage) {
      WebElement table = PO_View.checkElementBy(driver, "id", "routesForEmployeeTable").get(0);
      List<WebElement> rows = table.findElements(By.tagName("tr"));

      // Comprobamos que hay filas (la primera es el encabezado)
      Assertions.assertTrue(rows.size() > 1);

      // Validamos que haya 5 elementos por página como máximo
      int routesThisPage = rows.size() - 1;
      Assertions.assertTrue(routesThisPage <= 5);

      // Recorremos las rutas (omitimos el encabezado)



      // Intentar avanzar de página si hay botón "nextPage"
      List<WebElement> nextButton = driver.findElements(By.id("nextPage"));
      if (!nextButton.isEmpty() && nextButton.get(0).isDisplayed()) {
        nextButton.get(0).click();
      } else {
        hasNextPage = false;
      }
    }

    // Seleccionamos el botón de finalizar trayecto
    PO_View.checkElementBy(driver, "free", "/html/body/div/div[1]/table/tbody/tr/td[7]/a").get(0)
            .click();



    String buttonText = PO_RefuelView.getP().getString("route.end.title",
            PO_Properties.getSPANISH());
    PO_View.checkElementBy(driver, "text", buttonText).get(0).click();

    // Rellenamos el formulario con datos inválidos
    String odometer = "-1";
    PO_RouteView.fillEndRouteForm(driver, odometer, "");

    String checkText = PO_HomeView.getP().getString("error.route.end.negative", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);

    // Comprobamos que sale el error y que no ha finalizado el trayecto
    Assertions.assertEquals(checkText, result.get(0).getText());
    Assertions.assertNotNull(routesService.getActiveRouteByEmployeeDni(dni));
  }

  /**
   * Prueba de registro de fin de trayecto inválido. Trayecto no iniciado
   */
  @Test
  @Order(36)
  public void Prueba36() {
    String dni =  "10000001S";
    String password = "Us3r@1-PASSW";
    PO_LogInView.login(driver, dni, password, "Trayectos");

    // Accedemos manualmente a la URL de finalizar trayecto
    driver.get("http://localhost:8080/routes/end");

    // Comprobamos que se nos ha redirigido a la lista de mis trayectos pues no tenemos aún ninguno
    // iniciado
    String checkText = PO_HomeView.getP().getString("route.list.p1", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /**
   * Prueba de historial de trayectos de un vehículo
   */
  @Test
  @Order(37)
  public void Prueba37() {
    String dni =  "10000001S";
    String password = "Us3r@1-PASSW";
    PO_LogInView.login(driver, dni, password, "Trayectos");

    // Ahora accedemos al apartado de vehículos disponibles
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[2]/a", "/html/body/nav/div/ul[1]/li[2]/div/a");

    // Guardamos la información del primer vehículo de la tabla
    WebElement vehicleTable = PO_View.checkElementBy(driver, "class", "table table-hover").get(0);
    WebElement firstVehicleRow = vehicleTable.findElements(By.tagName("tr")).get(1);
    String plate = firstVehicleRow.getText().split(" ")[0];

    // Pulsamos en consultar los trayectos de dicho vehículo
    String buttonText = PO_RefuelView.getP().getString("vehicle.routes",
            PO_Properties.getSPANISH());
    PO_View.checkElementBy(driver, "text", buttonText).get(0).click();

    // Este vehículo tiene que tener 1 único trayecto.
    WebElement routesTable = PO_View.checkElementBy(driver, "class", "table table-hover").get(0);
    int numberOfRoutesInTable = routesTable.findElements(By.tagName("tr")).size()-1;

    Vehicle vehicle = vehiclesService.getVehicleByPlate(plate);
    int realNumberOfRoutes = routesService.getNumberOfRoutesForVehicle(vehicle);

    Assertions.assertEquals(realNumberOfRoutes, numberOfRoutesInTable);
  }

  /**
   * Mostrar el listado de repostajes y comprobar que se muestran todos los realizados por el
   * vehículo con la matrícula seleccionada.
   */
  @Test
  @Order(38)
  public void Prueba38() {
    String checkText = "Trayectos";
    String dni = "10000001L";
    String password = "Us3r@12-PASSW";
    PO_LogInView.login(driver, dni, password, checkText);

    // Accedemos al apartado de la lista de los repostajes
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[3]/a",
            "/html/body/nav/div/ul[1]/li[3]/div/a[2]");

    // Hacemos clic en el botón de ver repostajes del vehículo con matrícula 1234BCF
    PO_View.checkElementBy(driver, "free", "/html/body/div/div[1]/table/tbody/tr[1]/td[5]/a[2]")
            .get(0).click();

    // Comprobamos que estamos en la página de repostajes de ese vehículo
    checkText = PO_View.getP().getString("refuel.list.title1", PO_Properties.getSPANISH()) + " 1234BCF " +
            PO_View.getP().getString("refuel.list.title2", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Lista donde almacenaremos todos los repostajes visualizados
    List<String> allRefuels = new ArrayList<>();

    boolean hasNextPage = true;

    while (hasNextPage) {
      WebElement table = PO_View.checkElementBy(driver, "id", "refuelList").get(0);
      List<WebElement> rows = table.findElements(By.tagName("tr"));

      // Empezamos desde 1 para saltar la cabecera
      for (int i = 1; i < rows.size(); i++) {
        allRefuels.add(rows.get(i).getText());
      }

      List<WebElement> nextPageButton = driver.findElements(By.id("nextPage"));
      if (!nextPageButton.isEmpty() && nextPageButton.get(0).isDisplayed()) {
        nextPageButton.get(0).click();
      } else {
        hasNextPage = false;
      }
    }

    // Obtenemos el número de repostajes esperados para la matrícula 1234BCF
    Pageable pageable = Pageable.unpaged();
    int expectedRefuels = refuelService.getRefuelsForVehicle("1234BCF", pageable).getContent().size();

    // Comprobamos que los repostajes recogidos en la tabla coinciden con los del sistema
    Assertions.assertEquals(expectedRefuels, allRefuels.size());
  }


  // Mostrar vehículos disponibles solo cuando eres empleado standard
  @Test
  @Order(39)
  public void Prueba39MostrarDisponibles() {
    PO_LogInView.login(driver,"10000001S","Us3r@1-PASSW","Trayectos");
    //Pinchamos en la opción de menú de Vehículos
    PO_View.checkView(driver,"free","//*[@id=\"navbarDropdown\"]",0);
    //Esperamos a que aparezca la opción de lista de vehiculos disponibles para rol estandar
    PO_View.checkView(driver,"free","//a[contains(@href, 'vehicle/list/available')]",0);

    //Comprobamos que entramos en la lista de vehículos disponibles
    String checkText = PO_HomeView.getP().getString("list.vehicle.available.free",
        PO_Properties.getSPANISH());
    List<WebElement> result = PO_VehicleView.checkElementBy(driver, "text",
        checkText );
    Assertions.assertEquals(checkText, result.get(0).getText());


  }

  /**
   * Prueba de cambio de contraseña con datos válidos. Se cambia la contraseña y se comprueba que se
   * puede acceder a la cuenta con la nueva contraseña
   */
  @Test
  @Order(40)
  public void Prueba40() {
    String checkText = "Trayectos";
    String dni = "10000001S";
    String password = "Us3r@1-PASSW";

    PO_LogInView.login(driver, dni, password, checkText);

    // Accedemos a la sección de cambiar contraseña
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[2]/li[2]/a",
        "/html/body/nav/div/ul[2]/li[2]/div/a");
    // Cambiamos la contraseña
    String newPassword = "123456789Aa@";
    PO_UserView.fillChangePasswordForm(driver, password, newPassword, newPassword);

    PO_LogInView.logout(driver, PO_Properties.getSPANISH());

    // Comprobamos que ya no podemos iniciar sesión con la contraseña anterior
    checkText = PO_View.getP().getString("login.error", PO_Properties.getSPANISH());
    List<WebElement> result = PO_LogInView.login(driver, dni, password, checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Comprobamos que sí podemos iniciar sesión con la contraseña nueva
    checkText = "Trayectos";
    result = PO_LogInView.login(driver, dni, newPassword, "Trayectos");
    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /**
   * Prueba de cambio de contraseña con datos inválidos (contraseña anterior incorrecta). Se
   * comprueba que sale el mensaje de error y que no se ha cambiado la contraseña
   */
  @Test
  @Order(41)
  public void Prueba41() {
    String checkText = "Trayectos";
    String dni = "10000001S";
    String password = "Us3r@1-PASSW";

    PO_LogInView.login(driver, dni, password, checkText);

    // Accedemos a la sección de cambiar contraseña
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[2]/li[2]/a",
        "/html/body/nav/div/ul[2]/li[2]/div/a");

    String newPassword = "123456789Aa@";
    // Se introduce mal la contraseña actual
    PO_UserView.fillChangePasswordForm(driver, password + "A", newPassword, newPassword);

    // Comprobamos que sale el mensaje de error
    String errorMsg =
        PO_View.getP().getString("Error.change.password.not.match", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", errorMsg);
    Assertions.assertEquals(errorMsg, result.get(0).getText());

    // Comprobamos que la contraseña no ha cambiado
    PO_LogInView.logout(driver, PO_Properties.getSPANISH());

    // Comprobamos que no podemos iniciar sesión con la contraseña nueva
    checkText = PO_View.getP().getString("login.error", PO_Properties.getSPANISH());
    result = PO_LogInView.login(driver, dni, newPassword, checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Comprobamos que sí podemos iniciar sesión con la contraseña anterior
    checkText = "Trayectos";
    result = PO_LogInView.login(driver, dni, password, "Trayectos");
    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /**
   * Prueba de cambio de contraseña con datos inválidos (contraseña débil). Se comprueba que sale el
   * mensaje de error y que no se ha cambiado la contraseña
   */
  @Test
  @Order(42)
  public void Prueba42() {
    String checkText = "Trayectos";
    String dni = "10000001S";
    String password = "Us3r@1-PASSW";

    PO_LogInView.login(driver, dni, password, checkText);

    // Accedemos a la sección de cambiar contraseña
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[2]/li[2]/a",
        "/html/body/nav/div/ul[2]/li[2]/div/a");

    String newPassword = "1234";
    // Se introduce mal la contraseña actual
    PO_UserView.fillChangePasswordForm(driver, password, newPassword, newPassword);

    // Comprobamos que sale el mensaje de error
    String errorMsg =
        PO_View.getP().getString("Error.password.not.strong", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", errorMsg);
    Assertions.assertEquals(errorMsg, result.get(0).getText());

    // Comprobamos que la contraseña no ha cambiado
    PO_LogInView.logout(driver, PO_Properties.getSPANISH());

    // Comprobamos que no podemos iniciar sesión con la contraseña nueva
    checkText = PO_View.getP().getString("login.error", PO_Properties.getSPANISH());
    result = PO_LogInView.login(driver, dni, newPassword, checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Comprobamos que sí podemos iniciar sesión con la contraseña anterior
    checkText = "Trayectos";
    result = PO_LogInView.login(driver, dni, password, "Trayectos");
    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /**
   * Prueba de cambio de contraseña con datos inválidos (repetición de contraseña inválida). Se
   * comprueba que sale el mensaje de error y que no se ha cambiado la contraseña
   */
  @Test
  @Order(43)
  public void Prueba43() {
    String checkText = "Trayectos";
    String dni = "10000001S";
    String password = "Us3r@1-PASSW";

    PO_LogInView.login(driver, dni, password, checkText);

    // Accedemos a la sección de cambiar contraseña
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[2]/li[2]/a",
        "/html/body/nav/div/ul[2]/li[2]/div/a");

    String newPassword = "123456789Aa@";
    // Se introduce mal la contraseña actual
    PO_UserView.fillChangePasswordForm(driver, password, newPassword, newPassword + "A");

    // Comprobamos que sale el mensaje de error
    String errorMsg =
        PO_View.getP().getString("Error.password.confirm.not.match", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", errorMsg);
    Assertions.assertEquals(errorMsg, result.get(0).getText());

    // Comprobamos que la contraseña no ha cambiado
    PO_LogInView.logout(driver, PO_Properties.getSPANISH());

    // Comprobamos que no podemos iniciar sesión con la contraseña nueva
    checkText = PO_View.getP().getString("login.error", PO_Properties.getSPANISH());
    result = PO_LogInView.login(driver, dni, newPassword, checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Comprobamos que sí podemos iniciar sesión con la contraseña anterior
    checkText = "Trayectos";
    result = PO_LogInView.login(driver, dni, password, "Trayectos");
    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /**
   * Prueba de i18n. Visualiza 3 ventanas en español e inglés
   */
  @Test
  @Order(44)
  public void Prueba44() {
    String checkText = "Empleados";
    String dni = "12345678Z";
    String password = "@Dm1n1str@D0r";
    PO_LogInView.login(driver, dni, password, checkText);

    // Visualizamos que el home está en español
    checkText = PO_View.getP().getString("employees", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Cambiamos a inglés
    PO_NavView.changeLanguage(driver, "English");
    // Visualizamos que el home está en inglés
    checkText = PO_View.getP().getString("employees", PO_Properties.getENGLISH());
    result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Accedemos a la sección de añadir empleado
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[2]/a",
        "/html/body/nav/div/ul[1]/li[2]/div/a");
    // Cambiamos a español
    PO_NavView.changeLanguage(driver, "Spanish");
    // Visualizamos que la ventana está en español
    checkText = PO_View.getP().getString("add.employee.message", PO_Properties.getSPANISH());
    result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Cambiamos a inglés
    PO_NavView.changeLanguage(driver, "English");
    // Visualizamos que la ventana está en inglés
    checkText = PO_View.getP().getString("add.employee.message", PO_Properties.getENGLISH());
    result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Accedemos a la sección de lista de empleados
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[2]/a",
        "/html/body/nav/div/ul[1]/li[2]/div/a[2]");
    // Cambiamos a español
    PO_NavView.changeLanguage(driver, "Spanish");
    // Visualizamos que la ventana está en español
    checkText = PO_View.getP().getString("employees.list.message", PO_Properties.getSPANISH());
    result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Cambiamos a inglés
    PO_NavView.changeLanguage(driver, "English");
    // Visualizamos que la ventana está en inglés
    checkText = PO_View.getP().getString("employees.list.message", PO_Properties.getENGLISH());
    result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /**
   * Prueba de i18n. Visualiza 3 ventanas en inglés e italiano
   */
  @Test
  @Order(45)
  public void Prueba45() {
    String checkText = "Empleados";
    String dni = "12345678Z";
    String password = "@Dm1n1str@D0r";
    PO_LogInView.login(driver, dni, password, checkText);

    // Cambiamos a inglés
    PO_NavView.changeLanguage(driver, "English");
    // Visualizamos que el home está en inglés
    checkText = PO_View.getP().getString("employees", PO_Properties.getENGLISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Cambiamos a italiano
    PO_NavView.changeLanguage(driver, "Italian");
    // Visualizamos que el home está en inglés
    checkText = PO_View.getP().getString("employees", PO_Properties.getITALIAN());
    result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Accedemos a la sección de añadir empleado
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[2]/a",
        "/html/body/nav/div/ul[1]/li[2]/div/a");

    // Cambiamos a inglés
    PO_NavView.changeLanguage(driver, "English");
    // Visualizamos que la ventana está en inglés
    checkText = PO_View.getP().getString("employees", PO_Properties.getENGLISH());
    result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Cambiamos a italiano
    PO_NavView.changeLanguage(driver, "Italian");
    // Visualizamos que la ventana está en italiano
    checkText = PO_View.getP().getString("employees", PO_Properties.getITALIAN());
    result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Accedemos a la sección de lista de empleados
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[2]/a",
        "/html/body/nav/div/ul[1]/li[2]/div/a[2]");

    // Cambiamos a inglés
    PO_NavView.changeLanguage(driver, "English");
    // Visualizamos que la ventana está en inglés
    checkText = PO_View.getP().getString("employees.list.message", PO_Properties.getENGLISH());
    result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());

    // Cambiamos a italiano
    PO_NavView.changeLanguage(driver, "Italian");
    // Visualizamos que la ventana está en italiano
    checkText = PO_View.getP().getString("employees.list.message", PO_Properties.getITALIAN());
    result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /**
   * Prueba de seguridad. Se intenta acceder sin estar autenticado a la sección de listado de
   * empleados. Se debe redirigir al login
   */
  @Test
  @Order(46)
  public void Prueba46() {
    // Comprobamos que no estamos autenticados
    String checktext = PO_View.getP().getString("navbar.login.message", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checktext);
    Assertions.assertEquals(checktext, result.get(0).getText());
    // Intentamos acceder a la URL de listado de empleados
    driver.get("http://localhost:8080/employee/list");
    // Comprobamos que estamos en el formulario de Login
    checktext = PO_View.getP().getString("login.message", PO_Properties.getSPANISH());
    result = PO_View.checkElementBy(driver, "text", checktext);
    Assertions.assertEquals(checktext, result.get(0).getText());
  }

  /**
   * Prueba de seguridad. Se intenta acceder sin estar autenticado a la sección de listado de
   * vehículos disponibles. Se debe redirigir al login
   */
  @Test
  @Order(47)
  public void Prueba47() {
    // Comprobamos que no estamos autenticados
    String checktext = PO_View.getP().getString("navbar.login.message", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checktext);
    Assertions.assertEquals(checktext, result.get(0).getText());
    driver.get("http://localhost:8080/vehicle/list/available");
    // Comprobamos que estamos en el formulario de Login
    checktext = PO_View.getP().getString("login.message", PO_Properties.getSPANISH());
    result = PO_View.checkElementBy(driver, "text", checktext);
    Assertions.assertEquals(checktext, result.get(0).getText());
  }

  /**
   * Prueba de seguridad. Se intenta acceder a una ventana a la que no se tiene permiso de acceso.
   * Se comprueba que salta el mensaje de permiso denegado
   */
  @Test
  @Order(48)
  public void Prueba48() {
    String checkText = "Trayectos";
    String dni = "10000001S";
    String password = "Us3r@1-PASSW";

    PO_LogInView.login(driver, dni, password, checkText);
    // Intentamos acceder a la URL de añadir de empleado
    driver.get("http://localhost:8080/employee/add");

    checkText = PO_View.getP().getString("access.denied.message", PO_Properties.getSPANISH());
    List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
    Assertions.assertEquals(checkText, result.get(0).getText());
  }

  /**
   * Prueba de seguridad de logs. Se generan dos interacciones de cada tipo para los logs y se
   * comprueba que se visualizan correctamente en la lista de logs
   */
  @Test
  @Order(49)
  public void Prueba49() {
    // Generamos dos interacciones de fallo de login
    String checkText = PO_View.getP().getString("login.error", PO_Properties.getSPANISH());
    String dni = "12345678Z";
    String password = "@Dm1n1str@D0r";

    PO_LogInView.login(driver, dni, password + "1", checkText);
    PO_LogInView.login(driver, dni, password + "1", checkText);
    // Generamos dos interacciones de inicio de sesión y cierre de sesión
    checkText = "Empleados";
    PO_LogInView.login(driver, dni, password, checkText);
    PO_LogInView.logout(driver, PO_Properties.getSPANISH());
    PO_LogInView.login(driver, dni, password, checkText);
    PO_LogInView.logout(driver, PO_Properties.getSPANISH());
    // Volvemos a iniciar sesión para seguir con las otras acciones (hemos iniciado sesión 3 veces)
    PO_LogInView.login(driver, dni, password, checkText);
    // Generamos dos interacciones de añadir empleado
    // Accedemos a la sección de añadir empleado
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[2]/a",
        "/html/body/nav/div/ul[1]/li[2]/div/a");
    String dniInput = "12345678X";
    String nameInput = "Rodrigo";
    String lastNameInput = "José";

    // Rellenamos el formulario de añadir empleado
    PO_EmployeeView.fillAddEmployeeForm(driver, dniInput, nameInput, lastNameInput);

    // Accedemos a la sección de añadir empleado
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[2]/a",
        "/html/body/nav/div/ul[1]/li[2]/div/a");
    dniInput = "12345678W";
    nameInput = "Juan";
    lastNameInput = "José";

    // Rellenamos el formulario de añadir empleado
    PO_EmployeeView.fillAddEmployeeForm(driver, dniInput, nameInput, lastNameInput);

    // Accedemos a la sección de lista de logs
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[3]/a",
        "/html/body/nav/div/ul[1]/li[3]/div/a");

    // Comprobamos que se hayan registrado las acciones en el log

    // Para las peticiones (tienen que ser más de 2)
    String filterOption = PO_View.getP().getString("log.requests", PO_Properties.getSPANISH());
    WebElement logFilter = driver.findElement(By.id("logFilter"));
    Select select = new Select(logFilter);
    select.selectByVisibleText(filterOption);

    // Obtenemos las acciones de peticiones
    WebElement table = PO_View.checkElementBy(driver, "id", "logsTable").get(0);
    // Obtenemos todas las filas de la tabla
    List<WebElement> rows = table.findElements(By.tagName("tr"));
    Assertions.assertTrue(rows.size() > 2);

    // Para las altas de empleados (tienen que ser exactamente 2)
    filterOption = PO_View.getP().getString("log.add.employee", PO_Properties.getSPANISH());
    select.selectByVisibleText(filterOption);
    // Obtenemos las acciones de altas de empleados
    table = PO_View.checkElementBy(driver, "id", "logsTable").get(0);
    // Obtenemos todas las filas de la tabla
    rows = table.findElements(By.tagName("tr"));
    Assertions.assertEquals(2, rows.size() - 1);

    // Para los login fallidos (tienen que ser exactamente 2 (sin contar la cabecera))
    filterOption = PO_View.getP().getString("log.login.err", PO_Properties.getSPANISH());
    select.selectByVisibleText(filterOption);
    // Obtenemos las acciones de logs fallidos
    table = PO_View.checkElementBy(driver, "id", "logsTable").get(0);
    // Obtenemos todas las filas de la tabla
    rows = table.findElements(By.tagName("tr"));
    Assertions.assertEquals(2, rows.size() - 1);

    // Para los login exitosos (tienen que ser exactamente 3 (sin contar la cabecera))
    filterOption = PO_View.getP().getString("log.login.ex", PO_Properties.getSPANISH());
    select.selectByVisibleText(filterOption);
    // Obtenemos las acciones de logs fallidos
    table = PO_View.checkElementBy(driver, "id", "logsTable").get(0);
    // Obtenemos todas las filas de la tabla
    rows = table.findElements(By.tagName("tr"));
    Assertions.assertEquals(3, rows.size() - 1);

    // Para los logout (tienen que ser exactamente 2 (sin contar la cabecera))
    filterOption = PO_View.getP().getString("log.logout.log", PO_Properties.getSPANISH());
    select.selectByVisibleText(filterOption);
    // Obtenemos las acciones de logs fallidos
    table = PO_View.checkElementBy(driver, "id", "logsTable").get(0);
    // Obtenemos todas las filas de la tabla
    rows = table.findElements(By.tagName("tr"));
    Assertions.assertEquals(2, rows.size() - 1);
  }

  /**
   * Prueba de seguridad de logs. Se generan dos interacciones de fallo de login y se accede
   * a la sección de la lista de logs para borrar únicamente dichas dos interacciones
   */
  @Test
  @Order(50)
  public void Prueba50() {
    // Generamos dos interacciones de fallo de login
    String checkText = PO_View.getP().getString("login.error", PO_Properties.getSPANISH());
    String dni = "12345678Z";
    String password = "@Dm1n1str@D0r";

    PO_LogInView.login(driver, dni, password + "1", checkText);
    PO_LogInView.login(driver, dni, password + "1", checkText);
    // Iniciamos sesión bien
    checkText = "Empleados";
    PO_LogInView.login(driver, dni, password, checkText);

    // Accedemos a la sección de lista de logs
    PO_NavView.accessSection(driver, "/html/body/nav/div/ul[1]/li[3]/a",
        "/html/body/nav/div/ul[1]/li[3]/div/a");

    // Para los login fallidos (tienen que ser exactamente 2 (sin contar la cabecera))
    String filterOption = PO_View.getP().getString("log.login.err", PO_Properties.getSPANISH());
    WebElement logFilter = driver.findElement(By.id("logFilter"));
    Select select = new Select(logFilter);
    select.selectByVisibleText(filterOption);

    // Obtenemos las acciones de logs fallidos
    WebElement table = PO_View.checkElementBy(driver, "id", "logsTable").get(0);
    // Obtenemos todas las filas de la tabla
    List<WebElement> rows = table.findElements(By.tagName("tr"));
    Assertions.assertEquals(2, rows.size() - 1);

    // Borramos las acciones de log fallidos
    PO_View.checkElementBy(driver, "id", "deleteLogs").get(0).click();

    // Accedemos a la sección de los login fallidos de nuevo
    filterOption = PO_View.getP().getString("log.login.err", PO_Properties.getSPANISH());
    logFilter = driver.findElement(By.id("logFilter"));
    select = new Select(logFilter);
    select.selectByVisibleText(filterOption);

    // Comprobamos que no hay ninguno
    table = PO_View.checkElementBy(driver, "id", "logsTable").get(0);
    // Obtenemos todas las filas de la tabla
    rows = table.findElements(By.tagName("tr"));
    Assertions.assertEquals(0, rows.size() - 1);

    // Comprobamos que no están en base de datos
    int numberOfLoginFailActions = loggerService.getNumberOfLogsByType(LogEntry.getLogType(3));
    Assertions.assertEquals(0, numberOfLoginFailActions);
  }
}
