package com.openapi.cloud.core.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RestController
@RequestMapping("/api/customer")
public class CustomerController extends AbstractCoreUtilController {

    @GetMapping("/edit")
    public String edit() {
        return "customer - edit !";
    }

    @GetMapping("/add")
    public String add() {
        return "customer - add";
    }

    @GetMapping("/delete")
    public String delete() {
        return "customer - delete";
    }

    @GetMapping("/getbyid")
    public String getById() {
        return "customer - getById";
    }

    public String postTest() {
        return "postRequest!";
    }

    @PostMapping("/post")
    public String postRequest() {
        return "postRequest!";
    }

}
