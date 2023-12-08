package savers.info;

import structure.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.List;

public class SaveInfoXML implements SaverInfo{
    @Override
    public void saveData(String filePath, List<? extends FacultyOrEnrollee> dataList) {
        try {
            // Создание контекста JAXB для класса FacultiesWrapper
            JAXBContext context = JAXBContext.newInstance(
                    (dataList.get(0) instanceof Faculty) ? FacultiesWrapper.class : EnrolleesWrapper.class);

            // Создание маршаллера
            Marshaller marshaller = context.createMarshaller();

            // Установка свойства для форматированного вывода XML
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            if (dataList.get(0) instanceof Faculty)  {
            // Обертка для списка FacultyOrEnrollee
                FacultiesWrapper wrapper = new FacultiesWrapper();
                wrapper.setFaculties((List<Faculty>) dataList);
                marshaller.marshal(wrapper, new File(filePath));
            }
            else {
                EnrolleesWrapper wrapper = new EnrolleesWrapper();
                wrapper.setEnrollees((List<Enrollee>) dataList);
                marshaller.marshal(wrapper, new File(filePath));
            }
            // Маршаллинг и сохранение в файл

        } catch (JAXBException e) {
            e.printStackTrace(); // Обработка ошибок JAXB
        }
    }
}
