package org.develop.TeamProjectPanaderia.personal.controllers;


import org.develop.TeamProjectPanaderia.personal.dto.CreateResponseDto;
import org.develop.TeamProjectPanaderia.personal.mapper.PersonalMapper;
import org.develop.TeamProjectPanaderia.personal.services.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/personal")
public class PersonalControllers {
    private final PersonalService personalService;
    private final PersonalMapper personalMapper;

    @Autowired
    public PersonalControllers(PersonalService personalService, PersonalMapper personalMapper) {
        this.personalService = personalService;
        this.personalMapper = personalMapper;
    }
    @GetMapping
    public ResponseEntity<List<CreateResponseDto>> findAll(@RequestParam(required = false) Boolean isActive){
        var personal = this.personalService.findAll(isActive);
        return ResponseEntity.ok(personalMapper.toResponseList(personal));
    }
}
