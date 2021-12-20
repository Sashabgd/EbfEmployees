package com.itekako.EbfEmployees.database.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "employees",schema = "public")
public class Employee {
    @Id
    private long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String name;

    private String surname;

    private String email;

    private String address;

    private double salary;
}
