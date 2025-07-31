package com.github.andre10dias.spring_rest_api.service;

import com.github.andre10dias.spring_rest_api.controller.BookController;
import com.github.andre10dias.spring_rest_api.data.dto.v1.BookDTO;
import com.github.andre10dias.spring_rest_api.exception.RequiredObjectIsNullException;
import com.github.andre10dias.spring_rest_api.model.Book;
import com.github.andre10dias.spring_rest_api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import static com.github.andre10dias.spring_rest_api.mapper.ObjectMapper.parseListObject;
import static com.github.andre10dias.spring_rest_api.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    private final AtomicLong counter = new AtomicLong();
    private final Logger logger = Logger.getLogger(BookService.class.getName());

    public List<BookDTO> findAll() {
        logger.info("findAll");
        List<BookDTO> dtos = parseListObject(bookRepository.findAll(), BookDTO.class);
        dtos.forEach(BookService::hateoasLinkAdd);
        return dtos;
    }

    public BookDTO findById(Long id) {
        logger.info("findById: " + id);
        var book = bookRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Book not found")
        );
        var dto = parseObject(book, BookDTO.class);
        hateoasLinkAdd(dto);
        return dto;
    }


    public BookDTO create(BookDTO bookDto) {
        logger.info("create: " + bookDto);
        if (bookDto == null) {
            throw new RequiredObjectIsNullException();
        }

        var bookToSave = parseObject(bookDto, Book.class);
        var dto = parseObject(bookRepository.save(bookToSave), BookDTO.class);
        hateoasLinkAdd(dto);
        return dto;
    }

    public BookDTO update(BookDTO bookDto) {
        logger.info("update: " + bookDto);
        if (bookDto == null) {
            throw new RequiredObjectIsNullException();
        }

        var bookToUpdate = bookRepository.findById(bookDto.getId()).orElseThrow(
                () -> new RuntimeException("Book not found")
        );
        bookToUpdate.setTitle(bookDto.getTitle());
        bookToUpdate.setAuthor(bookDto.getAuthor());
        bookToUpdate.setLaunchDate(bookDto.getLaunchDate());
        bookToUpdate.setPrice(bookDto.getPrice());
        var dto = parseObject(bookRepository.save(bookToUpdate), BookDTO.class);
        hateoasLinkAdd(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("delete: " + id);
        var bookToDelete = bookRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Book not found")
        );
        bookRepository.delete(bookToDelete);
    }

    private static void hateoasLinkAdd(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }

}
