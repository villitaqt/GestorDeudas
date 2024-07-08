package com.grupoocho.gestor_deudas.service;

import com.grupoocho.gestor_deudas.model.Cronograma;
import com.grupoocho.gestor_deudas.repository.CronogramaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CronogramaService {

    private final CronogramaRepository cronogramaRepository;

    @Autowired
    public CronogramaService(CronogramaRepository cronogramaRepository) {
        this.cronogramaRepository = cronogramaRepository;
    }

    public Cronograma getCronogramaById(Long id) {
        return cronogramaRepository.findById(id).orElse(null);
    }

    public List<Cronograma> getAllCronogramas() {
        return cronogramaRepository.findAll();
    }

    public Cronograma guardarCronograma(Cronograma cronograma){
        return cronogramaRepository.save(cronograma);
    }
}
