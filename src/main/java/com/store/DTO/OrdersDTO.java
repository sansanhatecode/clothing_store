package com.store.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.store.model.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrdersDTO {
    Long orderDetailID;
    Double price;
    Integer quantity;
    String colorName;
    String name;
    String img;

}
