package ru.com.m74.spring.webinitializertest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mixam
 * @since 18.05.16 19:18
 */
@RestController
@RequestMapping(path = "/test", method = RequestMethod.GET)
public class ExampleController {

    @RequestMapping(value = "/helo", produces = "text/plain")
    public String helo() {
        return "helo";
    }
}
