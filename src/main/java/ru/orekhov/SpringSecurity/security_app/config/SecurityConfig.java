package ru.orekhov.SpringSecurity.security_app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.orekhov.SpringSecurity.security_app.services.PersonDetailsService;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)// теперь можем использовать спец аннотацию
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final PersonDetailsService personDetailsService;
    private final JWTFilter jwtFilter;// внедряем наш фильтр
    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService, JWTFilter jwtFilter) {
        this.personDetailsService = personDetailsService;
        this.jwtFilter = jwtFilter;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        // конфигурируем сам Spring Security(какая страница отвечает за вход,ошибки и тд)
        // конфигурируем авторизацию
        http.csrf().disable()
                .authorizeRequests()

                .antMatchers("/auth/login","/auth/registration", "/error").permitAll()
                .anyRequest().hasAnyRole("USER","ADMIN")// для всех остальных запросов и юзер и админ доступ
                .and()
                .formLogin().loginPage("/auth/login").
                loginProcessingUrl("/process_login").
                defaultSuccessUrl("/hello",true).
                failureUrl("/auth/login?error").
                and().
                logout().
                logoutUrl("/logout").
                logoutSuccessUrl("/auth/login").
                and().
                sessionManagement().
                sessionCreationPolicy(SessionCreationPolicy.STATELESS);// даем понять спринг секюрити что теперь стейтлесс политика
// добавляем фильтр который помогает проводить аутентификацию
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    //настраиваем аутентификацию
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService)//передаем наш сервис что бы спринг сам все сделал за нас
                .passwordEncoder(getPasswordEncoder());// добавили шифрование при аутентификации

    }
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();// добавили шифрование BCrypt
    }
    @Bean
    @Override // этот метод позволяет производить аутентификацию посредством инфы какую указали.(юзер и пароль)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();

    }

}