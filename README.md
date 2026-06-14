# Javier Prado Clinic Backend

Backend de la Clinica Javier Prado construido con Java 21 y Spring Boot. En su estado actual se enfoca en autenticacion con JWT, registro de usuarios, recuperacion de contrasena, consulta y edicion de perfil, carga de foto de perfil y health check.

## Estado actual

- Registro de pacientes con rol `PATIENT` por defecto
- Inicio de sesion con JWT
- Recuperacion y reseteo de contrasena por correo
- Consulta de perfil autenticado
- Actualizacion de perfil con soporte para foto en S3
- Health check para monitoreo basico
- Integracion parcial con el frontend actual

El modulo de citas todavia no esta implementado en la capa API, aunque el repositorio incluye definiciones SQL para tablas como `appointments` y `clinics` en `cjp_db.sql`.

## Stack

- Java 21
- Spring Boot 4.0.6
- Spring Security
- Spring Data JPA
- Spring Validation
- Java Mail
- JWT (`jjwt`)
- MySQL
- AWS S3 SDK
- Maven
- Docker

## Estructura del proyecto

```text
src/main/java/com/clinica_javierprado/cjp_backend/
  controller/
  domain/
  dto/
  exception/
  repository/
  security/
  service/
src/main/resources/
  application.yml
src/test/java/com/clinica_javierprado/cjp_backend/
```

## Modulos principales

### `controller`

Expone los endpoints REST actualmente disponibles:

- `AuthController`
- `UserController`
- `HealthController`

### `service`

Contiene la logica de negocio principal:

- `AuthService`
- `UserService`
- `EmailService`
- `FileStorageService`

### `security`

Configura autenticacion stateless con JWT, filtro de autenticacion y CORS.

### `domain`

Modelos persistidos principales:

- `User`
- `DoctorProfile`
- `PasswordResetToken`
- `Role`

## Endpoints implementados

### Autenticacion

```http
POST /api/auth/register
POST /api/auth/login
POST /api/auth/forgot-password
POST /api/auth/reset-password
```

### Usuario autenticado

```http
GET /api/users/me
PUT /api/users/profile
```

### Salud del servicio

```http
GET /api/health
```

## Detalle funcional

### Registro

- Endpoint: `POST /api/auth/register`
- Crea usuarios con rol `PATIENT`
- Valida email, DNI y telefono duplicados
- Devuelve un JWT en la respuesta

### Login

- Endpoint: `POST /api/auth/login`
- Autentica con email y contrasena
- Devuelve un JWT

### Recuperacion de contrasena

- `POST /api/auth/forgot-password` genera un token temporal y envia un correo
- `POST /api/auth/reset-password` aplica la nueva contrasena si el token sigue vigente

### Perfil de usuario

- `GET /api/users/me` devuelve el perfil autenticado
- `PUT /api/users/profile` actualiza perfil y admite `multipart/form-data`
- El `PUT` espera:

```text
data: EditProfileRequest
photo: MultipartFile opcional
```

No existe un endpoint separado `POST /api/users/profile-photo`.

## Flujo de autenticacion

1. El cliente llama `POST /api/auth/login` o `POST /api/auth/register`.
2. El backend responde con un JWT.
3. El cliente envia `Authorization: Bearer <token>` en endpoints protegidos.
4. `JwtAuthenticationFilter` valida el token antes de resolver la peticion.

## Variables de entorno

Variables usadas realmente por el codigo:

### Base de datos

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

### JWT

- `JWT_SECRET`
- `JWT_EXPIRATION_MS`

### Correo

- `MAIL_HOST`
- `MAIL_PORT`
- `MAIL_USERNAME`
- `MAIL_PASSWORD`

### AWS S3

- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `AWS_REGION`
- `AWS_S3_BUCKET`

Referencia: `.env.example`

## Configuracion actual relevante

- `application.yml` tiene valores por defecto para base de datos, JWT, mail y S3
- `spring.jpa.hibernate.ddl-auto` esta en `update`
- `spring.jpa.show-sql` esta en `true`
- CORS esta configurado de forma explicita en `SecurityConfig` para dominios Vercel concretos y `http://localhost:5173`

Para entornos reales conviene sobreescribir las variables y no depender de los fallbacks definidos en `application.yml`.

## Ejecucion local

### Requisitos

- Java 21
- Maven o Maven Wrapper
- MySQL accesible

### Preparacion

1. Copia `.env.example` como base para tus variables.
2. Configura una instancia MySQL compatible con `DB_URL`.
3. Ajusta credenciales JWT, correo y S3 segun tu entorno.

### Ejecutar con Maven Wrapper

```bash
./mvnw spring-boot:run
```

### Ejecutar con Maven instalado

```bash
mvn spring-boot:run
```

La aplicacion queda disponible por defecto en `http://localhost:8080`.

## Docker

### Construir imagen

```bash
docker build -t cjp-backend .
```

### Ejecutar con Docker Compose

```bash
docker compose up --build
```

Importante:

- El `docker-compose.yml` actual solo levanta el backend
- No aprovisiona una base de datos MySQL local
- Si no sobreescribes variables, el backend intentara usar los valores por defecto definidos en `application.yml`

## Integracion con el frontend actual

El frontend en `../JavierPradoClinic-frontend` hoy consume o espera principalmente estos endpoints:

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/users/me`

Estado de la integracion:

- Login y registro estan conectados
- El dashboard del frontend se apoya en `GET /api/users/me`
- El modulo visual de citas del frontend sigue mayormente mock y no tiene soporte backend aun
- La recuperacion de contrasena existe en backend, pero el frontend actual todavia no expone un flujo completo para usarla

## Limitaciones conocidas

- No hay endpoints implementados para citas, medicos o sedes
- No hay integracion OAuth2 implementada
- El link de reseteo de contrasena en `EmailService` apunta a `http://localhost:3000/reset-password`, que no coincide con el frontend actual en Vite
- CORS esta hardcodeado y no sale de variables de entorno
- La cobertura de tests es minima
- Existen valores sensibles por defecto en `application.yml` que deberian reemplazarse en entornos reales

## Archivos utiles

- `src/main/resources/application.yml`
- `.env.example`
- `docker-compose.yml`
- `Dockerfile`
- `cjp_db.sql`

## Siguientes pasos recomendados

- Implementar endpoints reales de citas
- Exponer catalogos de medicos, especialidades y sedes
- Parametrizar la URL base del frontend para emails de reset password
- Mover CORS a configuracion por entorno
- Agregar pruebas de autenticacion, perfil y errores
- Alinear el flujo de recuperacion de contrasena con el frontend
