package com.example.imobiliaria.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.imobiliaria.model.Imovel;
import com.example.imobiliaria.repository.ImovelRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    private final ImovelRepository repository;

    public HomeController(ImovelRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("novos", repository.findTop5ByOrderByIdDesc());
        return "index";
    }

    @GetMapping("/menu")
    public String menu(Model model) {

        List<Imovel> todos = repository.findAll();

        List<Imovel> medio = new ArrayList<>();
        List<Imovel> alto = new ArrayList<>();

        for (Imovel i : todos) {
            if ("MEDIO".equals(i.getCategoria())) {
                medio.add(i);
            } else {
                alto.add(i);
            }
        }

        model.addAttribute("medio", medio);
        model.addAttribute("alto", alto);

        return "menu";
    }

    @GetMapping("/imovel/{id}")
    public String detalhe(@PathVariable Long id, Model model) {

        Imovel imovel = repository.findById(id).orElse(null);

        if (imovel == null) return "redirect:/menu";

        model.addAttribute("imovel", imovel);
        return "imovel";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(HttpSession session, Model model) {

        if (session.getAttribute("logado") == null) {
            return "redirect:/login";
        }

        model.addAttribute("imoveis", repository.findAll());
        return "cadastrar";
    }

    @PostMapping("/salvar")
    public String salvar(
            @RequestParam String titulo,
            @RequestParam double preco,
            @RequestParam String endereco,
            @RequestParam int quartos,
            @RequestParam int banheiros,
            @RequestParam int vagas,
            @RequestParam String latitude,
            @RequestParam String longitude,
            @RequestParam String tipo,
            @RequestParam double area,
            @RequestParam String categoria,
            @RequestParam("imagens") List<MultipartFile> imagens
    ) {

        Imovel imovel = new Imovel();

        imovel.setTitulo(titulo);
        imovel.setPreco(preco);
        imovel.setEndereco(endereco);
        imovel.setQuartos(quartos);
        imovel.setBanheiros(banheiros);
        imovel.setVagas(vagas);
        imovel.setLatitude(latitude);
        imovel.setLongitude(longitude);
        imovel.setTipo(tipo);
        imovel.setArea(area);
        imovel.setCategoria(categoria);

        List<String> caminhos = new ArrayList<>();

        for (MultipartFile file : imagens) {
            if (!file.isEmpty()) {
                try {

                    String nome = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                    Path pasta = Paths.get("uploads");
                    Files.createDirectories(pasta);

                    Path caminho = pasta.resolve(nome);
                    Files.write(caminho, file.getBytes());

                    caminhos.add("uploads/" + nome);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        imovel.setImagens(caminhos);
        repository.save(imovel);

        return "redirect:/cadastrar";
    }

    @GetMapping("/imovel/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session) {

        if (session.getAttribute("logado") == null) {
            return "redirect:/login";
        }

        model.addAttribute("imovel", repository.findById(id).orElseThrow());
        return "editar";
    }

    @PostMapping("/imovel/editar/{id}")
    public String atualizar(
            @PathVariable Long id,
            @RequestParam String titulo,
            @RequestParam double preco,
            @RequestParam String endereco,
            @RequestParam int quartos,
            @RequestParam int banheiros,
            @RequestParam int vagas,
            @RequestParam double area,
            @RequestParam String categoria,
            HttpSession session
    ) {

        if (session.getAttribute("logado") == null) {
            return "redirect:/login";
        }

        Imovel i = repository.findById(id).orElseThrow();

        i.setTitulo(titulo);
        i.setPreco(preco);
        i.setEndereco(endereco);
        i.setQuartos(quartos);
        i.setBanheiros(banheiros);
        i.setVagas(vagas);
        i.setArea(area);
        i.setCategoria(categoria);

        repository.save(i);

        return "redirect:/imovel/" + id;
    }

    @PostMapping("/imovel/excluir/{id}")
    public String excluir(@PathVariable Long id, HttpSession session) {

        if (session.getAttribute("logado") == null) {
            return "redirect:/login";
        }

        repository.deleteById(id);

        return "redirect:/menu";
    }

    @PostMapping("/imovel/vendido/{id}")
    public String vendido(@PathVariable Long id, HttpSession session) {

        if (session.getAttribute("logado") == null) {
            return "redirect:/login";
        }

        Imovel i = repository.findById(id).orElseThrow();

        i.setVendido(true);

        repository.save(i);

        return "redirect:/imovel/" + id;
    }

    @GetMapping("/quem-somos")
    public String quemSomos() {
        return "quem-somos";
    }
}