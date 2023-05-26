package ru.orekhov.SpringSecurity.security_app.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
@PreAuthorize("hasRole('ROLE_ADMIN')")// ограничили доступ через аннотацию. только для админа
    public void doAdminStuff(){
        System.out.println("Only Admin here");
    }
}
