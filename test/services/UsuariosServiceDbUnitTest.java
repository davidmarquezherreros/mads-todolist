package services;

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

public class UsuariosServiceDbUnitTest {

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
        databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);

        // Definimos como operación SetUp CLEAN_INSERT, que hace un
        // DELETE_ALL de todas las tablase del dataset, seguido por un
        // INSERT. (http://dbunit.sourceforge.net/components.html)
        // Es lo que hace DbUnit por defecto, pero así queda más claro.
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);

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
    public void actualizaUsuarioLanzaExcepcionSiLoginYaExiste() {
      jpa.withTransaction(() -> {
        Usuario usuario = UsuariosService.findUsuario(2);

        // Copiamos el objeto usuario para crear un objeto igual
        // pero desconectado de la base de datos. De esta forma,
        // cuando modificamos sus atributos JPA no actualiza la
        // base de datos.

        Usuario desconectado = usuario.copy();

        // Cambiamos el login por uno ya existente
        desconectado.login = "juan";

        try {
            UsuariosService.modificaUsuario(desconectado);
            fail("No se ha lanzado excepción login ya existe");
        } catch (UsuariosException ex) {
        }
    });
  }
  // test que comprueba buscar un usuario con un id que no existe
  @Test
  public void buscarIdNoExiste(){
    jpa.withTransaction(() ->{
      try{
        UsuariosService.findUsuario(99);
        fail("No se ha lanzado la excepcion Id no existe");
      } catch(UsuariosException ex){

      }
    });
  }
  // test que comprueba buscar por un login que no existe
  @Test
  public void buscarLoginNoExiste(){
    jpa.withTransaction(() -> {
      try{
        UsuariosService.findUsuarioLogin("pepitogrillo");
        fail("No se ha lanzado la excepcion login no encontrado");
      }catch(UsuariosException ex){

      }
    });
  }
  // test que comprueba login password
  @Test
  public void ComprobarCredencialesNoExisten(){
    jpa.withTransaction(() -> {
      try{
        UsuariosService.Login("pepitogrillo","grillo");
        fail("No se ha lanzado la excepcion credenciales erroneas");
      } catch(UsuariosException ex){
      }
    });
  }
  // test que comprueba borrar un id que no existe
  @Test
  public void BorrarIdNoExiste(){
    jpa.withTransaction(() -> {
      try{
        UsuariosService.deleteUsuario(100);
        fail("No se ha lanzado la excepcion id a borrar no existe");
      }catch(UsuariosException ex){

      }
    });
  }
}
