package structure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
@XmlAccessorType(XmlAccessType.NONE)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Subject {
    @XmlAttribute
    public String name;

    @XmlAttribute
    public int points;
}
