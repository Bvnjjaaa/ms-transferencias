package cl.novabank.transferencias.controller;

import cl.novabank.transferencias.model.Transferencia;
import cl.novabank.transferencias.service.TransferenciaService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @PostMapping
    public ResponseEntity<Transferencia> realizarTransferencia(@RequestBody Transferencia transferencia) {
        Transferencia creada = transferenciaService.registrarTransferencia(transferencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping
    public ResponseEntity<List<Transferencia>> listarTodas() {
        return ResponseEntity.ok(transferenciaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transferencia> obtenerPorId(@PathVariable Long id) {
        return transferenciaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/origen/{cuentaOrigen}")
    public ResponseEntity<List<Transferencia>> listarPorCuentaOrigen(@PathVariable String cuentaOrigen) {
        return ResponseEntity.ok(transferenciaService.listarPorCuentaOrigen(cuentaOrigen));
    }

    @GetMapping("/destino/{cuentaDestino}")
    public ResponseEntity<List<Transferencia>> listarPorCuentaDestino(@PathVariable String cuentaDestino) {
        return ResponseEntity.ok(transferenciaService.listarPorCuentaDestino(cuentaDestino));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Transferencia> actualizarEstado(@PathVariable Long id,
                                                          @RequestParam String estado) {
        return transferenciaService.actualizarEstado(id, estado)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
