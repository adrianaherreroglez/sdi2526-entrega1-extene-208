package com.uniovi.sdi2425entrega121.validators;


import com.uniovi.sdi2425entrega121.entities.Employee;
import com.uniovi.sdi2425entrega121.services.EmployeesService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AddEmployeeFormValidator implements Validator {
  private final EmployeesService employeesService;

  public AddEmployeeFormValidator(EmployeesService employeesService) {
    this.employeesService = employeesService;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return Employee.class.equals(aClass);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Employee employee = (Employee) target;
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dni", "Error.empty");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "Error.empty");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "Error.empty");
    if (employee.getDni().length() != 9) {
      errors.rejectValue("dni", "Error.add.employee.dniLength");
    }
    if (employee.getDni().length() > 0 && !Character.isLetter(employee.getDni().charAt(employee.getDni().length() - 1))) {
      errors.rejectValue("dni", "Error.add.employee.dniLastChar");
    }
    Employee currentDBEmp = employeesService.getEmployeeByDni(employee.getDni());

    if (currentDBEmp != null && currentDBEmp.getId() != employee.getId()) {
      errors.rejectValue("dni", "Error.add.employee.dni.duplicate");
    }
  }
}

