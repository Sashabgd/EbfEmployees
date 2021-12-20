package com.itekako.EbfEmployees.Dtos;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class CreateCompanyRequest {
    @NotNull
    private String name;
}
