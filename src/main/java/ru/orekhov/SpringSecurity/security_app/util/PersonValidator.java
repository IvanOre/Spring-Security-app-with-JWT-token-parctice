package ru.orekhov.SpringSecurity.security_app.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.orekhov.SpringSecurity.security_app.models.Person;
import ru.orekhov.SpringSecurity.security_app.services.PersonDetailsService;

@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        try{
            personDetailsService.loadUserByUsername(person.getUsername());

    }catch (UsernameNotFoundException ignored){
            return;// все ок. чел не найден
        }

       errors.rejectValue("username","","Человек с таким именем уже существует");
        // не очень хороший код. тк опираемся на PersonDetailService и на исключение в нем. опора на исключение
        //не очень хороший стиль кода.могли бы реализовать отдельный сервис и там реализовать метод с логикой исключения
    }
}