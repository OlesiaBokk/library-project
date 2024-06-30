package ru.itgirl.library_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itgirl.library_project.model.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
