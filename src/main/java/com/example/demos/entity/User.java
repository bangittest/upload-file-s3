package com.example.demos.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "upload")
@Entity
public class User {
    @Id
    private Long id;
    private String file;
    private String path;
}
