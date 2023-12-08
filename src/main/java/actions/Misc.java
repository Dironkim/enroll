package actions;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import structure.Enrollee;
import structure.Faculty;
import structure.FacultyOrEnrollee;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Misc {
    public static List<String> loadSubjectOrder() {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader("src/main/resources/Subjects.csv"))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            String[] orderArray = reader.readNext();
            return Arrays.asList(orderArray);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace(); // Handle file reading errors
        }
        return null;
    }
    public static List<FacultyOrEnrollee> toAncestor(List<?extends FacultyOrEnrollee> inputList){
        List<FacultyOrEnrollee> result = inputList.stream()
                .map(element -> FacultyOrEnrollee.builder()
                        .name(element.getName())
                        .subjectList(element.getSubjectList())
                        .build())
                .collect(Collectors.toList());
        return result;
    }
    public static List<Enrollee>toChildEnrollee(List<FacultyOrEnrollee> inputList){
        List<Enrollee> result = inputList.stream()
                .map(element -> Enrollee.builder()
                        .name(element.getName())
                        .subjectList(element.getSubjectList())
                        .build())
                .collect(Collectors.toList());
        return result;
    }
    public static List<Faculty>toChildFaculty(List<FacultyOrEnrollee> inputList){
        List<Faculty> result = inputList.stream()
                .map(element -> Faculty.builder()
                        .name(element.getName())
                        .subjectList(element.getSubjectList())
                        .build())
                .collect(Collectors.toList());
        return result;
    }
}
