# Sistema de Gestión de Alquileres Temporarios

Sistema para administrar propiedades en alquiler temporario, gestionando propietarios, inquilinos, reservas, cobros y liquidaciones mensuales.

Desarrollado como trabajo integrador de la materia **Programación Orientada a Objetos II**.

---

##  Descripción

El sistema permite a una inmobiliaria administrar propiedades de distintos propietarios que se alquilan por períodos cortos.

Cada propiedad posee condiciones particulares como:

- precio por noche
- depósito de garantía
- política de cancelación
- esquema de comisión del propietario

El sistema contempla reglas de negocio reales como disponibilidad, penalidades por cancelación y liquidaciones con deducciones.

---

##  Funcionalidades Principales

###  Gestión de Propiedades y Propietarios
- Alta y modificación de propiedades
- Asociación de propiedades a propietarios
- Configuración de esquemas de comisión (fijo o escalonado)

###  Gestión de Reservas
- Registro de reservas con validación de disponibilidad
- Control de solapamientos con excepciones
- Cálculo automático de tarifas y depósito

###  Cancelaciones y Penalidades
- Cancelación de reservas según política configurada
- Aplicación automática de cargos según anticipación

###  Registro de Cobros
- Registro de señas y pagos parciales
- Control de saldo pendiente al ingreso

###  Liquidaciones a Propietarios
- Liquidación mensual por período
- Descuento de comisiones y gastos
- Aplicación de penalidades

###  Reportes e Historial
- Historial de ocupación por propiedad
- Ingresos por propietario
- Rentabilidad por período

---

##  Documentación del Proyecto

Toda la documentación se encuentra en la carpeta `/docs`.

El proyecto se desarrolla en **2 iteraciones dentro de 4 semanas**.

| Archivo | Descripción |
|--------|------------|
| docs/erp.md | Especificación de requisitos del sistema |
| docs/roadmap.md | Plan de iteraciones y roadmap del proyecto |
| docs/dp-iteracion-X.md | Diseño, UML, backlog y tareas de la iteración |
| docs/retrospectiva-iteracion-X.md | Análisis y reflexión al finalizar cada iteración |

---

##  Estado del Proyecto

- Iteración 1 → Sistema funcional al 50%
- Iteración 2 → Sistema completo + refactorización

---

##  Requisitos del Sistema

Completar según la tecnología utilizada.

Ejemplo:

- Java 17
- Maven o Gradle
- Base de datos (SQLite / PostgreSQL / otra)
- IDE recomendado (IntelliJ / VS Code / otro)

---

##  Cómo ejecutar el proyecto

(Completar cuando el sistema sea ejecutable)

Ejemplo:

```bash
git clone <https://github.com/BauerLuciano/Integrador-de-POO-II-Bauer-Olivieri.git>
cd proyecto
comando-para-ejecutar (ver despues que ponemos)
