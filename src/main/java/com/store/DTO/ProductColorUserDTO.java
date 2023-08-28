package com.store.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductColorUserDTO {
    long imgId;
    String image;
    long colorId;
    String reason;
}
