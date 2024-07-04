package com.grupoocho.gestor_deudas.Controller;

import com.grupoocho.gestor_deudas.model.Deuda;
import com.grupoocho.gestor_deudas.model.Recibo;
import com.grupoocho.gestor_deudas.repository.DeudaRepository;
import com.grupoocho.gestor_deudas.repository.ReciboRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deuda/recibo")
public class ReciboController {

    private final ReciboRepository reciboRepository;

    @Autowired
    public ReciboController(ReciboRepository reciboRepository) {
        this.reciboRepository = reciboRepository;
    }
    @PostMapping
    public void postRecibo(@RequestBody Recibo recibo){
        reciboRepository.save(recibo);
    }

    @GetMapping
    public List<Recibo> getRecibos(){
        return reciboRepository.findAll();
    }
}
