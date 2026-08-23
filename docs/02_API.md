# Transferencias — Contrato de la API REST

## Base

- **Base path**: `/api/transferencias`
- **Formato**: JSON — **Puerto**: 8080 (configurable con `PORT`)

## Recursos

| Método | Ruta | Códigos de estado | Descripción |
|--------|------|-------------------|-------------|
| GET | `/api/transferencias` | 200 | Lista todos los recursos |
| GET | `/api/transferencias/{id}` | 200 / 404 | Obtiene un recurso por id |
| POST | `/api/transferencias` | 201 / 400 | Crea un recurso |
| PUT | `/api/transferencias/{id}` | 200 / 404 / 400 | Actualiza un recurso |
| DELETE | `/api/transferencias/{id}` | 204 / 404 | Elimina un recurso |

## Atributos de un recurso

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| id | Long | - | Identificador autogenerado |
| nombre | String | Sí | Nombre del recurso |

| origen | String | No | Campo del dominio |
| destino | String | No | Campo del dominio |
| monto | BigDecimal | No | Campo del dominio |

## Ejemplos con curl

```bash
# Listar
curl http://localhost:8080/api/transferencias

# Crear
curl -X POST http://localhost:8080/api/transferencias \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Mi recurso"}'

# Obtener por id
curl http://localhost:8080/api/transferencias/1

# Actualizar
curl -X PUT http://localhost:8080/api/transferencias/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Recurso actualizado"}'

# Eliminar
curl -X DELETE http://localhost:8080/api/transferencias/1
```
