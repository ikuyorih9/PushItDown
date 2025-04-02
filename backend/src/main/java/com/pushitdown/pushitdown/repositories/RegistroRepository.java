package com.pushitdown.pushitdown.repositories;

import com.pushitdown.pushitdown.id.RegistroId;
import com.pushitdown.pushitdown.models.ExpedienteDTO;
import com.pushitdown.pushitdown.models.Registro;
import com.pushitdown.pushitdown.models.Usuario;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegistroRepository extends JpaRepository<Registro, RegistroId>{
    @Query("SELECT r FROM Registro r WHERE r.usuario.username =:username ORDER BY (r.id.data, r.id.hora) DESC LIMIT 1")
    Optional<Registro> findLastSessionByUsername(@Param("username") String username);
    
    @Query("SELECT r FROM Registro r WHERE r.usuario.username = :username ORDER BY (r.id.data, r.id.hora) DESC")
    List<Registro> findRegistrosByUsername(@Param("username") String username);

    @Query("SELECT r FROM Registro r WHERE r.usuario.username = :username AND r.id.data = :data")
    List<Registro> findByUsernameAndData(@Param("username") String username, @Param("data") LocalDate data);

    @Query("SELECT DISTINCT r.id.data FROM Registro r WHERE r.usuario.username = :username")
    List<LocalDate> findAllDaysByUsername(@Param("username") String username);

    // @Query("SELECT r FROM Registro r WHERE r.usuario.username = :username " +
    //    "AND r.id.dataHora BETWEEN :startOfDay AND :endOfDay " +
    //    "ORDER BY r.id.dataHora ASC")
    // List<Registro> findRegistrosByUsernameWithDayFilter(@Param("username") String username, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    List<Registro> findByUsuario(Usuario usuario);

    @Query("SELECT r FROM Registro r WHERE r.usuario.username = :username AND r.id.data = COALESCE(:data, r.id.data) AND r.tipo = COALESCE(:tipo, r.tipo) ORDER BY (r.id.data, r.id.hora) DESC")
    List<Registro> findByParams(@Param("username") String username, @Param("data") LocalDate data, @Param("tipo") String tipo);

    @Query("SELECT r FROM Registro r WHERE r.usuario.username = :username AND MONTH(r.id.data) = COALESCE(:mes, MONTH(r.id.data)) AND YEAR(r.id.data) = COALESCE(:ano, YEAR(r.id.data)) ORDER BY (r.id.data, r.id.hora) DESC")
    List<Registro> findByMonth(@Param("username") String username, @Param("mes") Integer mes, @Param("ano") Integer ano);


    @Query(value = """
        SELECT data, EXTRACT(EPOCH FROM (h_saida - h_entrada)) * 1000 AS expediente
        FROM (
            SELECT r.data AS data, SUM(r.hora) AS h_saida
            FROM registro r
            WHERE r.tipo = 'SAIDA'
                AND r.usuario = :username
                AND (:mes IS NULL OR EXTRACT(MONTH FROM r.data) = :mes)
                AND (:ano IS NULL OR EXTRACT(YEAR FROM r.data) = :ano)
            GROUP BY r.usuario, r.data
        ) AS saida
        NATURAL JOIN (
            SELECT r.data AS data, SUM(r.hora) AS h_entrada
            FROM registro r
            WHERE r.tipo = 'ENTRADA'
                AND r.usuario = :username
                AND (:mes IS NULL OR EXTRACT(MONTH FROM r.data) = :mes)
                AND (:ano IS NULL OR EXTRACT(YEAR FROM r.data) = :ano)
            GROUP BY r.usuario, r.data
        ) AS entrada
        ORDER BY data DESC
        """, nativeQuery = true)
    List<Object[]> findExpedientesPorData(@Param("username") String username, @Param("mes") Integer mes, @Param("ano") Integer ano);

}
