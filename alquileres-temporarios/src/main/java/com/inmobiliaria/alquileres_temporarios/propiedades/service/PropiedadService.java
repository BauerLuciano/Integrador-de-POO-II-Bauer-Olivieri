package com.inmobiliaria.alquileres_temporarios.propiedades.service;

import com.inmobiliaria.alquileres_temporarios.propiedades.model.ExcepcionCalendario;
import com.inmobiliaria.alquileres_temporarios.propiedades.model.Propiedad;
import com.inmobiliaria.alquileres_temporarios.propiedades.repository.ExcepcionRepository;
import com.inmobiliaria.alquileres_temporarios.propiedades.repository.PropiedadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropiedadService {

    private final PropiedadRepository propiedadRepo;
    private final ExcepcionRepository excepcionRepo;

    public Propiedad guardarPropiedad(Propiedad propiedad) {
        if (propiedad.getPrecioPorNoche() <= 0 || propiedad.getPorcentajeDeposito() < 0) {
            throw new RuntimeException("El precio debe ser mayor a 0 y el depósito no puede ser negativo.");
        }
        return propiedadRepo.save(propiedad);
    }


    public ExcepcionCalendario bloquearFechas(Long propiedadId, LocalDate inicio, LocalDate fin, String motivo) {
        Propiedad p = propiedadRepo.findById(propiedadId)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
        
        ExcepcionCalendario exc = new ExcepcionCalendario();
        exc.setPropiedad(p);
        exc.setFechaInicio(inicio);
        exc.setFechaFin(fin);
        exc.setMotivo(motivo);
        
        return excepcionRepo.save(exc);
    }

    public boolean verificarDisponibilidad(Long propiedadId, LocalDate inicio, LocalDate fin) {
        List<ExcepcionCalendario> bloqueos = excepcionRepo.findByPropiedadId(propiedadId);
        
        return bloqueos.stream().noneMatch(b -> b.seSolapaCon(inicio, fin));
    }

    public List<Propiedad> obtenerTodas() {
        return propiedadRepo.findAll();
    }
}