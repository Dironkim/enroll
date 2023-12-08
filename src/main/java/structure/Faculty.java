package structure;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
@SuperBuilder
@XmlAccessorType(XmlAccessType.NONE)
public class Faculty extends FacultyOrEnrollee{
    @XmlAttribute
    public String getName() {
        return name;
    }
    @XmlElement(name = "requirement")
    public List<Subject> getSubjectList() {
        return subjectList;
    }
}
