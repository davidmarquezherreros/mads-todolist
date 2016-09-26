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
      Logger.debug("Usuario: "+ UsuarioDAO.find(usuario.id));
      return UsuarioDAO.update(UsuarioDAO.find(usuario.id));
    }

    public static Usuario findUsuario(String id){
        return UsuarioDAO.find(id);
    }

    public static boolean deleteUsuario(String id) {
        // ToDo
        return false;
    }

    public static List<Usuario> findAllUsuarios() {
        List<Usuario> lista = UsuarioDAO.findAll();
        Logger.debug("Numero de usuarios: " + lista.size());
        return lista;
    }
}
