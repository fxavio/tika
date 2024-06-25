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
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class TikaApplication {

    public static void parseFileWithTika(String filePath) {
        Path path = Paths.get(filePath);

        try (InputStream stream = Files.newInputStream(path)) {
            StringWriter any = new StringWriter();
            BodyContentHandler handler = new BodyContentHandler(any);
            AutoDetectParser parser = new AutoDetectParser();
            Metadata metadata = new Metadata();
            ParseContext parseContext = new ParseContext();

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

    public static void parseFileFromURL(String fileURL) {
        try (InputStream stream = getFileInputStreamFromURL(fileURL)) {
            StringWriter any = new StringWriter();
            BodyContentHandler handler = new BodyContentHandler(any);
            AutoDetectParser parser = new AutoDetectParser();
            Metadata metadata = new Metadata();
            ParseContext parseContext = new ParseContext();

            parseContext.set(org.apache.tika.parser.Parser.class, parser);

            parser.parse(stream, handler, metadata, parseContext);

            String content = handler.toString();
            System.out.println("File content: " + "\n" + content + "\n\n");
        } catch (IOException e) {
            System.err.println("Error reading file from URL: " + e.getMessage());
        } catch (TikaException e) {
            System.err.println("Error parsing file from URL: " + e.getMessage());
        } catch (SAXException e) {
            System.err.println("Error processing file from URL: " + e.getMessage());
        }
    }

    public static InputStream getFileInputStreamFromURL(String fileURL) throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            return httpConn.getInputStream();
        } else {
            throw new IOException("Failed to fetch file from URL. Server replied with HTTP code: " + responseCode);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(TikaApplication.class, args);

        // Parse a file from the local filesystem
        parseFileWithTika("test.pdf");

        // Parse a file directly from a URL
//        parseFileFromURL("https://res.cloudinary.com/rxavio/raw/upload/v1719332301/Sample-SQL-File-1000-Rows_m1koev.sql");
        parseFileFromURL("https://res.cloudinary.com/rxavio/image/upload/v1719332051/PHP_and_MySQL.doc_ibpqkz.pdf");
    }
}