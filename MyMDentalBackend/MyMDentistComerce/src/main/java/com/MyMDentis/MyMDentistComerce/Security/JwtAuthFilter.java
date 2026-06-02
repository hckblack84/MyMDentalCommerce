package com.MyMDentis.MyMDentistComerce.Security;

import com.MyMDentis.MyMDentistComerce.Service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final List<String> noFilterPaths = List.of(
            "/MyMDentalCommerce/session/",
            "/MyMDentalCommerce/products/saveProduct2",
            "/MyMDentalCommerce/products/clientProducts",
            "/MyMDentalCommerce/products/clientProducts/page/",
            "/MyMDentalCommerce/products/getClientProductById/",
            "/MyMDentalCommerce/products/filterClientProducts/",
            "/MyMDentalCommerce/products/filterClientProductsByPage/",
            "/MyMDentalCommerce/products/getProduct/",
            "/MyMDentalCommerce/products/getMaxProductPages",
            "/MyMDentalCommerce/products/getMaxProductPagesByDepartment/",
            "/MyMDentalCommerce/departments/getDepartments",
            "/MyMDentalCommerce/departments/createDepartment"
    );

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    private Logger log = Logger.getLogger(JwtAuthFilter.class.getName());

    public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        for (String noFilterPath : noFilterPaths){
            if (path.startsWith(noFilterPath)){
                filterChain.doFilter(request, response);
                log.info("no filtering request to: " + path);
                return;
            }
        }
        log.info("Filtering request to: " + path);
        String token = null;

        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            try {
                if (jwtService.validToken(token)) {
                    String username = jwtService.extractUsername(token);
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext()
                            .setAuthentication(authenticationToken);
                    log.info("User " + username + " authenticated successfully.");
                } else {
                    log.warning("Invalid JWT token provided.");
                }
            } catch (UsernameNotFoundException e) {
                log.warning("User not found for the provided token.");
            }
        } else {
            log.warning("No JWT token found in the request cookies.");
        }

        filterChain.doFilter(request, response);
    }
}