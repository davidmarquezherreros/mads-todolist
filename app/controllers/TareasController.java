package controllers;

import java.util.List;
import javax.inject.*;

import play.*;
import play.mvc.*;
import views.html.*;
import static play.libs.Json.*;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.*;

import services.*;
import models.*;

public class TareasController extends Controller {

    @Inject FormFactory formFactory;

    @Transactional(readOnly = true)
    public Result listaTareas(Integer usuarioId){
      List<Tarea> tareas = null;

      try{
        tareas = TareasService.listaTareasUsuario(usuarioId);
      }catch(UsuariosException ex){
      }
      if(tareas != null){
        return ok(listaTareas.render(usuarioId, tareas, "Tareas del usuario "+UsuariosService.findUsuario(usuarioId).login));
      }
      else{
        return ok(listaUsuarios.render(UsuariosService.findAllUsuarios(), "El usuario con id: "+usuarioId+" no tiene tareas"));
      }
    }
    @Transactional(readOnly=true)
    public Result formularioNuevaTarea(Integer usuarioId) {
      if(UsuarioDAO.find(usuarioId) != null){
          return ok(formCreacionTarea.render(Form.form(Tarea.class),usuarioId,""));
      }
      else{
        return badRequest(listaUsuarios.render(UsuariosService.findAllUsuarios(),"El id especificado no existe."));
      }
    }
    @Transactional
    public Result grabaNuevaTarea(Integer usuarioId){
      if(UsuarioDAO.find(usuarioId) == null){
        return badRequest(listaUsuarios.render(UsuariosService.findAllUsuarios(),"El id especificado no existe."));
      }
      else{
        Form<Tarea> tareaForm = formFactory.form(Tarea.class).bindFromRequest();
        if(tareaForm.hasErrors()){
          return badRequest(formCreacionTarea.render(tareaForm,usuarioId,"Hay errores en el formulario"));
        }
        else{
          Tarea tarea = tareaForm.get();
          Usuario user = null;
          try{
            user = UsuariosService.findUsuario(usuarioId);
          }catch(UsuariosException ex){

          }
          if(user != null){
            Tarea t = new Tarea(user, tarea.descripcion);
            Tarea added = TareasService.grabaTareaUsuario(t);
            return ok(listaTareas.render(usuarioId, TareasService.listaTareasUsuario(usuarioId), "Tareas del usuario "+UsuariosService.findUsuario(usuarioId).login));
          }
          else{
            return badRequest(formCreacionTarea.render(tareaForm,usuarioId,"Ups! usuario no encontrado intentelo de nuevo!"));
          }
        }
      }
    }
    @Transactional
    public Result borraTarea(Integer idTarea, Integer idUsuario) {
      TareasService.deleteTarea(idTarea);
      return redirect(controllers.routes.TareasController.listaTareas(idUsuario));
    }
    @Transactional
    public Result detalleTarea(Integer id, Integer usuarioId) {
        if(TareasService.findTarea(id)!=null){
          return ok(DetalleTarea.render(TareasService.findTarea(id)));
        }
        else{
          return ok(listaTareas.render(usuarioId, TareasService.listaTareasUsuario(usuarioId), "Ups! el identificador especificado no existe"));
        }
    }
    @Transactional(readOnly = true)
    public Result editaTarea(Integer idTarea, Integer idUsuario) {
      if(UsuariosService.findUsuario(idUsuario)!=null && TareasService.findTarea(idTarea)!=null){
        Form<Tarea> tareaForm = formFactory.form(Tarea.class).bindFromRequest();
        Logger.debug(tareaForm.toString());
        Tarea t = new Tarea();
        t = TareasService.findTarea(idTarea);
        Form<Tarea> filledForm = tareaForm.fill(t);
        return ok(formModificacionTarea.render(filledForm,t.usuario.id,""));
      }
      else{
        return badRequest(listaTareas.render(idUsuario, TareasService.listaTareasUsuario(idUsuario),"La tarea con id: "+idTarea.toString()+" no existe!"));
      }
    }
        @Transactional
    public Result grabaTareaModificada(Integer idUsuario){
            Form<Tarea> tareaForm = formFactory.form(Tarea.class).bindFromRequest();
            if (tareaForm.hasErrors()) {
                return badRequest(formModificacionTarea.render(tareaForm, idUsuario, "Hay errores en el formulario"));
            }
            Tarea tarea = tareaForm.get();
            tarea.usuario = UsuariosService.findUsuario(idUsuario);
            tarea = TareasService.updateTarea(tarea);

            return redirect(controllers.routes.TareasController.listaTareas(idUsuario));
    }

}
