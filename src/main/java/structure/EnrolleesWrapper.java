package structure;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "enrollees")
@XmlAccessorType(XmlAccessType.NONE)
public class EnrolleesWrapper {

    private List<Enrollee> enrollees;

    @XmlElement(name = "enrollee")
    public List<Enrollee> getEnrollees() {
        return enrollees;
    }

    public void setEnrollees(List<Enrollee> enrollees) {
        this.enrollees = enrollees;
    }
}