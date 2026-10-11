package cl.novabank.transferencias.repository;

import cl.novabank.transferencias.model.Transferencia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    List<Transferencia> findByCuentaOrigen(String cuentaOrigen);

    List<Transferencia> findByCuentaDestino(String cuentaDestino);

    List<Transferencia> findByEstado(String estado);
}
