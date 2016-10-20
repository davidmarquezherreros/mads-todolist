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
      Usuario existente = UsuarioDAO.findUsuarioLogin(usuario.login);
      if (existente != null && existente.id != usuario.id)
          throw new UsuariosException("Login ya existente: " + usuario.login);
      UsuarioDAO.update(usuario);
      return usuario;
    }

    public static Usuario findUsuario(Integer id){
      Usuario buscar = UsuarioDAO.find(id);
        if(buscar != null){
          return buscar;
        }
        else{
          throw new UsuariosException("Id no encontrado: "+ id);
        }
    }

    public static boolean deleteUsuario(Integer id) {
        if(UsuarioDAO.find(id)!=null){
            UsuarioDAO.delete(id);
            if(UsuarioDAO.find(id)==null){
               return true;
            }else{
              return false;
            }
        }
        else{
          throw new UsuariosException("El id a borrar no existe: "+id);
        }
    }

    public static List<Usuario> findAllUsuarios() {
        List<Usuario> lista = UsuarioDAO.findAll();
        Logger.debug("Numero de usuarios: " + lista.size());
        return lista;
    }

    public static Usuario findUsuarioLogin(String login){
      Usuario user = UsuarioDAO.findUsuarioLogin(login);
      if(user != null){
        return user;
      }
      else{
        throw new UsuariosException("Login no encontrado: " + login);
      }
    }
    public static List<Usuario> Login(String login,String password){
      List<Usuario> lista = UsuarioDAO.Login(login,password);
      if(lista.size()>0){
        return lista;
      }
      else{
        throw new UsuariosException("Credenciales erroneas: "+login +":"+password);
      }
    }
}
