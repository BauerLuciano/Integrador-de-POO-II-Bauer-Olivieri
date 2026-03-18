export interface ReporteLiquidacion {
  ingresosEstadias: number;
  ingresosPenalidades: number;
  totalIngresosBrutos: number;
  comisionAgencia: number;
  gastosMantenimiento: number;
  totalALiquidar: number;
  detalleReservas: any[];
  detallePenalidades: any[];
  detalleGastos: any[];
}