package ru.orekhov.SpringSecurity.security_app.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {// тут будем генерировать токен и валидировать
    @Value("${jwt_secret}")// поместили его и значение в апликейшн пропертис
    private String secret;// наш секретный ключ для токена поместим в переменную

    //генерируем токен
    public String generateToken(String username){// дата из таймзоны нашей сейчас+120 минут и в константу
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(120).toInstant());

        return JWT.create()
                .withSubject("User Details")//новый токен. в нем хранятся поля сабжект и в нем данные пользователя
                .withClaim("username",username)// + клейм в виде ключ-значение
                .withIssuedAt(new Date())// время когда токен был выдан.текущее
                .withIssuer("orekhov")// кто выдал токен. в реальном приложении скорее быдует его название
                .withExpiresAt(expirationDate)// когда закончится токен. через 120 мин как указали выше
                .sign(Algorithm.HMAC256(secret));// выбираем алгоритм шифрования и передаем секретную строку


    }

    // реализуем еще один метод когда клиент шлем нам запрос с токеном. мы должны его валидировать. и извлечь юзернейм

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {// передаем в аргумент токен в виде строки

       JWTVerifier verifier =  JWT.require(Algorithm.HMAC256(secret))// валидируем наш секрет
                .withSubject("User Details")// у него должен быть такой юзер детайлс
                .withIssuer("orekhov")// должен быть выдан тем же
                .build();// только такой токен будет проходить нашу валидацию

             DecodedJWT jwt = verifier.verify(token);// декодируемый jwt
            return jwt.getClaim("username").asString();// получаем юзернейм как строку после декодирования




    }


}
