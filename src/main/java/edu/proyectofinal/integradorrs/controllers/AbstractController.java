package edu.proyectofinal.integradorrs.controllers;

import edu.proyectofinal.integradorrs.model.Dashboard;
import edu.proyectofinal.integradorrs.model.Token;
import edu.proyectofinal.integradorrs.model.UnifiedUpdate;
import edu.proyectofinal.integradorrs.model.Update;
import facebook4j.Post;
import facebook4j.ResponseList;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.charset.Charset;
import java.util.Collection;
import twitter4j.Status;

/**
 * Created by mariano on 25/03/16.
 * @param <T>
 */
public class AbstractController<T > {

    static final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    protected ResponseEntity<T> createdResult(T result) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getClass()).toUri());

        httpHeaders.setContentType(contentType);
        return new ResponseEntity<>(result, httpHeaders, HttpStatus.CREATED);
    }

    protected ResponseEntity<Collection<T>> collectionResult(Collection<T> result) {
        HttpHeaders httpHeaders = buildHeaders();
        return new ResponseEntity<>(result, httpHeaders, HttpStatus.OK);
    }

    protected ResponseEntity<T> createdErrorResult(T result) {
        HttpHeaders httpHeaders = buildHeaders();
        return new ResponseEntity<>(result, httpHeaders, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<T> singleResult(T result) {
        HttpHeaders httpHeaders = buildHeaders();
        return new ResponseEntity<>(result, httpHeaders, HttpStatus.OK);
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/")
                .build().toUri());
        httpHeaders.setContentType(contentType);
        return httpHeaders;
    }

    protected ResponseEntity<Token> singleResult(Token token) {
        HttpHeaders httpHeaders = buildHeaders();
        return new ResponseEntity<Token>(token, httpHeaders, HttpStatus.OK);    }

    ResponseEntity<ResponseList<Post>> collectionResult(ResponseList<Post> Status) {
        HttpHeaders httpHeaders = buildHeaders();
        return new ResponseEntity<ResponseList<Post>>(Status, httpHeaders, HttpStatus.OK);    

    }

    ResponseEntity<Update> collectionResult(Update Status) {
        HttpHeaders httpHeaders = buildHeaders();
        return new ResponseEntity<Update>(Status, httpHeaders, HttpStatus.OK);    
    }

    ResponseEntity<Dashboard> Result(Dashboard aDB) {
        HttpHeaders httpHeaders = buildHeaders();
        return new ResponseEntity<Dashboard>(aDB, httpHeaders, HttpStatus.OK);
    }

}

