package ru.orekhov.SpringSecurity.security_app.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.orekhov.SpringSecurity.security_app.models.Person;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person,Integer> {
    Optional<Person> findByUsername(String username);
    // ищем человека по имени,а JPA возвращает нам его из таблицы Person

}