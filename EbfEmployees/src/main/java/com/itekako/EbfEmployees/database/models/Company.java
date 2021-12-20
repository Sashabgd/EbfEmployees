package com.itekako.EbfEmployees.database.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "companies",schema = "public")
public class Company {
    @Id
    private long id;
    private String name;
}
