package com.example.imobiliaria.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.imobiliaria.model.Agendamento;
import com.example.imobiliaria.repository.AgendamentoRepository;

@Controller
public class ContatoController {

    @Autowired
    private AgendamentoRepository repo;

    // LISTA DE HORÁRIOS BASE
    private final List<String> TODOS_HORARIOS = List.of(
            "09:00","10:00","11:00",
            "14:00","15:00","16:00"
    );

    // 🔥 CARREGA PÁGINA + FILTRA HORÁRIOS DISPONÍVEIS POR DATA
    @GetMapping("/contato")
    public String contato(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate data,
            Model model
    ) {

        List<String> disponiveis = TODOS_HORARIOS;

        if (data != null) {
            List<Agendamento> agendados = repo.findByData(data);

            List<String> ocupados = agendados.stream()
                    .map(Agendamento::getHorario)
                    .toList();

            disponiveis = TODOS_HORARIOS.stream()
                    .filter(h -> !ocupados.contains(h))
                    .toList();
        }

        model.addAttribute("horariosDisponiveis", disponiveis);
        model.addAttribute("dataSelecionada", data);

        return "contato";
    }

    // 🔥 SALVAR AGENDAMENTO (COM VALIDAÇÃO PRA NÃO DAR 400)
    @PostMapping("/agendar")
    public String agendar(
            @RequestParam String nome,
            @RequestParam String telefone,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) String horario,
            Model model
    ) {

        // ❌ EVITA ERRO 400
        if (horario == null || horario.isBlank()) {
            return "redirect:/contato?erro=horario";
        }

        // ❌ EVITA DUPLICAR HORÁRIO
        boolean jaExiste = repo.existsByDataAndHorario(data, horario);
        if (jaExiste) {
            return "redirect:/contato?erro=ocupado";
        }

        Agendamento a = new Agendamento();
        a.setNome(nome);
        a.setTelefone(telefone);
        a.setData(data);
        a.setHorario(horario);

        repo.save(a);

        return "redirect:/contato?sucesso";
    }
}