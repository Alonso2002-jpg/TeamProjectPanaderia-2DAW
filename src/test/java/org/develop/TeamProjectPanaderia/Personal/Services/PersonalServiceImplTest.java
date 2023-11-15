package org.develop.TeamProjectPanaderia.Personal.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaResponseDto;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.categoria.repositories.CategoriaRepository;
import org.develop.TeamProjectPanaderia.rest.personal.mapper.PersonalMapper;
import org.develop.TeamProjectPanaderia.rest.personal.services.PersonalServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
