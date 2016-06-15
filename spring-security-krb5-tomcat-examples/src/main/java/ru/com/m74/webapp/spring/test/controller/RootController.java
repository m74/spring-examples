package ru.com.m74.webapp.spring.test.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mixam
 * @since 14.05.16 13:15
 */
@RestController
public class RootController {
    @RequestMapping(value = "/remoteUser", produces = "application/json")
    public Object principal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
