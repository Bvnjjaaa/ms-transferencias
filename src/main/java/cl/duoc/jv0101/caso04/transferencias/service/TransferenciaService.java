package cl.duoc.jv0101.caso04.transferencias.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import cl.duoc.jv0101.caso04.transferencias.model.Transferencia;
import cl.duoc.jv0101.caso04.transferencias.repository.TransferenciaRepository;

@Service
public class TransferenciaService {

    private final TransferenciaRepository repository;

    public TransferenciaService(TransferenciaRepository repository) {
        this.repository = repository;
    }

    public List<Transferencia> findAll() {
        return repository.findAll();
    }

    public Optional<Transferencia> findById(Long id) {
        return repository.findById(id);
    }

    public Transferencia create(Transferencia recurso) {
        return repository.save(recurso);
    }

    public Optional<Transferencia> update(Long id, Transferencia datos) {
        return repository.findById(id).map(existente -> {
            existente.setNombre(datos.getNombre());
            existente.setOrigen(datos.getOrigen());
            existente.setDestino(datos.getDestino());
            existente.setMonto(datos.getMonto());
            return repository.save(existente);
        });
    }

    public boolean delete(Long id) {
        return repository.findById(id).map(existente -> {
            repository.delete(existente);
            return true;
        }).orElse(false);
    }
}
