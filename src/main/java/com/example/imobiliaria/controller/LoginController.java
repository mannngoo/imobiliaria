package com.example.imobiliaria.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final String USER = "admin";
    private final String SENHA = "123456";

    // TELA
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // PROCESSA LOGIN
    @PostMapping("/login")
    public String logar(
            @RequestParam String usuario,
            @RequestParam String senha,
            HttpSession session
    ) {

        if (USER.equals(usuario) && SENHA.equals(senha)) {
            session.setAttribute("logado", true);
            return "redirect:/cadastrar";
        }

        return "redirect:/login?erro";
    }

    // LOGOUT (único no sistema)
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}