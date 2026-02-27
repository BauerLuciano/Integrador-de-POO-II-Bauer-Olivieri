# Especificación de Requisitos de Software (ERP)
## Sistema de Gestión de Alquileres Temporarios

### 1. Descripción del Proyecto
El proyecto consiste en desarrollar un sistema para administrar propiedades en alquiler temporario, gestionando propietarios, inquilinos, reservas y liquidaciones. El sistema será utilizado por una inmobiliaria que administra propiedades de distintos propietarios, las cuales se alquilan por períodos cortos.

---

### 2. Módulos y Requerimientos Funcionales

#### 2.1. Gestión de Propiedades y Propietarios
* El sistema debe permitir el alta, baja y modificación de propiedades.
* Cada propiedad tiene condiciones particulares (precio por noche, depósito, política de cancelación).
* El sistema debe permitir gestionar a los propietarios y asociar cada propiedad a su respectivo dueño.
* Los propietarios tienen distintos esquemas de comisión pactados con la inmobiliaria (ej. porcentaje fijo, escalonado por monto, etc.).

#### 2.2. Gestión de Reservas y Disponibilidad
* El sistema debe permitir el registro de reserva.
* Al registrar una reserva, se debe validar la disponibilidad, aplicar la tarifa correspondiente y calcular el depósito requerido.
* Las reservas no pueden solaparse, salvo por excepciones registradas como mantenimiento o uso personal del propietario.

#### 2.3. Registro de Cobros e Imputación
* El sistema debe permitir el registro de pagos de reservas con soporte para señas, pagos parciales y saldo al ingreso.

#### 2.4. Cancelaciones y Penalidades
* El sistema debe permitir la anulación de reservas aplicando la política correspondiente según la anticipación con que se cancela.
* Las políticas de cancelación deben soportar diferentes escenarios: sin cargo, cargo parcial o cargo total.

#### 2.5. Liquidaciones Mensuales a Propietarios
* Se deben generar liquidaciones a propietarios por período (mensuales).
* Estas liquidaciones deben calcularse descontando comisiones, gastos de mantenimiento y penalidades aplicables.

#### 2.6. Historial y Reportes
* El sistema debe permitir consultar el historial de ocupación por propiedad.
* Se debe poder consultar los ingresos por propietario y la rentabilidad por período.
