package structure;

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
public class Faculty extends FacultyOrEnrollee{
    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @XmlElement(name = "requirement")
    public List<Subject> getSubjectList() {
        return subjectList;
    }
    public Faculty() {
    }
}
