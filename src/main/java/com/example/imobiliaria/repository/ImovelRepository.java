package com.example.imobiliaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.imobiliaria.model.Imovel;

public interface ImovelRepository extends JpaRepository<Imovel, Long> {

    List<Imovel> findTop5ByOrderByIdDesc();

    List<Imovel> findByPrecoBetween(double min, double max);
}