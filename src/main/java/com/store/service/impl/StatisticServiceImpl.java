package com.store.service.impl;

import com.store.dao.ProductDAO;
import com.store.dao.StatisticDAO;
import com.store.model.Products;
import com.store.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private StatisticDAO dao;
    @Autowired
    private ProductDAO productDAO;

    @Override
    public String[][] getTotalPriceMonth(String month, String year) {
        int i = 0;
        int u = 0;
        String[][] result = new String[0][0];
        LocalDate currentTime = LocalDate.now();
        if (year.equals("0")) {
            String day = currentTime.getDayOfMonth() + "";
            month = currentTime.getMonthValue() + "";
            year = currentTime.getYear() + "";
            u = Integer.parseInt(day);
            result = new String[2][u];
            for (i = 0; i < u; i++) {
                String day1 = String.valueOf(Integer.parseInt(day) - i);
                result[0][(u - 1) - i] = day1 + "-" + month + "-" + year;
                result[1][(u - 1) - i] = dao.getTotalPriceMonth(day1, month, year);
            }
        } else if (month.equals("0")) {
            String day1 = "%";
            month = "12";
            u = 12;
            if (Integer.parseInt(year) >= currentTime.getYear()) {
                month = currentTime.getMonthValue() + "";
                year = currentTime.getYear() + "";
                u = currentTime.getMonthValue();
            }
            result = new String[2][u];
            for (i = 0; i < u; i++) {
                String month1 = String.valueOf(Integer.parseInt(month) - i);
                result[0][(u - 1) - i] = month1 + "-" + year;
                result[1][(u - 1) - i] = dao.getTotalPriceMonth(day1, month1, year);
            }
        } else if (!month.equals("0")) {

            u = checkDay(month, year);
            String day = String.valueOf(u);
            if (Integer.parseInt(year) > currentTime.getYear()) {
                year = currentTime.getYear() + "";
                month = currentTime.getMonthValue() + "";
                day = currentTime.getDayOfMonth() + "";
                u = Integer.parseInt(day);
            }else if (Integer.parseInt(year) == currentTime.getYear()) {
                if (Integer.parseInt(month) >= currentTime.getMonthValue()) {
                    day = currentTime.getDayOfMonth() + "";
                    month = currentTime.getMonthValue() + "";
                    u = Integer.parseInt(day);
                }
            }
            result = new String[2][u];
            for (i = 0; i < u; i++) {
                String day1 = String.valueOf(Integer.parseInt(day) - i);
                result[0][(u - 1) - i] = day1 + "-" + month + "-" + year;
                result[1][(u - 1) - i] = dao.getTotalPriceMonth(day1, month, year);
            }
        }
        return result;
    }

    @Override
    public String[][] getProductTotal(String day, String month, String year) {
        String[][] productToTal = new String[0][0];
        LocalDate currentTime = LocalDate.now();
        // Nếu không có năm
        if (year.equals("0")) {
            day = "%"; // Lấy tất cả ngày trong tháng
            month = currentTime.getMonthValue() + "";
            year = currentTime.getYear() + "";
            List<String> productSoLuong = dao.getProductTotal(day, month, year);
            productToTal = new String[2][productSoLuong.size() + 1];
            for (int i = productSoLuong.size(); i > 0; i--) {

                String[] output = productSoLuong.get(i - 1).split("\\,");
                Products products = productDAO.findByProductID(output[0]);
                productToTal[0][i - 1] = products.getName();
                productToTal[1][i - 1] = output[1];
            }
            productToTal[0][productSoLuong.size()] = "0";
            productToTal[1][productSoLuong.size()] = "0";

        }
        // Nếu tháng bằng 0
        else if (month.equals("0")) {
            day = "%"; // Lấy tất cả ngày trong tháng
            month = "%"; // Lấy tất cả tháng trong năm
            // Nếu năm > năm hiện tại => năm = năm hiện tại
            if (Integer.parseInt(year) > currentTime.getYear()) {
                year = currentTime.getYear() + "";
            }
            List<String> productSoLuong = dao.getProductTotal(day, month, year);
            productToTal = new String[2][productSoLuong.size() + 1];
            for (int i = productSoLuong.size(); i > 0; i--) {

                String[] output = productSoLuong.get(i - 1).split("\\,");
                Products products = productDAO.findByProductID(output[0]);
                productToTal[0][i - 1] = products.getName();
                productToTal[1][i - 1] = output[1];
            }
            productToTal[0][productSoLuong.size()] = "0";
            productToTal[1][productSoLuong.size()] = "0";

        }
        // Nếu tháng khác không
        else if (!month.equals("0")) {
            // Nếu ngày bằng 0
            if (day.equals("0")) {
                day = "%"; // Lấy tất cả ngày trong tháng
                // Nếu năm >= năm hiện tại
                if (Integer.parseInt(year) >= currentTime.getYear()) {
                    year = currentTime.getYear() + "";
                    // Nếu tháng > tháng hiện tại => tháng = tháng hiện tại
                    if (Integer.parseInt(month) > currentTime.getMonthValue()) {
                        month = currentTime.getMonthValue() + "";
                    }
                }
                List<String> productSoLuong = dao.getProductTotal(day, month, year);
                productToTal = new String[2][productSoLuong.size() + 1];
                for (int i = productSoLuong.size(); i > 0; i--) {

                    String[] output = productSoLuong.get(i - 1).split("\\,");
                    Products products = productDAO.findByProductID(output[0]);
                    productToTal[0][i - 1] = products.getName();
                    productToTal[1][i - 1] = output[1];
                }
                productToTal[0][productSoLuong.size()] = "0";
                productToTal[1][productSoLuong.size()] = "0";
            }
            // Nếu ngày khác 0
            else {
                // Nếu năm >= năm hiện tại
                if (Integer.parseInt(year) >= currentTime.getYear()) {
                    year = currentTime.getYear() + "";
                    // Nếu tháng > tháng hiện tại => tháng = tháng hiện tại
                    if (Integer.parseInt(month) > currentTime.getMonthValue()) {
                        month = currentTime.getMonthValue() + "";
                        day = currentTime.getMonthValue() + "";
                    } else if (Integer.parseInt(month) == currentTime.getMonthValue()){
                        // Nếu ngày > ngày hiện tại => ngày = ngày hiện tại
                        if (Integer.parseInt(day) >= currentTime.getDayOfMonth()) {
                            day = currentTime.getDayOfMonth() + "";
                        }
                    }
                }
                List<String> productSoLuong = dao.getProductTotal(day, month, year);
                productToTal = new String[2][productSoLuong.size() + 1];
                for (int i = productSoLuong.size(); i > 0; i--) {

                    String[] output = productSoLuong.get(i - 1).split("\\,");
                    Products products = productDAO.findByProductID(output[0]);
                    productToTal[0][i - 1] = products.getName();
                    productToTal[1][i - 1] = output[1];
                }
                productToTal[0][productSoLuong.size()] = "0";
                productToTal[1][productSoLuong.size()] = "0";
            }

        }
        return productToTal;
    }


    private Integer checkDay(String month, String year) {
        // Tìm tổng số ngày trong tháng
        int TotalDay = 0;
        switch (month) {
            case "1":
            case "3":
            case "5":
            case "7":
            case "8":
            case "10":
            case "12":
                TotalDay = 31;
                break;
            case "4":
            case "6":
            case "9":
            case "11":
                TotalDay = 30;
                break;
            case "2":
                if (Integer.parseInt(year) % 400 == 0 || (Integer.parseInt(year) % 4 == 0 && Integer.parseInt(year) % 100 != 0))
                    TotalDay = 29;
                else
                    TotalDay = 28;
                break;
        }
        return TotalDay;
    }


}
