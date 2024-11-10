package org.example.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "subjects", columnDefinition = "TEXT")
    private String subjects;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    private List<Author> authors = new ArrayList<>();;

    @Column(name = "translators", columnDefinition = "TEXT")
    private String translators;

    @Column(name = "bookshelves", columnDefinition = "TEXT")
    private String bookshelves;

    @Column(name = "languages", columnDefinition = "TEXT")
    private String languages;

    @Column(name = "copyright")
    private Boolean copyright;

    @Column(name = "media_type", nullable = false)
    private String media_type;

    @Column(name = "formats", columnDefinition = "TEXT")
    private String formats;

    @Column(name = "download_count")
    private int download_count;

    public Book() {}

    public Book(String title, String subjects, List<Author> authors, String translators, String bookshelves, String languages, Boolean copyright, String media_type, String formats, int download_count) {
        this.title = title;
        this.subjects = subjects;
        this.authors = authors;
        this.translators = translators;
        this.bookshelves = bookshelves;
        this.languages = languages;
        this.copyright = copyright;
        this.media_type = media_type;
        this.formats = formats;
        this.download_count = download_count;
    }

    // Getters y setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public String getTranslators() {
        return translators;
    }

    public void setTranslators(String translators) {
        this.translators = translators;
    }

    public String getBookshelves() {
        return bookshelves;
    }

    public void setBookshelves(String bookshelves) {
        this.bookshelves = bookshelves;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public Boolean getCopyright() {
        return copyright;
    }

    public void setCopyright(Boolean copyright) {
        this.copyright = copyright;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getFormats() {
        return formats;
    }

    public void setFormats(String formats) {
        this.formats = formats;
    }

    public int getDownload_count() {
        return download_count;
    }

    public void setDownload_count(int download_count) {
        this.download_count = download_count;
    }
}

