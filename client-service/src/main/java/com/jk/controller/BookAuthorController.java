package com.jk.controller;

import com.google.protobuf.Descriptors;
import com.jk.service.BookAuthorClientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.management.Descriptor;
import java.util.List;
import java.util.Map;

/**
 * @author Junaid.Khan
 *
 */

@RestController
@AllArgsConstructor
public class BookAuthorController {

    BookAuthorClientService  bookAuthorClientService;


    @GetMapping("/author/{authorId}")
    public Map<Descriptors.FieldDescriptor,Object> getAuthor(@PathVariable String authorId){

        return bookAuthorClientService.
                getAuthor(Integer.parseInt(authorId));
    }

    @GetMapping("/book/{authorId}")
    public Map<Descriptors.FieldDescriptor,Object> getBookByAuthor(@PathVariable String authorId) throws InterruptedException {

        List<Map<Descriptors.FieldDescriptor, Object>> booksByAuthor = bookAuthorClientService.
                getBooksByAuthor(Integer.parseInt(authorId));
        return (Map<Descriptors.FieldDescriptor, Object>) booksByAuthor;
    }

}
