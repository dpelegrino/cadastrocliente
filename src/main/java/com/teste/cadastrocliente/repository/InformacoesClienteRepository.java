package com.teste.cadastrocliente.repository;

import com.teste.cadastrocliente.entidades.InformacoesCliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformacoesClienteRepository extends JpaRepository<InformacoesCliente, Long> {
}
