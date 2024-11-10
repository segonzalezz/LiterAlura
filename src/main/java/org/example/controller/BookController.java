package org.example.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.transaction.Transactional;
import org.example.model.Author;
import org.example.model.Book;
import org.example.repository.BookRepository;
import org.example.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void saveBooksFromAPI() {
        try {
            bookService.fetchAndSaveBooks();
            System.out.println("Libros guardados en la base de datos con éxito.");
        } catch (IOException e) {
            System.out.println("Error al consumir o guardar los datos: " + e.getMessage());
        }
    }

    @Transactional
    public void showStoredBooks() {
        List<Book> books = bookRepository.findAll();
        books.forEach(book -> {
            System.out.println("ID: " + book.getId());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Subjects: " + book.getSubjects());

            // Imprimir detalles de los autores
            System.out.println("Authors:");
            book.getAuthors().forEach(author -> {
                System.out.println("  - Name: " + author.getName());
                System.out.println("    Birth Year: " + author.getBirthYear());
                System.out.println("    Death Year: " + author.getDeathYear());
            });

            System.out.println("Translators: " + book.getTranslators());
            System.out.println("Bookshelves: " + book.getBookshelves());
            System.out.println("Languages: " + book.getLanguages());
            System.out.println("Copyright: " + book.getCopyright());
            System.out.println("Media Type: " + book.getMedia_type());
            System.out.println("Formats: " + book.getFormats());
            System.out.println("Download Count: " + book.getDownload_count());
            System.out.println("------------------------------");
        });
    }



    @GetMapping("/catalog")
    public String showCatalog(Model model) {
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "index";
    }

    // Buscar libros por título
    @PostMapping("/search")
    public String searchByTitle(@RequestParam("title") String title, Model model) {
        List<Book> books = bookRepository.findAll()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
        model.addAttribute("books", books);
        model.addAttribute("message", "Resultados de búsqueda para: " + title);
        return "index";
    }

    // Filtrar libros por idioma
    @PostMapping("/filter")
    public String filterByLanguage(@RequestParam("language") String language, Model model) {
        List<Book> books = bookRepository.findAll()
                .stream()
                .filter(book -> book.getLanguages().contains(language))
                .collect(Collectors.toList());
        model.addAttribute("books", books);
        model.addAttribute("message", "Libros en el idioma: " + language);
        return "index";
    }

    @GetMapping("/topDownloadedBooks")
    public String getTopDownloadedBooks(Model model) {
        List<Book> topBooks = bookRepository.findTop10ByOrderByDownloadCountDesc();
        model.addAttribute("books", topBooks);
        return "topDownloadedBooks";
    }

    @PostMapping("/authorsInYearRange")
    public String searchAuthorsInYearRange(
            @RequestParam("startYear") int startYear,
            @RequestParam("endYear") int endYear,
            Model model) {
        List<Author> authors = bookRepository.findAuthorsInYearRange(startYear, endYear);
        model.addAttribute("authors", authors);
        if (authors.isEmpty()) {
            model.addAttribute("message", "No se encontraron autores en el rango de años especificado.");
        }
        return "authorsTable";
    }
}
