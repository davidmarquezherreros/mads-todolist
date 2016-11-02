package dao;

import play.db.Database;
import play.db.Databases;
import play.db.jpa.*;
import org.junit.*;
import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import java.io.FileInputStream;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import models.*;

public class UsuarioDaoDbUnitTest {

    static Database db;
    static JPAApi jpa;
    JndiDatabaseTester databaseTester;

    @BeforeClass
    static public void initDatabase() {
        db = Databases.inMemoryWith("jndiName", "DefaultDS");
        // Necesario para inicializar el nombre JNDI de la BD
        db.getConnection();
        // Se activa la compatibilidad MySQL en la BD H2
        db.withConnection(connection -> {
            connection.createStatement().execute("SET MODE MySQL;");
        });
        jpa = JPA.createFor("memoryPersistenceUnit");
    }

    @Before
    public void initData() throws Exception {
        databaseTester = new JndiDatabaseTester("DefaultDS");
        IDataSet initialDataSet = new FlatXmlDataSetBuilder().build(new
        FileInputStream("test/resources/usuarios_dataset.xml"));
        databaseTester.setDataSet(initialDataSet);

        // Definimos como operación TearDown DELETE_ALL para que se
        // borren todos los datos de las tablas del dataset
        // (el valor por defecto DbUnit es DatabaseOperation.NONE)
        //databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);

        // Definimos como operación SetUp CLEAN_INSERT, que hace un
        // DELETE_ALL de todas las tablase del dataset, seguido por un
        // INSERT. (http://dbunit.sourceforge.net/components.html)
        // Es lo que hace DbUnit por defecto, pero así queda más claro.
        //databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);

        databaseTester.onSetup();
    }

    @After
    public void clearData() throws Exception {
        databaseTester.onTearDown();
    }

    @AfterClass
    static public void shutdownDatabase() {
        jpa.shutdown();
        db.shutdown();
    }

    @Test
    public void findUsuarioPorLogin() {
        jpa.withTransaction(() -> {
            Usuario usuario = UsuarioDAO.findUsuarioLogin("juan");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy");
            try {
                Date diezDiciembre93 = sdf.parse("10-12-1993");
                assertTrue(usuario.login.equals("juan") &&
                           usuario.fechaNacimiento.compareTo(diezDiciembre93) == 0);
            } catch (java.text.ParseException ex) {
            }
        });
    }

    @Test
    public void actualizaUsuario() {
        jpa.withTransaction(() -> {
            Usuario usuario = UsuarioDAO.find(2);
            usuario.apellidos = "Anabel Pérez";
            UsuarioDAO.update(usuario);
        });

        jpa.withTransaction(() -> {
            Usuario usuario = UsuarioDAO.find(2);
            assertThat(usuario.apellidos, equalTo("Anabel Pérez"));
        });
    }
    // Test para comprobar el funcionamiento de busqueda de un usuario basado en su login + password
    @Test
    public void UsuarioLogin(){
      jpa.withTransaction(() -> {
        Usuario user = UsuarioDAO.find(3);
        Usuario usuario = UsuarioDAO.Login(user.login,user.password).get(0);
        assertThat(usuario.login,equalTo("david"));
      });
    }
    // Test para comprobar el funcionamiento de borrado de un usuario con un usuario que existe
    @Test
    public void UsuarioDelete() {
        jpa.withTransaction(() -> {
            UsuarioDAO.delete(3);
            Usuario usuario = UsuarioDAO.find(3);
            assertNull(usuario);
        });
    }
    // Test para comprobar el funcionamiento de buscar  un usuario con un id que no existe
    @Test
    public void UsuarioBuscarNoExiste() {
        jpa.withTransaction(() -> {
            Usuario usuario = UsuarioDAO.find(4);
            assertNull(usuario);
        });
    }
}
