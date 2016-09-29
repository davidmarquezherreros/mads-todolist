package services;

import play.*;
import play.mvc.*;
import play.db.jpa.*;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

import models.*;

public class UsuariosService {
    public static Usuario grabaUsuario(Usuario usuario) {
        return UsuarioDAO.create(usuario);
    }

    public static Usuario modificaUsuario(Usuario usuario) {
      Logger.debug("UsuarioService modificar: "+ usuario);
      return UsuarioDAO.update(usuario);
    }

    public static Usuario findUsuario(String id){
        return UsuarioDAO.find(id);
    }

    public static boolean deleteUsuario(String id) {
        Logger.debug("Usuario a borrar: " + UsuarioDAO.find(id).toString());
         UsuarioDAO.delete(id);
        if(UsuarioDAO.find(id)==null){
              return true;
        }else
          return false;
    }

    public static List<Usuario> findAllUsuarios() {
        List<Usuario> lista = UsuarioDAO.findAll();
        Logger.debug("Numero de usuarios: " + lista.size());
        return lista;
    }
}
