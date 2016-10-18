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
        if(UsuariosService.findUsuarioLogin(usuario.login) != null){
          return ok(formCreacionUsuario.render(formFactory.form(Usuario.class),"Error, el login ya existe"));
        }else{
          usuario = UsuariosService.grabaUsuario(usuario);
          flash("grabaUsuario", "El usuario se ha guardado correctamente");
          return redirect(controllers.routes.UsuariosController.listaUsuarios());
        }
    }

    @Transactional
    public Result grabaUsuarioModificado() {

      Form<Usuario> usuarioForm = formFactory.form(Usuario.class).bindFromRequest();
      if (usuarioForm.hasErrors()) {
          return badRequest(formModificacionUsuario.render(usuarioForm, "Hay errores en el formulario"));
      }
      Usuario usuario = usuarioForm.get();

      usuario = UsuariosService.modificaUsuario(usuario);
      if(usuario.nombre != "empty"){
        flash("grabaUsuario", "El usuario se ha modificado correctamente");
      }
      else{
        flash("grabaUsuario", "No se puede modificar el login");
      }

      return redirect(controllers.routes.UsuariosController.listaUsuarios());
    }

    @Transactional
    public Result detalleUsuario(Integer id) {
        if(UsuariosService.findUsuario(id)!=null){
          return ok(DetalleUsuario.render(UsuariosService.findUsuario(id)));
        }
        else{
          return saludo(", el identificador "+id+" no existe!");
        }
    }

    @Transactional
    public Result editaUsuario(Integer id) {
      if(UsuariosService.findUsuario(id)!=null){
        Form<Usuario> usuarioForm = formFactory.form(Usuario.class).bindFromRequest();
        Logger.debug(usuarioForm.toString());
        Usuario user = new Usuario();
        user = UsuariosService.findUsuario(id);
        Form<Usuario> filledForm = usuarioForm.fill(user);
        return ok(formModificacionUsuario.render(filledForm,""));
      }
      else{
        return saludo(", el identificador "+id+" no existe!");
      }
    }

    @Transactional
    public Result borraUsuario(Integer id) {
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
        Usuario userDB = UsuariosService.findUsuarioLogin(usuario.login);
        Logger.debug(userDB.toString());
        if(userDB != null){
          Logger.debug("El usuario ya existe en la base de datos");
          Usuario aux = userDB;
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
    @Transactional
    public Result LoginUsuario(){
      return ok(formLoginUsuario.render(formFactory.form(Usuario.class),""));
    }
    @Transactional
    public Result checkLoginUsuario(){
      Form<Usuario> usuarioForm = formFactory.form(Usuario.class).bindFromRequest();
      if (usuarioForm.hasErrors()) {
          return badRequest(formLoginUsuario.render(usuarioForm, "Hay errores en el formulario"));
      }
      Usuario usuario = usuarioForm.get();
      if(usuario.password.isEmpty()==true){
        return badRequest(formLoginUsuario.render(usuarioForm, "Introduzca la contraseña, si no tiene vaya a registro"));
      }
      List<Usuario> userDB = UsuariosService.Login(usuario.login,usuario.password);
      Logger.debug(userDB.toString());
      if(userDB.size()>0){
        return ok(saludo.render(usuario.login));
      }
      else{
        return badRequest(formLoginUsuario.render(usuarioForm, "Error en la contraseña / login"));
      }
    }
    // Mensaje de error
    public Result saludo(String nombre){
      return ok(saludo.render(nombre));
    }
}
