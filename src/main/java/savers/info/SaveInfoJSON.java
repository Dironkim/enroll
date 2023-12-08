package savers.info;

import com.squareup.moshi.Moshi;
import okio.BufferedSink;
import okio.Okio;
import structure.Enrollee;
import structure.Faculty;
import structure.FacultyOrEnrollee;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SaveInfoJSON implements SaverInfo{
    @Override
    public void saveData(String filePath, List<? extends FacultyOrEnrollee> dataList) {
        // Создание экземпляра Moshi с поддержкой адаптера для полиморфизма
        Moshi moshi = new Moshi.Builder()
                .add(createJsonAdapterFactory())
                .build();

        // Создание адаптера для списка FacultyOrEnrollee
        JsonAdapter<List<? extends FacultyOrEnrollee>> jsonAdapter = moshi.adapter(
                com.squareup.moshi.Types.newParameterizedType(List.class, FacultyOrEnrollee.class)
        );

        // Сериализация и сохранение в файл
        try {
            // Записываем JSON в строку
            File file = new File(filePath);
            String fileContents= jsonAdapter.toJson(dataList);
            try (BufferedSink bufferedSink = Okio.buffer(Okio.sink(new File(filePath)))) {
                // Используем JsonWriter с отступами
                com.squareup.moshi.JsonWriter jsonWriter = com.squareup.moshi.JsonWriter.of(bufferedSink);
                jsonWriter.setIndent("    ");
                jsonAdapter.toJson(jsonWriter, dataList);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Обработка ошибок записи файла
        }
    }

    // Создание адаптера для поддержки полиморфизма
    private PolymorphicJsonAdapterFactory<FacultyOrEnrollee> createJsonAdapterFactory() {
        return PolymorphicJsonAdapterFactory.of(FacultyOrEnrollee.class, "type")
                .withSubtype(Faculty.class, "faculty")
                .withSubtype(Enrollee.class, "enrollee");
    }
}
