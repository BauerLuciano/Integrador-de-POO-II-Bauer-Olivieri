package com.inmobiliaria.alquileres_temporarios.gastos.service;

import com.inmobiliaria.alquileres_temporarios.gastos.model.GastoMantenimiento;
import com.inmobiliaria.alquileres_temporarios.gastos.repository.GastoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GastoService {

    private final GastoRepository gastoRepo;

    @Transactional
    public GastoMantenimiento registrarGasto(GastoMantenimiento gasto) {
        return gastoRepo.save(gasto);
    }

    public List<GastoMantenimiento> listarPorPropiedad(Long propiedadId) {
        return gastoRepo.findByPropiedadId(propiedadId);
    }

    @Transactional
    public void eliminarGasto(Long id) {
        gastoRepo.deleteById(id);
    }
}