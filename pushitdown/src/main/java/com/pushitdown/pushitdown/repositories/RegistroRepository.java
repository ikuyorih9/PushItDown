package com.pushitdown.pushitdown.repositories;

import com.pushitdown.pushitdown.id.RegistroId;
import com.pushitdown.pushitdown.models.Registro;
import com.pushitdown.pushitdown.models.Usuario;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
}
