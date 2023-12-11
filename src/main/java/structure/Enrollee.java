package structure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
@SuperBuilder
@XmlAccessorType(XmlAccessType.NONE)
public class Enrollee extends FacultyOrEnrollee {
    @XmlAttribute(name = "fullName")
    public String getFullName() {
        return name;
    }
    public void setFullName(String fullName) {
        this.name = fullName;
    }
    @XmlElement(name = "subject")
    public List<Subject> getSubjectList() {
        return subjectList;
    }
    public Enrollee() {
    }
}
