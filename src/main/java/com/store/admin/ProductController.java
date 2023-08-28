        package com.store.admin;

        import com.store.DTO.ProductDTO;
        import com.store.DTO.ProductImgDTO;
        import com.store.configs.CustomConfiguration;
        import com.store.constant.SessionConstant;
        import com.store.dao.*;
        import com.store.model.*;
        import com.store.service.ProductColorsService;
        import com.store.service.ProductImgService;
        import com.store.service.ProductService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.PageRequest;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.web.bind.annotation.*;
        import org.springframework.web.servlet.mvc.support.RedirectAttributes;

        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpSession;
        import java.io.IOException;
        import java.io.InputStream;
        import java.nio.file.Files;
        import java.nio.file.Path;
        import java.nio.file.Paths;
        import java.nio.file.StandardCopyOption;
        import java.sql.SQLException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Optional;
        import java.util.stream.Collectors;
        import java.util.stream.IntStream;
    import com.store.dao.StatusDAO;
        @Controller
        @RequestMapping("/admin/product")

        public class ProductController {
            public static List<String> cate2 = new ArrayList<>();
            public static String currentType ;
            @Autowired
            ColorsDAO colorsDAO;
            @Autowired
            ProductImgDAO productImgDAO;
            @Autowired
            ProductService productService;
            @Autowired
            CategoryDAO categoryDAO;
            @Autowired
            CustomConfiguration customConfiguration;
            @Autowired
            ProductDAO productDAO;
            @Autowired
            ProductImgService productImgService;
            @Autowired
            ProductColorsService productColorsService;
            @Autowired
            staffDAO staffDAO;
            @Autowired
            StatusDAO statusDAO;
            @Autowired
            OrderDetailDAO orderDetailDAO;
            @RequestMapping("")
            public String adminProduct(Model model, String type, HttpServletRequest request, String search, ProductImgDTO prdImg , ProductDTO productRequest, @RequestParam("page") Optional<Integer> page,
                                       @RequestParam("size") Optional<Integer> size, @RequestParam("page2") Optional<Integer> page2,
                                       @RequestParam("size2") Optional<Integer> size2) {
                HttpSession session = request.getSession();
                Users currentUser = (Users) session.getAttribute(SessionConstant.CURRENT_USER);
                staff st = staffDAO.findByStaffID(currentUser);
                int orderProcessing = statusDAO.selectCountStatusName(st);
                model.addAttribute("mss",orderProcessing);
                int currentPage = page.orElse(1);
                int pageSize = size.orElse(5);
                int currentPage2 = page2.orElse(1);
                int pageSize2 = size2.orElse(5);
                if (productRequest == null) {
                    productRequest = new ProductDTO();
                }
                //list products có trạng thái de[reacation = true (ko bán nữa)
                List<Products> products = productService.findDeprecatedTrue();
                //list products có trạng thái de[reacation = true (đang bán nữa)
                List<Products> productsDepFalse = productService.findDeprecatedFalse();
                //list categories all
                List<Categories> categories = categoryDAO.findAll();
                //list product color all
                List<Product_Colors> productColors = productColorsService.findAll();
                //list productImg all
                List<Product_Images> productImg = productImgService.findAll();
                //list product All
                List<Products> productAll = productDAO.findAll();
                //list color All
                List<Colors> colorList = colorsDAO.findAll();
                //add class disabled (không cho thao tác lên thẻ input của id khi update)
                model.addAttribute("disabled", "disabled");
                model.addAttribute("productColor", productColors);
                //currentPage của table product
                model.addAttribute("currentPage", currentPage);
                //currenPage của table productbyColorAndImg
                model.addAttribute("currentPage2", currentPage2);
                model.addAttribute("categories", categories);
                model.addAttribute("productRequest", productRequest);
                model.addAttribute("productImgRequest", new ProductImgDTO());
                model.addAttribute("productColorRequest", new ProductImgDTO());
                model.addAttribute("type", type);
                model.addAttribute("colorList", colorList);
                model.addAttribute("productAll", productAll);
                Page<Products> productPage = productService.findPaginated(PageRequest.of(currentPage - 1, pageSize), productAll);
                Page<Product_Images> productImgPage = productImgService.findPaginated(PageRequest.of(currentPage2 - 1, pageSize2), productImg);
                    model.addAttribute("productImg", productImgPage);

                // product id = null thì findall color id còn có thì find theo colorid
                List<Product_Colors> listColorProduct ;
                if (prdImg.getProduct() == null){
                    listColorProduct = productColorsService.findByProductID1("Ao-Balo");
                } else {
                    model.addAttribute("productID", prdImg.getProduct().getProductID());
                    listColorProduct = productColorsService.findByProductID1(prdImg.getProduct().getProductID());
                }
                model.addAttribute("listColorProduct", listColorProduct);
                if (type == null || type.isEmpty()){
                    type = "null";
                }
                if (search != null){
                    if (productRequest.getType() != null){
                        currentType = productRequest.getType();
                    }
                       switch ( currentType){
                       case "":
                           type = "search";
                           break;
                       case "db":
                           type = "searchStatusDb";
                           break;
                       case "nb":
                           type = "searchStatusNb";
                           break;
                       case "category":
                           type = "searchCategory";
                   }

                }
                switch (type) {
                    case "null":
                        model.addAttribute("url", "/admin/product");
                        productPage = productService.findPaginated(PageRequest.of(currentPage - 1, pageSize), productAll);
                        model.addAttribute("productPage", productPage);
                        cate2 = new ArrayList<>();
                        break;
                    case "db":
                        productPage = productService.findPaginated(PageRequest.of(currentPage - 1, pageSize), products);
                        model.addAttribute("productPage", productPage);
                        cate2 = new ArrayList<>();
                        model.addAttribute("url", "/admin/product?type=db");
                        break;
                    case "nb":
                        productPage = productService.findPaginated(PageRequest.of(currentPage - 1, pageSize), productsDepFalse);
                        model.addAttribute("productPage", productPage);
                        cate2 = new ArrayList<>();
                        model.addAttribute("url", "/admin/product?type=nb");
                        break;
                    case "category":
                        List<String> cate = productRequest.listProductByCategory;
                        if (!cate.isEmpty()) {
                            cate2 = cate;
                        }
                        if(cate.isEmpty() && currentPage == 1){
                            cate2 =cate;
                        }
                        List<Categories> categoryIds = cate2
                                .parallelStream().map(item -> {
                                    Categories cat = new Categories();
                                    cat.setCategoryID(item);
                                    return cat;
                                }).collect(Collectors.toList());
                        List<Products> productscategory = productDAO.findByCategoryIn(categoryIds);
                        model.addAttribute("showFormFilter", "showFormFilter");
                        model.addAttribute("formProductByCategory", "formProductByCategory col-6");
                        if (productscategory == null || productscategory.isEmpty()) {
                            productPage = productService.findPaginated(PageRequest.of(currentPage - 1, pageSize), productAll);
                            model.addAttribute("productPage", productPage);
                            model.addAttribute("url", "/admin/product");

                        } else {
                            productPage = productService.findPaginated(PageRequest.of(currentPage - 1, pageSize), productscategory);
                            model.addAttribute("productPage", productPage);
                            model.addAttribute("url", "/admin/product?type=category");
                        }
                        break;
                    case "search":
                        List<Products> ListProductBySearch = productService.findBySearch(search);
                        productPage = productService.findPaginated(PageRequest.of(currentPage - 1, pageSize), ListProductBySearch);
                        model.addAttribute("productPage", productPage);
                        model.addAttribute("url", "/admin/product?search="+ search);
                        cate2 = new ArrayList<>();
                        break;
                    case "searchStatusDb":
                        List<Products> ListProductBySearchstatusDb = productService.findBySearchDb(search);
                        productPage = productService.findPaginated(PageRequest.of(currentPage - 1, pageSize), ListProductBySearchstatusDb);
                        model.addAttribute("productPage", productPage);
                        model.addAttribute("url", "/admin/product?search="+ search);
                        cate2 = new ArrayList<>();
                        break;
                    case "searchStatusNb":
                        List<Products> ListProductBySearchstatusNb = productService.findBySearchNb(search);
                        productPage = productService.findPaginated(PageRequest.of(currentPage - 1, pageSize), ListProductBySearchstatusNb);
                        model.addAttribute("productPage", productPage);
                        model.addAttribute("url", "/admin/product?search="+ search);
                        cate2 = new ArrayList<>();
                        break;
                    case "searchCategory":
                       String cateID1 = cate2.stream().collect(Collectors.joining(String.valueOf(",")));
                    List<Products> ListProductBySearchCate = productService.findByseatchCateId(search, cateID1);
                        productPage = productService.findPaginated(PageRequest.of(currentPage - 1, pageSize), ListProductBySearchCate);
                        model.addAttribute("productPage", productPage);
                        model.addAttribute("url", "/admin/product?search="+ search);
                }
                int totalPages = productPage.getTotalPages();
                if (totalPages > 0) {
                    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                            .boxed()
                            .collect(Collectors.toList());
                    model.addAttribute("pageNumbers", pageNumbers);
                }
                int totalPagesImg = productImgPage.getTotalPages();
                if (totalPagesImg > 0) {
                    List<Integer> pageNumbers2 = IntStream.rangeClosed(1, totalPagesImg)
                            .boxed()
                            .collect(Collectors.toList());
                    model.addAttribute("pageNumbers2", pageNumbers2);
                }
                return "/admin/product/index";
            }
            @GetMapping("/deleteProductImgAndColor")
            public String Updatequery(@RequestParam("productImgId") long imgId, RedirectAttributes redirectAttributes) throws IOException {
                Optional<Product_Images> productImg = productImgService.findById(imgId);
                Optional<Product_Colors> productColor = productColorsService.findByID(productImg.get().getProductcolor().getColorID());
                int img = productImgService.countImg(productImg.get().getImage());
                Optional<Products> product = productService.findByID(productColor.get().getProduct().getProductID());
                String a =  productImg.get().getProductcolor().getProduct().getCategory().getCategoryID();
                if (img == 1) {
                    Files.deleteIfExists(Paths.get("src/main/resources/static/images/product/" + productImg.get().getProductcolor().getProduct().getCategory().getCategoryID() +"/" + img));
                }
                try {
                    productImgService.deleteImg(imgId);
                    redirectAttributes.addFlashAttribute("message", "Xóa thành công ảnh " + productImg.get().getImage() + " màu " + productColor.get().getColor_name() + " sản phẩm " + product.get().getName());

                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("message", "Xóa thất bại ");
                    e.printStackTrace();
                }

                redirectAttributes.addFlashAttribute("status", "done_delete");
                return "redirect:/admin/product";
            }

            @GetMapping("/DeleteProductColor")
            public String DeleteProductColor(@RequestParam("ColorID") long colorid, RedirectAttributes redirectAttributes) throws IOException {
                // từ colorID lấy ra prdcolor
                Optional<Product_Colors> productColor = productColorsService.findByID(colorid);
                // lấy ra prdimg
                List<Product_Images> productImg = productImgService.findByProductcolorId(productColor.get());
                try {
                    // kiểm tra màu đó đã xuất hiện order chưa chưa thì cho xóa còn đã tồn tại order_detail rồi thì k cho xóa
                  if (orderDetailDAO.findByColorId(productColor.get()).isEmpty() ){
                      //thực hiện xóa prdcolor
                      productColorsService.deleteColor(productColor.get().getColorID());
                      // duyệt qua các ảnh nếu số lượng ảnh là 1 thì xóa
                      productImg.forEach( item -> {
                          int img = productImgService.countImg(item.getImage());
                          if (img == 1){
                              try {
                                  productImgService.deleteImg(item.getImgID());
                                  Files.deleteIfExists(Paths.get("src/main/resources/static/images/product/" + productColor.get().getProduct().getCategory().getCategoryID() +"/" + item.getImage()));
                              } catch (IOException e) {
                                  throw new RuntimeException(e);
                              }
                          } else{
                              productImgService.deleteImg(item.getImgID());
                          }
                      });
                      redirectAttributes.addFlashAttribute("message", "Xóa thành công màu " + productColor.get().getColor_name() + " trong sản phẩm " + productColor.get().getProduct().getName());
                  } else {
                      redirectAttributes.addFlashAttribute("message", "Xóa không thành công màu " + productColor.get().getColor_name() + " trong sản phẩm " +
                              productColor.get().getProduct().getName()+" vì màu này đã có đơn đặt hàng ");
                  }
                }catch (Exception e) {
                    redirectAttributes.addFlashAttribute("message", "Xóa thất bại ");
                    e.printStackTrace();
                }
                redirectAttributes.addFlashAttribute("status", "done_delete");
                return "redirect:/admin/product";
            }
            @GetMapping("/updateStatusProduct")
            public String Updatequery(@RequestParam("productID") String productID, RedirectAttributes redirectAttributes) {
                Optional<Products> product = productService.findByID(productID);
                if (product.get().isDeprecated()) {
                    try {
                        //ngưng bán thì vào đây -- ktra xem product đó có màu chưa nếu có rồi thì k cho xóa--- chưa có thì xóa
                       if (productColorsService.findbyProductID(productID).isEmpty()){
                           if (productImgService.countImg(product.get().getImg()) == 1){
                               Files.deleteIfExists(Paths.get("src/main/resources/static/images/product/"+ product.get().getCategory().getCategoryID()+"/"+ product.get().getImg()));
                           }
                           productService.deleteProduct(productID);
                           redirectAttributes.addFlashAttribute("message", "Xóa thành công " + productID);
                       } else {
                           redirectAttributes.addFlashAttribute("message", "Thất bại, sản phẩm mã " + productID + "đã tồn tại màu hãy xóa màu trước ");
                       }

                    } catch (Exception e) {
                        redirectAttributes.addFlashAttribute("message", "Xóa thất bại " + productID);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        productService.deleteLogical(productID);
                        redirectAttributes.addFlashAttribute("message", "Chuyển thành công sản phẩm " + productID + " sang trạng thái ngưng bán");
                    } catch (Exception e) {
                        e.printStackTrace();
                        redirectAttributes.addFlashAttribute("message", "Chuyển thất bại sản phẩm " + productID + " sang trạng thái ngưng bán");
                    }
                }
                redirectAttributes.addFlashAttribute("status", "done_delete");
                return "redirect:/admin/product";
            }


            @GetMapping("/updateStatusTrue")
            public String UpdateStatusTrue(@RequestParam("productID") String productID, RedirectAttributes
                    redirectAttributes) {
                try {
                    productService.updateStatusTrue(productID);
                    redirectAttributes.addFlashAttribute("message", "Chuyển thành công sản phẩm " + productID + " sang trạng thái đang bán");
                } catch (Exception e) {
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("message", "Chuyển thất bại sản phẩm " + productID + " sang trạng thái đang bán");
                }
                redirectAttributes.addFlashAttribute("status", "done_delete");
                return "redirect:/admin/product";
            }

            @PostMapping("/create")
            public String doPostCreateProduct(@ModelAttribute("productRequest") ProductDTO productReq, RedirectAttributes
                    redirectAttributes) {
                Products products;
                String img;
                Path path = Paths.get("src/main/resources/static/images/product/"+ productReq.getCategory().getCategoryID()+"/");
                try {
                    InputStream inputStream = productReq.getImg().getInputStream();
                    img = productReq.getImg().getOriginalFilename();
                    if (img == null) {
                        img = "logo.png";
                    }
                    Files.copy(inputStream, path.resolve(img), StandardCopyOption.REPLACE_EXISTING);
                    products = customConfiguration.modelMapper().map(productReq, Products.class );
                    products.setImg(img);
                    productService.save(products);
                    redirectAttributes.addFlashAttribute("message", "Thêm mới thành công sản phẩm " + productReq.getName());
                } catch (Exception e) {
                    System.out.println(e);
                    redirectAttributes.addFlashAttribute("message", "Thêm mới thất bại sản phẩm " + productReq.getName());
                }
                redirectAttributes.addFlashAttribute("status", "done_delete");
                return "redirect:/admin/product";
            }

            @PostMapping("/createImgProduct")
            public String doPostCreateImgProduct(@ModelAttribute("productImgRequest") ProductImgDTO
                productImgDTO, RedirectAttributes redirectAttributes, Model model) {
                Product_Images productImages;

                String categoryId = productService.findByID(productImgDTO.getProduct().getProductID()).get().getCategory().getCategoryID();
                String img;
                Path path = Paths.get("src/main/resources/static/images/product/"+categoryId);
                    try {
                        InputStream inputStream = productImgDTO.getImg().getInputStream();
                        img = productImgDTO.getImg().getOriginalFilename();
                        int countImg = productImgDAO.countImg(img);
                        if (img == null) {
                            img = "logo.png";
                        }
                        if (countImg == 0 ){Files.copy(inputStream, path.resolve(img), StandardCopyOption.REPLACE_EXISTING);}

                        Optional<Product_Colors> productColors = productColorsService.findByID(productImgDTO.getColorid());
                        productImages = new Product_Images(img, productColors.get());
                        productImgService.save(productImages);
                        redirectAttributes.addFlashAttribute("message", "Thêm mới thành công sản phẩm ");
                    } catch (Exception e) {
                        System.out.println(e);
                        redirectAttributes.addFlashAttribute("message", "Thêm mới thất bại sản phẩm ");

                }
                model.addAttribute("table2", "table2");
                model.addAttribute("table1", "handletable records table-responsive table1");
                redirectAttributes.addFlashAttribute("status", "done_delete");
                return "redirect:/admin/product";
            }
            @PostMapping("/createColorProduct")
            public String doPostCreateColorProduct(@ModelAttribute("productColorRequest") ProductImgDTO
                                                           productImgDTO, RedirectAttributes redirectAttributes, Model model) throws SQLException {
                Optional<Product_Colors> productColorsServiceByproductIDAndColorHex = productColorsService.findByproductIDAndColorHex(productImgDTO.getProduct(), productImgDTO.getColorHex());
                Colors color = colorsDAO.findByRGBColor(productImgDTO.getColorHex());
                if (productColorsServiceByproductIDAndColorHex.isPresent()) {
                    redirectAttributes.addFlashAttribute("status", "done_delete");
                    redirectAttributes.addFlashAttribute("message", "Sản phẩm đã tồn tại màu " + color.getNameColor()+ " mã màu: " + color.getRGBColor());
                } else {
                    try {
                        Product_Colors productColors;
                        productColors = new Product_Colors();
                        productColors.setColorhex(color.getRGBColor());
                        productColors.setColor_name(color.getNameColor());
                        productColors.setProduct(productImgDTO.getProduct());
                        productColors.setAvailable(productImgDTO.getAvailable());
                        productColorsService.save(productColors);
                        redirectAttributes.addFlashAttribute("message", "Thêm mới thành công sản phẩm ");
                        model.addAttribute("table2", "table2");
                        model.addAttribute("table1", "handletable records table-responsive table1");
                        redirectAttributes.addFlashAttribute("status", "done_delete");
                        redirectAttributes.addFlashAttribute("message", "Thêm mới thành công sản phẩm ");
                    }catch (Exception e){
                        System.out.println(e);
                        redirectAttributes.addFlashAttribute("status", "done_delete");
                        redirectAttributes.addFlashAttribute("message", "Thêm mới thất bại sản phẩm ");
                    }
                }

                return "redirect:/admin/product";
            }
            @PostMapping("/update")
            public String doPostUpdateProduct(@ModelAttribute("productRequest") ProductDTO productReq, RedirectAttributes
                    redirectAttributes) {
                Optional<Products> product = productService.findByID(productReq.getProductID());
                Products products = null;
                String img;
                Path path = Paths.get("src/main/resources/static/images/product/"+ productReq.getCategory().getCategoryID()+"/");
                if (productReq.getImg().isEmpty()) {
                    try {
                        img = product.get().getImg();
                        products = customConfiguration.modelMapper().map(productReq, Products.class);
                        products.setImg(img);
                        productService.save(products);
                        redirectAttributes.addFlashAttribute("message", "Cập nhật thành công " + productReq.getName());
                        redirectAttributes.addFlashAttribute("status", "done_delete");
                    } catch (Exception e) {
                        e.printStackTrace();
                        redirectAttributes.addFlashAttribute("message", "Cập nhật thất bại " + productReq.getName());
                        redirectAttributes.addFlashAttribute("status", "done_delete");

                    }
                } else {
                    img = productReq.getImg().getOriginalFilename();
                        String previousImg = product.get().getImg();
                        String previousCate = product.get().getCategory().getCategoryID();
                    try {
                        if (img == null) {
                            img = "logo.png";
                        }

                        InputStream inputStream = productReq.getImg().getInputStream();
                        Files.copy(inputStream, path.resolve(img), StandardCopyOption.REPLACE_EXISTING);
                        products = customConfiguration.modelMapper().map(productReq, Products.class);
                        products.setImg(img);
                        Files.deleteIfExists(Paths.get("src/main/resources/static/images/product/"+ previousCate +"/"+ previousImg));
                        productService.save(products);
                        redirectAttributes.addFlashAttribute("message", "Cập nhật thành công " + productReq.getName());
                        redirectAttributes.addFlashAttribute("status", "done_delete");
                    } catch (Exception e) {
                        e.printStackTrace();
                        redirectAttributes.addFlashAttribute("message", "Cập nhật thất bại " + productReq.getName());
                        redirectAttributes.addFlashAttribute("status", "done_delete");
                    }

                }
                redirectAttributes.addFlashAttribute("status", "done_delete");
                return "redirect:/admin/product";
            }
            @PostMapping("/updateProductImg")
            public String updateProductImgAndColor(@ModelAttribute("productImgRequest") ProductImgDTO
                                                           productReq, RedirectAttributes redirectAttributes) {
                    Optional<Product_Colors> productColor = productColorsService.findByID(productReq.getColorid());
                Optional<Product_Images> productImages = productImgService.findById(productReq.getImgid());
                Product_Images productimage = new Product_Images() ;
                String img;
                Path path = Paths.get("src/main/resources/static/images/product/" + productReq.getProduct().getCategory().getCategoryID()+"/");
                if (productReq.getImg().isEmpty()) {
                    try {
                        img = productImages.get().getImage();
                            productimage.setImage(img);
                            productimage.setImgID(productImages.get().getImgID());
                            productimage.setProductcolor(productColor.get());
                            productImgService.save(productimage);
                            redirectAttributes.addFlashAttribute("message", "Cập nhật thành công " + productReq.getProduct().getProductID());

                    } catch (Exception e) {
                        e.printStackTrace();
                        redirectAttributes.addFlashAttribute("message", "Cập nhật thất bại " + productReq.getProduct().getProductID());
                    }
                } else {
                    img = productReq.getImg().getOriginalFilename();
                    try {
                        if (img == null) {
                            img = "logo.png";
                        }
                        int countImg = productImgDAO.countImg(img);
                        if (countImg == 0) {
                            InputStream inputStream = productReq.getImg().getInputStream();
                            Files.copy(inputStream, path.resolve(img), StandardCopyOption.REPLACE_EXISTING);
                            productimage.setImgID(productImages.get().getImgID());
                            productimage.setImage(img);
                            productimage.setProductcolor(productColor.get());
                            productImgService.save(productimage);
                            redirectAttributes.addFlashAttribute("message", "Cập nhật thành công " + productReq.getProduct().getProductID());

                        } else {
                            productimage.setImgID(productImages.get().getImgID());
                            productimage.setImage(img);
                            productimage.setProductcolor(productColor.get());
                            productImgService.save(productimage);
                            redirectAttributes.addFlashAttribute("message", "Cập nhật thành công, tuy nhiên tên ảnh bạn vừa thêm đã tồn tại trước đó vui lòng kiểm tra lại " + productReq.getProduct().getProductID());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        redirectAttributes.addFlashAttribute("message", "Cập nhật thất bại " + productReq.getProduct().getProductID());
                    }

                }
                redirectAttributes.addFlashAttribute("status", "done_delete");
                return "redirect:/admin/product";
            }
            @PostMapping("/updateProductColor")
            public String updateProductColor(@ModelAttribute("productImgRequest") ProductImgDTO
                                                           productReq, RedirectAttributes redirectAttributes) throws SQLException {

                try {
                    Optional<Product_Colors> productColor = productColorsService.findByID(productReq.getColorid());
                    Product_Colors prd = customConfiguration.modelMapper().map(productReq, Product_Colors.class);
                    prd.setColor_name(productReq.getColor());
                    productColorsService.save(prd);
                    redirectAttributes.addFlashAttribute("message", "Cập nhật thành công " + productReq.getProduct().getProductID());

                } catch (Exception e) {
                        e.printStackTrace();
                        redirectAttributes.addFlashAttribute("message", "Cập nhật thất bại " + productReq.getProduct().getProductID());
                    }


                redirectAttributes.addFlashAttribute("status", "done_delete");
                return "redirect:/admin/product";
            }
        }
