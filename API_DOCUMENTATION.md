# 📚 Documentación de la API

Documentación completa de los endpoints REST del Sistema de Gestión para Clínica Veterinaria.

## 📋 Tabla de Contenidos

- [Autenticación](#-autenticación)
- [Usuarios](#-usuarios)
- [Clientes (Owners)](#-clientes-owners)
- [Mascotas (Pets)](#-mascotas-pets)
- [Veterinarios (Vets)](#-veterinarios-vets)
- [Especialidades](#-especialidades)
- [Servicios Veterinarios](#-servicios-veterinarios)
- [Citas (Appointments)](#-citas-appointments)
- [Historiales Médicos](#-historiales-médicos)
- [Clínica](#-clínica)
- [Códigos de Estado HTTP](#-códigos-de-estado-http)
- [Manejo de Errores](#-manejo-de-errores)

## 🔐 Autenticación

La API utiliza autenticación basada en JWT (JSON Web Tokens). Todos los endpoints (excepto los de autenticación) requieren un token JWT en el header `Authorization`.

### Formato del Header

```
Authorization: Bearer <token>
```

### Endpoints de Autenticación

#### POST /api/auth/register

Registra un nuevo usuario en el sistema.

**Permisos**: Público (no requiere autenticación)

**Request Body**:
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123",
  "role": "ADMIN"
}
```

**Response** (201 Created):
```
User registered successfully
```

**Errores**:
- `400 Bad Request`: Usuario o email ya existe, datos inválidos

---

#### POST /api/auth/login

Inicia sesión y obtiene un token JWT.

**Permisos**: Público (no requiere autenticación)

**Request Body**:
```json
{
  "username": "johndoe",
  "password": "password123"
}
```

**Response** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer"
}
```

**Errores**:
- `401 Unauthorized`: Credenciales inválidas

---

## 👥 Usuarios

### GET /api/users

Obtiene la lista de todos los usuarios registrados.

**Permisos**: `ADMIN`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "role": "ADMIN",
    "enabled": true,
    "createdAt": "2025-01-27T10:00:00",
    "updatedAt": "2025-01-27T10:00:00"
  }
]
```

---

### GET /api/users/{id}

Obtiene los detalles de un usuario específico.

**Permisos**: `ADMIN` o el propio usuario

**Response** (200 OK):
```json
{
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "role": "ADMIN",
  "enabled": true,
  "createdAt": "2025-01-27T10:00:00",
  "updatedAt": "2025-01-27T10:00:00"
}
```

---

### PUT /api/users/{id}

Actualiza los datos de un usuario.

**Permisos**: `ADMIN` o el propio usuario

**Request Body**:
```json
{
  "email": "newemail@example.com",
  "password": "newpassword123"
}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "username": "johndoe",
  "email": "newemail@example.com",
  "role": "ADMIN",
  "enabled": true
}
```

---

### DELETE /api/users/{id}

Elimina (desactiva) un usuario.

**Permisos**: `ADMIN`

**Response** (204 No Content)

---

## 👤 Clientes (Owners)

### GET /api/owners

Obtiene la lista de todos los propietarios registrados.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "firstName": "Juan",
    "lastName": "Pérez",
    "email": "juan@example.com",
    "phone": "+1234567890",
    "address": "Calle Principal 123",
    "createdAt": "2025-01-27T10:00:00",
    "updatedAt": "2025-01-27T10:00:00"
  }
]
```

---

### GET /api/owners/{id}

Obtiene los detalles de un propietario específico.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Response** (200 OK):
```json
{
  "id": 1,
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan@example.com",
  "phone": "+1234567890",
  "address": "Calle Principal 123",
  "createdAt": "2025-01-27T10:00:00",
  "updatedAt": "2025-01-27T10:00:00"
}
```

---

### POST /api/owners

Registra un nuevo propietario.

**Permisos**: `ADMIN`, `RECEPCIONISTA`

**Request Body**:
```json
{
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan@example.com",
  "phone": "+1234567890",
  "address": "Calle Principal 123"
}
```

**Response** (201 Created):
```json
{
  "id": 1,
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan@example.com",
  "phone": "+1234567890",
  "address": "Calle Principal 123",
  "createdAt": "2025-01-27T10:00:00",
  "updatedAt": "2025-01-27T10:00:00"
}
```

---

### PUT /api/owners/{id}

Actualiza los datos de un propietario.

**Permisos**: `ADMIN`, `RECEPCIONISTA`

**Request Body**:
```json
{
  "firstName": "Juan Carlos",
  "lastName": "Pérez",
  "email": "juancarlos@example.com",
  "phone": "+1234567890",
  "address": "Calle Principal 456"
}
```

**Response** (200 OK): Similar al POST

---

### DELETE /api/owners/{id}

Elimina un propietario.

**Permisos**: `ADMIN`

**Response** (204 No Content)

---

## 🐕 Mascotas (Pets)

### GET /api/pets

Obtiene la lista de todas las mascotas registradas.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "name": "Max",
    "species": "DOG",
    "breed": "Golden Retriever",
    "age": 3,
    "weight": 25.5,
    "ownerId": 1,
    "createdAt": "2025-01-27T10:00:00",
    "updatedAt": "2025-01-27T10:00:00"
  }
]
```

---

### GET /api/pets/{id}

Obtiene los detalles de una mascota específica.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Response** (200 OK):
```json
{
  "id": 1,
  "name": "Max",
  "species": "DOG",
  "breed": "Golden Retriever",
  "age": 3,
  "weight": 25.5,
  "ownerId": 1,
  "createdAt": "2025-01-27T10:00:00",
  "updatedAt": "2025-01-27T10:00:00"
}
```

---

### GET /api/owners/{ownerId}/pets

Obtiene todas las mascotas de un propietario específico.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Response** (200 OK): Array de mascotas

---

### POST /api/pets

Registra una nueva mascota.

**Permisos**: `ADMIN`, `RECEPCIONISTA`

**Request Body**:
```json
{
  "name": "Max",
  "species": "DOG",
  "breed": "Golden Retriever",
  "age": 3,
  "weight": 25.5,
  "ownerId": 1
}
```

**Response** (201 Created): Objeto de mascota

---

### PUT /api/pets/{id}

Actualiza los datos de una mascota.

**Permisos**: `ADMIN`, `RECEPCIONISTA`

**Request Body**:
```json
{
  "name": "Maximus",
  "species": "DOG",
  "breed": "Golden Retriever",
  "age": 4,
  "weight": 28.0,
  "ownerId": 1
}
```

**Response** (200 OK): Objeto de mascota actualizado

---

### DELETE /api/pets/{id}

Elimina una mascota.

**Permisos**: `ADMIN`

**Response** (204 No Content)

---

## 🩺 Veterinarios (Vets)

### GET /api/vets

Obtiene la lista de todos los veterinarios.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "firstName": "Dr. María",
    "lastName": "González",
    "email": "maria@vetclinic.com",
    "phone": "+1234567890",
    "licenseNumber": "VET-12345",
    "specialties": [
      {
        "id": 1,
        "name": "Cirugía"
      }
    ],
    "createdAt": "2025-01-27T10:00:00",
    "updatedAt": "2025-01-27T10:00:00"
  }
]
```

---

### GET /api/vets/{id}

Obtiene los detalles de un veterinario específico.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Response** (200 OK): Objeto de veterinario

---

### POST /api/vets

Registra un nuevo veterinario.

**Permisos**: `ADMIN`

**Request Body**:
```json
{
  "firstName": "Dr. María",
  "lastName": "González",
  "email": "maria@vetclinic.com",
  "phone": "+1234567890",
  "licenseNumber": "VET-12345",
  "specialtyIds": [1, 2]
}
```

**Response** (201 Created): Objeto de veterinario

---

### PUT /api/vets/{id}

Actualiza los datos de un veterinario.

**Permisos**: `ADMIN`

**Request Body**: Similar al POST

**Response** (200 OK): Objeto de veterinario actualizado

---

### DELETE /api/vets/{id}

Elimina un veterinario.

**Permisos**: `ADMIN`

**Response** (204 No Content)

---

## 🎓 Especialidades

### GET /api/specialties

Obtiene la lista de todas las especialidades.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "name": "Cirugía",
    "description": "Especialidad en cirugía veterinaria",
    "createdAt": "2025-01-27T10:00:00",
    "updatedAt": "2025-01-27T10:00:00"
  }
]
```

---

### GET /api/specialties/{id}

Obtiene los detalles de una especialidad específica.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Response** (200 OK): Objeto de especialidad

---

### POST /api/specialties

Crea una nueva especialidad.

**Permisos**: `ADMIN`

**Request Body**:
```json
{
  "name": "Cardiología",
  "description": "Especialidad en cardiología veterinaria"
}
```

**Response** (201 Created): Objeto de especialidad

---

### PUT /api/specialties/{id}

Actualiza una especialidad.

**Permisos**: `ADMIN`

**Response** (200 OK): Objeto de especialidad actualizado

---

### DELETE /api/specialties/{id}

Elimina una especialidad.

**Permisos**: `ADMIN`

**Response** (204 No Content)

---

## 💉 Servicios Veterinarios

### GET /api/services

Obtiene la lista de todos los servicios veterinarios.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "name": "Consulta General",
    "description": "Consulta veterinaria general",
    "type": "CONSULTATION",
    "price": 50.00,
    "duration": 30,
    "createdAt": "2025-01-27T10:00:00",
    "updatedAt": "2025-01-27T10:00:00"
  }
]
```

**Tipos de servicio disponibles**:
- `CONSULTATION` - Consulta
- `VACCINATION` - Vacunación
- `SURGERY` - Cirugía
- `EMERGENCY` - Emergencia
- `GROOMING` - Estética

---

### GET /api/services/{id}

Obtiene los detalles de un servicio específico.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Response** (200 OK): Objeto de servicio

---

### GET /api/services/type/{type}

Obtiene servicios filtrados por tipo.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Parámetros**:
- `type`: Tipo de servicio (CONSULTATION, VACCINATION, etc.)

**Response** (200 OK): Array de servicios del tipo especificado

---

### POST /api/services

Crea un nuevo servicio veterinario.

**Permisos**: `ADMIN`

**Request Body**:
```json
{
  "name": "Vacunación Anual",
  "description": "Vacunación anual completa",
  "type": "VACCINATION",
  "price": 75.00,
  "duration": 15
}
```

**Response** (201 Created): Objeto de servicio

---

### PUT /api/services/{id}

Actualiza un servicio veterinario.

**Permisos**: `ADMIN`

**Response** (200 OK): Objeto de servicio actualizado

---

### DELETE /api/services/{id}

Elimina un servicio veterinario.

**Permisos**: `ADMIN`

**Response** (204 No Content)

---

## 📅 Citas (Appointments)

### GET /api/appointments

Obtiene la lista de todas las citas.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "appointmentDate": "2025-02-01T10:00:00",
    "status": "PENDING",
    "notes": "Primera consulta",
    "petId": 1,
    "vetId": 1,
    "serviceId": 1,
    "createdAt": "2025-01-27T10:00:00",
    "updatedAt": "2025-01-27T10:00:00"
  }
]
```

**Estados disponibles**:
- `PENDING` - Pendiente
- `CONFIRMED` - Confirmada
- `CANCELLED` - Cancelada
- `COMPLETED` - Completada

---

### GET /api/appointments/{id}

Obtiene los detalles de una cita específica.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Response** (200 OK): Objeto de cita

---

### GET /api/appointments/pet/{petId}

Obtiene todas las citas de una mascota específica.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Response** (200 OK): Array de citas

---

### GET /api/appointments/vet/{vetId}

Obtiene todas las citas de un veterinario específico.

**Permisos**: `ADMIN`, `VET`

**Response** (200 OK): Array de citas

---

### POST /api/appointments

Crea una nueva cita.

**Permisos**: `ADMIN`, `RECEPCIONISTA`

**Request Body**:
```json
{
  "appointmentDate": "2025-02-01T10:00:00",
  "notes": "Primera consulta",
  "petId": 1,
  "vetId": 1,
  "serviceId": 1
}
```

**Response** (201 Created): Objeto de cita

---

### PUT /api/appointments/{id}

Actualiza una cita.

**Permisos**: `ADMIN`, `RECEPCIONISTA`

**Request Body**:
```json
{
  "appointmentDate": "2025-02-01T11:00:00",
  "notes": "Consulta actualizada",
  "petId": 1,
  "vetId": 1,
  "serviceId": 1
}
```

**Response** (200 OK): Objeto de cita actualizado

---

### PATCH /api/appointments/{id}/confirm

Confirma una cita pendiente.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Response** (200 OK): Objeto de cita con estado `CONFIRMED`

---

### PATCH /api/appointments/{id}/cancel

Cancela una cita.

**Permisos**: `ADMIN`, `RECEPCIONISTA`

**Response** (200 OK): Objeto de cita con estado `CANCELLED`

---

## 📋 Historiales Médicos

### GET /api/medical-records

Obtiene la lista de todos los historiales médicos.

**Permisos**: `ADMIN`, `VET`

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "petId": 1,
    "vetId": 1,
    "diagnosis": "Resfriado común",
    "treatment": "Antibióticos y reposo",
    "notes": "Mascota en buen estado general",
    "visitDate": "2025-01-27T10:00:00",
    "createdAt": "2025-01-27T10:00:00",
    "updatedAt": "2025-01-27T10:00:00"
  }
]
```

---

### GET /api/medical-records/{id}

Obtiene los detalles de un historial médico específico.

**Permisos**: `ADMIN`, `VET`

**Response** (200 OK): Objeto de historial médico

---

### GET /api/medical-records/pet/{petId}

Obtiene todos los historiales médicos de una mascota específica.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Response** (200 OK): Array de historiales médicos

---

### GET /api/medical-records/vet/{vetId}

Obtiene todos los historiales médicos creados por un veterinario específico.

**Permisos**: `ADMIN`, `VET`

**Response** (200 OK): Array de historiales médicos

---

### POST /api/medical-records

Crea un nuevo historial médico.

**Permisos**: `ADMIN`, `VET`

**Request Body**:
```json
{
  "petId": 1,
  "vetId": 1,
  "diagnosis": "Resfriado común",
  "treatment": "Antibióticos y reposo",
  "notes": "Mascota en buen estado general",
  "visitDate": "2025-01-27T10:00:00"
}
```

**Response** (201 Created): Objeto de historial médico

---

### PUT /api/medical-records/{id}

Actualiza un historial médico.

**Permisos**: `ADMIN`, `VET`

**Request Body**: Similar al POST

**Response** (200 OK): Objeto de historial médico actualizado

---

### DELETE /api/medical-records/{id}

Elimina un historial médico.

**Permisos**: `ADMIN`

**Response** (204 No Content)

---

## 🏥 Clínica

### GET /api/clinic

Obtiene la configuración de la clínica.

**Permisos**: `ADMIN`, `VET`, `RECEPCIONISTA`

**Response** (200 OK):
```json
{
  "id": 1,
  "name": "Veterinary Clinic",
  "address": "Calle Principal 123",
  "phone": "+1234567890",
  "email": "info@vetclinic.com",
  "openingHours": "09:00",
  "closingHours": "18:00"
}
```

---

### PUT /api/clinic

Actualiza la configuración de la clínica.

**Permisos**: `ADMIN`

**Request Body**:
```json
{
  "name": "Veterinary Clinic Updated",
  "address": "Calle Principal 456",
  "phone": "+1234567890",
  "email": "info@vetclinic.com",
  "openingHours": "08:00",
  "closingHours": "19:00"
}
```

**Response** (200 OK): Objeto de configuración actualizado

---

### GET /api/clinic/stats

Obtiene estadísticas de la clínica.

**Permisos**: `ADMIN`, `VET`

**Response** (200 OK):
```json
{
  "totalOwners": 150,
  "totalPets": 200,
  "totalAppointments": 500,
  "totalRevenue": 25000.00,
  "pendingAppointments": 15,
  "completedAppointments": 485
}
```

---

## 📊 Códigos de Estado HTTP

La API utiliza los siguientes códigos de estado HTTP:

- **200 OK**: Petición exitosa
- **201 Created**: Recurso creado exitosamente
- **204 No Content**: Operación exitosa sin contenido de respuesta
- **400 Bad Request**: Solicitud inválida (datos faltantes o incorrectos)
- **401 Unauthorized**: No autenticado o token inválido
- **403 Forbidden**: No tiene permisos para realizar la operación
- **404 Not Found**: Recurso no encontrado
- **500 Internal Server Error**: Error interno del servidor

## ⚠️ Manejo de Errores

### Formato de Error

Cuando ocurre un error, la API devuelve un objeto con la siguiente estructura:

```json
{
  "timestamp": "2025-01-27T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/owners",
  "details": [
    {
      "field": "email",
      "message": "Email is required"
    }
  ]
}
```

### Errores Comunes

#### 401 Unauthorized
```json
{
  "timestamp": "2025-01-27T10:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid or expired token",
  "path": "/api/owners"
}
```

#### 403 Forbidden
```json
{
  "timestamp": "2025-01-27T10:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied. Required role: ADMIN",
  "path": "/api/users"
}
```

#### 404 Not Found
```json
{
  "timestamp": "2025-01-27T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Owner with id 999 not found",
  "path": "/api/owners/999"
}
```

#### 400 Bad Request (Validación)
```json
{
  "timestamp": "2025-01-27T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/owners",
  "details": [
    {
      "field": "email",
      "message": "must be a well-formed email address"
    },
    {
      "field": "firstName",
      "message": "must not be blank"
    }
  ]
}
```

## 🔗 Recursos Adicionales

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- [README.md](./README.md) - Documentación general del proyecto
- [ARCHITECTURE.md](./ARCHITECTURE.md) - Arquitectura del sistema

---

**Última actualización**: 2025-01-27

