package ru.itgirl.library_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.itgirl.library_project.model.entity.Book;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Optional<Book> findBookByName(String name);

    Optional<Book> findBookById(Long id);

    @Query(nativeQuery = true, value = "Select * from book where name = ?")
    Optional<Book> findBookByNameBySql(String name);
}
