package com.example.imobiliaria.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.imobiliaria.model.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByData(LocalDate data);

    boolean existsByDataAndHorario(LocalDate data, String horario);
}