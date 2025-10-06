# 🪖 TEG Game – Juego de Estrategia por Turnos (Java + Angular)

Proyecto final integrador grupal (**TPI**) de la materia **Programación III – UTN FRC**, desarrollado durante el segundo año de la carrera **Tecnicatura Universitaria en Programación**.  
Evaluación final: **8/10** ✅

---

## 🎯 Descripción general

Este proyecto reproduce la lógica del clásico juego de estrategia **T.E.G. (Plan Táctico y Estratégico de la Guerra)**, desarrollado como una aplicación **Full Stack** con **Java Spring Boot (backend)** y **Angular (frontend)**.

Incluye un sistema de **turnos**, **fases de juego**, **jugador humano y bots**, **objetivos secretos y comunes**.

---

## 👥 Trabajo en equipo

El desarrollo fue realizado en grupo, aplicando metodologías colaborativas con **Git y GitHub**.  
Participé activamente en el **backend (Spring Boot)**, la **arquitectura del sistema**, la **integración con Angular** y la organización del código siguiendo buenas prácticas de versionado.

---

## 🛠️ Tecnologías utilizadas

### 🧩 Backend
- **Java 17**
- **Spring Boot**
- **H2 Database (en memoria)**
- **JUnit** para testing
- **Maven** como gestor de dependencias

### 💻 Frontend
- **Angular**
- **TypeScript**
- **HTML5 / CSS3**
- **Bootstrap**
- **AOS (Animate on Scroll)**

### ⚙️ Control de versiones
- **Git / GitHub**
---

## 🗄️ Base de datos H2

El backend utiliza una **base de datos H2 en memoria**, lo que permite ejecutar el sistema sin instalar un motor externo.

**URL de la consola:**  
[`http://localhost:8080/h2-console`](http://localhost:8080/h2-console)

**Credenciales predeterminadas:**
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Usuario:** `sa`
- **Contraseña:** *(vacía)*

📌 Los datos se eliminan al reiniciar la aplicación.  
Podés migrar fácilmente a **MySQL** si necesitás persistencia permanente.

---

## 🚀 Ejecución del proyecto

### 🧱 1. Backend (Java Spring Boot)
```bash
cd Back/tpi-teg-grupo-11
mvn clean install
mvn spring-boot:run
```
El servidor se iniciará en: http://localhost:8080

### 💻 2. Frontend (Angular)

En una nueva terminal:
```bash
cd Front/tpi-teg-grupo-11
npm install
npm start
```
La aplicación estará disponible en: http://localhost:4200

### 🧩 Funcionalidades principales
- **Sistema de turnos y fases de juego (refuerzo, ataque, reagrupamiento)**
- **Jugador humano y bots con dificultades diferentes**
- **Objetivos secretos y objetivo común (conquistar 30 países)**

### 🧠 Conceptos aplicados
- **Arquitectura en capas (Controller / Service / Repository / DTO / Mapper)**
- **Comunicación REST API entre frontend y backend**
- **Manejo de entidades y relaciones en JPA**
- **Pruebas unitarias con JUnit**
- **Uso de H2 en memoria para entornos de desarrollo y testing**
- **Buenas prácticas de control de versiones en equipo**
