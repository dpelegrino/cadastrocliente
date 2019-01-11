package com.teste.cadastrocliente.controller;

import com.teste.cadastrocliente.comun.Response;
import com.teste.cadastrocliente.entidades.Cliente;
import com.teste.cadastrocliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by danilo.
 * Serviços referente a Cliente
 */
@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    Response<Cliente> response = new Response<Cliente>();

    @PostMapping
    //Chamada do serviço de inclusao do cliente (POST)
    public ResponseEntity<Response<Cliente>> incluir(@Valid @RequestBody Cliente cliente, HttpServletRequest request) {

        try {
            //Efetua chamada de negocio para inclusao do cliente
            response = clienteService.incluirCliente(cliente, "189.46.254.201");
            //Caso obtenha erro de validação no service, retorna Bad Request e a descrição do erro(Setado no service)
            if (response.getErrors() != null && response.getErrors().size() > 0) {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        //Retorna ok caso não tenha encontrado nenhum problema no service.
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public Cliente pesquisarPorId(@PathVariable("id") Long id) {
        return clienteService.buscarPorId(id);
    }

    @GetMapping("/all")
    public List<Cliente> pesquisarTodos() {
        return clienteService.buscarTodos();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Cliente>> removerCliente(@PathVariable("id") Long id) {
        response = clienteService.removerCliente(id);

        if (response.getErrors() != null && response.getErrors().size() > 0) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public Response putCliente(@RequestBody Cliente cliente) {
        return clienteService.editarCliente(cliente);
    }
}
