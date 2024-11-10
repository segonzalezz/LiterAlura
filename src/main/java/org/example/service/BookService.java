package org.example.service;

import com.google.gson.*;
import jakarta.transaction.Transactional;
import org.example.model.Author;
import org.example.model.Book;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

@Service
public class BookService {
    private static final String GUTENDEX_API_URL = "https://gutendex.com/books/";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Transactional
    public void fetchAndSaveBooks() throws IOException {
        URL url = new URL(GUTENDEX_API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Failed : HTTP Error code : " + responseCode);
        }

        StringBuilder inline = new StringBuilder();
        try (Scanner scanner = new Scanner(url.openStream())) {
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
        }

        JsonObject jsonObject = JsonParser.parseString(inline.toString()).getAsJsonObject();
        JsonArray booksArray = jsonObject.getAsJsonArray("results");

        List<Book> books = new ArrayList<>();
        Gson gson = new Gson();

        for (JsonElement bookElement : booksArray) {
            JsonObject bookJson = bookElement.getAsJsonObject();

            Book book = new Book();
            book.setTitle(bookJson.get("title").getAsString());
            book.setSubjects(gson.toJson(bookJson.getAsJsonArray("subjects")));
            book.setBookshelves(gson.toJson(bookJson.getAsJsonArray("bookshelves")));
            book.setLanguages(gson.toJson(bookJson.getAsJsonArray("languages")));
            book.setCopyright(bookJson.has("copyright") && !bookJson.get("copyright").isJsonNull() ? bookJson.get("copyright").getAsBoolean() : null);
            book.setMedia_type(bookJson.has("media_type") && !bookJson.get("media_type").isJsonNull() ? bookJson.get("media_type").getAsString() : "Unknown");
            book.setFormats(gson.toJson(bookJson.getAsJsonObject("formats")));
            book.setDownload_count(bookJson.get("download_count").getAsInt());

            // Procesamiento de autores
            JsonArray authorsArray = bookJson.getAsJsonArray("authors");
            List<Author> authors = new ArrayList<>();

            for (JsonElement authorElement : authorsArray) {
                JsonObject authorJson = authorElement.getAsJsonObject();

                String name = authorJson.get("name").getAsString();
                Integer birthYear = authorJson.has("birth_year") && !authorJson.get("birth_year").isJsonNull() ? authorJson.get("birth_year").getAsInt() : null;
                Integer deathYear = authorJson.has("death_year") && !authorJson.get("death_year").isJsonNull() ? authorJson.get("death_year").getAsInt() : null;

                Optional<Author> existingAuthor = authorRepository.findByNameAndBirthYearAndDeathYear(name, birthYear, deathYear);
                Author author = existingAuthor.orElseGet(() -> new Author(name, birthYear, deathYear));

                if (!existingAuthor.isPresent()) {
                    authorRepository.save(author); // Guarda el nuevo autor
                }

                authors.add(author);
            }

            book.setAuthors(authors); // Asocia los autores con el libro
            books.add(book); // Agrega el libro a la lista
        }

        bookRepository.saveAll(books); // Guarda todos los libros
        conn.disconnect();

        System.out.println("Libros guardados en la base de datos con éxito.");
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

}
