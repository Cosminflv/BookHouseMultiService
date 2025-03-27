package org.example.bookhouseapiservice.utils;

import org.example.bookhouseapiservice.dtos.BorrowedBookEntityDTO;
import org.example.bookhouseapiservice.models.BorrowedBookEntity;

public class Converters {

    private Converters() {}

    public static BorrowedBookEntityDTO convertBorrowedBookToDTO(BorrowedBookEntity borrowedBook) {
        BorrowedBookEntityDTO dto = new BorrowedBookEntityDTO();

        dto.setId(borrowedBook.getId());
        dto.setBorrowedAt(borrowedBook.getBorrowedAt());
        dto.setReturnDate(borrowedBook.getReturnDate());
        dto.setTitle(borrowedBook.getBook().getTitle());
        dto.setIsbn(borrowedBook.getBook().getIsbn());
        dto.setAuthor(borrowedBook.getBook().getAuthor());
        return dto;
    }
}
