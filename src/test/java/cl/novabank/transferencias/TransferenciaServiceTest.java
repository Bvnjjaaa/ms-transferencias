package cl.novabank.transferencias;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cl.novabank.transferencias.evento.TransferenciaRegistrada;
import cl.novabank.transferencias.model.Transferencia;
import cl.novabank.transferencias.repository.TransferenciaRepository;
import cl.novabank.transferencias.service.TransferenciaService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.web.server.ResponseStatusException;

@DataJpaTest(showSql = false)
@Import(TransferenciaService.class)
@RecordApplicationEvents
class TransferenciaServiceTest {
    @Autowired
    private TransferenciaRepository repository;

    @Autowired
    private ApplicationEvents events;

    @Autowired
    private TransferenciaService service;

    @Test
    void publicaElIdentificadorAsignadoPorLaBaseDeDatos() {
        Transferencia input = transferencia("demo-destino");
        input.setId(999L);

        Transferencia registrada = service.registrarTransferencia(input);

        assertThat(registrada.getId()).isNotNull().isNotEqualTo(999L);
        assertThat(registrada.getEstado()).isEqualTo("COMPLETADA");
        assertThat(repository.findById(registrada.getId())).isPresent();
        assertThat(events.stream(TransferenciaRegistrada.class)).containsExactly(
                new TransferenciaRegistrada(registrada.getId()));
    }

    @Test
    void rechazaUnaTransferenciaALaMismaCuentaSinGuardarNiPublicar() {
        assertThatThrownBy(() -> service.registrarTransferencia(transferencia("demo-origen")))
                .isInstanceOf(ResponseStatusException.class);
        assertThat(repository.count()).isZero();
        assertThat(events.stream(TransferenciaRegistrada.class)).isEmpty();
    }

    private Transferencia transferencia(String destino) {
        Transferencia input = new Transferencia();
        input.setCuentaOrigen("demo-origen");
        input.setCuentaDestino(destino);
        input.setMonto(BigDecimal.TEN);
        return input;
    }
}
