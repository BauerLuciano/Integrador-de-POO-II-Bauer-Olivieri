# Roadmap del Proyecto: Sistema de Gestión de Alquileres Temporarios

Este documento presenta el plan de ruta para el desarrollo del sistema, dividido en dos iteraciones de 2 semanas cada una, totalizando 4 semanas de desarrollo.

## Iteración 1: Core de Gestión y Reservas Básicas (Release v0.1)
[cite_start]**Objetivo:** Lograr el 50% del proyecto funcional. Se prioriza el modelado del dominio base, la gestión de entidades principales y el motor de disponibilidad.

### Historias de Usuario (HU):

* [cite_start]**HU01 - Gestión de Propietarios:** Como administrador, quiero registrar y modificar los datos de los propietarios del sistema[cite: 3, 9].
* [cite_start]**HU02 - Esquemas de Comisión:** Como administrador, quiero definir y asignar distintos esquemas de comisión a los propietarios (porcentaje fijo, escalonado por monto, etc.)[cite: 9].
* [cite_start]**HU03 - Gestión de Propiedades:** Como administrador, quiero dar de alta y modificar propiedades, definiendo sus condiciones particulares (precio por noche, depósito, política de cancelación)[cite: 6, 9].
* [cite_start]**HU04 - Asociación Propiedad-Propietario:** Como administrador, quiero vincular cada propiedad con su respectivo propietario para establecer a quién pertenece y qué comisión aplica[cite: 6, 9].
* [cite_start]**HU05 - Excepciones de Calendario:** Como administrador, quiero registrar bloqueos en el calendario de una propiedad por excepciones como mantenimiento o uso personal del propietario[cite: 7].
* [cite_start]**HU06 - Validación de Disponibilidad:** Como administrador, quiero que el sistema valide que las fechas de una nueva reserva no se solapen con otras reservas o excepciones existentes[cite: 7, 10].
* [cite_start]**HU07 - Registro de Reservas:** Como administrador, quiero registrar una reserva de alquiler temporario, aplicando las tarifas correspondientes y calculando el depósito necesario[cite: 3, 10].

---

## Iteración 2: Finanzas, Reportes y Refactoring (Release v1.0)
[cite_start]**Objetivo:** Completar el 100% de los requerimientos funcionales, integrar el flujo de caja, calcular penalidades y aplicar refactoring sobre el código de la Iteración 1.

### Historias de Usuario (HU):

* [cite_start]**HU08 - Registro de Cobros:** Como administrador, quiero registrar pagos asociados a las reservas, incluyendo soporte para señas, pagos parciales y saldos al ingreso[cite: 13].
* [cite_start]**HU09 - Cancelación de Reservas:** Como administrador, quiero poder procesar la anulación de una reserva en el sistema[cite: 11].
* [cite_start]**HU10 - Aplicación de Penalidades:** Como administrador, quiero que el sistema aplique la política de cancelación correspondiente según la anticipación (sin cargo, cargo parcial o total) al anular una reserva[cite: 11].
* [cite_start]**HU11 - Registro de Gastos:** Como administrador, quiero poder registrar gastos de mantenimiento asociados a una propiedad para luego deducirlos[cite: 7, 12].
* [cite_start]**HU12 - Liquidación a Propietarios:** Como administrador, quiero generar liquidaciones mensuales por propietario, descontando comisiones, gastos de mantenimiento y sumando las penalidades a favor[cite: 12].
* [cite_start]**HU13 - Historial y Reportes:** Como administrador, quiero consultar el historial de ocupación por propiedad, los ingresos por propietario y la rentabilidad por período[cite: 14].
* [cite_start]**HU14 - Refactoring Técnico:** Como equipo de desarrollo, queremos revisar, limpiar y refactorizar el código generado en la iteración 1 para mejorar la calidad del diseño y asegurar la mantenibilidad.