package edu.proyectofinal.integradorrs.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import edu.proyectofinal.integradorrs.exceptions.EmptyResultException;
import edu.proyectofinal.integradorrs.model.Token;
import edu.proyectofinal.integradorrs.model.Usuario;
import edu.proyectofinal.integradorrs.services.usuario.UsuarioService;
import edu.proyectofinal.integradorrs.services.usuario.impl.UsuarioServiceMongo;

@RestController

@RequestMapping("/api/usuario")
public class LoginController extends AbstractController<Usuario> {

	
	
	@Autowired
    private UsuarioServiceMongo usuarioService;
	
	 @RequestMapping(method = RequestMethod.GET, value="/greeting")
	    public String greeting() {

		 return "Hello"+usuarioService.getById("1").getUsername();

	    }
	 
	  @RequestMapping(method = RequestMethod.GET, value="/details")
	    public String details() {

		  return usuarioService.getById("1").toString();
 
	    }


	  //atentos al problema de seguridad con esta funcion
	  
    @RequestMapping(method = RequestMethod.GET, path="/getall")
    public ResponseEntity<Collection<Usuario>> getAll() {

        System.out.println("getAll");

        Collection<Usuario> usuarios = usuarioService.getAllUsuarios();

        return super.collectionResult(usuarios);

    }

   // fix http://stackoverflow.com/questions/3526523/spring-mvc-pathvariable-getting-truncated
    @RequestMapping(method = RequestMethod.GET, value = "/email/{email:.+}")
    public ResponseEntity<Usuario> getByEmail(@Validated @PathVariable("email") String email) {

        System.out.println("getByEmail");
        System.out.println("Email: " + email);

        Usuario usuario = usuarioService.getByEmail(email);

        if (null == usuario) {
            throw new EmptyResultException(Usuario.class);
        }
        return super.singleResult(usuario);

    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/token/twitter/email/{email:.+}")
    public ResponseEntity<Token> getTokenByEmail(@Validated @PathVariable("email") String email, String socialnetwork) {

        Token token = new Token();
        System.out.println("getTokenByEmail");
        System.out.println("Email: " + email);
        System.out.println("Social Network: " + socialnetwork);

       Collection<Token> tokens = usuarioService.getTokenByEmail(email);
       for(Token atoken: tokens)
       {
           if(atoken.getSocialnetwork().equals(socialnetwork))
           {
               token = atoken;
           }
       }
       
        if (null == token) {
            throw new EmptyResultException(Usuario.class);
        }
        return super.singleResult(token);

    }
    


    @RequestMapping(method = RequestMethod.GET, value = "/id/{id}")
    public ResponseEntity<Usuario> getById(@Validated @PathVariable("id") String id) {

        System.out.println("getById");
        System.out.println("Id: " + id);

        Usuario usuario = usuarioService.getById(id);
        if (null == usuario) {
            throw new EmptyResultException(Usuario.class);
        }
        return super.singleResult(usuario);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Usuario> save(@RequestBody Usuario usuario) {

        usuarioService.save(usuario);

        return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);

    }
    
    @RequestMapping(method = RequestMethod.POST, value="/savetoken")
    public ResponseEntity<Token> saveToken(@RequestBody Token token) {

        usuarioService.saveToken(token);

        return new ResponseEntity<Token>(token, HttpStatus.OK);

    }
    
@ResponseBody @RequestMapping(method = RequestMethod.POST, value="/newuser")
    public ResponseEntity<Void> newuser(@RequestBody Usuario user, UriComponentsBuilder ucBuilder) {

	  if (usuarioService.userExists(user)) {
          System.out.println("A User with name " + user.getUsername() + " already exist");
          return new ResponseEntity<Void>(HttpStatus.CONFLICT);
      }
	  
        usuarioService.saveUser(user);
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);

    }


}
