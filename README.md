###### mads-todolist
#### **TICKETS**
El tablero con los tickets se puede encontrar [aqui](https://trello.com/b/YeGc58bU/todolist-tickets-david-marquez-herreros).
  1. TIC-1 Página Home con saludo
  2. TIC-2 Incluir bootstrap
  3. TIC-3 Página crear usuario
  4. TIC-4 Página listado de usuarios
  5. TIC-5 Página detalle de un usuario
  6. TIC-6 Página editar un usuario
  7. TIC-7 Página borrar un usuario
  8. TIC-8 Breve documentacion del repositorio
  9. TIC-9 Pantalla registro de usuarios
  10. TIC-10 Pantalla login usuarios
  11. TIC-11 Mejoras en la apariencia
  
#### **TIC-1 Página Home con saludo**
  En este ticket se creo una pagina muy basica siguiendo las explicaciones de la practica guiada.
#### **TIC-2 Incluir Bootstrap**
  Para añadir Bootstrap, me fui a la pagina de [descarga](http://getbootstrap.com/getting-started/#download) y descargue la primera opcion.
  Una vez que estaba descargada me fui a mi proyecto de play y copie la carpeta en la carpeta "public".
#### **TIC-3 Página crear usuario**
  Para hacer la pagina crear usuario hice lo siguiente:
    1. Cree el formulario [formCreacionUsuario.scala.html](https://github.com/davidmarquezherreros/mads-todolist/blob/master/app/views/formCreacionUsuario.scala.html), este formulario contiene los campos necesarios para crear un usuario.
    
    2. Puse una nueva ruta en el fichero /conf/routes para los metodos GET y POST
    3. Hice las funciones que devuelven el formulario de creacion y la funcion que controla el evento post.
    4. Cree las siguientes clases: models/UsuarioDAO.java , models/Usuario.java y services/UsuarioService.java
#### **TIC-4 Página listado de usuarios**
  En este ticket se creo una pagina para listar todos los usuarios en la aplicacion para hacer esta pagina segui el ejempo del codigo de ayuda.
#### **TIC-5 Página detalle de un usuario**
  Para hacer la pagina detalle usuario hice lo siguiente:
    1. Cree una nueva pagina [DetalleUsuario.scala.html](https://github.com/davidmarquezherreros/mads-todolist/blob/master/app/views/DetalleUsuario.scala.html)
    
    2. Puse una nueva ruta en el fichero /conf/routes para el metodo GET.
    3. Hice la funcion que devuelve los datos de un usuario a partir de su identificador.
    4. Agregue la funcion findUsuario en services/UsuarioService.java
#### **TIC-6 Página editar un usuario**
  Para hacer la pagina editar usuario hice lo siguiente:
    1. Cree un nuevo formulario [formModificacionUsuario.scala.html](https://github.com/davidmarquezherreros/mads-todolist/blob/master/app/views/formModificacionUsuario.scala.html), este formulario contiene los datos del usuario que se quiere modificar.
    2. Para rellenar el formulario segui el tutorial de play utilizando el metodo fill:
              Form<Usuario> usuarioForm = formFactory.form(Usuario.class).bindFromRequest();
              Usuario user = new Usuario();
              user = UsuariosService.findUsuario(id);
              Form<Usuario> filledForm = usuarioForm.fill(user);
              return ok(formModificacionUsuario.render(filledForm,""));
#### **TIC-7 Página borrar un usuario**
#### **TIC-8 Breve documentacion del repositorio**
#### **TIC-9 Pantalla registro de usuarios**
#### **TIC-10 Pantalla login usuarios**
#### **TIC-11 Mejoras en la apariencia**
