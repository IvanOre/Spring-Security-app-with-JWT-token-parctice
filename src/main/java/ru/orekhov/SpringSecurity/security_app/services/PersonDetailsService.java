package ru.orekhov.SpringSecurity.security_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.orekhov.SpringSecurity.security_app.models.Person;
import ru.orekhov.SpringSecurity.security_app.repositories.PeopleRepository;
import ru.orekhov.SpringSecurity.security_app.security.PersonDetails;

import java.util.Optional;

// предназначается именно для спринг секьюрити и что бы он занчл что сервис загружает пользователя,реализуем
// специальный интерфейс UserDetailsService/ даст понять что загружает пользователя по его имени
@Service
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;
    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUsername(s);
        if (person.isEmpty())// если пользователя с таким именем нет
            throw new UsernameNotFoundException("User not found!");
        // если человек есть то оборачиваем его в PersonDetails  и возвращаем его из этого метода
        return new PersonDetails(person.get());

    }

}