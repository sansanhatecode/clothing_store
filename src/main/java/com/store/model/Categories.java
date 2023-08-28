package com.store.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@SuppressWarnings("serial")
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Categories {
    @Id
    String categoryID;
    String name;
    @JsonIgnore
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    List<Products> products;
}