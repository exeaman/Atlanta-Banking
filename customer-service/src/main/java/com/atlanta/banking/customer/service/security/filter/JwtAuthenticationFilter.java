package com.atlanta.banking.customer.service.security.filter;
/*
import com.atlanta.banking.audit.service.security.SecurityConstants;
import com.atlanta.banking.audit.service.security.jwt.JwtService;
import com.atlanta.banking.audit.service.security.principle.JwtPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Url requested {}", request.getRequestURI());
        String header = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
        log.info("Authorization header: {}", header);
        if (!StringUtils.hasText(header) || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            log.info("Auth failed exiting without authorities and credentials");
            return;
        }

        String token = header.substring(SecurityConstants.TOKEN_PREFIX.length());

        if (!jwtService.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            log.info("The token is not valid.");
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            List<SimpleGrantedAuthority> authorities = jwtService.extractRoles(token)
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            log.info("The authorities {}", authorities);
            JwtPrincipal principal = new JwtPrincipal(
                    jwtService.extractEmployeeId(token),
                    jwtService.extractUsername(token),
                    authorities
            );

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            authorities
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        log.info("Got the auth.");
        filterChain.doFilter(request, response);
    }
}
*/
