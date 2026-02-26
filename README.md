# Sistema de Gestión de Alquileres Temporarios

Este proyecto consiste en desarrollar un sistema para administrar propiedades en alquiler temporario, gestionando propietarios, inquilinos, reservas y liquidaciones[cite: 3]. [cite_start]Permite a una inmobiliaria administrar propiedades de distintos propietarios que se alquilan por períodos cortos[cite: 5]. [cite_start]El sistema está diseñado para manejar condiciones particulares por propiedad (precio por noche, depósito, política de cancelación) y distintos esquemas de comisión pactados con la inmobiliaria[cite: 6]. 

Desarrollado como trabajo integrador para la materia Programación Orientada a Objetos II.

## Características Principales

* [cite_start]**Gestión de Propiedades y Propietarios:** Alta y modificación de propiedades con sus condiciones, y asociación al propietario con su esquema de comisión (porcentaje fijo, escalonado por monto, etc.)[cite: 9].
* [cite_start]**Gestión de Reservas:** Registro de reservas con validación de disponibilidad, aplicación de tarifas y cálculo de depósito[cite: 10]. [cite_start]Las reservas pueden solaparse con excepciones (mantenimiento, uso personal del propietario)[cite: 7].
* [cite_start]**Cancelaciones y Penalidades:** Anulación de reservas aplicando la política correspondiente según la anticipación con que se cancela (sin cargo, cargo parcial o total)[cite: 11].
* [cite_start]**Registro de Cobros e Imputación:** Registro de pagos de reservas con soporte para señas, pagos parciales y saldo al ingreso[cite: 13].
* [cite_start]**Liquidaciones Mensuales:** Generación de liquidaciones mensuales descontando comisiones, gastos de mantenimiento y penalidades aplicables[cite: 12].
* [cite_start]**Reportes e Historial:** Consulta del historial de ocupación por propiedad, ingresos por propietario y rentabilidad por período[cite: 14].

## Estructura de Documentación (`/docs`)

El ciclo de vida del proyecto se divide en 2 iteraciones a lo largo de 4 semanas, y toda la planificación se encuentra en la carpeta `docs/`.

* [cite_start]`docs/erp.md`: Un documento de especificación de requisitos de software para el proyecto[cite: 15].
* [cite_start]`docs/roadmap.md`: Un plan tentativo de las historias de usuario que se entregarán en cada iteración[cite: 16].
* [cite_start]`docs/dp-iteracion-X.md`: Documento de diseño y planificación para la iteración X[cite: 17]. [cite_start]Incluye el trabajo en equipo, diagrama de clases UML, wireframes, casos de uso, backlog y tareas[cite: 17, 18, 19].
* [cite_start]`docs/retrospectiva-iteracion-X.md`: Documento de retrospectiva que debe completarse tan pronto como se complete la iteración para reflexionar sobre lo cumplido y los retos[cite: 20, 23].

##  Instrucciones de Ejecución

## ARREGLAR ESTOO
### Requisitos previos
* [Lenguaje de programación y versión, ej: Java 17 / Python 3.10 / C# .NET 8]
* [Herramienta de construcción o base de datos si aplica, ej: Maven, Gradle, SQLite]

### Instalación y uso
1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/tu-usuario/nombre-del-repo.git](https://github.com/tu-usuario/nombre-del-repo.git)
   cd nombre-del-repo
