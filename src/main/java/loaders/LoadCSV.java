package loaders;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import structure.FacultyOrEnrollee;
import structure.Subject;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadCSV implements Loader{

    @Override
    public List<FacultyOrEnrollee> loadData(String filePath) {
        return loadFoEsFromCSV(filePath);
    }

    public List<FacultyOrEnrollee> loadFoEsFromCSV(String filePath) {
        List<FacultyOrEnrollee> FoEList = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build()) // Set separator here
                .build()) {
            String[] header = reader.readNext(); // Пропустить заголовок

            String[] line;
            while ((line = reader.readNext()) != null) {
                String fullName = line[0];
                List<Subject> subjects = parseSubjects(header, line);

                var FoE = FacultyOrEnrollee.builder()
                        .name(fullName)
                        .subjectList(subjects)
                        .build();

                FoEList.add(FoE);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace(); // Обработка ошибок чтения файла
        }

        return FoEList;
    }

    // Метод для парсинга предметов и их баллов из строки CSV
    private List<Subject> parseSubjects(String[] header, String[] line) {
        List<Subject> subjects = new ArrayList<>();

        for (int i = 1; i < line.length; i++) {
            if (!line[i].isEmpty()) {
                String subjectName = header[i];
                int points = Integer.parseInt(line[i]);
                subjects.add(new Subject(subjectName, points));
            }
        }

        return subjects;
    }



}
