import { Propietario } from './propietario';

// Interfaz para la política
export interface PoliticaCancelacion {
  tipo: string; 
  porcentajeRetencion?: number; // Solo para Estricta
  diasAnticipacionSinCargo?: number; // Solo para Flexible
  porcentajePenalidadTardia?: number; // Solo para Flexible
}

export interface Propiedad {
  id?: number;
  direccion: string;
  precioPorNoche: number;
  porcentajeDeposito: number;
  politicaCancelacion: PoliticaCancelacion; 
  estado: string; 
  propietario: Propietario; 
}