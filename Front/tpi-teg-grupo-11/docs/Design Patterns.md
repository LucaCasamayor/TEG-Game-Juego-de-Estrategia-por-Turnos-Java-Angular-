# 🛠️ Patrones de Diseño Aplicados en el TPI-TEG

Este documento detalla los patrones de diseño seleccionados para la implementación del juego TEG.

---

## 1. Singleton

- **Descripción**: Asegura que una clase tenga una única instancia y proporciona un punto de acceso global a ella.
- **Dónde se aplica**:
  - Controla el estado global del juego.
  - `ConexionBD` (Java): gestiona la conexión a la base de datos.
- **Problema que resuelve**:
  - Evita la creación múltiple de objetos críticos (por ejemplo, el controlador principal del juego o la conexión a base de datos).
  - Facilita el acceso centralizado y coherente a estos recursos.

---

## 2. Factory Method

- **Descripción**: Permite crear objetos sin especificar su clase concreta, delegando la creación en subclases o métodos especializados.
- **Dónde se aplica**:
  - Creación de misiones, territorios, jugadores y ejércitos.
- **Problema que resuelve**:
  - Centraliza la lógica de creación de entidades con distintas variantes.
  - Permite extender fácilmente nuevos tipos sin modificar código existente.

---

## 3. Strategy

- **Descripción**: Permite definir una familia de algoritmos y hacerlos intercambiables en tiempo de ejecución.
- **Dónde se aplica**:
  - Estrategias de los bots: agresiva, defensiva, equilibrada.
  - Distribución de ejércitos.
- **Problema que resuelve**:
  - Separa el comportamiento del jugador de su implementación.
  - Facilita cambiar estrategias dinámicamente sin alterar la lógica principal.

---

## 4. Observer

- **Descripción**: Define una relación de dependencia uno-a-muchos entre objetos para que cuando uno cambie de estado, se notifique automáticamente a los demás.
- **Dónde se aplica**:
  - Actualización de la interfaz en el frontend
  - Sincronización entre componentes del juego en el backend.
- **Problema que resuelve**:
  - Automatiza la actualización de la UI cuando cambian datos del juego.
  - Mejora la reactividad de la app.

---

## 5. State

- **Descripción**: Permite que un objeto altere su comportamiento cuando su estado interno cambia.
- **Dónde se aplica**:
  - Gestión de fases del turno: colocación, ataque, fortificación.
  - Control de flujo del juego: inicio, en juego, finalizado.
- **Problema que resuelve**:
  - Elimina condicionales extensos al modelar comportamientos por estado.
  - Organiza mejor las transiciones entre fases del juego.
