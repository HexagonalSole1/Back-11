# API de Autenticación - Spring Security 6

## Descripción
API REST implementada con Spring Security 6 y JWT siguiendo arquitectura hexagonal. Incluye autenticación, autorización basada en roles y endpoints protegidos.

## Endpoints Disponibles

### Autenticación (Públicos)

#### POST `/api/auth/register`
Registra un nuevo usuario en el sistema. **El primer usuario se convierte automáticamente en ADMIN**.

**Request Body:**
```json
{
  "username": "usuario123",
  "password": "password123",
  "email": "usuario@ejemplo.com",
  "name": "Juan",
  "surname": "Pérez",
  "lastName": "García",
  "secondLastName": "López",
  "phone": "1234567890",
  "role": "USER"
}
```

**Comportamiento:**
- 🎯 **Primer usuario**: Se convierte automáticamente en ADMIN
- 👤 **Usuarios posteriores**: Solo pueden registrarse como USER (ignora el campo role)

#### POST `/api/auth/bootstrap-admin`
Crea el administrador inicial del sistema. **Solo funciona si no hay usuarios registrados**.

**Request Body:**
```json
{
  "username": "admin",
  "password": "admin123",
  "email": "admin@ejemplo.com",
  "name": "Admin",
  "surname": "Inicial"
}
```

**Roles Disponibles:**
- `USER` - Usuario estándar
- `ADMIN` - Administrador
- `MODERATOR` - Moderador

**Response:**
```json
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "id": 1,
    "username": "usuario123",
    "email": "usuario@ejemplo.com",
    "roles": ["USER"]
  }
}
```

#### POST `/api/auth/login`
Autentica un usuario existente.

**Request Body:**
```json
{
  "username": "usuario123",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "id": 1,
    "username": "usuario123",
    "email": "usuario@ejemplo.com",
    "roles": ["USER"]
  }
}
```

### Endpoints de Prueba

#### GET `/api/test/public`
Endpoint público que no requiere autenticación.

**Response:**
```json
{
  "success": true,
  "message": "Este es un endpoint público",
  "data": "Acceso público permitido"
}
```

#### GET `/api/test/protected`
Endpoint protegido que requiere autenticación.

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Este es un endpoint protegido",
  "data": "Hola usuario123, has accedido a un endpoint protegido"
}
```

#### GET `/api/test/admin`
Endpoint de administrador que requiere rol ADMIN.

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Este es un endpoint de administrador",
  "data": "Hola usuario123, tienes permisos de administrador"
}
```

#### POST `/api/auth/admin/create-user`
Endpoint para que administradores creen usuarios con cualquier rol.

**Headers:**
```
Authorization: Bearer <admin_token>
```

**Request Body:**
```json
{
  "username": "nuevousuario",
  "password": "password123",
  "email": "nuevo@ejemplo.com",
  "name": "Nuevo",
  "surname": "Usuario",
  "role": "ADMIN"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "id": 2,
    "username": "nuevousuario",
    "email": "nuevo@ejemplo.com",
    "roles": ["ADMIN"]
  }
}
```

## Configuración

### Variables de Entorno
- `jwt.secret`: Clave secreta para firmar JWT (por defecto: 404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970)
- `jwt.expiration`: Tiempo de expiración del token en milisegundos (por defecto: 86400000 = 24 horas)

### Base de Datos
El sistema crea automáticamente las siguientes tablas:
- `users`: Información de usuarios
- `roles`: Roles del sistema (ADMIN, USER, MODERATOR)
- `user_roles`: Relación muchos a muchos entre usuarios y roles

## Arquitectura

### Capas de la Arquitectura Hexagonal

1. **Domain Layer** (`auth/domain/`)
   - `entities/`: User, Role
   - `repositories/`: IUserRepository

2. **Application Layer** (`auth/application/`)
   - `services/`: IAuthService, IUserService, IJwtService
   - `mappers/`: UserMapper

3. **Infrastructure Layer** (`auth/infrastructure/`)
   - `controllers/`: UserController, TestController
   - `config/`: SecurityConfig, DataInitializer
   - `security/`: JwtAuthenticationFilter, JwtAuthenticationEntryPoint
   - `dtos/`: Request/Response DTOs

## Seguridad

- **Autenticación**: JWT (JSON Web Tokens)
- **Autorización**: Basada en roles (ADMIN, USER, MODERATOR)
- **CORS**: Configurado para permitir todas las orígenes
- **Validación**: Bean Validation en DTOs
- **Encriptación**: BCrypt para contraseñas

## Uso

1. **Registro**: Usa `/api/auth/register` para crear un nuevo usuario
2. **Login**: Usa `/api/auth/login` para obtener un token JWT
3. **Acceso Protegido**: Incluye el token en el header `Authorization: Bearer <token>`
4. **Roles**: Los usuarios se crean con rol USER por defecto

## Ejemplo de Uso con cURL

```bash
# Registrar usuario
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@ejemplo.com",
    "name": "Test",
    "surname": "User"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'

# Acceder a endpoint protegido
curl -X GET http://localhost:8080/api/test/protected \
  -H "Authorization: Bearer <token_obtenido_del_login>"
```
