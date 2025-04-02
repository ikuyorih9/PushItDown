package com.pushitdown.pushitdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.classfile.ClassFile.Option;
import java.nio.channels.Pipe.SourceChannel;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pushitdown.pushitdown.exceptions.NotFoundException;
import com.pushitdown.pushitdown.exceptions.UserRegisteredException;
import com.pushitdown.pushitdown.id.RegistroId;
import com.pushitdown.pushitdown.models.Registro;
import com.pushitdown.pushitdown.models.Usuario;
import com.pushitdown.pushitdown.models.UsuarioDTO;
import com.pushitdown.pushitdown.repositories.RegistroRepository;
import com.pushitdown.pushitdown.repositories.UsuarioRepository;
import com.pushitdown.pushitdown.services.RegistroService;
import com.pushitdown.pushitdown.services.UsuarioService;

@SpringBootTest
class PushitdownApplicationTests {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RegistroRepository registroRepository;

	@Autowired RegistroService registroService;

	@Test
	void contextLoads() {
	}

	@Test
	void adicionarUsuario(){
		UsuarioDTO user = new UsuarioDTO("ikuyorih9", "Hugo", "hugo");
		usuarioService.cadastrar(user);

		assertThrows(UserRegisteredException.class, ()->{
			UsuarioDTO user2 = new UsuarioDTO("ikuyorih9", "Hugo", "hugo");
			usuarioService.cadastrar(user2);
		});
	}

	@Test
	void assertSenha(){
		Usuario user = usuarioRepository.findByUsername("ikuyorih9").orElseThrow(() -> new RuntimeException("foda"));
		assertEquals(true, passwordEncoder.matches("hugonakamura", user.getSenha()));
	}

	@Test
	void adicionaRegistro(){
		String username = "ikuyorih9";
		Usuario found = usuarioRepository.findByUsername(username).orElseThrow(()-> new NotFoundException("Usuário não está cadastrado no sistema."));
        RegistroId id = new RegistroId(found.getUsername(), LocalDate.now(), LocalTime.now());
        registroRepository.save(new Registro(id, found,"ENTRADA"));
		System.out.println("Registrado.");
	}

	@Test
	void assertBadInserts(){
		
		assertThrows(RuntimeException.class, ()->{
			Usuario found = usuarioRepository.findByUsername("ikuyorih").orElseThrow(()-> new NotFoundException("Usuário não está cadastrado no sistema."));
			RegistroId id = new RegistroId(found.getUsername(), LocalDate.now(), LocalTime.now());
        	registroRepository.save(new Registro(id, found,"entrada"));
		});
	}

	@Test
	void getRegistros(){
		List<Registro> registros = registroRepository.findRegistrosByUsername("ikuyorih9");
		for(Registro reg: registros){
			System.out.println(reg.toString());
		}
	}

	@Test
	void getLastSession(){
		Registro reg = registroRepository.findLastSessionByUsername("ikuyorih9").orElseThrow(()-> new NotFoundException("Usuário não existe"));
		System.out.println("[REGISTRO]: " + reg.toString());

	}

	@Test
	void dayFilterTest(){
		List<Registro> regs = registroRepository.findByUsernameAndData("ikuyorih9", LocalDate.now());
		for(Registro r : regs){
			System.out.println("[REGISTRO]: " + r.toString());
		}
	}

	@Test
	void totalMsWorkedToday(){
		List<Registro> regs = registroService.retornaRegistrosPorData("ikuyorih9", LocalDate.now());
		Duration total = Duration.ZERO;
		LocalTime inicio = null;
		for(Registro r : regs){
			System.out.println("[REGISTRO]: " + r.toString());
			String tipo = r.getTipo();
			LocalTime hora = r.getId().getHora();
			if(tipo.equals("ENTRADA")){
				inicio = hora;
			}
			else{
				total = total.plus(Duration.between(inicio, hora));
				inicio = null;
			}
		}
		System.out.println("[TOTAL HORAS]: " + total.toMillis());

		System.out.println("[TOTAL DA FUNCAO]: " + registroService.obtemTempoDeTrabalhoPorData("ikuyorih9", LocalDate.now()));
	}
	
	@Test
	void getAllDaysWorked(){
		List<LocalDate> regs = registroRepository.findAllDaysByUsername("ikuyorih9");
		for(LocalDate r : regs){
			System.out.println(r.toString());
		}
	}
}
