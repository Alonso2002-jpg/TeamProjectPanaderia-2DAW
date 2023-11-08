package org.develop.TeamProjectPanaderia.personal.services;

import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.personal.repositories.PersonalRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PersonalServiceImpl {
    private final PersonalRepository personalRepository;

    public PersonalServiceImpl(PersonalRepository personalRepository) {
        this.personalRepository = personalRepository;
    }
    //  private final PersonalMapper personalMapper;



}
