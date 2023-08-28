package com.store.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto  implements Serializable {

    private String userId;
    private String address;
    private String phone;
    private Double totalPrice = 0D;
    private Integer totalQuantity =0;
    private HashMap<String, CartDetailDto> details = new HashMap<>();
}
