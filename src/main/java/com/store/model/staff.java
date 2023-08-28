package com.store.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "staff")
public class staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ID;
    String workingSession;
    int orderProcessing;
    int doneProcessing;
    @OneToOne
    @JoinColumn(name ="staffID")
    private Users staffID;   
}
