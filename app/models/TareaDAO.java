package models;

import play.*;
import play.mvc.*;
import play.db.jpa.*;
import javax.persistence.*;

public class TareaDAO {
    public static Tarea find(Integer idTarea) {
        return JPA.em().find(Tarea.class, idTarea);
    }
    public static Tarea create (Tarea t) {
        JPA.em().persist(t);
        // Hacemos un flush y un refresh para asegurarnos de que se realiza
        // la creación en la BD y se devuelve el id inicializado
        JPA.em().flush();
        JPA.em().refresh(t);
        return t;
    }
    public static Tarea createTareaUsuario(Tarea t){
      t.nulificaAtributos();
      JPA.em().persist(t);
      // Hacemos un flush y un refresh para asegurarnos de que se realiza
      // la creación en la BD y se devuelve el id inicializado
      JPA.em().flush();
      JPA.em().refresh(t);
      Logger.debug(t.toString());
      return t;
    }
    public static void delete(Integer idTarea) {
        Tarea tarea = JPA.em().getReference(Tarea.class, idTarea);
        Logger.debug(tarea.toString());
        JPA.em().remove(tarea);
        JPA.em().flush();
        JPA.em().refresh(tarea.usuario);
    }
}
