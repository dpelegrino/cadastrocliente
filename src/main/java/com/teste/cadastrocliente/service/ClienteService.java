package com.teste.cadastrocliente.service;

import com.teste.cadastrocliente.comun.Response;
import com.teste.cadastrocliente.comun.RestConstErrosEnum;
import com.teste.cadastrocliente.entidades.*;
import com.teste.cadastrocliente.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by danilo on 26/04/2018.
 */
@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    private static final String URL_LAT_LONG= "https://ipvigilante.com/";
    private static final String URL_INFO_GEOGRAFICA= "https://www.metaweather.com/api/location/search/?lattlong=";
    private static final String URL_INFO_CLIMA = "https://www.metaweather.com/api/location/";

    private RestTemplate restTemplate = new RestTemplate();

    public Response incluirCliente(Cliente cliente, String ip) {
        Response resp = validaInclusao(cliente);
        if (resp.getErrors() != null && resp.getErrors().size() > 0) {
            return resp;
        }

        //Busca informacoes de latitude e longitude para
        InformacoesCliente informacoesCliente = getInformacoesLatiLongIP(ip);

        if(informacoesCliente != null){
            informacoesCliente.setCliente(cliente);
            //Busca informacoes de localizacao
            InfoGeograficas infoGeograficas = getInformacoesGeograficas(informacoesCliente.getLatitude(), informacoesCliente.getLongitute());
            if(infoGeograficas != null){
                //Busca informacoes de clima
                ConsolidatedWeather consolidatedWeather = getInformacoesTempo(infoGeograficas.getWoeid());
                if(consolidatedWeather != null){
                    informacoesCliente.setMaxTemp(consolidatedWeather.getMax_temp());
                    informacoesCliente.setMinTemp(consolidatedWeather.getMin_temp());
                }
            }
            cliente.setInformacoesCliente(informacoesCliente);
        }
        cliente.setIp(ip);
        clienteRepository.save(cliente);

        resp.setData(RestConstErrosEnum.CADASTRO_SUCESSO);

        return resp;
    }

    public Response editarCliente(Cliente cliente) {
        Response resp = validaEdicao(cliente);
        if (resp.getErrors() != null && resp.getErrors().size() > 0) {
            return resp;
        }

        clienteRepository.save(cliente);

        resp.setData(RestConstErrosEnum.EDITADO_SUCESSO);

        return resp;
    }

    public Response removerCliente(Long id) {
        Response resp = validaRemocao(id);
        if (resp.getErrors() != null && resp.getErrors().size() > 0) {
            return resp;
        }

        Optional<Cliente> cli = clienteRepository.findById(id);
        clienteRepository.delete(cli.get());

        resp.setData(RestConstErrosEnum.REMOVIDO_SUCESSO);

        return resp;
    }

    public Cliente buscarPorId(Long idCliente) {
        Optional<Cliente> cli = clienteRepository.findById(idCliente);
        if(cli != null){
            return cli.get();
        }
        return null;
    }

    public List<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    private Response validaInclusao(Cliente cliente) {
        Response<Cliente> response = new Response<Cliente>();

        if (cliente.getNome() != null) {
            Cliente cliExiste = clienteRepository.findByNome(cliente.getNome());
            if(cliExiste != null){
                response.getErrors().add(RestConstErrosEnum.NOME_CADASTRADO + cliente.getNome());
            }
        }
        return response;
    }

    private Response validaEdicao(Cliente cliente) {
        Response<Cliente> response = new Response<Cliente>();

        if (cliente.getId() != null) {
            Optional<Cliente> cliExiste = clienteRepository.findById(cliente.getId());
            if(!cliExiste.isPresent()){
                response.getErrors().add(RestConstErrosEnum.CLIENTE_NAO_EXISTE + cliente.getId());
            }
        }
        return response;
    }

    private Response validaRemocao(Long idCliente) {
        Response<Cliente> response = new Response<Cliente>();

        if (idCliente != null) {
            Optional<Cliente> cliExiste = clienteRepository.findById(idCliente);
            if(cliExiste == null){
                response.getErrors().add(RestConstErrosEnum.CLIENTE_NAO_EXISTE + idCliente);
            }
        }
        return response;
    }

    private InfoGeograficas getInformacoesGeograficas(String latitude, String longitude){
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        converter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));

        restTemplate.getMessageConverters().add(converter);

        ResponseEntity<InfoGeograficas[]> responseEntity = restTemplate.getForEntity(this.URL_INFO_GEOGRAFICA + latitude + "," + longitude, InfoGeograficas[].class);
        InfoGeograficas[] objects = responseEntity.getBody();

        if(objects != null){
            return objects[0];
        }
        return null;
    }

    private InformacoesCliente getInformacoesLatiLongIP(String ip){
        Geographical geographical = restTemplate.getForObject(this.URL_LAT_LONG + ip, Geographical.class);

        if(geographical != null){
            InformacoesCliente informacoesCliente = new InformacoesCliente();
            informacoesCliente.setLatitude(geographical.getData().getLatitude());
            informacoesCliente.setLongitute(geographical.getData().getLongitude());

            return informacoesCliente;
        }
        return null;
    }

    private ConsolidatedWeather getInformacoesTempo(String woeid){
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        converter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));

        restTemplate.getMessageConverters().add(converter);

        ResponseEntity<Weather> responseEntity = restTemplate.getForEntity(this.URL_INFO_CLIMA + woeid, Weather.class);
        Weather objects = responseEntity.getBody();

        if(objects != null){
            return objects.getConsolidatedWeathers().get(0);
        }
        return null;
    }
}
