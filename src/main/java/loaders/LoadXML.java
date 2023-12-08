package loaders;

import structure.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

public class LoadXML implements Loader{
    @Override
    public List<? extends FacultyOrEnrollee> loadData(String filePath) {
        try {
            Class<?> targetClass = determineTargetClass(filePath);

            // Создание контекста JAXB для класса, который определен по имени файла
            JAXBContext context = JAXBContext.newInstance(targetClass);

            // Создание анмаршаллера
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Чтение данных из файла
            File file = new File(filePath);
            Object rootObject = unmarshaller.unmarshal(file);

            if (rootObject instanceof FacultiesWrapper) {
                return ((FacultiesWrapper) rootObject).getFaculties();
            } else if (rootObject instanceof EnrolleesWrapper) {
                return ((EnrolleesWrapper) rootObject).getEnrollees();
            } else {
                // Вернуть пустой список или обработать по-другому
                return List.of();
            }
        } catch (JAXBException e) {
            e.printStackTrace(); // Обработка ошибок JAXB
            return null;
        }
    }
    private Class<?> determineTargetClass(String filePath) {
        if (filePath.contains("Facult")) {
            return FacultiesWrapper.class;
        } else  {
            return EnrolleesWrapper.class;
        }
    }
}
