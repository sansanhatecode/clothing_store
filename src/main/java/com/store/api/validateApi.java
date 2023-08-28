package com.store.api;

import com.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.store.model.Users;
import java.util.ArrayList;

@RestController
public class validateApi {
    @Autowired
    UserService userService;
    @PostMapping("api/validateForm")
    public String validateFormRegister(@RequestBody ArrayList<String> list){
        String column = list.get(0);
        String value = list.get(1);
        String mess = "";
        int quantity = 0;
        switch (column){
            case "userID":
                 quantity = userService.findCountUserID(value);
                 mess = "Tên đăng nhập ";
                 break;
            case "email":
                quantity = userService.findCountEmail(value);
                mess = "Email ";
                break;
            case "phone":
                quantity = userService.findCountphone(value);
                mess = "Số điện thoại ";
                break;
        }
        if (quantity > 0) {
            mess += "đã tồn tại! ";
            return  mess;
        } else {
            mess = "";
        return mess;
        }
    }
}
