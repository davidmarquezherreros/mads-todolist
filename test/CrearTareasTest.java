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
import services.*;
import java.util.*;

public class CrearTareasTest {

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
          FileInputStream("test/resources/tareas_dataset.xml"));
          databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
          databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
          databaseTester.setDataSet(initialDataSet);
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
      public void crearTarea(){
        jpa.withTransaction(() -> {
          Tarea t = new Tarea("tarea de prueba");
          Tarea tarea = TareaDAO.create(t);
          Tarea buscar = TareaDAO.find(tarea.id);
          assertNotNull(buscar);
        });
      }
      // Crear una tarea asignandosela a un Usuario
      @Test
      public void crearTareaUsuario(){
        jpa.withTransaction(() ->{
          Usuario usuario = UsuarioDAO.find(1);
          Tarea tarea = new Tarea(usuario, "TEST 2");
          TareaDAO.createTareaUsuario(tarea);
          List<Tarea> lista = usuario.tareas;
          assertEquals(4,lista.size());
          assertTrue(lista.contains(tarea));
        });
      }
      @Test
      public void createTareaService(){
        jpa.withTransaction(() -> {
          Tarea t = new Tarea("tarea sin usuario");
          Tarea tarea = TareasService.grabaTarea(t);
          assertNotNull(TareaDAO.find(tarea.id));
        });
      }
      @Test
      public void createTareaUsuarioService(){
        jpa.withTransaction(() -> {
          Tarea t = new Tarea(UsuarioDAO.find(1),"tarea sin usuario");
          Tarea tarea = TareasService.grabaTareaUsuario(t);
          assertNotNull(TareaDAO.find(tarea.id));
        });
      }
}
