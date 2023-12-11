package structure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.List;
@Data
@SuperBuilder
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.NONE)
public class FacultyOrEnrollee {
    public String name;
    public List<Subject> subjectList;
    public FacultyOrEnrollee() {
        subjectList=new ArrayList<>();
    }
}
