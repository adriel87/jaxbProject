
import library.Book;
import library.BookStore;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


public class Main {
    //definimos el nombre y la ruta relativa donde se alojara nuestro xml
    private static final String BOOKSTORE_XML = "./bookstore-jaxb.xml";

    public static void main(String[] args) throws JAXBException, IOException {
        var bookList = new ArrayList<Book>();

        // Crear lo libros
        var book1 = new Book();
        book1.setIsbn("978-0060554736");
        book1.setName("The Game");
        book1.setAuthor("Neil Strauss");
        book1.setPublisher("Harpercollins");
        bookList.add(book1);

        var book2 = new Book();
        book2.setIsbn("978-3832180577");
        book2.setName("Feuchtgebiete");
        book2.setAuthor("Charlotte Roche");
        book2.setPublisher("Dumont Buchverlag");
        bookList.add(book2);

        // crear la tienda y asignar lo libros
        var bookstore = new BookStore();
        bookstore.setName("Fraport Bookstore");
        bookstore.setLocation("Frankfurt Airport");
        bookstore.setBookList(bookList);

        // create JAXB context and instantiate marshaller
        // crear el contexto de JAXB e instanciar marshaller

        JAXBContext context = JAXBContext.newInstance(BookStore.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // escribir el contenido de bookstore por consola
        m.marshal(bookstore, System.out);

        // lo guardamos en un nuevo archivo
        m.marshal(bookstore, new File(BOOKSTORE_XML));

        // ver desde el archivo previamente guardado su contenido
        System.out.println();
        System.out.println("Output from our XML File: ");
        Unmarshaller um = context.createUnmarshaller();
        BookStore bookstore2 = (BookStore) um.unmarshal(new FileReader(
                BOOKSTORE_XML));
        List<Book> list = bookstore2.getBooksList();
        for (Book book : list) {
            System.out.println("Book: " + book.getName() + " from "
                    + book.getAuthor());
        }
    }


}
