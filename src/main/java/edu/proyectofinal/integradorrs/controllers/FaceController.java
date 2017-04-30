package edu.proyectofinal.integradorrs.controllers;


import edu.proyectofinal.integradorrs.Adapters.Adapter;
import edu.proyectofinal.integradorrs.model.Post;
import edu.proyectofinal.integradorrs.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.proyectofinal.integradorrs.model.TweetsModel;
import edu.proyectofinal.integradorrs.model.Update;
import edu.proyectofinal.integradorrs.services.facebook.FaceService;
import edu.proyectofinal.integradorrs.services.tweets.TweetsService;
import facebook4j.ResponseList;
import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import twitter4j.Status;

/**
 *
 * @author Emi
 */
@RestController
@CrossOrigin
@RequestMapping("/api/face")
public class FaceController extends AbstractController<Status> {

    @Autowired
    private FaceService faceService;
    

    @RequestMapping(method = RequestMethod.GET, value="/email/{email:.+}")
    public ResponseEntity<ResponseList<facebook4j.Post>> getTimeline(@Validated @PathVariable("email") String email) {

        System.out.println("Obtener muro");
        System.out.println(email);

        ResponseList<facebook4j.Post> Status = faceService.getAllPost(email);

        return super.collectionResult(Status);

    }
    
    @RequestMapping(method = RequestMethod.GET, value="/UserTimeline/{email:.+}")
    public ResponseEntity<ResponseList<facebook4j.Post>> getUserTimeline(@Validated @PathVariable("email") String email) {

        System.out.println("Obtener posteos propios");
        System.out.println(email);

        ResponseList<facebook4j.Post> Status = faceService.getUserTimeline(email);

        return super.collectionResult(Status);

    }
    
    @RequestMapping(method = RequestMethod.GET, value="/getbyid/{email:.+}")
    public ResponseEntity<Update> getByID(@Validated @PathVariable("email") String email,String id) {

        System.out.println("Obtener publicación propia por ID de post");
        System.out.println(email);

        Update Status = Adapter.FacebookPostToUpdate(email, faceService.GetById(id, email));

        return super.collectionResult(Status);

    }
    
    @RequestMapping(method = RequestMethod.POST, value="/Create")
    public ResponseEntity<Post> PostTweet(@RequestBody Post post) {

        System.out.println("Create Post");

        String result = faceService.Post(post.getEmail(), post.getTexto());
        post.setResult(result);
        return new ResponseEntity<Post>(post, HttpStatus.OK);
    }

}