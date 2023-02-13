package com.example.desafio.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.desafio.models.entidades.Cidade;
import com.example.desafio.models.repositorios.CidadeRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;

   



    public void saveCidadesFromCsv(MultipartFile file) throws IOException {

          // Lê o arquivo CSV usando o OpenCSV
          List<Cidade> cidades = new ArrayList<>();
          try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
              CsvToBean<Cidade> csvToBean = new CsvToBeanBuilder<Cidade>(reader)
                      .withType(Cidade.class)
                      .withIgnoreLeadingWhiteSpace(true)
                      .build();
              cidades = csvToBean.parse();
          }
  
          // Salva as cidades no banco de dados
          cidadeRepository.saveAll(cidades);

      
}


public List<Cidade> filterCidadesByColumn(String coluna, String filtro) throws IOException {
    List<Cidade> allCidades = cidadeRepository.findAll();
    List<Cidade> filteredCidades = new ArrayList<>();

    for (Cidade cidade : allCidades) {
        switch (coluna) {
            case "ibge_id":
                if (String.valueOf(cidade.getIbgeId()).contains(filtro)) {
                    filteredCidades.add(cidade);
                }
                break;
            case "UF":
                if (cidade.getUF().contains(filtro)) {
                    filteredCidades.add(cidade);
                }
                break;
            case "Nome":
                if (cidade.getNome().contains(filtro)) {
                    filteredCidades.add(cidade);
                }
                break;
            case "Capital":
                if (String.valueOf(cidade.getCapital()).contains(filtro)) {
                    filteredCidades.add(cidade);
                }
                break;
            default:
                break;
        }
    }

    return filteredCidades;
}
}