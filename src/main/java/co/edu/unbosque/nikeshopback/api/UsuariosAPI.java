package co.edu.unbosque.nikeshopback.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.nikeshopback.dao.UsuariosDAO;
import co.edu.unbosque.nikeshopback.model.Usuarios;

@RestController //esta es una clase REST
@RequestMapping("usuarios")
public class UsuariosAPI
{
	@Autowired //inyecta la dependencia de todos los métodos del JPA para usuarioDAO
	private UsuariosDAO usuariosDAO;
	
	@ResponseStatus(
		    value = HttpStatus.CONFLICT, 
		    reason = "La cedula de usuario no esta registrada"
		)
		public class UserNotFoundException 
		        extends RuntimeException {
		 
		    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public UserNotFoundException(String string)
		{
				super(string);
			}
		}
	
	@PostMapping("/guardar")//Request convierte en un objeto Java desde un JSon
	public Usuarios guardar(@RequestBody Usuarios usuario) {
		try
		{
			if(usuario.getCedula_usuario() != 0)
			{
				Optional<Usuarios> opUser = usuariosDAO.findById(usuario.getCedula_usuario());
				
				Usuarios user = opUser.orElseThrow(() -> new UserNotFoundException("El id" + usuario.getCedula_usuario() + "no se encuentra registrado"));
				 
				user.setCedula_usuario(0);
				
				return user;
			}
			
			return usuario;
		}
		
		catch (Exception e)
		{
			Usuarios user = usuariosDAO.save(usuario);
			
			return user;
		}
	}
	
	@GetMapping("/listar/{id}")
	public Optional<Usuarios> listarPorId(@PathVariable("id") Long id){
		return usuariosDAO.findById(id);
	}
	
	@GetMapping("/listar")
	public List<Usuarios> listar(){
		return usuariosDAO.findAll();
	}
	
	@DeleteMapping("/eliminar/{id}")
	public void eliminar(@PathVariable("id") Long id) {
		usuariosDAO.deleteById(id);
	}
	
	@PutMapping("/actualizar/{id}")
	public void actualizar(@PathVariable("id") Long id, @RequestBody Usuarios usuario) {
		Usuarios user = usuariosDAO.getById(id);
		
		user.setEmail_usuario(usuario.getEmail_usuario());
		user.setNombre_usuario(usuario.getNombre_usuario());
		user.setUsuario(usuario.getUsuario());
		user.setPassword(usuario.getPassword());
		
		usuariosDAO.save(user);
	}
}
