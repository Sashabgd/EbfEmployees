package com.itekako.EbfEmployees.Dtos;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class EmployeeDetails {

    @NotNull
    private long companyId;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private String email;

    @NotNull
    private String address;

    @NotNull
    @Min(value = 0)
    private double salary;
}
