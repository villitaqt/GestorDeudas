package com.grupoocho.gestor_deudas.controller;

import com.grupoocho.gestor_deudas.model.Cronograma;
import com.grupoocho.gestor_deudas.repository.CronogramaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class CronogramaController {

    @Autowired
    private CronogramaRepository cronogramaRepository;

    @GetMapping("/cronograma")
    public String showForm(Model model) {
        model.addAttribute("cronograma", new Cronograma());
        return "formularioCronograma"; // El nombre del archivo HTML en src/main/resources/templates
    }

    @PostMapping("/cronograma")
    public String submitForm(@ModelAttribute Cronograma cronograma) {
        cronogramaRepository.save(cronograma);
        return "redirect:/api/cronograma"; // Redirigir a la página del formulario después de guardar
    }
}