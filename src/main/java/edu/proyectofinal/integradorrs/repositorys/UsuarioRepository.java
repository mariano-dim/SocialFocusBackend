package edu.proyectofinal.integradorrs.repositorys;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.proyectofinal.integradorrs.model.Usuario;

@RepositoryRestResource(collectionResourceRel = "usuarios", path = "usuarios")
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    
    /**
     *
     * @param email
     * @return
     */
	
	//implementar Async queries para campos pesados http://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#repositories.query-methods.query-lookup-strategies
	
    @Query("{ 'email' : ?0}")
    public Usuario findByEmail(String email);
    @Query("{ 'id' : ?0}")
    public Usuario findById(String id);
    @Query("{ 'nickname':?0}")
    public Usuario findByNickname(String nickname);
    
	
}
