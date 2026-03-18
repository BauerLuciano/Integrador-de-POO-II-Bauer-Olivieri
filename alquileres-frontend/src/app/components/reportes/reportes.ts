import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReporteLiquidacion } from '../../models/reporte-liquidacion';
import { PropietarioService } from '../../services/propietario';
import { ReporteService } from '../../services/reporte';
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
  
  // Inicializamos con el mes actual en formato "YYYY-MM" (ej: "2026-03")
  periodoSeleccionado: string = new Date().toISOString().slice(0, 7);

  datos: ReporteLiquidacion | null = null;

  private propietarioService = inject(PropietarioService);
  private reporteService = inject(ReporteService);

  ngOnInit() {
    this.propietarioService.getPropietarios().subscribe(res => this.propietarios = res);
  }

  consultar() {
    if (this.idPropietarioSel == 0) return alert("Seleccioná un propietario");

    // "periodoSeleccionado" viene como "2026-03". Lo partimos:
    const [anio, mes] = this.periodoSeleccionado.split('-').map(Number);

    this.reporteService.getLiquidacion(this.idPropietarioSel, mes, anio)
      .subscribe(res => this.datos = res);
  }
}