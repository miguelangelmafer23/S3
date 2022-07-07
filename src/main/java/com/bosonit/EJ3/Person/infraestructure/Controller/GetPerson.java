package com.bosonit.EJ3.Person.infraestructure.Controller;


import com.bosonit.EJ3.Person.Exceptions.NotFoundException;
import com.bosonit.EJ3.Person.application.Port.GetPersonPort;
import com.bosonit.EJ3.Person.domain.PersonaEnt;
import com.bosonit.EJ3.Person.infraestructure.DTOs.OutPersonaDTO;
import com.bosonit.EJ3.Person.infraestructure.DTOs.OutputPersonaStudentDTO;
import com.bosonit.EJ3.Person.infraestructure.DTOs.OutputPersonaTeacherDTO;
import com.bosonit.EJ3.Person.infraestructure.DTOs.OutputSimpleDTO;
import com.bosonit.EJ3.Person.infraestructure.Repository.PersonaRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/person")
public class GetPerson {

    @Autowired
    GetPersonPort getPersonPort;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PersonaRepository personaRepository;

    @GetMapping("/id/{id}")

    public OutPersonaDTO getPersonByID(@PathVariable Integer id, @RequestParam(defaultValue = "simple", required = false) String outputType) throws Exception {
        try {
            //comprobar si es estudiante o profesor
            PersonaEnt checker = personaRepository.findById(id).orElseThrow(() -> new NotFoundException("PERSONAL DATA NOT FOUND"));
            if (outputType.equals("full")) {
                //cuando el id persona no esta asignado
                if (checker.getTeacherEnt() == null & checker.getStudentEnt() == null) {
                    OutPersonaDTO outPersonaDTO = modelMapper.map(checker, OutPersonaDTO.class);
                    return outPersonaDTO;
                    //si el id persona esta asignado a estudiante o profesor
                } else if (checker.getTeacherEnt() != null) {
                    OutputPersonaTeacherDTO outputPersonaTeacherDTO = modelMapper.map(checker, OutputPersonaTeacherDTO.class);
                    return outputPersonaTeacherDTO;
                } else if (checker.getStudentEnt() != null) {
                    OutputPersonaStudentDTO outputPersonaStudentDTO = modelMapper.map(checker, OutputPersonaStudentDTO.class);
                    return outputPersonaStudentDTO;
                }
            } else {
                OutputSimpleDTO outputSimpleDTO = modelMapper.map(checker, OutputSimpleDTO.class);
                return outputSimpleDTO;
            }
        } catch (Exception e) {
            throw new NotFoundException("No se encuentra el id: " + id);
        }
        return null;
    }

    @GetMapping("/name/{nombre}")
    public List<OutPersonaDTO> getPersonByName(@PathVariable String nombre, @RequestParam(defaultValue = "simple", required = false) String outputType) throws Exception {
        //devolver persona si no esta asoaciado a student teacher
        List<PersonaEnt> personaEntList = getPersonPort.getPersonByName(nombre);
        List<OutPersonaDTO> outPersonaDTOList = new ArrayList<>();

        if (outputType.equals("full")) {
            personaEntList.forEach(personaEnt -> {
                if (personaEnt.getTeacherEnt() == null) {
                    OutputPersonaStudentDTO outputPersonaStudentDTO = modelMapper.map(personaEnt, OutputPersonaStudentDTO.class);
                    outPersonaDTOList.add(outputPersonaStudentDTO);
                } else if (personaEnt.getStudentEnt() == null) {
                    OutputPersonaTeacherDTO outputPersonaTeacherDTO = modelMapper.map(personaEnt, OutputPersonaTeacherDTO.class);
                    outPersonaDTOList.add(outputPersonaTeacherDTO);
                } else {
                    OutPersonaDTO outPersonaDTO = modelMapper.map(personaEnt, OutPersonaDTO.class);
                    outPersonaDTOList.add(outPersonaDTO);
                }
            });
            return outPersonaDTOList;
        } else {
            personaEntList.forEach(personaEnt -> {
                OutPersonaDTO outPersonaDTO = modelMapper.map(personaEnt, OutPersonaDTO.class);
                outPersonaDTOList.add(outPersonaDTO);
            });
            return outPersonaDTOList;
        }
    }

    @GetMapping("/all")
    public List<OutPersonaDTO> getAllPerson(@RequestParam(defaultValue = "simple", required = false) String outputType) {

        List<PersonaEnt> personaEntList = getPersonPort.getAllPerson();
        List<OutPersonaDTO> outPersonaDTOList = new ArrayList<>();

        if (outputType.equals("full")) {
            personaEntList.forEach(personaEnt -> {


                if (personaEnt.getTeacherEnt() == null) {
                    OutputPersonaStudentDTO outputPersonaStudentDTO = modelMapper.map(personaEnt, OutputPersonaStudentDTO.class);
                    outPersonaDTOList.add(outputPersonaStudentDTO);
                } else if (personaEnt.getStudentEnt() == null) {
                    OutputPersonaTeacherDTO outputPersonaTeacherDTO = modelMapper.map(personaEnt, OutputPersonaTeacherDTO.class);
                    outPersonaDTOList.add(outputPersonaTeacherDTO);
                } else {
                    OutPersonaDTO outPersonaDTO = modelMapper.map(personaEnt, OutPersonaDTO.class);
                    outPersonaDTOList.add(outPersonaDTO);
                }
            });
            return outPersonaDTOList;
        } else {
            personaEntList.forEach(personaEnt -> {
                OutPersonaDTO outPersonaDTO = modelMapper.map(personaEnt, OutPersonaDTO.class);
                outPersonaDTOList.add(outPersonaDTO);
            });
            return outPersonaDTOList;
        }
    }
}


