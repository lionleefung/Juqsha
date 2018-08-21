package lightcone.com.pack.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Role implements Serializable {
    @JsonProperty("name")
    public String name;
    @JsonProperty("rolelabel")
    public String rolelabel;
    @JsonProperty("relations")
    public List<Relation> relations;
}
