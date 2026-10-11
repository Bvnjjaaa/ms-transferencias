package cl.novabank.transferencias.service;

import cl.novabank.transferencias.model.Transferencia;
import cl.novabank.transferencias.repository.TransferenciaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferenciaService {

    private static final String ESTADO_COMPLETADA = "COMPLETADA";

    private final TransferenciaRepository transferenciaRepository;

    public TransferenciaService(TransferenciaRepository transferenciaRepository) {
        this.transferenciaRepository = transferenciaRepository;
    }

    @Transactional
    public Transferencia registrarTransferencia(Transferencia transferencia) {
        if (transferencia.getFecha() == null) {
            transferencia.setFecha(LocalDateTime.now());
        }
        if (transferencia.getEstado() == null || transferencia.getEstado().isBlank()) {
            transferencia.setEstado(ESTADO_COMPLETADA);
        }
        return transferenciaRepository.save(transferencia);
    }

    @Transactional(readOnly = true)
    public Optional<Transferencia> obtenerPorId(Long id) {
        return transferenciaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Transferencia> listarPorCuentaOrigen(String cuentaOrigen) {
        return transferenciaRepository.findByCuentaOrigen(cuentaOrigen);
    }

    @Transactional(readOnly = true)
    public List<Transferencia> listarPorCuentaDestino(String cuentaDestino) {
        return transferenciaRepository.findByCuentaDestino(cuentaDestino);
    }

    @Transactional(readOnly = true)
    public List<Transferencia> listarTodas() {
        return transferenciaRepository.findAll();
    }

    @Transactional
    public Optional<Transferencia> actualizarEstado(Long id, String estado) {
        return transferenciaRepository.findById(id)
                .map(transferencia -> {
                    transferencia.setEstado(estado);
                    return transferenciaRepository.save(transferencia);
                });
    }
}
