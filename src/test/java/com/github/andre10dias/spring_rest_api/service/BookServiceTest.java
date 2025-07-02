package com.github.andre10dias.spring_rest_api.service;

import com.github.andre10dias.spring_rest_api.data.dto.v1.BookDTO;
import com.github.andre10dias.spring_rest_api.exception.RequiredObjectIsNullException;
import com.github.andre10dias.spring_rest_api.mapper.mocks.MockBook;
import com.github.andre10dias.spring_rest_api.model.Book;
import com.github.andre10dias.spring_rest_api.repository.BookRepository;
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
class BookServiceTest {

    MockBook input;

    @InjectMocks
    private BookService service;

    @Mock
    BookRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        List<Book> bookList = input.mockEntityList();

        when(repository.findAll()).thenReturn(bookList);

        List<BookDTO> result = service.findAll();

        assertNotNull(result);
        assertEquals(14, result.size());

        for (BookDTO dto : result) {
            assertNotNull(dto.getId());
            assertNotNull(dto.getLinks());

            boolean hasSelfLink = dto.getLinks().stream()
                    .anyMatch(link ->
                            "self".equals(link.getRel().value())
                                    && link.getHref().endsWith("/api/books/v1/" + dto.getId())
                                    && "GET".equals(link.getType())
                    );

            assertTrue(hasSelfLink, "Link 'self' com tipo 'GET' não encontrado para id " + dto.getId());
        }

        verify(repository, times(1)).findAll();
    }

    @Test
    void findById() {
        Book book = input.mockEntity(1);
        book.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(book));

        BookDTO result = service.findById(book.getId());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        boolean hasExpectedLink = result.getLinks().stream()
                .anyMatch(link ->
                        "self".equals(link.getRel().value())
                                && link.getHref().endsWith("/api/books/v1/1")
                                && "GET".equals(link.getType())
                );

        assertTrue(hasExpectedLink, "Link 'self' com href '/api/books/v1/1' e tipo 'GET' não encontrado");
    }

    @Test
    void create() {
        BookDTO dto = input.mockDTO(1);

        Book persisted = new Book();
        persisted.setId(1L);
        persisted.setTitle(dto.getTitle());
        persisted.setAuthor(dto.getAuthor());
        persisted.setLaunchDate(dto.getLaunchDate());
        persisted.setPrice(dto.getPrice());

        when(repository.save(any(Book.class))).thenReturn(persisted);

        BookDTO result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        boolean hasExpectedLink = result.getLinks().stream()
                .anyMatch(link ->
                        "self".equals(link.getRel().value())
                                && link.getHref().endsWith("/api/books/v1/" + result.getId())
                                && "GET".equals(link.getType())
                );

        assertTrue(hasExpectedLink, "Link 'self' com href '/api/books/v1' e tipo 'POST' não encontrado");
    }

    @Test
    void update() {
        Book book = input.mockEntity(1);
        book.setId(1L);

        BookDTO dto = input.mockDTO(1);

        when(repository.findById(dto.getId())).thenReturn(Optional.of(book));
        when(repository.save(any(Book.class))).thenReturn(book);

        BookDTO result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        boolean hasExpectedLink = result.getLinks().stream()
                .anyMatch(link ->
                        "update".equals(link.getRel().value())
                                && link.getHref().endsWith("/api/books/v1")
                                && "PUT".equals(link.getType())
                );

        assertTrue(hasExpectedLink, "Link 'self' com href '/api/books/v1' e tipo 'PUT' não encontrado");
    }

    @Test
    void delete() {
        Book book = input.mockEntity(1);
        book.setId(1L);
        BookDTO dto = input.mockDTO(1);

        when(repository.findById(dto.getId())).thenReturn(Optional.of(book));
        service.delete(dto.getId());

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Book.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testCreateWithNullBook() {
        RequiredObjectIsNullException exception = assertThrows(
                RequiredObjectIsNullException.class,
                () -> service.create(null)
        );

        assertEquals("It is not allowed to persist a null object!", exception.getMessage());
    }

    @Test
    void testUpdateWithNullBook() {
        RequiredObjectIsNullException exception = assertThrows(
                RequiredObjectIsNullException.class,
                () -> service.update(null)
        );

        assertEquals("It is not allowed to persist a null object!", exception.getMessage());
    }


}