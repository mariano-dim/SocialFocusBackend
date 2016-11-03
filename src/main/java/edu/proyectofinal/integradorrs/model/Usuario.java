package edu.proyectofinal.integradorrs.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import edu.proyectofinal.integradorrs.configurations.Role;

@Document(collection = "usuarios")
public class Usuario implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	private boolean nonCredsexpired = false;
	@Field
	private String username;
	@Field
    private String password;
	@Field
	private String apellido;
	@Field
	private String nickname;
    @Id
    private String id;
    @Field
    @Indexed(unique = true)
    private String email;
    @Field
    private String clave;
    @Field
    private Date creationDate;
    @Field
    private String telefono;
    @Field
    private Date fechaNacimiento;
    @Field
    private String pais;
    @Field
    private String provincia;
    @Field
    private String ciudad;
    @Field
    private String calle;
    @Field
    private ConcurrentHashMap<String, Long> cuentas;
    @Field
    private Collection<SimpleGrantedAuthority> authorities;
    @Field
    private Usuario user;
    @Field
	private boolean nonAccexpired;
    @Field
	private boolean nonlocked;
    @Field
	private boolean enabled;
 
    public Usuario(String id, String email,String nickname,String password,String role,  String clave, Date creationDate, String telefono,
            Date fechaNacimiento, String pais, String provincia, String ciudad, String calle) {
    	super();
    	/*
    	Role aux = new Role(role);
    	List<Role> aux2 = null;
    	
    	aux2.add(aux);*/
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        //this.authorities = aux2;
        this.clave = clave;
        this.creationDate = creationDate;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.pais = pais;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.calle = calle;        
        this.cuentas = new ConcurrentHashMap<String,Long>();
    }
    
    public Usuario(Usuario user){
    	this.user = user;
    	this.user.setAccountExpired(user.isAccountNonExpired());
    	user.enabled = true;
    	user.nonlocked = true;
    	user.nonCredsexpired = true;
    	SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(
				"ROLE_ADMIN");
		
    }
    
    public Usuario(){
       	this.user = user;
    	this.nonAccexpired = true;
    	this.enabled = true;
    	this.nonlocked = true;
    	this.nonCredsexpired = true;
    }
    
    
    public Usuario(String id,String nickname, String password,String role){
    	this.id = id;
    	this.nickname = nickname;
    	this.password = password;
    	SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(
				"ROLE_ADMIN");
		((Collection<SimpleGrantedAuthority>) authorities).add(adminAuthority);
    }

    public String getApellido() {
    	return this.apellido;
    }
    public String getNickname() {
    	return this.nickname;
    }
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getPais() {
        return this.pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
    
    public void setUsername(String nombre) {
    	this.username = nombre;
    }
    public void addCuenta(String nombre_cuenta, Long id_cuenta) {
    	this.cuentas.putIfAbsent(nombre_cuenta, id_cuenta);
    }
    
    public void setAccountExpired(boolean valor) {
    	this.nonAccexpired = valor;
    }
    
    public void setAccountLocked(boolean valor) {
    	this.nonlocked = valor;
    }
    
    public void setCredExpired(boolean valor) {
    	this.nonCredsexpired = valor;
    }

//    @Override
//    @JsonIgnore
//    public boolean isAnErrorResponse() {
//        return false;
//    }
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(" id:- ").append(this.getId());
        str.append(" nickname:- ").append(this.getNickname());
        str.append(" password:- ").append(this.getPassword());
        str.append(" authorities:- ").append(this.getAuthorities());
        str.append(" email:- ").append(this.getEmail());
        str.append(" creationDate:- ").append(this.getCreationDate());
        str.append(" telefono:- ").append(this.getTelefono() );
        str.append(" fechaNacimiento:- ").append( this.getFechaNacimiento());
        str.append(" pais:- ").append(this.getPais());
        str.append(" provincia:- ").append(this.getProvincia());
        str.append(" ciudad:- ").append(this.getCiudad());
        str.append(" calle:- ").append(this.getCalle());
        

         return str.toString();
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}
	
	public void setAuthorities(Role rol) {
		SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
    	user.authorities.add(adminAuthority);
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {

		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.nonAccexpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.nonlocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.nonCredsexpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;

	}
	
	
}
