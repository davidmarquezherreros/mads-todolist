package services;

import java.util.List;
import java.util.ArrayList;

import models.*;

public class TareasService {

    public static List<Tarea> listaTareasUsuario(Integer usuarioId) {
        Usuario usuario = UsuarioDAO.find(usuarioId);
        if (usuario != null) {
            return usuario.tareas;
        } else {
            throw new UsuariosException("Usuario no encontrado");
        }
    }
    public static Tarea grabaTarea(Tarea t){
      return TareaDAO.create(t);
    }
    public static Tarea grabaTareaUsuario(Tarea t){
      return TareaDAO.createTareaUsuario(t);
    }
    public static boolean deleteTarea(Integer id){
      if(TareaDAO.find(id)!=null){
          TareaDAO.delete(id);
          if(TareaDAO.find(id)==null){
             return true;
          }else{
            return false;
          }
      }
      else{
        return false;
      }
    }
    public static Tarea findTarea(Integer id){
      return TareaDAO.find(id);
    }
    public static Tarea updateTarea(Tarea t){
      return TareaDAO.update(t);
    }
}
