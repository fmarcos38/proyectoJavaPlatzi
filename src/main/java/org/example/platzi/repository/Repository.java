package org.example.platzi.repository;

import java.sql.SQLException;
import java.util.List;

public interface Repository <T>{

    //metodo leer todos -> guardo en una lista
    List<T> findAll() throws SQLException;

    //metodo leo por ID
    T getById(Integer id) throws SQLException;

    //metodo insert y update
    void save(T t) throws SQLException;

    //metodo delete
    void delete(Integer id) throws SQLException;
}
