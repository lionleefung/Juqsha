package lightcone.com.pack.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class PlotBean implements Serializable {
    @JsonProperty("plot_id")
    public int plot_id;
    @JsonProperty("plot_name")
    public String plot_name;
    @JsonProperty("plot_pernum")
    public int plot_pernum;
    @JsonProperty("plot_type")
    public int plot_type;
    @JsonProperty("plot_pic")
    public String plot_pic;
    @JsonProperty("plot_describe")
    public String plot_describe;
    @JsonProperty("plot_plays")
    public List<Play> plot_plays;
    @JsonProperty("plot_roles")
    public List<Role> plot_roles;

    public String getImage() {
        return "file:///android_asset/plot/bg_pic/" + plot_pic;
    }
}
