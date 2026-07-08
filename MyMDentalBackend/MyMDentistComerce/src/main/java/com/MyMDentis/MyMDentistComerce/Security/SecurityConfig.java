package com.MyMDentis.MyMDentistComerce.Security;

import com.MyMDentis.MyMDentistComerce.Model.Roles;
import com.MyMDentis.MyMDentistComerce.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${spring.cors.origins.test}")
    private String testPath;
    @Value("${spring.cors.origins.cloudfront}")
    private String cloudfrontPath;
    @Value("${spring.cors.origins.s3Bucket}")
    private String s3BucketPath;


    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, CustomUserDetailsService customUserDetailsService){
        this.jwtAuthFilter = jwtAuthFilter;
        this.customUserDetailsService = customUserDetailsService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/MyMDentalCommerce/session/**",
                                "/MyMDentalCommerce/users/findbyemail/**"
                        ).permitAll()

                        .requestMatchers("/MyMDentalCommerce/pay/webhook").permitAll()

                        .requestMatchers(
                                "/MyMDentalCommerce/products/clientProducts/page/**",
                                "/MyMDentalCommerce/products/getClientProductById/**",
                                "/MyMDentalCommerce/products/filterClientProductsByPage/**",
                                "/MyMDentalCommerce/products/getMaxProductPages",
                                "/MyMDentalCommerce/products/getMaxProductPagesByDepartment/**",
                                "/MyMDentalCommerce/departments/getDepartments"
                        ).permitAll()

                        .requestMatchers("/MyMDentalCommerce/pay/createOrder").authenticated()
                        .requestMatchers("/MyMDentalCommerce/users/update/**", "/MyMDentalCommerce/users/getUseremail/**", "/MyMDentalCommerce/users/updatePerfil/**").authenticated()
                        .requestMatchers("/MyMDentalCommerce/Reserved/saveNewReserved").authenticated()

                        .requestMatchers(
                                "/MyMDentalCommerce/products/adminProducts/page/**",
                                "/MyMDentalCommerce/products/filterAdminProducts/**",
                                "/MyMDentalCommerce/products/saveProduct",
                                "/MyMDentalCommerce/products/editProduct/**",
                                "/MyMDentalCommerce/departments/createDepartment",
                                "/MyMDentalCommerce/Reserved/getAllReserved",
                                "/MyMDentalCommerce/Reserved/getActiveReserved",
                                "/MyMDentalCommerce/Reserved/getNoActiveReserved",
                                "/MyMDentalCommerce/Reserved/getReservedById/**",
                                "/MyMDentalCommerce/Reserved/getReservedByUser/**",
                                "/MyMDentalCommerce/Reserved/checkReserved/**"
                        ).hasAnyAuthority(Roles.ADMINISTRATOR.name(), Roles.WORKER.name())

                        .requestMatchers(
                                "/MyMDentalCommerce/products/deleteProduct/**",
                                "/MyMDentalCommerce/users/getUsers",
                                "/MyMDentalCommerce/users/adminUpdate/**"
                        ).hasAuthority(Roles.ADMINISTRATOR.name())

                        .anyRequest().denyAll()
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(testPath, cloudfrontPath, s3BucketPath));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

}
