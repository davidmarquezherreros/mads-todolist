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

public class ModificarTareasTest {

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
      public void ModificarTarea(){
        jpa.withTransaction(() -> {
          Tarea tarea = TareaDAO.find(1);
          tarea.descripcion = "Test modificar tarea";
          TareaDAO.update(tarea);
          Usuario u = UsuarioDAO.find(1);
          assertTrue(u.tareas.contains(tarea));
        });
      }
      @Test
      public void ModificarTareaService(){
        jpa.withTransaction(() -> {
          Tarea tarea = TareaDAO.find(1);
          tarea.descripcion = "Test modificar tarea Services";
          TareaServices.updateTarea(tarea);
          Usuario u = UsuarioDAO.find(1);
          assertTrue(u.tareas.contains(tarea));
        });
      }
}
