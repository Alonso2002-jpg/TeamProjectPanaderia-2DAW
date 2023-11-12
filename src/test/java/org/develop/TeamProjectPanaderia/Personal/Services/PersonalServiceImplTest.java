package org.develop.TeamProjectPanaderia.Personal.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaResponseDto;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.repositories.CategoriaRepository;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.personal.mapper.PersonalMapper;
import org.develop.TeamProjectPanaderia.personal.models.Personal;
import org.develop.TeamProjectPanaderia.personal.services.PersonalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class PersonalServiceImplTest {
    private Categoria categotia;
    private ObjectMapper mapper;
    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private NotificacionMapper<CategoriaResponseDto> notificacionMapper;
    @Mock
    private PersonalMapper perorMapper;
    @InjectMocks
    private PersonalServiceImpl personalService;

}
