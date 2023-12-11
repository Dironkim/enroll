package savers.info;

import actions.Misc;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.exceptions.CsvValidationException;
import structure.Enrollee;
import structure.Faculty;
import structure.FacultyOrEnrollee;
import structure.Subject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SaveInfoCSV implements SaverInfo{
    private final List<String> subjectOrder; // Порядок предметов
    public SaveInfoCSV(){
        subjectOrder=Misc.loadSubjectOrder();
    }
    // Загрузка порядка предметов из файла
    @Override
    public void saveData(String filePath, List<? extends FacultyOrEnrollee> dataList) {
        try (CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(filePath))
                .withSeparator(';')
                .build()) {
            // Записать заголовок
            writer.writeNext(getHeader(dataList.get(0)));

            // Записать данные
            for (FacultyOrEnrollee data : dataList) {
                writer.writeNext(getData(data));
            }
        } catch (IOException e) {
            e.printStackTrace(); // Обработка ошибок записи файла
        }
    }

    private String[] getHeader(FacultyOrEnrollee foe) {
        // Создать заголовок
        String[] header = new String[subjectOrder.size() + 1];
        header[0] = foe instanceof Enrollee ? "ФИО" : "Факультет";
        header[1]= foe instanceof  Enrollee? "":"Проходной";
        for (int i = 2; i < subjectOrder.size(); i++) {
            header[i] = subjectOrder.get(i);
        }
        return header;
    }

    // Метод для получения данных
    private String[] getData(FacultyOrEnrollee foe) {
        List<Subject> subjects = foe.getSubjectList();
        String[] data = new String[subjectOrder.size() + 1];
        data[0] = foe.getName();

        for (int i = 1; i < subjectOrder.size(); i++) {
            String subjectName = subjectOrder.get(i);
            Subject subject = findSubject(subjects, subjectName);
            data[i] = (subject != null) ? String.valueOf(subject.getPoints()) : "";
        }
        return data;
    }

    // Найти предмет в списке
    private Subject findSubject(List<Subject> subjects, String subjectName) {
        for (Subject subject : subjects) {
            if (subject.getName().equals(subjectName)) {
                return subject;
            }
        }
        return null;
    }
}
