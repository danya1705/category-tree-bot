package com.github.danya1705.categorytreebot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    private String path;

    public long getLevel() {
        return path.chars().filter(c -> '/' == c).count();
    }
}
