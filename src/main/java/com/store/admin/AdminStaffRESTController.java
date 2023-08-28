package com.store.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.dao.staffDAO;
import com.store.model.Authorities;
import com.store.model.staff;

@CrossOrigin("*")
@RestController
@RequestMapping("/admin/staff")
public class AdminStaffRESTController {
	@Autowired
	staffDAO staffdao;
	
	@GetMapping
	public List<staff> findAll() {	
		return staffdao.findAll();	
	}
	
	@PostMapping
    public ResponseEntity<String> addStaff(@RequestBody staff newStaff) {
        try {
            staff savedStaff = staffdao.save(newStaff);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm nhân viên thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi thêm nhân viên: " + e.getMessage());
        }
    }
	
//	@DeleteMapping("/removeStaff/{userID}")
//	public ResponseEntity<String> removeStaff(@PathVariable String userID) {
//	    try {
//	        staff staff = staffdao.findByStaffID(userID);
//	        if (staff != null) {
//	            staffdao.delete(staff);
//	            return ResponseEntity.ok("Xóa nhân viên thành công");
//	        } else {
//	            return ResponseEntity.notFound().build();
//	        }
//	    } catch (Exception e) {
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi xóa nhân viên");
//	    }
//	}
	@DeleteMapping("/removeStaff/{id}")
	public void delete(@PathVariable("id") Integer id) {
		staffdao.deleteById(id);
	}
}
