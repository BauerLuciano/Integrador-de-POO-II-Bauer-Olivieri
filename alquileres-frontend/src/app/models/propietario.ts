export interface EsquemaComision {
  tipo_esquema: string; 
  porcentaje?: number; 
  montoUmbral?: number; 
  porcentajeBase?: number; 
  porcentajeExcedente?: number; 
}

export interface Propietario {
  id?: number;
  dni: string;
  nombre: string;
  apellido: string;
  email?: string;
  telefono?: string;
  activo?: boolean;
  esquemaComision: EsquemaComision; 
}