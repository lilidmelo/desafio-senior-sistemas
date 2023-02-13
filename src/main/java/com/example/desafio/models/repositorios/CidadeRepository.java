package com.example.desafio.models.repositorios;


import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.desafio.models.entidades.Cidade;


@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {
 
 
    @Query("SELECT c FROM Cidades c WHERE c.capital = :capital")
    List<Cidade> findByCapital(@Param("capital") Boolean capital);
 

    
    @Query("SELECT c FROM Cidades c WHERE c.uf = :uf")
    List<Cidade> findByUF(@Param("uf") String uf);
 

  @Query("SELECT c.uf, COUNT(c.uf) FROM Cidades c GROUP BY c.uf")
  List<Object[]> countByUf();

  @Transactional
  @Modifying
  @Query("INSERT INTO Cidades (ibge_id, UF, nome, capital) VALUES (:ibgeId, :uf, :nome, :capital)")
  void insertAll(@Param("ibgeId") List<Integer> ibgeId, @Param("uf") List<String> uf,
                 @Param("nome") List<String> nome, @Param("capital") List<Boolean> capital);
 


}

   