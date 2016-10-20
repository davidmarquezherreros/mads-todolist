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
}
