package ru.orekhov.SpringSecurity.security_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.orekhov.SpringSecurity.security_app.models.Person;
import ru.orekhov.SpringSecurity.security_app.repositories.PeopleRepository;

@Service
public class RegistrationService {

    private final PeopleRepository peopleRepository;

    private final PasswordEncoder passwordEncoder;// внедрили пасворд инкодер с шифрованием
@Autowired
    public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
}

@Transactional
    public void register(Person person){// сохранили человека. рега прошла успешно

    person.setPassword(passwordEncoder.encode(person.getPassword()));// устанавливаем пароль челу с енкод

    person.setRole("ROLE_USER");// каждый новый пользователь теперь имеет роль юзер

    peopleRepository.save(person);

    }
}
