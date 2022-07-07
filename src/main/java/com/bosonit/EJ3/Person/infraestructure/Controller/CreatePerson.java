package com.bosonit.EJ3.Person.infraestructure.Controller;

import com.bosonit.EJ3.Person.Exceptions.UnprocesableException;
import com.bosonit.EJ3.Person.application.Port.CreatePersonPort;
import com.bosonit.EJ3.Person.domain.PersonaEnt;
import com.bosonit.EJ3.Person.infraestructure.DTOs.InputPersonaDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/person")
public class CreatePerson {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    CreatePersonPort createPersonPort;

    @PostMapping("/add")
    public InputPersonaDTO addPersona(@RequestBody InputPersonaDTO inputPersonaDTO) throws Exception {
        try {
            PersonaEnt personaEnt = createPersonPort.addPersona(modelMapper.map(inputPersonaDTO, PersonaEnt.class));
            inputPersonaDTO.setId_persona(personaEnt.getId_persona());
            return inputPersonaDTO;
        } catch (Exception e) {
            throw new UnprocesableException("Valores incorrectos");
        }
    }
}
