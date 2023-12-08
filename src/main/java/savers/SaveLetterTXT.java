package savers;

import structure.Enrollee;
import structure.Faculty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class SaveLetterTXT {

    public static void saveLetter(Enrollee enrollee, List<Faculty> faculties) {
        try {
            // Загружаем текст-шаблон из файла
            String templatePath = "src/main/resources/Letter.txt";
            String template = new String(Files.readAllBytes(Paths.get(templatePath)));

            // Заменяем вхождение [fullName] на имя абитуриента
            template = template.replace("[fullName]", enrollee.getFullName());

            // Заменяем вхождение [facultyNames] на названия всех факультетов из списка
            String facultyNames = faculties.stream()
                    .map(Faculty::getName)
                    .collect(Collectors.joining("\n"));
            template = template.replace("[facultyNames]", facultyNames);

            // Сохраняем в новый файл с именем абитуриента
            String fileName = enrollee.getFullName() + ".txt";
            Path outputPath = Paths.get("src/main/resources", fileName);
            Files.write(outputPath, template.getBytes());

            System.out.println("Letter saved to: " + outputPath.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
