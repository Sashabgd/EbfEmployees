package com.itekako.EbfEmployees.database.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Generated;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "companies",schema = "public")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String name;
}
