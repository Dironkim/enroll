package loaders;

import structure.FacultyOrEnrollee;

import java.util.List;

public interface Loader {
    public List<FacultyOrEnrollee> loadData(String filePath);

}
