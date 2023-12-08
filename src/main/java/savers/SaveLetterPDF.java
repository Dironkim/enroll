package savers;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import structure.Enrollee;
import structure.Faculty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName.TIMES_BOLD;

public class SaveLetterPDF {
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

            // Создаем PDF-документ
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            // Добавляем текст в PDF-документ
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont( new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700);

                // Добавляем содержимое письма в PDF
                contentStream.showText(template);

                contentStream.endText();
            }

            // Сохраняем PDF в новый файл с именем абитуриента в директории src/main/resources
            String fileName = enrollee.getFullName() + ".pdf";
            Path outputPath = Paths.get("src/main/resources", fileName);
            document.save(outputPath.toAbsolutePath().toString());
            document.close();

            System.out.println("Letter saved to: " + outputPath.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
