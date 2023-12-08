package loaders;

import structure.Faculty;
import structure.FacultyOrEnrollee;

import java.util.List;

public interface Loader {
    public List<? extends FacultyOrEnrollee> loadData(String filePath);

}
