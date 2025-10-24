package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import db.Conexion;
import model.Usuarios;

public class UsuariosRepository {
    private static final Logger LOGGER = Logger.getLogger(UsuariosRepository.class.getName());
    public void insertarUsuario(Usuarios usuario){
        String sql = "INSERT INTO CLARA.USUARIOS (NOMBRE, EDAD) VALUES (?, ?)";

        try (Connection connection = Conexion.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setLong(2, usuario.getEdad());

            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                LOGGER.info(() -> "Usuario insertado correctamente: " + usuario.getNombre());
            } else {
                LOGGER.warning(() -> "No se insertó el usuario: " + usuario.getNombre());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar usuario: " + usuario.getNombre(), e);
        }
    }


   
    public void mostrarUsuario() throws SQLException{
        String sql = "SELECT * FROM CLARA.USUARIOS";
        try (Connection connection = Conexion.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet set = preparedStatement.executeQuery()) {
            while (set.next()) {
                int id = set.getInt("id");
                String nombre = set.getString("nombre");
                int edad = set.getInt("edad");
                System.out.println("Usuario: " + id + ", Nombre: " + nombre + ", Edad: " + edad);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al mostrar usuarios", e);
            throw e;
        }
    }

  
    
public Usuarios obtenerUsuarioPorId(Long id) throws SQLException {
    String sql = "SELECT id, nombre, edad FROM CLARA.USUARIOS WHERE id = ?";
    try (Connection connection = Conexion.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setLong(1, id);
        try (ResultSet rs = preparedStatement.executeQuery()) {
            if (rs.next()) {
                Long foundId = rs.getLong("id");
                String nombre = rs.getString("nombre");
                Long edad = rs.getLong("edad");
                return new Usuarios(foundId, nombre, edad);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    }
    return null; 
}

    
    public boolean eliminarUsuario(Long id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("id no puede ser null");
        }

        String sql = "DELETE FROM CLARA.USUARIOS WHERE id = ?";
        try (Connection connection = Conexion.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.info(() -> "Usuario eliminado correctamente, id=" + id);
                return true;
            } else {
                LOGGER.info(() -> "No se encontró usuario para eliminar, id=" + id);
                return false;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar usuario id=" + id, e);
            throw new SQLException("Error al eliminar usuario id=" + id, e);
        }
    }

    public boolean actualizarUsuario(Usuarios usuario) throws SQLException {
        if (usuario == null) {
            throw new IllegalArgumentException("usuario no puede ser null");
        }
        if (usuario.getId() == null) {
            throw new IllegalArgumentException("id no puede ser null");
        }

        String sql = "UPDATE CLARA.USUARIOS SET nombre = ?, edad = ? WHERE id = ?";
        try (Connection connection = Conexion.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setLong(2, usuario.getEdad());
            ps.setLong(3, usuario.getId());
            int updated = ps.executeUpdate();
            if (updated == 1) {
                LOGGER.info(() -> "Usuario actualizado correctamente, id=" + usuario.getId());
                return true;
            } else {
                LOGGER.info(() -> "No se encontró usuario para actualizar, id=" + usuario.getId());
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar usuario id=" + usuario.getId(), e);
            throw new SQLException("Error al actualizar usuario id=" + usuario.getId(), e);
        }
    }


    
}
