package com.course.security.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Ponto de entrada para autenticação JWT que lida com respostas não autorizadas.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Método para iniciar o processo de autentiacação quando uma requisição não autorizada é feita
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}
