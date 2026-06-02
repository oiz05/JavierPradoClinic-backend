# Backend - Sistema de Gestión de Citas Médicas CJP

## 📋 Descripción

Sistema backend para la gestión de citas médicas de la Clínica Javier Prado (CJP), desarrollado con **Java 21** y **Spring Boot**.

La plataforma permite a pacientes agendar citas médicas, gestionar sus consultas, recuperar contraseñas, administrar su perfil y autenticarse mediante JWT y OAuth2.

---

## 🚀 Tecnologías Utilizadas

### Backend

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Hibernate
- JWT Authentication
- OAuth2 Login
- Maven

### Base de Datos

- MySQL 8
- AWS RDS

### Infraestructura

- Docker
- AWS ECS
- AWS ECR

### Almacenamiento

- Amazon S3

### Servicios Externos

- Resend (correo electrónico)
- Amazon SES (implementación futura)

---

## 🏗️ Arquitectura del Proyecto

```text
cjp_backend/
├── CjpBackendApplication.java
├── controller
│   ├── AuthController.java
│   ├── HealthController.java
│   └── UserController.java
├── domain
│   ├── DoctorProfile.java
│   ├── PasswordResetToken.java
│   ├── Role.java
│   └── User.java
├── dto
│   ├── AuthResponse.java
│   ├── EditProfileRequest.java
│   ├── ForgotPasswordRequest.java
│   ├── LoginRequest.java
│   ├── MessageResponse.java
│   ├── RegisterRequest.java
│   ├── ResetPasswordRequest.java
│   └── UserProfileResponse.java
├── exception
│   └── GlobalExceptionHandler.java
├── repository
│   ├── DoctorProfileRepository.java
│   ├── PasswordResetTokenRepository.java
│   └── UserRepository.java
├── security
│   ├── JwtAuthenticationFilter.java
│   ├── JwtService.java
│   ├── oauth2
│   ├── SecurityConfig.java
│   └── UserDetailsServiceImpl.java
└── service
    ├── AuthService.java
    ├── EmailService.java
    ├── FileStorageService.java
    └── UserService.java
```

---

## 📂 Descripción de Carpetas

### controller

Contiene los controladores REST encargados de exponer los endpoints consumidos por el frontend.

### domain

Entidades del dominio que representan las tablas y modelos de negocio del sistema.

### dto

Objetos de transferencia de datos utilizados para la comunicación entre cliente y servidor.

### repository

Interfaces encargadas del acceso a datos mediante Spring Data JPA.

### security

Configuración de autenticación y autorización basada en JWT y OAuth2.

### service

Contiene la lógica de negocio de la aplicación.

### exception

Manejo global de excepciones y respuestas de error.

---

## 🔐 Seguridad

El sistema utiliza autenticación basada en JWT.

### Flujo de autenticación

1. El usuario envía sus credenciales.
2. El backend valida la información.
3. Se genera un JWT.
4. El frontend almacena el token.
5. Las solicitudes protegidas incluyen el encabezado:

```http
Authorization: Bearer <token>
```

---

## 👥 Roles del Sistema

### PATIENT

- Registro de cuenta
- Inicio de sesión
- Gestión de perfil
- Recuperación de contraseña

### DOCTOR

- Acceso a funcionalidades médicas
- Gestión de información profesional

### ADMINISTRATOR

- Administración general del sistema

---

## 📡 API REST

### Autenticación

#### Registro

```http
POST /api/auth/register
```

#### Inicio de sesión

```http
POST /api/auth/login
```

#### Recuperar contraseña

```http
POST /api/auth/forgot-password
```

#### Restablecer contraseña

```http
POST /api/auth/reset-password
```

---

### Usuario

#### Obtener perfil

```http
GET /api/users/me
```

#### Actualizar perfil

```http
PUT /api/users/profile
```

#### Subir foto de perfil

```http
POST /api/users/profile-photo
```

---

### Salud de la Aplicación

#### Health Check

```http
GET /api/health
```

---

## 🗄️ Entidades Principales

### User

Representa a los usuarios registrados en el sistema.

Campos principales:

- id
- dni
- firstName
- lastName
- email
- password
- phoneNumber
- profilePhoto
- role

### DoctorProfile

Información profesional asociada a un usuario médico.

Campos principales:

- cmp
- medicalSpecialty
- user

### PasswordResetToken

Gestiona los tokens temporales para recuperación de contraseñas.

Campos principales:

- token
- expirationDate
- user

---

## ⚙️ Variables de Entorno

```properties
# Database
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

# JWT
jwt.secret=
jwt.expiration=

# AWS S3
aws.access-key=
aws.secret-key=
aws.region=
aws.s3.bucket=

# Email
resend.api-key=
```

---

## 🐳 Docker

### Construir imagen

```bash
docker build -t cjp-backend .
```

### Ejecutar contenedor

```bash
docker run -p 8080:8080 cjp-backend
```

---

## ☁️ Despliegue

El backend está preparado para desplegarse en:

- AWS ECS Fargate
- AWS ECR
- AWS RDS
- Amazon S3

---

## 🧪 Ejecución Local

### Clonar repositorio

```bash
git clone https://github.com/tu-organizacion/cjp-backend.git
```

### Entrar al proyecto

```bash
cd cjp-backend
```

### Ejecutar aplicación

```bash
./mvnw spring-boot:run
```

o

```bash
mvn spring-boot:run
```

---

## 📈 Funcionalidades Implementadas

- ✅ Registro de usuarios
- ✅ Inicio de sesión con JWT
- ✅ Inicio de sesión con OAuth2
- ✅ Recuperación de contraseña
- ✅ Gestión de perfil
- ✅ Carga de imágenes a Amazon S3
- ✅ Health Check para despliegue en ECS
- ✅ Manejo global de excepciones
- ✅ Autorización basada en roles

---

## 🚧 Funcionalidades Futuras

- Gestión completa de citas médicas
- Gestión de clínicas y sedes
- Historial médico
- Notificaciones por correo
- Notificaciones SMS
- Panel administrativo avanzado
- Integración con Amazon SES

---

## 👨‍💻 Equipo de Desarrollo

Proyecto desarrollado para la Clínica Javier Prado como parte del curso de Proyecto Integrador II de la carrera de Ingeniería de Sistemas e Informática.
