package ru.orekhov.SpringSecurity.security_app.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.orekhov.SpringSecurity.security_app.security.JWTUtil;
import ru.orekhov.SpringSecurity.security_app.services.PersonDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JWTFilter extends OncePerRequestFilter {

private final JWTUtil jwtUtil;
private final PersonDetailsService personDetailsService;

    public JWTFilter(JWTUtil jwtUtil, PersonDetailsService personDetailsService) {
        this.jwtUtil = jwtUtil;
        this.personDetailsService = personDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      String authHeader = request.getHeader("Authorization");// извлечем хедер авторизация
        // если наш хедер существует(не нул),не пустой,начинается на Bearer
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")){
            String jwt = authHeader.substring(7);//сабстринг начиная с 7 индекса(Bearer )

            if (jwt.isBlank()){// если наш jwt пустой
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid JWT Token in Bearer Header");
            }else{
                try {


                    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);// это наш токен все ок
                    UserDetails userDetails = personDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authToken = new
                            UsernamePasswordAuthenticationToken(userDetails,userDetails.getPassword(),//получаем пароль из бд
                            userDetails.getAuthorities());// получаем его авторизацию

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {// если нет контекста
                        SecurityContextHolder.getContext().setAuthentication(authToken);// мы его кладем

                    }
                }catch (JWTVerificationException exc){// если вылезла ошибка верификации
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid JWT Token");

                }
            }
        }
// ПОСЛЕ ТОГО КАК в запросе получили все нужное,надо запрос дальше продвинуть по цепочек наших фильтров
        filterChain.doFilter(request,response);
    }
}
