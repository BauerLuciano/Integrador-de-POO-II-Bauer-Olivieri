\# Retrospectiva: Iteración 1



\*\*Fecha\*\*: 6 de Marzo de 2026  

\*\*Equipo\*\*: Bauer Luciano y Olivieri Ricardo



\## 🟢 ¿Qué salió bien?

\* \*\*Integración Modular\*\*: Se logró acoplar con éxito el módulo de Propietarios (Bauer) con el de Propiedades y Reservas (Olivieri) mediante una arquitectura limpia.

\* \*\*Cálculo de Comisiones\*\*: El uso del patrón Strategy permitió delegar correctamente la responsabilidad del cálculo a las clases de comisión, manteniendo el código del Propietario limpio.

\* \*\*Validación de Disponibilidad\*\*: La interfaz `BloqueoCalendario` facilitó la reutilización de la lógica de solapamiento de fechas tanto en reservas como en bloqueos de mantenimiento.



\## 🟡 ¿Qué dificultades encontramos?

\* \*\*Deserialización de JSON\*\*: Hubo complicaciones al recibir objetos de clases abstractas por API REST. Se resolvió aplicando anotaciones `@JsonTypeInfo` de Jackson.

\* \*\*Validación de DNI en Edición\*\*: La validación inicial de DNI duplicado fallaba al intentar actualizar un propietario existente. Se corrigió implementando una consulta personalizada en el repositorio (`existsByDniAndIdNot`).

\* \*\*Manejo de Nulos\*\*: Se detectaron errores al intentar operar con propiedades que carecían de propietario asignado en la base de datos (datos de prueba antiguos).



\## 🔵 Aprendizajes y Próximos Pasos

\* \*\*Uso de BigDecimal\*\*: Se confirmó que es la mejor práctica para el manejo de montos financieros para evitar errores de redondeo.

\* \*\*Transaccionalidad\*\*: Es fundamental marcar con `@Transactional` los servicios que realizan múltiples operaciones de lectura/escritura para asegurar la integridad de los datos.

\* \*\*Iteración 2\*\*: El equipo se enfocará en el registro de cobros, cancelaciones con penalidad y liquidaciones mensuales.

