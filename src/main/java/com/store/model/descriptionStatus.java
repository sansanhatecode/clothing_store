package com.store.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "descriptionStatus")
public class descriptionStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int descriptionID;
    private String descriptionName;
}
