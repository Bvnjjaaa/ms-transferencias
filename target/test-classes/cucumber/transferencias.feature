# language: es
Característica: Servicio Transferencias (microservicio transferencias del caso caso04)
  Los escenarios validan el contrato REST del microservicio alineado a sus endpoints.

  Escenario: el listado del recurso responde 200
    Dado el servicio "Transferencias" está disponible
    Cuando consulto el listado de "transferencias"
    Entonces el listado responde con código 200

  Escenario: ciclo de vida completo del recurso
    Dado un nuevo "transferencia" con nombre "hola-cucumber"
    Cuando consulto el "transferencia" recién creado
    Entonces el recurso tiene nombre "hola-cucumber" y código 200
    Cuando actualizo el "transferencia" con nombre "cucumber-actualizado"
    Entonces el recurso queda con nombre "cucumber-actualizado" y código 200
    Cuando elimino el "transferencia"
    Entonces la eliminación responde con código 204
    Y al consultar el "transferencia" eliminado responde 404
