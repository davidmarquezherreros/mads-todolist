# **mads-todolist**



### **DOCUMENTACIÓN DE USUARIO**
#### **FUNCIONALIDADES**
##### **REGISTRO**
Para registrarse en la pagina web el usuario tendrá que completar el siguiente formulario:

Los únicos campos obligatorios son el "Login" y los campos de la contraseña.
##### **INICIO DE SESIÓN**
##### **DAR DE ALTA USUARIOS**
##### **DAR DE BAJA USUARIOS**
##### **EDITAR USUARIOS**
##### **VER DATOS USUARIO**
##### **MODIFICAR USUARIO**


### **DOCUMENTACIÓN DE DESARROLLADOR**
#### **TICKETS**
El tablero con los tickets se puede encontrar [aquí](https://trello.com/b/YeGc58bU/todolist-tickets-david-marquez-herreros).
  1. TIC-1 Página Home con saludo
  2. TIC-2 Incluir bootstrap
  3. TIC-3 Página crear usuario
  4. TIC-4 Página listado de usuarios
  5. TIC-5 Página detalle de un usuario
  6. TIC-6 Página editar un usuario
  7. TIC-7 Página borrar un usuario
  8. TIC-8 Breve documentación del repositorio
  9. TIC-9 Pantalla registro de usuarios
  10. TIC-10 Pantalla login usuarios
  11. TIC-11 Mejoras en la apariencia
  
##### **TIC-1 Página Home con saludo**
  En este ticket se creo una pagina muy básica siguiendo las explicaciones de la practica guiada.
##### **TIC-2 Incluir Bootstrap**
  Para añadir Bootstrap, me fui a la pagina de [descarga](http://getbootstrap.com/getting-started/#download) y descargue la primera opción.
  Una vez que estaba descargada me fui a mi proyecto de play y copie la carpeta en la carpeta "public".
##### **TIC-3 Página crear usuario**
  Para hacer la pagina crear un usuario hice lo siguiente:
   1. Cree el formulario formCreacionUsuario.scala.html, este formulario contiene los campos necesarios para crear un usuario.
   2. Puse una nueva ruta en el fichero /conf/routes para los métodos GET y POST.
   3. Hice las funciones que devuelven el formulario de creación y la función que controla el evento post.
   4. Cree las siguientes clases: models/UsuarioDAO.java , models/Usuario.java y services/UsuarioService.java.

##### **TIC-4 Página listado de usuarios**
En este ticket se creo una pagina para listar todos los usuarios en la aplicación para hacer esta pagina seguí el ejemplo del código de ayuda.

##### **TIC-5 Página detalle de un usuario**
  Para hacer la pagina detalle de un usuario hice lo siguiente:
  1. Cree una nueva pagina DetalleUsuario.scala.html.
  2. Puse una nueva ruta en el fichero /conf/routes para el metodo GET.
  3. Hice la función que devuelve los datos de un usuario a partir de su identificador.
```java
        public Result detalleUsuario(String id) {
	        return ok(DetalleUsuario.render(UsuariosService.findUsuario(id)));
	    }
```
4. Agregue la función findUsuario en services/UsuarioService.java.

##### **TIC-6 Página editar un usuario**
  Para hacer la pagina editar usuario hice lo siguiente:
    1. Cree un nuevo formulario formModificacionUsuario.scala.html, este formulario contiene los datos del usuario que se quiere modificar.
    2. Para rellenar el formulario seguí el tutorial de play utilizando el metodo fill:
```java
    public Result editaUsuario(String id) {
        Form<Usuario> usuarioForm = formFactory.form(Usuario.class).bindFromRequest();
	        Usuario user = new Usuario();
	        user = UsuariosService.findUsuario(id);
	        Form<Usuario> filledForm = usuarioForm.fill(user);
	        return ok(formModificacionUsuario.render(filledForm,""));
	    }
```
3. Cuando ya tenemos los datos del usuario a modificar se llama a la función grabaUsuarioModificado. La función grabaUsuarioModificado hace lo siguiente:
```java
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
```
Como se puede observar en el fragmento de código anterior la funcion grabaUsuarioModificado llama a la función modificaUsuario para que gestione la modificación del usuario en la "base de datos".
  4. La función modificaUsuario a se encarga de llamar a UsuarioDAO.update que modifica el usuario en la "base de datos".
  
##### **TIC-7 Página borrar un usuario**
  Para hacer la pagina borrar un usuario hice lo siguiente:
	  1. Me descargue y añadí el fichero jquery.jmin.js, para que funcionase el script de borrado del código de ayuda.
	  2. Modifique el fichero /services/UsuariosService.java para que la función deleteUsuario llamase a UsuarioDAO y devolviese verdadero si había borrado el usuario y falso si no.
```java
	    public static boolean deleteUsuario(String id) {
        UsuarioDAO.delete(id);
        if(UsuarioDAO.find(id)==null){
              return true;
        }else{
          return false;
          }
	  }
```
El fragmento de código anterior muestra las modificaciones realizadas en el fichero /services/UsuariosService.java
    
##### **TIC-8 Breve documentacion del repositorio**
  Modifique el archivo [README.md](https://github.com/davidmarquezherreros/mads-todolist/blob/master/README.md)
##### **TIC-9 Pantalla registro de usuarios**
Para hacer la pantalla de registro de usuarios  hice lo siguiente:
1. Cree el formulario formCreacionUsuario, este formulario contiene los campos necesarios para registrar un usuario (son los mismo que para crear un usuario salvo que en este formulario se le pide una contraseña al usuario).
2. Puse una nueva ruta en el fichero conf/routes para los eventos GET y POST.
3. El evento get esta controlado por la siguiente función:
```java
    public Result RegistroNuevoUsuario(){
      return ok(formRegistroUsuario.render(formFactory.form(Usuario.class),""));
    }
```
4. El evento post esta controlado por la siguiente función:
```java
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
```
El fragmento de código anterior hace las siguientes comprobaciones:
- Si las contraseñas introducidas no coinciden, salta un mensaje de error diciendo que las contraseñas no coinciden forzando a volver a escribirlas.
- Si el usuario ya tiene contraseña, salta un mensaje diciendo que para cambiarla tiene que ponerse en contacto con el administrador.
- Si el usuario esta registrado sin contraseña y todos los campos son correctos, la contraseña se modifica y salta un mensaje indicando que todo fue un éxito.
- Si el usuario no esta registrado previamente por el administrador, se dará de alta un nuevo usuario y saltara un mensaje indicando que el registro fue un éxito.

##### **TIC-10 Pantalla login usuarios**
Para hacer la pantalla de login de usuarios  hice lo siguiente:
1. Cree el formulario formLoginUsuario, este formulario contiene los campos necesarios para iniciar sesion con un usuario.
2. Puse una nueva ruta en el fichero conf/routes para los eventos GET y POST.
3. El evento get esta controlado por la siguiente función:
```java
    public Result LoginUsuario(){
      return ok(formLoginUsuario.render(formFactory.form(Usuario.class),""));
    }
```
4. El evento post esta controlado por la siguiente función:
```java
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
      if(userDB.size()>0){
        return ok(saludo.render(usuario.login));
      }
      else{
        return badRequest(formLoginUsuario.render(usuarioForm, "Error en la contraseña / login"));
      }
    }
```
El fragmento de código anterior hace las siguientes comprobaciones:
- Comprueba si hay errores en el formulario, si los hubiese salta un mensaje de error indicandolo.
- Comprueba si las credenciales introducidas son correctas, si lo son la aplicación web te redirige a una pagina con un saludo.
- Si las credenciales no son correctas aparece un mensaje indicandolo

##### **TIC-11 Mejoras en la apariencia**
Se han hecho mejoras en el código html que utiliza la aplicación para que sea mas atractiva al usuario.

