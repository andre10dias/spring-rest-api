package com.github.andre10dias.spring_rest_api.service;

import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import com.github.andre10dias.spring_rest_api.data.dto.v2.PersonDTOv2;
import com.github.andre10dias.spring_rest_api.exception.RequiredObjectIsNullException;
import com.github.andre10dias.spring_rest_api.mapper.mocks.MockPerson;
import com.github.andre10dias.spring_rest_api.model.Person;
import com.github.andre10dias.spring_rest_api.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    MockPerson input;

    @InjectMocks
    private PersonService service;

    @Mock
    PersonRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        List<Person> personList = input.mockEntityList();

        when(repository.findAll()).thenReturn(personList);

        List<PersonDTO> result = service.findAll();

        assertNotNull(result);
        assertEquals(14, result.size());

        for (PersonDTO dto : result) {
            assertNotNull(dto.getId());
            assertNotNull(dto.getLinks());

            boolean hasSelfLink = dto.getLinks().stream()
                    .anyMatch(link ->
                            "self".equals(link.getRel().value())
                                    && link.getHref().endsWith("/api/people/v1/" + dto.getId())
                                    && "GET".equals(link.getType())
                    );

            assertTrue(hasSelfLink, "Link 'self' com tipo 'GET' não encontrado para id " + dto.getId());
        }

        verify(repository, times(1)).findAll();
    }

    @Test
    void findById() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(person));

        PersonDTO result = service.findById(person.getId());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        boolean hasExpectedLink = result.getLinks().stream()
                .anyMatch(link ->
                        "self".equals(link.getRel().value())
                                && link.getHref().endsWith("/api/people/v1/1")
                                && "GET".equals(link.getType())
                );

        assertTrue(hasExpectedLink, "Link 'self' com href '/api/people/v1/1' e tipo 'GET' não encontrado");
    }

    @Test
    void create() {
        PersonDTO dto = input.mockDTO(1);

        Person persisted = new Person();
        persisted.setId(1L);
        persisted.setFirstName(dto.getFirstName());
        persisted.setLastName(dto.getLastName());
        persisted.setAddress(dto.getAddress());
        persisted.setGender(dto.getGender());

        when(repository.save(any(Person.class))).thenReturn(persisted);

        PersonDTO result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        boolean hasExpectedLink = result.getLinks().stream()
                .anyMatch(link ->
                        "self".equals(link.getRel().value())
                                && link.getHref().endsWith("/api/people/v1/" + result.getId())
                                && "GET".equals(link.getType())
                );

        assertTrue(hasExpectedLink, "Link 'self' com href '/api/people/v1' e tipo 'POST' não encontrado");
    }

    @Test
    void update() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        PersonDTO dto = input.mockDTO(1);

        when(repository.findById(dto.getId())).thenReturn(Optional.of(person));
        when(repository.save(any(Person.class))).thenReturn(person);

        PersonDTO result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        boolean hasExpectedLink = result.getLinks().stream()
                .anyMatch(link ->
                        "update".equals(link.getRel().value())
                                && link.getHref().endsWith("/api/people/v1")
                                && "PUT".equals(link.getType())
                );

        assertTrue(hasExpectedLink, "Link 'self' com href '/api/people/v1' e tipo 'PUT' não encontrado");
    }

    @Test
    void delete() {
        Person person = input.mockEntity(1);
        person.setId(1L);
        PersonDTO dto = input.mockDTO(1);

        when(repository.findById(dto.getId())).thenReturn(Optional.of(person));
        service.delete(dto.getId());

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Person.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void createV2() {
        PersonDTOv2 dto = new PersonDTOv2();
        dto.setFirstName("Test");
        dto.setLastName("User");
        dto.setAddress("Test Address");
        dto.setGender("Other");

        Person entity = new Person();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setAddress(dto.getAddress());
        entity.setGender(dto.getGender());

        when(repository.save(any(Person.class))).thenReturn(entity);

        PersonDTOv2 result = service.createV2(dto);

        assertNotNull(result);
        assertNotNull(result.getLinks());

        boolean hasCreateLink = result.getLinks().stream()
                .anyMatch(link ->
                        "create".equals(link.getRel().value())
                                && link.getHref().endsWith("/api/people/v1/v2")
                                && "POST".equals(link.getType())
                );

        assertTrue(hasCreateLink, "Link 'create' com href '/api/people/v1/v2' e tipo 'POST' não encontrado");

        verify(repository).save(any(Person.class));
    }

    @Test
    void testCreateWithNullPerson() {
        RequiredObjectIsNullException exception = assertThrows(
                RequiredObjectIsNullException.class,
                () -> service.create(null)
        );

        assertEquals("It is not allowed to persist a null object!", exception.getMessage());
    }

    @Test
    void testUpdateWithNullPerson() {
        RequiredObjectIsNullException exception = assertThrows(
                RequiredObjectIsNullException.class,
                () -> service.update(null)
        );

        assertEquals("It is not allowed to persist a null object!", exception.getMessage());
    }


}