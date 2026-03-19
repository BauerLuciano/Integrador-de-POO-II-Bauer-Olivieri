import { Propiedad } from './propiedad';

export interface Reserva {
  id?: number;
  propiedad?: Propiedad; 
  inquilino: string; 
  fechaInicio: string | Date;
  fechaFin: string | Date;
  montoTotal: number;
  depositoRetenido: number;
  comisionInmobiliaria?: number;
  montoPenalidad?: number;
  estado?: string;
  
  liquidada?: boolean;           
  motivoCancelacion?: string;    
  detalleCancelacion?: string;   
}