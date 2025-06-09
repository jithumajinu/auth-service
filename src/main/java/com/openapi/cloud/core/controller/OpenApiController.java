package com.openapi.cloud.core.controller;

import com.acid.core.web.constants.ApiErrorCode;
import com.acid.core.web.model.ModelPage;
import com.acid.core.web.model.ApiResponse;
import com.acid.core.web.model.ApiResponse.ApiError;

import com.openapi.cloud.core.config.swagger.annotations.OpenApiOperations;
import com.openapi.cloud.core.constants.NotificationType;
import com.openapi.cloud.core.exception.NotFoundException;
import com.openapi.cloud.core.mapper.ProductMapper;
import com.openapi.cloud.core.model.ValidationGroups;
import com.openapi.cloud.core.model.dto.ProductDto;
import com.openapi.cloud.core.model.dto.request.GetAllProductRequest;
import com.openapi.cloud.core.model.dto.request.ProductRequest;
import com.openapi.cloud.core.service.MessageResourceHolder;
import com.openapi.cloud.core.service.ProductService;
import com.openapi.cloud.core.service.UserNotificationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

// import com.acid.core.web.GreetingService

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/openApis")
public class OpenApiController extends AbstractCoreUtilController {

    @Autowired
    private final ProductService productService;

    private final UserNotificationService notificationService;


    public OpenApiController(ProductService productService, UserNotificationService notificationService) {
        this.productService = productService;
        this.notificationService = notificationService;
    }

    /**
     * GET
     */
    @GetMapping("/triggerError")
    public ResponseEntity<String> triggerError() {
        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * GET
     */
    @OpenApiOperations.OpenApiListOperation(tags = "Product", summary = "", description = "Product list", operationId = "ProductList")
    @GetMapping("/product")
    public ApiResponse<List<ProductDto>> productList() {
        log.info("API:/product get all products ::start");
        var responseBuilder = ApiResponse.<List<ProductDto>>builder().flag(true);
        List<ProductDto> productList = productService.getProductList();
        responseBuilder.data(productList);
        log.info("API:/product get all products ::end");
        return responseBuilder.build();
    }

    /**
     * GET
     */
    @OpenApiOperations.OpenApiListOperation(tags = "Product", summary = "", description = "Product list Paged", operationId = "ProductListPaged")
    @GetMapping("/product-page")
    public ApiResponse<ModelPage<ProductDto>> productListPaged(
            GetAllProductRequest request,
            HttpServletResponse httpServletResponse

    ) {
        log.info("API:/product-page ::start");

        var responseBuilder = ApiResponse.<ModelPage<ProductDto>>builder().flag(true);
        var productList = productService.getProductPage(request);
        responseBuilder.data(productList);
        log.info("API:product-page ::end");
        return responseBuilder.build();
    }

    /**
     * POST /product
     *
     * @param productRequest (required)
     * @return successful operation (status code 201)
     */
    @OpenApiOperations.OpenApiObjectOperation(tags = "Product", summary = "createProduct", description = "createProduct", operationId = "createProduct")
    @PostMapping("/product")
    public ApiResponse<ProductDto> createProduct(
            @Validated(ValidationGroups.Create.class) @RequestBody(required = true) ProductRequest productRequest,
            BindingResult bindingResult, HttpServletResponse httpServletResponse) {

        var responseBuilder = ApiResponse.<ProductDto>builder().flag(true);
        if (bindingResult.hasErrors()) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiError.builder()
                            .errorCode(ApiErrorCode.INPUT_ERROR)
                            .message(MessageResourceHolder.get().getLabel(ApiErrorCode.INPUT_ERROR.getLabel()))
                            .errors(formatInputErrors(bindingResult))
                            .build())
                    .build();


        }
        ProductDto newProduct = productService.createProduct(ProductMapper.MAPPER.toDto(productRequest));
        responseBuilder.data(newProduct);
        return responseBuilder.build();
    }

    /**
     * PUT /{productId}
     *
     * @param productId (required)
     * @param product   (required)
     */
    @OpenApiOperations.OpenApiObjectOperation(tags = "Product", summary = "Update an existing Product", description = "Update an existing Product", operationId = "updateProductIdById")
    @PutMapping("product/{productId}")
    public ResponseEntity<String> updateProductById(@PathVariable("productId") Long productId,
                                                    @Validated(ValidationGroups.Update.class) @RequestBody(required = true) ProductRequest product) {
        return ResponseEntity.ok("Resource updated successfully");
    }

    /**
     * GET /{productId}
     *
     * @param productId (required)
     * @return successful operation (status code 200)
     */
    @OpenApiOperations.OpenApiObjectOperation(tags = "Product", summary = "productId", description = "productId", operationId = "productId")
    @GetMapping("product/{productId}")
    public ResponseEntity<ProductDto> productById(@PathVariable("productId") Long productId) {

        if (productId == 0) {
            throw new NotFoundException("Product with ID " + productId + " not found.");
        }
        ProductDto productDto = productService.getProductById(productId);

        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    /**
     * DELETE /{productId}
     *
     * @param productId to delete (required)
     * @return Successful operation (status code 200)
     */
    @OpenApiOperations.OpenApiObjectOperation(tags = "Product", summary = "deleteProduct", description = "deleteProduct", operationId = "deleteProduct")
    @DeleteMapping("products/{productId}")
    public ResponseEntity<String> deleteProductId(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok("Resource deleted successfully");
    }

    /**
     * POST sendNotification
     *
     * @param to      (required)
     * @param subject (required)
     * @param message (required)
     * @param type    (required)
     * @return successful operation (status code 200)
     */
    @OpenApiOperations.OpenApiObjectOperation(tags = "Notification", summary = "Notification", description = "Notification", operationId = "Notification")
    @PostMapping("/send")
    public ApiResponse<String> sendNotification(@RequestParam String to, @RequestParam String subject,
                                                @RequestParam String message, @RequestParam NotificationType type) {

        log.info("API:/send notification ::start");

        var responseBuilder = ApiResponse.<String>builder().flag(true);
        if (type == NotificationType.EMAIL) {
            log.error("API:/send notification ::exception throw");
            throw new NotFoundException("email type not Found test purpose");
        }
        notificationService.notifyUser(to, subject, message, type);

        responseBuilder.data("Success");
        log.info("API:/send notification ::end");
        return responseBuilder.build();

    }
}
