# Retrospectiva: Iteración 2 (Release v1.0)

**Fecha:** 21 de Marzo de 2026
**Equipo:** Bauer Luciano y Olivieri Ricardo

## 🟢 ¿Qué salió bien?

* **Liquidaciones completadas:** Le encontramos la vuelta a la lógica de liquidación mensual (HU-12). Logramos que el sistema cruce bien los ingresos por reservas terminadas, descuente la comisión correspondiente y reste los gastos de mantenimiento.
* **Reutilización de código:** Haber implementado el patrón *Strategy* en la Iteración 1 nos salvó bastante tiempo ahora. Lo aplicamos para las Políticas de Cancelación (HU-10) y nos evitó hacer un montón de `if/else` anidados.
* **Estados de Reserva:** Modificar la reserva para que tenga estados (`ACTIVA`, `CANCELADA`, `FINALIZADA`) funcionó perfecto. Ahora, al cancelar, las fechas se liberan automáticamente en el calendario sin que perdamos el registro de la penalidad cobrada.
* **El refactoring ayudó (HU-14):** Pasar todos los montos a `BigDecimal` nos sacó de encima los dolores de cabeza con los decimales que veníamos arrastrando.

## 🟡 ¿Qué dificultades encontramos?

* **Consultas complejas en Spring Data (JPQL):** Armar las consultas para traer todos los datos del mes en las liquidaciones fue bastante más difícil de lo que pensábamos. Tuvimos que investigar a fondo cómo hacer bien los cruces de tablas para no traer información de más.
* **Manejo de fechas en cancelaciones:** Calcular exactamente con cuántos días de anticipación estaba cancelando un inquilino nos trajo varios bugs iniciales por los casos límite con la clase `LocalDate`.
* **Validaciones de cobros:** Controlar los pagos nos llevó más tiempo del esperado (HU-08). Tuvimos que agregar varias validaciones extra para asegurarnos de que la suma de señas y pagos parciales nunca supere el monto total de la reserva.

## 🔵 Aprendizajes y Conclusión del Proyecto

* **El valor de planificar antes de codear:** Nos dimos cuenta de que tener el diagrama de clases bien armado desde el principio nos hizo mucho más fácil agregar las entidades de Pagos y Gastos en esta segunda etapa.
* **Ojo con la Base de Datos:** Aprendimos (a prueba y error) a tener cuidado con el problema de "N+1" en Hibernate al momento de armar los reportes de historial y rentabilidad.
* **Cierre del proyecto:** Logramos completar el 100% de las funcionalidades requeridas para el Release v1.0 y refactorizar lo que había quedado pendiente de la primera entrega. El sistema cumple con todo lo planteado en el ERP.
