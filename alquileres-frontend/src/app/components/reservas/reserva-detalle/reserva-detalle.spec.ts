import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservaDetalle } from './reserva-detalle';

describe('ReservaDetalle', () => {
  let component: ReservaDetalle;
  let fixture: ComponentFixture<ReservaDetalle>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReservaDetalle]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReservaDetalle);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
