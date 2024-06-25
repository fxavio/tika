package org.tika.tika;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class TikaApplication {

    public static void parseFileWithTika(String filePath) {
        Path path = Paths.get(filePath);

        try (InputStream stream = Files.newInputStream(path)) {

            //BodyContentHandler handler = new BodyContentHandler(100000000);
            StringWriter any = new StringWriter();
            BodyContentHandler handler = new BodyContentHandler(any);
            AutoDetectParser parser = new AutoDetectParser();
            Metadata metadata = new Metadata();
            ParseContext parseContext = new ParseContext();

            // Explicitly setting the parser context for TXT files
            parseContext.set(org.apache.tika.parser.Parser.class, parser);

            parser.parse(stream, handler, metadata, parseContext);

            String content = handler.toString();
            System.out.println("File content: " + "\n" + content + "\n\n");
            System.out.println("Current directory : " + System.getProperty("user.dir"));

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (TikaException e) {
            System.err.println("Error parsing file: " + e.getMessage());
        } catch (SAXException e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(TikaApplication.class, args);

//        parseFileWithTika("sample.txt");
//        parseFileWithTika("test.txt");
//        parseFileWithTika("9781107038929_frontmatter.pdf");
          parseFileWithTika("test.pdf");
//        parseFileWithTika("test.doc");
//        parseFileWithTika("sample.docx");
//        parseFileWithTika("test.doc");
//        parseFileWithTika("sample.xlsx");
//        parseFileWithTika("sample.ppt");
//        parseFileWithTika("unique-passenger-counts-over-100-by-NZ-port-and-citizenship-year-ended-june-2020.csv");
//        parseFileWithTika("sample.sql");
//        parseFileWithTika("sample.java");
//        parseFileWithTika("sample.py");
//        parseFileWithTika("sample.c");
//        parseFileWithTika("sample.cpp");
//        parseFileWithTika("sample.cs");
//        parseFileWithTika("sample.js");
//        parseFileWithTika("sample.css");
//        parseFileWithTika("sample.html");
//        parseFileWithTika("sample.json");
//        parseFileWithTika("sample.rb");
//        parseFileWithTika("sample.php");
//        parseFileWithTika("sample.go");
//        parseFileWithTika("sample.yaml");
    }
}
