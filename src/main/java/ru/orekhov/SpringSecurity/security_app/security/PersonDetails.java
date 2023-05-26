package ru.orekhov.SpringSecurity.security_app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.orekhov.SpringSecurity.security_app.models.Person;

import java.util.Collection;
import java.util.Collections;

public class PersonDetails implements UserDetails {

    private final Person person;// внедрили наш персон для которого персондетаилс обертка

    public PersonDetails(Person person) {
        this.person = person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
// человек получает свою роль ROLE_USER или ROLE_ADMIN
       return Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));


    }

    @Override
    public String getPassword() {// возвращаем пароль этого человека
        return this.person.getPassword();
    }

    @Override
    public String getUsername() {// возвращает имя человека
        return this.person.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {// показывает что сущность активна. не просрочен
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {// не заблокирован
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {// пароль не просрочен
        return true;
    }

    @Override
    public boolean isEnabled() {// аккаунт включен
        return true;
    }
    // нужно ,чтобы получать данные аутентифицированного пользователя
    public Person getPerson(){
        return this.person;
    }
}
