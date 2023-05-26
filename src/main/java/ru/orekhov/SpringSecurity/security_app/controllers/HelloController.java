package ru.orekhov.SpringSecurity.security_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.orekhov.SpringSecurity.security_app.security.PersonDetails;
import ru.orekhov.SpringSecurity.security_app.services.AdminService;

@Controller
public class HelloController {

    private final AdminService adminService;// внедрили админ сервис
@Autowired
    public HelloController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/hello")
    public String sayHello(){
    return "hello";

    }
    @GetMapping("/showUserInfo")
    @ResponseBody
    public String showUserInfo(){
        // в контекстХолдере сохраним инфу полученную из контекста который получили при успешной аутентификации
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // получаем принципал из нашей аутентификации
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();// даункастим до PersonDetails

       return personDetails.getUsername();// возвращаем что получили из персон детаилс
    }

    @GetMapping("/admin")
    public String adminPage(){
    adminService.doAdminStuff();
    return "admin";
    }
}
