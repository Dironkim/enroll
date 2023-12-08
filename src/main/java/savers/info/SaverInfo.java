package savers.info;

import structure.FacultyOrEnrollee;

import java.util.List;

public interface SaverInfo {
    public void saveData(String filePath, List<? extends FacultyOrEnrollee> dataList);
}
