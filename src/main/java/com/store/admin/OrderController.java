    package com.store.admin;

    import com.store.dao.OrderDAO;
    import com.store.dao.OrderDetailDAO;
    import com.store.model.Order_Details;
    import com.store.model.Orders;
    import com.store.model.Products;
    import com.store.model.Users;
    import com.store.service.OrderService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;

    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;
    import java.util.Optional;
    import java.util.stream.Collectors;
    import java.util.stream.IntStream;

    import com.store.DTO.sellingProductsDTO;

    @Controller
    @RequestMapping("/admin/order")
    public class OrderController {
        @Autowired
        OrderService orderService;
        @Autowired
        OrderDAO orderDAO;
        @Autowired
        OrderDetailDAO orderDetailDAO;

        @RequestMapping("")
        public String adminUser(Model model, @RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size, sellingProductsDTO sellingProductsDTO, String StatusOrd) {
            int currentPage = page.orElse(1);
            int pageSize = size.orElse(5);
            int countFalseOrder = orderDAO.countByStatus("4");
            int countDoneOrder = orderDAO.countByStatus("5");
            int countAllOrder = orderDAO.countAll();
            int countProcessingOrder = orderDAO.countByStatusProcessing();
            model.addAttribute("sellingProductsDTO", sellingProductsDTO);
            model.addAttribute("MonthAndYear", sellingProductsDTO.getMonthAndYear());
            if (sellingProductsDTO.getMonthAndYear() == null) {
                sellingProductsDTO.setMonthAndYear("All");
            }
            model.addAttribute("StatusOrd", StatusOrd);
            String MonthAndYear = sellingProductsDTO.getMonthAndYear();
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("dislabled", "pagination dislabled");
            model.addAttribute("active", "active pagination");
            //lấy ra tháng và năm có trong order
            List<String> MonthAndCount = orderDAO.findDateAnDCountMonth();
            model.addAttribute("MonthAndCount", MonthAndCount);
            List<String> MonthandYearList = Arrays.stream(MonthAndYear.split("-")).collect(Collectors.toList());
            String Month = MonthandYearList.get(0);

            //
            List<Orders> list = new ArrayList<>();
            List<Long> lsOrderID = new ArrayList<>();
            if (MonthAndYear.equals("All")) {
                list = orderDAO.findAllDESC();
            } else {
                model.addAttribute("url", "?MonthAndYear="+MonthAndYear);
                String Year = MonthandYearList.get(1);
                countProcessingOrder = orderDAO.countByStatusProcessingAndMonthYear(Month, Year);
                countDoneOrder = orderDAO.countByMonthAndYearAndStatus(Month, Year, "5");
                countFalseOrder = orderDAO.countByMonthAndYearAndStatus(Month, Year, "4");
                countAllOrder = orderDAO.countByMonthAndYear(Month, Year);
                //lấy ra id từ tháng
                lsOrderID = orderDAO.findOrderByMonth(Month,Year);
                List<Orders> ls = new ArrayList<>();
                lsOrderID.forEach(item -> {
                    Orders ord = orderDAO.findByOrderID(item);
                    ls.add(ord);
                });
                    list = ls;
                // lấy ra order
            }
            List<Orders> ls = new ArrayList<>();
            if (StatusOrd != null) {
                if (Month.equals("All")){
                    switch (StatusOrd) {
                        case "All":
                            list = orderDAO.findAllDESC();
                            break;
                        case "Processing":
                            lsOrderID = orderDAO.findOrderByStatusProcessing();
                            lsOrderID.forEach(item -> {
                                Orders ord = orderDAO.findByOrderID(item);
                                ls.add(ord);
                            });
                            list = ls;
                            break;
                        case "CancelOrder":
                            lsOrderID = orderDAO.findOrderByStatus("4");
                            lsOrderID.forEach(item -> {
                                Orders ord = orderDAO.findByOrderID(item);
                                ls.add(ord);
                            });
                            list = ls;
                            break;
                        case "done":
                            lsOrderID = orderDAO.findOrderByStatus("5");
                            lsOrderID.forEach(item -> {
                                Orders ord = orderDAO.findByOrderID(item);
                                ls.add(ord);
                            });
                            list = ls;
                            break;
                    }
                    model.addAttribute("url", "?MonthAndYear=All&StatusOrd="+StatusOrd);

                } else {
                    String Year = MonthandYearList.get(1);
                    switch (StatusOrd) {
                        case "All":
                            lsOrderID = orderDAO.findOrderByMonth(Month, Year);
                            lsOrderID.forEach(item -> {
                                Orders ord = orderDAO.findByOrderID(item);
                                ls.add(ord);
                            });

                            break;
                        case "Processing":
                            lsOrderID = orderDAO.findOrderByMonthAndProcessing(Month, Year);
                            lsOrderID.forEach(item -> {
                                Orders ord = orderDAO.findByOrderID(item);
                                ls.add(ord);
                            });

                            break;
                        case "CancelOrder":
                            lsOrderID = orderDAO.findOrderByMonthAndCancelOrder(Month, Year);
                            lsOrderID.forEach(item -> {
                                Orders ord = orderDAO.findByOrderID(item);
                                ls.add(ord);
                            });
                            break;
                        case "done":
                            lsOrderID = orderDAO.findOrderByMonthAndDone(Month, Year);
                            lsOrderID.forEach(item -> {
                                Orders ord = orderDAO.findByOrderID(item);
                                ls.add(ord);
                            });

                            break;
                    }
                    list = ls;
                    model.addAttribute("url", "?MonthAndYear="+MonthAndYear+"&StatusOrd="+StatusOrd);
                }

            }
            model.addAttribute("countAllOrder", countAllOrder);
            model.addAttribute("countFalseOrder", countFalseOrder);
            model.addAttribute("countDoneOrder", countDoneOrder);
            model.addAttribute("countProcessingOrder", countProcessingOrder);
            Page<Orders> ordersPage = orderService.findPaginatedOrder(PageRequest.of(currentPage - 1, pageSize), list);
            model.addAttribute("Orders", ordersPage);
            List<List> listOrderDetails = new ArrayList<>();
            ordersPage.forEach(item -> {
                List<Order_Details> ord = orderDetailDAO.findByOrderID(item.getOrderID());
                listOrderDetails.add(ord);
            });
            model.addAttribute("listOrderDetails", listOrderDetails);
            int totalPages = ordersPage.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }

            return "/admin/order/order";
        }

    }
