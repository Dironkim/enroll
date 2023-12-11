package loaders;

import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;
import structure.Enrollee;
import structure.Faculty;
import structure.FacultyOrEnrollee;
import com.squareup.moshi.Moshi;

import com.squareup.moshi.JsonAdapter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;
public class LoadJSON implements Loader{
    @Override
    public List<FacultyOrEnrollee> loadData(String filePath) {
        try {
            // Создание экземпляра Moshi с поддержкой адаптера для полиморфизма
            Moshi moshi = new Moshi.Builder()
                    .add(createJsonAdapterFactory())
                    .build();

            // Создание адаптера для списка FacultyOrEnrollee
            JsonAdapter<List<FacultyOrEnrollee>> jsonAdapter = moshi.adapter(
                    com.squareup.moshi.Types.newParameterizedType(List.class, FacultyOrEnrollee.class)
            );
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
            return jsonAdapter.fromJson(jsonContent);
        } catch (IOException e) {
            e.printStackTrace(); // Обработка ошибок чтения файла
            return null;
        }
    }


    // Создание адаптера для поддержки полиморфизма
    private PolymorphicJsonAdapterFactory<FacultyOrEnrollee> createJsonAdapterFactory() {
        return PolymorphicJsonAdapterFactory.of(FacultyOrEnrollee.class, "type")
                .withSubtype(Faculty.class, "faculty")
                .withSubtype(Enrollee.class, "enrollee");
    }
}
