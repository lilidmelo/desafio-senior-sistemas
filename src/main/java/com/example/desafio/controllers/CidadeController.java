package com.example.desafio.controllers;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.desafio.models.entidades.Cidade;
import com.example.desafio.models.repositorios.CidadeRepository;
import com.example.desafio.services.CidadeService;

@RestController
@RequestMapping("/api/cidades")
public class CidadeController {

  @Autowired
  CidadeRepository cidadeRepository;

  @Autowired
  private CidadeService cidadeService;

  
  public CidadeController(CidadeService cidadeService) {
    this.cidadeService = cidadeService;
  }

  @PostMapping("/add")
  public ResponseEntity<Cidade> addCidade(@RequestBody Cidade cidade) {
    Cidade novaCidade = cidadeRepository.save(cidade);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(novaCidade.getIbgeId()).toUri();
    return ResponseEntity.created(location).build();
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> deleteCidade(@PathVariable int id) {
    cidadeRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/total")
  public ResponseEntity<Long> countCidades() {
    Long count = cidadeRepository.count();
    return ResponseEntity.ok(count);
  }

  @GetMapping(value = "/capital", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Cidade> getCidadesCapital() {
    return cidadeRepository.findByCapital(true);
  }

  @GetMapping(value = "/quantidade_estado", produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, Long> countByUf() {
    return cidadeRepository.findAll().stream()
        .collect(Collectors.groupingBy(Cidade::getUF, Collectors.counting()));
  }

  @GetMapping(value = "/informacoes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Cidade> getCidadeById(@PathVariable(value = "id") Integer id) {
    Cidade cidade = cidadeRepository.findById(id)
        .orElse(null);
    if (cidade == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(cidade, HttpStatus.OK);
  }


  @GetMapping("/estado/{uf}")
  public ResponseEntity<List<Cidade>> getCidadesPorEstado(@PathVariable String uf) {
      List<Cidade> cidades = cidadeRepository.findByUF(uf);
      if (cidades.isEmpty()) {
          return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(cidades);
  }


  @GetMapping("/mais-distantes")
  public ResponseEntity<Cidade[]> getCidadesMaisDistantes() {
      List<Cidade> cidades = cidadeRepository.findAll();
      double maxDistance = 0;
      Cidade cidade1 = null;
      Cidade cidade2 = null;
      for (int i = 0; i < cidades.size(); i++) {
          for (int j = i + 1; j < cidades.size(); j++) {
              Cidade c1 = cidades.get(i);
              Cidade c2 = cidades.get(j);
              double distance = calculateDistance(c1, c2);
              if (distance > maxDistance) {
                  maxDistance = distance;
                  cidade1 = c1;
                  cidade2 = c2;
              }
          }
      }
      Cidade[] result = {cidade1, cidade2};
      return ResponseEntity.ok(result);
  }

  private double calculateDistance(Cidade c1, Cidade c2) {
    double lat1 = Math.toRadians(c1.getLatitude());
    double lat2 = Math.toRadians(c2.getLatitude());
    double deltaLat = Math.toRadians(c2.getLatitude() - c1.getLatitude());
    double deltaLon = Math.toRadians(c2.getLongitude() - c1.getLongitude());
    
    double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) + 
               Math.cos(lat1) * Math.cos(lat2) * 
               Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  
  //o raio da Terra é aproximadamente 6371 km
    double d = 6371 * c;
    
    return d;
}
  @PostMapping("/le_planilha")
  public ResponseEntity<String> saveCSV(@RequestParam("file") MultipartFile file) {
    try {
      cidadeService.saveCidadesFromCsv(file);
      return ResponseEntity.ok("Arquivo carregado com sucesso.");
  } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ocorreu um erro ao processar o arquivo.");
  }
  }

  @PostMapping("/saveAll")
public ResponseEntity<String> saveAllCities(@RequestBody List<Cidade> cidades) {
    cidadeRepository.saveAll(cidades);
    return new ResponseEntity<>("Todas as cidades foram salvas com sucesso!", HttpStatus.OK);

}


@GetMapping("/filtro")
public List<Cidade> filterCidadesByColumn(@RequestParam("coluna") String coluna,
                                           @RequestParam("filtro") String filtro) throws IOException {
    return cidadeService.filterCidadesByColumn(coluna, filtro);
}



@GetMapping("/estatisticas")
public ResponseEntity<Map<String, Object>> getCityStats() {
  List<Cidade> cidades = cidadeRepository.findAll();
  Map<String, Long> stateCounts = cidades.stream()
      .collect(Collectors.groupingBy(Cidade::getUF, Collectors.counting()));
  Optional<Map.Entry<String, Long>> min = stateCounts.entrySet().stream().min(Map.Entry.comparingByValue());
  Optional<Map.Entry<String, Long>> max = stateCounts.entrySet().stream().max(Map.Entry.comparingByValue());

  Map<String, Object> retorno = new HashMap<>();
  retorno.put("totalCities", cidades.size());
  retorno.put("stateWithMostCities", max.map(Map.Entry::getKey).orElse(""));
  retorno.put("stateWithLeastCities", min.map(Map.Entry::getKey).orElse(""));

  return ResponseEntity.ok(retorno);
}


}
