import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReporteLiquidacion } from '../../models/reporte-liquidacion';
import { PropietarioService } from '../../services/propietario';
import { ReporteService } from '../../services/reporte';
import { ReservaService } from '../../services/reserva'; 
import { Propietario } from '../../models/propietario';

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reportes.html'
})
export class ReportesComponent implements OnInit {
  propietarios: Propietario[] = [];
  idPropietarioSel: number = 0;
  
  periodoSeleccionado: string = new Date().toISOString().slice(0, 7);
  datos: ReporteLiquidacion | null = null;
  historialPagos: any[] = [];

  mostrarEstadisticas: boolean = false;
  fechaInicioEstadistica: string = '';
  fechaFinEstadistica: string = '';
  datosEstadistica: any = null;

  private propietarioService = inject(PropietarioService);
  private reporteService = inject(ReporteService);
  private reservaService = inject(ReservaService); 

  ngOnInit() {
    this.propietarioService.getPropietarios().subscribe(res => this.propietarios = res);
  }

  consultar() {
    this.mostrarEstadisticas = false; 
    if (this.idPropietarioSel == 0) return alert("Seleccioná un propietario");

    const [anio, mes] = this.periodoSeleccionado.split('-').map(Number);

    this.reporteService.getLiquidacion(this.idPropietarioSel, mes, anio)
      .subscribe(res => this.datos = res);

    this.reporteService.getHistorialLiquidaciones(this.idPropietarioSel)
      .subscribe(res => this.historialPagos = res);
  }

  descargarPDF() {
    if (this.idPropietarioSel == 0) return alert("Seleccioná un propietario primero");
    const [anio, mes] = this.periodoSeleccionado.split('-').map(Number);
    this.reporteService.exportarPDF(this.idPropietarioSel, mes, anio);
  }

  consultarEstadisticas() {
    this.datos = null; 
    this.mostrarEstadisticas = true;
    
    if (this.idPropietarioSel == 0) return alert("Seleccioná un propietario");
    if (!this.fechaInicioEstadistica || !this.fechaFinEstadistica) return alert("Seleccioná un rango de fechas");

    this.reservaService.getIngresosPropietario(this.idPropietarioSel, this.fechaInicioEstadistica, this.fechaFinEstadistica)
      .subscribe(res => this.datosEstadistica = res);
  }

  confirmarLiquidacion() {
    if (this.idPropietarioSel == 0) return alert("Seleccioná un propietario primero");
    
    if (!confirm("¿Estás seguro de que querés liquidar y marcar como pagado? Esta acción guardará el registro y no se puede deshacer.")) {
      return; 
    }

    const [anio, mes] = this.periodoSeleccionado.split('-').map(Number);

    this.reporteService.confirmarLiquidacion(this.idPropietarioSel, mes, anio).subscribe({
      next: (res) => {
        alert("¡Liquidación registrada y guardada exitosamente!");
        this.datos = null; 
        // Refrescamos el historial automáticamente
        this.reporteService.getHistorialLiquidaciones(this.idPropietarioSel)
          .subscribe(historial => this.historialPagos = historial);
      },
      error: (err) => {
        console.error("Error al liquidar", err);
        alert("Hubo un problema al procesar la liquidación.");
      }
    });
  }
}