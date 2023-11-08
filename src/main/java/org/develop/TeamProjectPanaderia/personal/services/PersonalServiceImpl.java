package org.develop.TeamProjectPanaderia.personal.services;

import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.personal.mapper.PersonalMapper;
import org.develop.TeamProjectPanaderia.personal.repositories.PersonalRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PersonalServiceImpl {
    private final PersonalRepository personalRepository;
    private final PersonalMapper personalMapper;


    public PersonalServiceImpl(PersonalRepository personalRepository, PersonalMapper personalMapper) {
        this.personalRepository = personalRepository;
        this.personalMapper = personalMapper;
    }



}
