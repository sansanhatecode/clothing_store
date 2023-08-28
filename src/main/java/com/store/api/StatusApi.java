    package com.store.api;

    import com.store.DTO.OrdersDTO;
    import com.store.configs.CustomConfiguration;
    import com.store.dao.OrderDetailDAO;
    import com.store.model.Order_Details;
    import com.store.model.Product_Colors;
    import com.store.model.Product_Images;
    import com.store.service.OrderDetailsService;
    import com.store.service.ProductColorsService;
    import com.store.service.ProductImgService;
    import com.store.service.ProductService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;

    @RestController
    public class StatusApi {
        @Autowired
        OrderDetailsService orderDetailsService;
        @Autowired
        OrderDetailDAO orderDetailDAO;
        @Autowired
        CustomConfiguration customConfiguration;
        @Autowired
        ProductImgService productImgService;
        @Autowired
        ProductService productService;
        @Autowired
        ProductColorsService productColorsService;
        @RequestMapping("/api/status/{id}")
        public List<OrdersDTO> listProductColorByProductId(@PathVariable long id) {
            List<Order_Details> listOrder = orderDetailDAO.findByOrder_OrderID(id);
            List<OrdersDTO> ord = new ArrayList<>();
            listOrder.forEach(item -> {
                OrdersDTO a = new OrdersDTO();
                a.setOrderDetailID(item.getOrderDetailID());
                a.setPrice(item.getPrice());
                a.setQuantity(item.getQuantity());
                a.setColorName(item.getColorId().getColor_name());
                a.setName(productService.findByID(item.getProduct().getProductID()).get().getName());
                Product_Images img = productImgService.findByProductcolorId(productColorsService.findbyProductID(item.getProduct().getProductID()).get(0)).get(0);
                a.setImg(img.getImage());
                ord.add(a);
            });
            return ord ;
        }
        @GetMapping("/api/orderDetails/{orderId}")
        public List<OrdersDTO> listOrderDetails(@PathVariable long orderId){
            List<Order_Details> listOrder = orderDetailDAO.findByOrder_OrderID(orderId);
            List<OrdersDTO> ListOrd = new ArrayList<>();
            listOrder.forEach(item -> {
                OrdersDTO ord = new OrdersDTO();
                ord.setColorName(item.getColorId().getColor_name());
                ord.setPrice(item.getPrice());
                ord.setQuantity(item.getQuantity());
                ord.setName(item.getProduct().getName());
                ord.setImg(item.getProduct().getImg());
                ord.setOrderDetailID(item.getOrderDetailID());
                ListOrd.add(ord);
            });
            return ListOrd;
        }
    }
