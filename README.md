# ms-transferencias — Microservicio de Transferencias

Microservicio de **Transferencias** de **NovaBank** (banca digital / fintech).

| | |
|---|---|
| Stack | Spring Boot 3.2.5 · Java 17 · Maven · Spring Data JPA · H2 · Lombok |
| Puerto | 8083 |
| Aplicación | `TransferenciasApplication` (`cl.novabank.transferencias`) |
| Persistencia | H2 en memoria: `novabank_transfers_db` |

## Endpoints

Base URL: `/api/transferencias`

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/transferencias` | Registra una transferencia (fecha actual y estado `COMPLETADA` por defecto) |
| GET | `/api/transferencias` | Lista todas las transferencias |
| GET | `/api/transferencias/{id}` | Obtiene una transferencia por id |
| GET | `/api/transferencias/origen/{cuentaOrigen}` | Lista por cuenta origen |
| GET | `/api/transferencias/destino/{cuentaDestino}` | Lista por cuenta destino |
| PUT | `/api/transferencias/{id}/estado?estado=...` | Actualiza el estado de una transferencia |

Estados posibles: `PENDIENTE`, `COMPLETADA`, `RECHAZADA`.

## H2 Console

Disponible en `http://localhost:8083/h2-console` (JDBC URL: `jdbc:h2:mem:novabank_transfers_db`).

## Cómo compilar

```bash
mvnw.cmd clean package
```

El artefacto se genera en `target/ms-transferencias-1.0.0.jar`.

## Cómo ejecutar

```bash
mvnw.cmd spring-boot:run
# http://localhost:8083
```

## Cómo ejecutar con Docker

```bash
docker compose up --build
# http://localhost:8083
```
