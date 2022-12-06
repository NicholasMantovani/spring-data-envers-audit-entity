package rz.demo.boot.data.envers.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.stream.Stream;

/**
 * @author Rashidi Zin
 */
public interface BookTestRepository extends JpaRepository<BookTest, Long>, RevisionRepository<BookTest, Long, Integer> {

    Stream<Book> findAllByAuthor(String author);

}
