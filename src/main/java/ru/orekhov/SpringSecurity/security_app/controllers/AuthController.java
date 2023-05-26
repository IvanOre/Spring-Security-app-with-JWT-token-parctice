package ru.orekhov.SpringSecurity.security_app.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.orekhov.SpringSecurity.security_app.dto.AuthenticationDTO;
import ru.orekhov.SpringSecurity.security_app.dto.PersonDTO;
import ru.orekhov.SpringSecurity.security_app.models.Person;
import ru.orekhov.SpringSecurity.security_app.security.JWTUtil;
import ru.orekhov.SpringSecurity.security_app.services.RegistrationService;
import ru.orekhov.SpringSecurity.security_app.util.PersonValidator;

import javax.validation.Valid;
import java.util.Map;

@RestController // поменяли на рест потому что будем все делать через постман , а не браузер
@RequestMapping("/auth")
public class AuthController {
    private final RegistrationService registrationService;
    private final PersonValidator personValidator;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;


@Autowired
    public AuthController(PersonValidator personValidator, RegistrationService registrationService,
                          JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
}

  /*  @GetMapping("/login")// страница нашей аутентификации/представление в хтмл логин
    public String loginPage(){
    return "auth/login";

    }
@GetMapping("/registration")
    public String registrationPage(@ModelAttribute ("person") Person person){// получаем данные человека person
    return "auth/registration";
    }*/
@PostMapping("/registration")
    public Map<String,String > performRegistration(@RequestBody @Valid PersonDTO personDTO,
                                   BindingResult bindingResult){

    Person person = convertToPerson(personDTO);// получаем этого человека после конвертации

    personValidator.validate(person,bindingResult);// проверка на чела с существующим именем(на валидность)

    if (bindingResult.hasErrors())
        return Map.of("message","Ошибка!");// если что-то не атк выкидываем ошибку в строке MAP(выше сделали)

    registrationService.register(person);// зарегали человека

    // генерируем токен в виде Json и отправляем клиенту
    String token = jwtUtil.generateToken(person.getUsername());//  в качестве данных вшитых в токен передаем имя пользователя

    return Map.of("jwt_token",token);// возвращаем jwt token

    }
@PostMapping("/login")// выдаем новый токен по логину и паролю
    public Map<String,String> performLogin(@RequestBody AuthenticationDTO authenticationDTO){
    UsernamePasswordAuthenticationToken authInputToken =
            new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                    authenticationDTO.getPassword());// из аутентификации юзера получим юзернейм и пароль
            try {
                authenticationManager.authenticate(authInputToken);
            }catch (BadCredentialsException e){// вылезет ошибка если неправильный логин и пароль передан был
                return Map.of("message","Incorrect credentials!");
            }

            String token = jwtUtil.generateToken(authenticationDTO.getUsername());// если все успешно,то создаем токен
            return Map.of("jwt_token",token);
    }

    public Person convertToPerson(PersonDTO personDTO){// конвертируем наше ДТО в человека Персон
    return this.modelMapper.map(personDTO, Person.class);
    }
}
