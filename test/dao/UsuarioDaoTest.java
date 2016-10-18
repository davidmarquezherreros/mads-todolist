package dao;

import play.db.Database;
import play.db.Databases;
import play.db.jpa.*;
import org.junit.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import models.*;

public class UsuarioDaoTest {

    Database db;
    JPAApi jpa;

    @Before
    public void initDatabase() {
        db = Databases.inMemoryWith("jndiName", "DefaultDS");
        // Necesario para inicializar el nombre JNDI de la BD
        db.getConnection();
        // Se activa la compatibilidad MySQL en la BD H2
        db.withConnection(connection -> {
            connection.createStatement().execute("SET MODE MySQL;");
        });
        jpa = JPA.createFor("memoryPersistenceUnit");
    }

    @After
    public void shutdownDatabase() {
        db.withConnection(connection -> {
            connection.createStatement().execute("DROP TABLE Usuario;");
        });
        jpa.shutdown();
        db.shutdown();
    }

    @Test
    public void creaBuscaUsuario() {
        Integer id = jpa.withTransaction(() -> {
            Usuario nuevo = new Usuario("pepe", "pepe");
            nuevo = UsuarioDAO.create(nuevo);
            return nuevo.id;
        });

        jpa.withTransaction(() -> {
            Usuario usuario = UsuarioDAO.find(id);
            assertThat(usuario.login, equalTo("pepe"));
        });
    }

    @Test
    public void buscaUsuarioLogin() {
        jpa.withTransaction(() -> {
            Usuario usuario = UsuarioDAO.findUsuarioLogin("pepe");
            assertNull(usuario);
        });
    }
    // Test para comprobar el funcionamiento de busqueda de un usuario basado en su login + password
    @Test
    public void UsuarioLogin(){
      String parametros = jpa.withTransaction(() ->{
        Usuario nuevo = new Usuario("david","1234");
        nuevo = UsuarioDAO.create(nuevo);
        return nuevo.login+":"+nuevo.password;
      });
      jpa.withTransaction(() -> {
        String[] parts = parametros.split(":");
        Usuario usuario = UsuarioDAO.Login(parts[0],parts[1]).get(0);
        assertThat(usuario.login,equalTo("david"));
      });
    }
    // Test para comprobar el funcionamiento de borrado de un usuario
    @Test
    public void UsuarioDelete() {
        Integer id = jpa.withTransaction(() -> {
            Usuario nuevo = new Usuario("pepe", "pepe");
            nuevo = UsuarioDAO.create(nuevo);
            return nuevo.id;
        });

        jpa.withTransaction(() -> {
            UsuarioDAO.delete(id);
            Usuario usuario = UsuarioDAO.find(id);
            assertNull(usuario);
        });
    }
    // Test para comprobar el funcionamiento de modificar un usuario
    @Test
    public void UsuarioUpdate() {
        Integer id = jpa.withTransaction(() -> {
            Usuario nuevo = new Usuario("pepe", "pepe");
            nuevo = UsuarioDAO.create(nuevo);
            return nuevo.id;
        });

        jpa.withTransaction(() -> {
            Usuario modificar = new Usuario("Juan", "1234");
            Usuario usuario = UsuarioDAO.update(modificar);
            assertThat(usuario.login,equalTo("Juan"));
            assertThat(usuario.password,equalTo("1234"));
        });
    }
}
