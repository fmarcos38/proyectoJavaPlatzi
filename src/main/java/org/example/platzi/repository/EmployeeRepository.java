package org.example.platzi.repository;

import org.example.platzi.model.Employee;
import org.example.platzi.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository implements Repository<Employee>{

    //creo la conexión a la DB con la clase Q se encarda de dicha tarea
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance();
    }

    //metodo getAll desde la DB
    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>(); //creo un arrayList para lo q me traigo de la DB
        try(
                Statement myStmt = getConnection().createStatement();
                ResultSet myRes = myStmt.executeQuery("SELECT * FROM employees")
        ){
            while (myRes.next()){
                Employee e = createEmployee(myRes);//esta sería la parte TIPO de normalizar la data traida
                employees.add(e);//agrego cada empleado a la lista q retornaré
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employees;//retorno la lista de los empleados q están en la DB
    }

    //desarrollo el metodo busca x ID correspondiente a la Interfaz(ya q EN la interfaz SOLO se declara PERO no se desarrolla)
    @Override
    public Employee getById(Integer id) throws SQLException {
        Employee employee = null;

        try(
                PreparedStatement myStamt = getConnection().prepareStatement("SELECT * FROM employees WHERE id=?");
        ){
            myStamt.setInt(1, id);
            try(ResultSet myRes = myStamt.executeQuery()){
                if(myRes.next()){
                    employee = createEmployee(myRes);
                }
            }
        }
        return employee;
    }

    //metodo insertar
    @Override
    public void save(Employee employee) throws SQLException {

        String sql;
        if(employee.getId() != null && employee.getId() > 0){
            sql = "UPDATE employees SET first_name =?, last_name =?, email =?, salary =? WHERE id =?";
        }else {
            sql = "INSERT INTO employees ( first_name, last_name, email, salary) VALUES (?,?,?,?)";
        }

        try(
                PreparedStatement myStamt = getConnection().prepareStatement(sql);
        ){
            //realizo la insercion
            myStamt.setString(1,employee.getFirst_name());
            myStamt.setString(2,employee.getLast_name());
            myStamt.setString(3,employee.getEmail());
            myStamt.setFloat(4,employee.getSalary());
            if(employee.getId() != null && employee.getId() > 0){
                myStamt.setInt(5, employee.getId());
            }
            //ejecuto la consulta
            myStamt.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try(PreparedStatement myStamt = getConnection().prepareStatement("DELETE FROM employees WHERE id =?")){
            myStamt.setInt(1,id);
            myStamt.executeUpdate();
        }
    }

    //metodo creado con click derech REFACTOR -> createMethod (ver en video 11 min 9)
    //lo utilizo en cada metodo de arriba ES como nomalizar PARA la info q viene
    private Employee createEmployee(ResultSet myResu) throws SQLException {
        Employee employe = new Employee();
        employe.setId(myResu.getInt("id"));
        employe.setFirst_name(myResu.getString("first_name"));
        employe.setLast_name(myResu.getString("last_name"));
        employe.setEmail(myResu.getString("email"));
        employe.setSalary(myResu.getFloat("salary"));

        return employe;
    }
}
