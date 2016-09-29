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

public class UsuariosController extends Controller {

    @Inject FormFactory formFactory;

    @Transactional(readOnly = true)
    // Devuelve una página con la lista de usuarios
    public Result listaUsuarios() {
      // Obtenemos el mensaje flash guardado en la petición
      // por el controller grabaUsuario
      String mensaje = flash("grabaUsuario");
      List<Usuario> usuarios = UsuariosService.findAllUsuarios();
      return ok(listaUsuarios.render(usuarios, mensaje));
    }

    // Devuelve un formulario para crear un nuevo usuario
    public Result formularioNuevoUsuario() {
        return ok(formCreacionUsuario.render(formFactory.form(Usuario.class),""));
    }

    @Transactional
    // Añade un nuevo usuario en la BD y devuelve código HTTP
    // de redirección a la página de listado de usuarios
    public Result grabaNuevoUsuario() {

        Form<Usuario> usuarioForm = formFactory.form(Usuario.class).bindFromRequest();
        if (usuarioForm.hasErrors()) {
            return badRequest(formCreacionUsuario.render(usuarioForm, "Hay errores en el formulario"));
        }
        Usuario usuario = usuarioForm.get();
        Logger.debug("Usuario a grabar: " + usuario.toString());
        usuario = UsuariosService.grabaUsuario(usuario);
        flash("grabaUsuario", "El usuario se ha guardado correctamente");
        return redirect(controllers.routes.UsuariosController.listaUsuarios());
    }

    @Transactional
    public Result grabaUsuarioModificado() {
      Form<Usuario> usuarioForm = formFactory.form(Usuario.class).bindFromRequest();
      if (usuarioForm.hasErrors()) {
          return badRequest(formCreacionUsuario.render(usuarioForm, "Hay errores en el formulario"));
      }
      Usuario usuario = usuarioForm.get();
      usuario = UsuariosService.modificaUsuario(usuario);
      flash("grabaUsuario", "El usuario se ha modificado correctamente");
      return redirect(controllers.routes.UsuariosController.listaUsuarios());
    }

    @Transactional
    public Result detalleUsuario(String id) {
        Logger.debug("Usuario: " + UsuarioDAO.find(id).toString());
        return ok(DetalleUsuario.render(UsuariosService.findUsuario(id)));
    }

    @Transactional
    public Result editaUsuario(String id) {
        Form<Usuario> usuarioForm = formFactory.form(Usuario.class).bindFromRequest();
        Logger.debug(usuarioForm.toString());
        Usuario user = new Usuario();
        user = UsuariosService.findUsuario(id);
        Form<Usuario> filledForm = usuarioForm.fill(user);
        return ok(formModificacionUsuario.render(filledForm,""));
    }

    @Transactional
    public Result borraUsuario(String id) {
        UsuariosService.deleteUsuario(id);
        Logger.debug("Usuario a borrar: "+UsuariosService.findUsuario(id));
        return redirect(controllers.routes.UsuariosController.listaUsuarios());
    }

    // Funcionalidades extra
    @Transactional
    public Result RegistroNuevoUsuario(){
      return ok(formRegistroUsuario.render(formFactory.form(Usuario.class),""));
    }
    @Transactional
    public Result grabaRegistroNuevoUsuario(){
      Form<Usuario> usuarioForm = formFactory.form(Usuario.class).bindFromRequest();
      if (usuarioForm.hasErrors()) {
          return badRequest(formRegistroUsuario.render(usuarioForm, "Hay errores en el formulario"));
      }
      Usuario usuario = usuarioForm.get();
      int expresion = usuario.password.compareTo(usuario.repetirpassword);
      boolean passwordOK = true;
      if(expresion != 0){
        passwordOK = false;
      }
      if(passwordOK == false){
        return badRequest(formRegistroUsuario.render(usuarioForm, "Las contraseñas no coinciden"));
      }
      else{
        List<Usuario> userDB = UsuariosService.findUsuarioLogin(usuario.login);
        Logger.debug(userDB.toString());
        if(userDB.size()>0){
          Logger.debug("El usuario ya existe en la base de datos");
          Usuario aux = userDB.get(0);
          if(aux.password == null){
            aux.password = usuario.password;
            UsuariosService.modificaUsuario(aux);
          }
          else{
            return badRequest(formRegistroUsuario.render(usuarioForm, "Ya existe este usuario contacte con el administrador para mas informacion"));
          }
        }
        else{
          Logger.debug("El usuario no existe en la base de datos");
          UsuariosService.grabaUsuario(usuario);
        }
      }
      return ok(formRegistroUsuario.render(usuarioForm,"El registro fue un exito"));
    }
}
