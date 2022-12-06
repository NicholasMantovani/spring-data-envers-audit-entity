package rz.demo.boot.data.envers.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Add Value S.p.A. by nicholas.mantovani
 * @version dic  06, 2022
 * @since 8.5.0
 */
@Service
public class TestService {
    @Autowired
    private BookRepository bookRepository;


    private void doSomeStuff() {
        bookRepository.findAll();
    }

    public String getNewUser() {
        String user = "wade.wilson";
        List<Book> bookList = bookRepository.findAll();
        if (!bookList.isEmpty()) {
            user = bookList.get(0).getAuthor();
        }
        return user;
    }

}
