package cl.novabank.transferencias.evento;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@Profile("cloud")
public class OutboxTransferencias {
    private static final Logger log = LoggerFactory.getLogger(OutboxTransferencias.class);

    private final JdbcTemplate jdbc;
    private final ObjectMapper json;
    private final SqsClient sqs;
    private final String queueUrl;

    public OutboxTransferencias(JdbcTemplate jdbc, ObjectMapper json, SqsClient sqs,
            @Value("${TRANSFER_QUEUE_URL}") String queueUrl) {
        this.jdbc = jdbc;
        this.json = json;
        this.sqs = sqs;
        this.queueUrl = queueUrl;
    }

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void registrar(TransferenciaRegistrada evento) {
        UUID id = UUID.randomUUID();
        ObjectNode payload = json.createObjectNode()
                .put("eventoId", id.toString())
                .put("tipo", "TRANSFERENCIA_REGISTRADA")
                .put("transferenciaId", evento.transferenciaId())
                .put("registradaEn", Instant.now().toString());
        jdbc.update("INSERT INTO transferencia_outbox (id, transferencia_id, carga) VALUES (?, ?, ?)",
                id, evento.transferenciaId(), payload.toString());
    }

    @Scheduled(fixedDelayString = "${OUTBOX_INTERVAL_MS:1000}")
    @Transactional
    public void publicarSiguiente() {
        List<EventoPendiente> pendientes = jdbc.query("""
                SELECT id, carga FROM transferencia_outbox
                WHERE publicado_en IS NULL
                ORDER BY registrado_en, id
                LIMIT 1 FOR UPDATE SKIP LOCKED
                """, (rs, row) -> new EventoPendiente(rs.getObject("id", UUID.class), rs.getString("carga")));
        if (pendientes.isEmpty()) {
            return;
        }

        EventoPendiente pendiente = pendientes.get(0);
        var resultado = sqs.sendMessage(request -> request.queueUrl(queueUrl).messageBody(pendiente.carga()));
        jdbc.update("UPDATE transferencia_outbox SET publicado_en = CURRENT_TIMESTAMP WHERE id = ?", pendiente.id());
        log.info("Publicado evento {} en SQS con mensaje {}", pendiente.id(), resultado.messageId());
    }

    private record EventoPendiente(UUID id, String carga) {}
}
