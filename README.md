# ms-transferencias — Microservicio de Transferencias

Microservicio correspondiente al **caso NovaBank** (Banca digital / fintech) de la **Evaluación Parcial N°2**.

| | |
|---|---|
| **Asignatura** | JVY0101 — Java: Diseño y Construcción de Soluciones Nativas en Nube |
| **Stack** | Spring Boot 3.5.16 · Java 17 · Maven Wrapper · Spring Data JPA · H2 Database |
| **Arquitectura** | Capas (Controller, Service, Repository, Model) |
| **Puerto** | 8083 |

## Responsabilidad (SRP)

Registra y administra las transferencias de dinero entre cuentas de NovaBank, almacenando cuenta origen, cuenta destino, monto, fecha, estado (COMPLETADA por defecto) y glosa. Permite consultar transferencias por cuenta origen o destino y actualizar su estado. Su base de datos es una **H2 en memoria** (`jdbc:h2:mem:novabank_transfers_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`), cumpliendo aislamiento de datos por dominio.

## Consola y Persistencia

Al ejecutar el servicio en el puerto `8083`, se puede acceder a la consola de persistencia H2:

- **H2 Console**: `http://localhost:8083/h2-console`
- **JDBC URL**: `jdbc:h2:mem:novabank_transfers_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
- **Usuario**: `sa`
- **Contraseña**: *(en blanco)*

## Endpoints Principales (`/api/transferencias`)

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/transferencias` | Registra una transferencia entre cuentas; si no se envían, asigna fecha actual y estado COMPLETADA; responde 201. |
| GET | `/api/transferencias` | Lista todas las transferencias registradas. |
| GET | `/api/transferencias/{id}` | Obtiene una transferencia por id; responde 404 si no existe. |
| GET | `/api/transferencias/origen/{cuentaOrigen}` | Lista las transferencias emitidas desde una cuenta origen. |
| GET | `/api/transferencias/destino/{cuentaDestino}` | Lista las transferencias recibidas en una cuenta destino. |
| PUT | `/api/transferencias/{id}/estado` | Actualiza el estado de una transferencia; responde 404 si no existe. |

## Instrucciones de Clonación, Compilación y Ejecución Local

1. **Clonar el repositorio:**
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd ms-transferencias
   ```

2. **Compilar y empaquetar con Maven Wrapper:**
   ```bash
   .\mvnw.cmd clean package -DskipTests
   ```

3. **Ejecutar el ejecutable .jar generado:**
   ```bash
   java -jar target/ms-transferencias-1.0.0.jar
   ```

## Cómo ejecutar las pruebas

```bash
.\mvnw.cmd test
```

## Ejecución EP3

Desde la carpeta de este servicio, ejecuta `docker compose up --build --wait`. El puerto local es `8083` y la salud se consulta en `http://localhost:8083/actuator/health`. Para compilar y ejecutar las pruebas con JDK 17 y Maven, usa `mvn -B -ntp clean verify`.

La imagen se construye en dos etapas y ejecuta la aplicación con UID 10001. El perfil local usa H2 y el perfil `cloud` usa PostgreSQL con credenciales montadas desde Docker Secrets.
`GET /api/transferencias/{id}/comprobante` está disponible en el perfil `cloud` y devuelve el comprobante emitido en S3. El POST valida el monto y que origen y destino sean distintos.
