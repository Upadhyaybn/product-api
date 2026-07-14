package com.bhawani.product_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * हमारी पहली REST API का controller.
 *
 * RestController बताता है कि यह class HTTP requests handle करेगी
 * और method से लौटाया गया data सीधे HTTP response में जाएगा.
 */
@RestController
@RequestMapping("/api/v1")
public class HelloController {

    /**
     * GET /api/v1/hello request को handle करता है.
     *
     * @return API success message
     */
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello Bhawani! Your first Spring Boot API is running successfully.";
    }
}