package com.store.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "StatusName")
public class StatusName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int statusID;
   private String statusName;

}
