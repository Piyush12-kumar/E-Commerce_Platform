package com.example.ecommerce_project.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "tagId")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(unique = true)
    private String name;

    private String description;

    @ManyToMany(mappedBy = "tags")
    private Set<Product> products = new HashSet<>();

    // Getters, Setters, and Constructors remain the same...
    public Long getTagId() { return tagId; }
    public void setTagId(Long tagId) { this.tagId = tagId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Set<Product> getProducts() { return products; }
    public void setProducts(Set<Product> products) { this.products = products; }

    public Tag() {
        this.tagId = null;
        this.name = null;
        this.description = null;
        this.products = new HashSet<>();
    }

    public Tag(Long tagId, String name, String description, Set<Product> products) {
        this.tagId = tagId;
        this.name = name;
        this.description = description;
        this.products = products;
    }
}