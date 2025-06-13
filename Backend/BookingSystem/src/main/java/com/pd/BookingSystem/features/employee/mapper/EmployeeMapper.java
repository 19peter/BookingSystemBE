package com.pd.BookingSystem.features.employee.mapper;

import com.pd.BookingSystem.features.employee.dto.EmployeeDetailsDto;
import com.pd.BookingSystem.features.employee.entity.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {


     EmployeeDetailsDto employeeToEmployeeDto(Employee employee);

}
