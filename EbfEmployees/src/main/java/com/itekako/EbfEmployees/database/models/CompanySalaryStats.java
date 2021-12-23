package com.itekako.EbfEmployees.database.models;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Accessors(chain = true)
public class CompanySalaryStats {
    public CompanySalaryStats(){};
    public CompanySalaryStats(Long id,String name,double avgSalary){
        this.id = id;
        this.name = name;
        this.avgSalary = avgSalary;
    }
    @Id
    private long id;
    private String name;
    private double avgSalary;
}
