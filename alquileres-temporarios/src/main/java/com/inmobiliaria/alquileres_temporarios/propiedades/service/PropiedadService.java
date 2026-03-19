package com.inmobiliaria.alquileres_temporarios.propiedades.service;

import com.inmobiliaria.alquileres_temporarios.propiedades.model.ExcepcionCalendario;
import com.inmobiliaria.alquileres_temporarios.propiedades.model.Propiedad;
import com.inmobiliaria.alquileres_temporarios.propiedades.repository.ExcepcionRepository;
import com.inmobiliaria.alquileres_temporarios.propiedades.repository.PropiedadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- EL ESCUDO IMPORTADO

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropiedadService {

    private final PropiedadRepository propiedadRepo;
    private final ExcepcionRepository excepcionRepo;

    @Transactional
    public Propiedad guardarPropiedad(Propiedad propiedad) {
        if (propiedad.getPrecioPorNoche().compareTo(BigDecimal.ZERO) <= 0 || 
            propiedad.getPorcentajeDeposito().compareTo(BigDecimal.ZERO) < 0) {
            
            throw new RuntimeException("El precio debe ser mayor a 0 y el depósito no puede ser negativo.");
        }
        return propiedadRepo.save(propiedad);
    }

    @Transactional
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
        return propiedadRepo.findAllOptimizadas();
    }

    @Transactional
    public void eliminar(Long id) {
        if (!propiedadRepo.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: La propiedad con ID " + id + " no existe.");
        }
        propiedadRepo.deleteById(id);
    }

    public Propiedad buscarPorId(Long id) {
        return propiedadRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la propiedad con ID: " + id));
    }

    @Transactional
    public Propiedad actualizar(Long id, Propiedad datosNuevos) {
        Propiedad propiedadExistente = buscarPorId(id);
    
        propiedadExistente.setDireccion(datosNuevos.getDireccion());
        propiedadExistente.setPrecioPorNoche(datosNuevos.getPrecioPorNoche());
        propiedadExistente.setPorcentajeDeposito(datosNuevos.getPorcentajeDeposito());
        propiedadExistente.setPoliticaCancelacion(datosNuevos.getPoliticaCancelacion());
        propiedadExistente.setPropietario(datosNuevos.getPropietario());
        
        if (datosNuevos.getEstado() != null) {
            propiedadExistente.setEstado(datosNuevos.getEstado());
        }
        return guardarPropiedad(propiedadExistente);
    }
}