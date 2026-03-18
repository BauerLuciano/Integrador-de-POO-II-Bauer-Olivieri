import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { PropiedadService } from '../../../services/propiedad';
import { Propiedad } from '../../../models/propiedad';

@Component({
  selector: 'app-propiedad-detalle',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './propiedad-detalle.html'
})
export class PropiedadDetalleComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private propService = inject(PropiedadService);
  private location = inject(Location);

  propiedad: Propiedad | null = null;
  loading: boolean = true;

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      if (id) {
        this.propService.getPropiedadById(id).subscribe({
          next: (data) => {
            this.propiedad = data;
            this.loading = false;
          },
          error: (err) => {
            console.error('Error cargando detalle', err);
            this.loading = false;
          }
        });
      }
    });
  }

  volver() {
    this.location.back();
  }
}