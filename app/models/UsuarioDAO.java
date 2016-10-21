package models;

import play.*;
import play.mvc.*;
import play.db.jpa.*;
import javax.persistence.*;
import java.util.List;
import java.util.Date;

public class UsuarioDAO {
    public static Usuario create (Usuario usuario) {
        usuario.nulificaAtributos();
        JPA.em().persist(usuario);
        // Hacemos un flush y un refresh para asegurarnos de que se realiza
        // la creación en la BD y se devuelve el id inicializado
        JPA.em().flush();
        JPA.em().refresh(usuario);
        Logger.debug(usuario.toString());
        return usuario;
    }

    public static Usuario find(Integer idUsuario) {
        return JPA.em().find(Usuario.class, idUsuario);
    }

    public static Usuario update(Usuario usuario) {
        return JPA.em().merge(usuario);
    }

    public static void delete(Integer idUsuario) {
        Usuario usuario = JPA.em().getReference(Usuario.class, idUsuario);
        List<Tarea> lista = usuario.tareas;
        if(lista.size()>0){
          for(int i = 0; i < lista.size(); i++){
            TareaDAO.delete(lista.get(i).id);
          }
        }
        JPA.em().remove(usuario);
    }

    public static List<Usuario> findAll() {
        TypedQuery<Usuario> query = JPA.em().createQuery(
                  "select u from Usuario u ORDER BY id", Usuario.class);
        return query.getResultList();
    }

    public static Usuario findUsuarioLogin(String LoginUsuario){
      TypedQuery<Usuario> query = JPA.em().createQuery("select u from Usuario AS u WHERE u.login='"+LoginUsuario+"'",Usuario.class);
      List<Usuario> resultado = query.getResultList();
      Usuario u = null;
      if(resultado.size()>0){
        u = resultado.get(0);
      }
      return u;
    }
    public static List<Usuario> Login(String LoginUsuario, String password){
      TypedQuery<Usuario> query = JPA.em().createQuery("select u from Usuario u WHERE u.login='"+LoginUsuario+"' AND u.password='"+password+"'",Usuario.class);
      return query.getResultList();
    }
}
