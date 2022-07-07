package com.bosonit.EJ3.Security;

import com.bosonit.EJ3.Person.Exceptions.NotFoundException;
import com.bosonit.EJ3.Person.Exceptions.UnprocesableException;
import com.bosonit.EJ3.Person.application.Port.GetPersonPort;
import com.bosonit.EJ3.Person.domain.PersonaEnt;
import com.bosonit.EJ3.Person.infraestructure.Repository.PersonaRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LoginController {

    @Autowired
    GetPersonPort getPersonPort;

    @PostMapping("/login")
//comprobar nombre variable
    public ResponseEntity<String> login(@RequestParam("username") String username,@RequestParam("password") Integer pwd)
        throws NotFoundException, UnprocesableException
    {
        List<PersonaEnt> personaEntList = getPersonPort.getPersonByUsername(username);
        if (personaEntList.size() == 0) throw new NotFoundException("Usuario " + username + " no encontrado");
        if (personaEntList.size() > 1) throw new NotFoundException("Existe más de un usuario con el mismo username");
        PersonaEnt personaEnt = personaEntList.get(0);
        Integer password = personaEnt.getPassword();
        if(!pwd.equals(password)) throw new UnprocesableException("Password incorrecta");
        String rol =(personaEnt.getAdmin()) ? "ROLE_ADMIN": "ROLE_USER";
        return new ResponseEntity<>(getJWTToken(username,rol), HttpStatus.OK);
    }


    private String getJWTToken(String username,String rol) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(rol);

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }
}
