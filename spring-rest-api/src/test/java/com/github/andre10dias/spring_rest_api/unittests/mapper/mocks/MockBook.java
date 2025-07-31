package com.github.andre10dias.spring_rest_api.unittests.mapper.mocks;

import com.github.andre10dias.spring_rest_api.data.dto.v1.BookDTO;
import com.github.andre10dias.spring_rest_api.model.Book;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockBook {

    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookDTO mockDTO() {
        return mockDTO(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            var person = mockEntity(i);
            persons.add(person);
        }
        return persons;
    }

    public List<BookDTO> mockDTOList() {
        List<BookDTO> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockDTO(i));
        }
        return persons;
    }
    
    public Book mockEntity(Integer number) {
        int monthNumber = getMonthNumber(number);
        int dayNumber = getDayNumber(number, monthNumber);
        return new Book(
                number.longValue(),
                "Title Test" + number,
                "Author Test" + number,
                LocalDateTime.of(1950 + number, 1 + monthNumber, 1 + dayNumber, 0, 0),
                BigDecimal.valueOf(10.01 + number)

        );
    }

    public BookDTO mockDTO(Integer number) {
        int monthNumber = getMonthNumber(number);
        int dayNumber = getDayNumber(number, monthNumber);
        return new BookDTO(
                number.longValue(),
                "Title Test" + number,
                "Author Test" + number,
                LocalDateTime.of(1950 + number, 1 + monthNumber, 1 + dayNumber, 0, 0),
                BigDecimal.valueOf(10.01 + number)
        );
    }

    private Integer getMonthNumber(int number) {
        return number <= 11 ? number : 0;
    }

    private Integer getDayNumber(int number, int monthNumber) {
        return (monthNumber == 1 && number <= 27) || (monthNumber != 1 && number <= 29) ? number : 0;
    }

}