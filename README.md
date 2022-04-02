# JAXB

## Que es?

es una libreria diseñada para Java que nos ayuda a trabajar con archivos de lenguaje de marcas, en concreto XML

## Que vamos a hacer?

1. Como instalar la libreria en nuestro proyecto de java + maven
2. crearemos nuestras clases de pruebas para ver como funciona las notaciones basicas
3. crearemos una clase principal que nos sirva para testear el correcto funcionamiento y verlo en ejecucion

## Instalacion
dentro deun proyecto con maven tenemos nuestro archivo `pom.xml` en la raiz. Este archivo le dice al proyecto una serie 
configuraciones que debe establecer, una de ellas son las dependencias

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>jaxbProject</artifactId>
    <version>1.0-SNAPSHOT</version>
<!--como vemos en este proyecto usamos la version 17 del JDK-->
    <properties> 
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
<!--Esta es la libreria que debemos usar para JAXB-->
            <groupId>com.sun.xml.bind</groupId>
            <artifactId>jaxb-impl</artifactId>
            <version>2.3.3</version>
        </dependency>
    </dependencies>

</project>
```

Una vez configurado nuestro `pom.xml` debemos compilar para que nos genere esta dependecia en nuestro proyecto
y no nos muestre error durante el desarrollo del ejercicio, en Intellil IDEA podemos referescar las dependencias con un 
icono que nos aparecera en la esquina superior derecha o pulsando con el boton derecho del raton sobre el proyecto y
navegando hasta `maven>reload`

## creando clases

### libro

creamos una nueva clase y incluimos el siguiente codigo

```java
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "book")
// si quieres definir el orden correcto a la hora de crear un libro
// es opcional
@XmlType(propOrder = { "author", "name", "publisher", "isbn" })
public class Book {

    private String name;
    private String author;
    private String publisher;
    private String isbn;

    // con esta etiqueta cambiaremos el nombre de salida para el archivo xml
    // es opcional
    @XmlElement(name = "title")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
```

`@XmlRootElement(name = "book")` con esta etiqueta indicamos el elemento raiz para los objetos del tipo book

`@XmlType(propOrder={orden de los elementos})`

`@XmlElement(name = "title")` definir otro tag para la etiqueta para la propiedad a la que se le aplique

### Libreria
ahora creamos la libreria que sera donde esten nuestro libros

```java
package library;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace="libray")
public class BookStore {
    //XmlElementWrapper genera un elemento que va a envolver la lsita de libros
    @XmlElementWrapper(name = "bookList")
    // XmlElement establecer el nombre de la entidad
    @XmlElement(name = "book")
    private List<Book> bookList;
    private String name;
    private String location;

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public List<Book> getBooksList() {
        return bookList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

```

`@XmlElementWrapper(name = "bookList")` este wrapper, nos va a englobar todos los libros


### clase Main para probar

Por ultimo vamos a hacer un pequeño codigo para probar nuestras clases con sus anotaciones xml

```java
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
```

## conclusion
Hemos visto como trabajar con notaciones en nuestras clases de java para manejar archivos xml

para mas informacion sobre la libreria usada podemos ir a los siguientes enlaces

[JAXB](https://jesperdj.com/2018/09/30/jaxb-on-java-9-10-11-and-beyond/)

