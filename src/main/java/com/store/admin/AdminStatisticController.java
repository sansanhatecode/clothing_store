package com.store.admin;

import com.store.dao.StatisticDAO;
import com.store.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.Arrays;

@Controller
public class AdminStatisticController {

    @Autowired
    StatisticService service;
    @Autowired
    StatisticDAO statisticDAO;

    @GetMapping("/admin/TotalStatisticsByDay/{month}/{year}")
    public String TotalStatistic(@PathVariable("month") String month,
                                 @PathVariable("year") String year,
                                 Model model) {
        String[][] chartData = service.getTotalPriceMonth(month, year);
        model.addAttribute("chartData", chartData);
        String Date;
        String checkDate[] = chartData[0][0].split("\\-");
        if (Arrays.stream(checkDate).count() == 3) {
            Date = "tháng " + checkDate[1] + ", năm " + checkDate[2];
        } else {
            Date = "năm " + checkDate[1];
        }
        ;
        model.addAttribute("date", Date);
        return "/admin/statistic/statisticTotalPrice";
    }

    @GetMapping("/admin/ProductStatisticsByDay/{day}/{month}/{year}")
    public String ProductStatistics(@PathVariable("day") String day,
                                    @PathVariable("month") String month,
                                    @PathVariable("year") String year,
                                    Model model) {
        String[][] productTotal = service.getProductTotal(day, month, year);
        model.addAttribute("productTotal", productTotal);
        LocalDate currentTime = LocalDate.now();
        String Date = "";
        if (year.equals("0")) {
            Date = "tháng " + currentTime.getMonthValue() + "" + ", năm " + currentTime.getYear() + "";
        } else if (month.equals("0")) {
            if (Integer.parseInt(year) > currentTime.getYear()) {
                year = currentTime.getYear() + "";
            }
            Date = "năm " + year;
        } else if (!month.equals("0")) {
            if (day.equals("0")) {
                if (Integer.parseInt(year) > currentTime.getYear()) {
                    year = currentTime.getYear() + "";
                    month = currentTime.getMonthValue() + "";
                } else if (Integer.parseInt(year) == currentTime.getYear()) {
                    if (Integer.parseInt(month) > currentTime.getMonthValue()) {
                        month = currentTime.getMonthValue() + "";
                    }
                }
                Date = "tháng " + month + "" + ", năm " + year + "";
            } else {
                if (Integer.parseInt(year) >= currentTime.getYear()) {
                    year = currentTime.getYear() + "";
                    // Nếu tháng > tháng hiện tại => tháng = tháng hiện tại
                    if (Integer.parseInt(month) > currentTime.getMonthValue()) {
                        month = currentTime.getMonthValue() + "";
                        day = currentTime.getDayOfMonth() + "";
                    } else if (Integer.parseInt(month) == currentTime.getMonthValue()){
                        // Nếu ngày > ngày hiện tại => ngày = ngày hiện tại
                        if (Integer.parseInt(day) >= currentTime.getDayOfMonth()) {
                            day = currentTime.getDayOfMonth() + "";
                        }
                    }
                }
                    Date = "ngày " + day + ", tháng " + month + ", năm " + year;
            }
        }
        model.addAttribute("date", Date);
        return "/admin/statistic/statisticProduct";

    }
}
