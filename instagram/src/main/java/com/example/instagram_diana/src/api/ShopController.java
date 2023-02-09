package com.example.instagram_diana.src.api;

import com.example.instagram_diana.config.BaseResponse;
import com.example.instagram_diana.src.model.Product;
import com.example.instagram_diana.src.service.ShopService;
import com.example.instagram_diana.src.utils.JwtService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("app/shops")
public class ShopController {

    private final JwtService jwtService;

    private final ShopService shopService;

    public ShopController(JwtService jwtService, ShopService shopService) {
        this.jwtService = jwtService;
        this.shopService = shopService;
    }

    @PostMapping("/products")
    public BaseResponse<?> product(@RequestBody Product product){

        shopService.uploadProduct(product);

        return new BaseResponse<>("상품 등록 완료");
    }

    @GetMapping ("/products")
    public BaseResponse<?> productLists(){

        List<Product> productList = shopService.products();

        return new BaseResponse<>(productList);
    }

}
