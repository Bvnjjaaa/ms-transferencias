package cl.duoc.jv0101.caso04.transferencias.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.duoc.jv0101.caso04.transferencias.model.Transferencia;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
}
