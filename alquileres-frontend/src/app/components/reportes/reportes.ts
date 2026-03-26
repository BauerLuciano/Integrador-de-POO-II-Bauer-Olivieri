import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReporteLiquidacion } from '../../models/reporte-liquidacion';
import { PropietarioService } from '../../services/propietario';
import { ReporteService } from '../../services/reporte';
import { ReservaService } from '../../services/reserva'; 
import { Propietario } from '../../models/propietario';
import { AlertService } from '../../services/alert.service';

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
  datos: any | null = null; // Lo flexibilizamos porque cambiamos la estructura del DTO
  historialPagos: any[] = [];

  mostrarEstadisticas: boolean = false;
  fechaInicioEstadistica: string = '';
  fechaFinEstadistica: string = '';
  datosEstadistica: any = null;

  paginaActual: number = 1;

  private propietarioService = inject(PropietarioService);
  private reporteService = inject(ReporteService);
  private reservaService = inject(ReservaService); 
  private alertService = inject(AlertService);

  ngOnInit() {
    this.propietarioService.getPropietarios().subscribe(res => this.propietarios = res);
  }

  resetearVistas(): void {
    this.datos = null;
    this.historialPagos = [];
    this.mostrarEstadisticas = false;
    this.datosEstadistica = null;
    this.paginaActual = 1;
  }

  consultar(): void {
    this.mostrarEstadisticas = false; 
    this.paginaActual = 1; 

    if (this.idPropietarioSel == 0) {
      this.alertService.notificacion('Atención', 'Seleccioná un propietario', 'warning');
      return; 
    }

    const [anio, mes] = this.periodoSeleccionado.split('-').map(Number);

    this.reporteService.getLiquidacion(this.idPropietarioSel, mes, anio)
      .subscribe(res => this.datos = res);

    this.reporteService.getHistorialLiquidaciones(this.idPropietarioSel)
      .subscribe(res => {
        this.historialPagos = res.sort((a: any, b: any) => b.id - a.id);
      });
  }

  descargarPDF(): void {
    if (this.idPropietarioSel == 0) {
      this.alertService.notificacion('Atención', 'Seleccioná un propietario primero', 'warning');
      return; 
    }
    const [anio, mes] = this.periodoSeleccionado.split('-').map(Number);
    this.reporteService.exportarPDF(this.idPropietarioSel, mes, anio);
  }

  consultarEstadisticas(): void {
    this.datos = null; 
    this.mostrarEstadisticas = true;
    
    if (this.idPropietarioSel == 0) {
      this.alertService.notificacion('Atención', 'Seleccioná un propietario', 'warning');
      return; 
    }
    if (!this.fechaInicioEstadistica || !this.fechaFinEstadistica) {
      this.alertService.notificacion('Atención', 'Seleccioná un rango de fechas', 'warning');
      return; 
    }

    this.reservaService.getIngresosPropietario(this.idPropietarioSel, this.fechaInicioEstadistica, this.fechaFinEstadistica)
      .subscribe(res => this.datosEstadistica = res);
  }

  confirmarLiquidacion(): void {
    if (this.idPropietarioSel == 0) {
      this.alertService.notificacion('Atención', 'Seleccioná un propietario primero', 'warning');
      return; 
    }

    if (this.datos?.detalleReservas?.length === 0 && this.datos?.detalleGastos?.length === 0) {
      this.alertService.notificacion('Sin movimientos', 'No hay ingresos ni gastos para liquidar en este período.', 'info');
      return;
    }
    
    this.alertService.confirmar(
      '¿Liquidar y marcar como pagado?', 
      'Esta acción guardará el registro financiero y no se puede deshacer.', 
      'Sí, liquidar'
    ).then((result: any) => {
      if (result.isConfirmed) {
        const [anio, mes] = this.periodoSeleccionado.split('-').map(Number);

        this.reporteService.confirmarLiquidacion(this.idPropietarioSel, mes, anio).subscribe({
          next: (res) => {
            this.alertService.exito('¡Liquidación Exitosa!', 'El registro fue guardado correctamente.');
            this.datos = null; 
            this.paginaActual = 1; 
            this.reporteService.getHistorialLiquidaciones(this.idPropietarioSel)
              .subscribe(historial => {
                this.historialPagos = historial.sort((a: any, b: any) => b.id - a.id);
              });
          },
          error: (err) => {
            console.error("Error al liquidar", err);
            this.alertService.error('Error', 'Hubo un problema al procesar la liquidación.');
          }
        });
      }
    });
  }
}