package com.so.pedidos.Repository;

import com.so.pedidos.model.auditoria;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuditoriaRepository extends JpaRepository<auditoria, Integer> {

}