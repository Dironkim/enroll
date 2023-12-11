package savers;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import structure.Enrollee;
import structure.Faculty;

import java.io.FileOutputStream;
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
            Document document = new Document();
            String fileName = enrollee.getFullName() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/"+ fileName));
            document.open();
            Font font = FontFactory.getFont("/fonts/DejaVuSans.ttf", "cp1251", BaseFont.EMBEDDED, 10);
            Paragraph paragraph = new Paragraph(template, font);

            document.add(paragraph);
            document.close();
            System.out.println("Letter saved ");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
