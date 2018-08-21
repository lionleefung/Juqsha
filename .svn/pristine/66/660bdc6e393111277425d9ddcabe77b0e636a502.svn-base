package lightcone.com.pack.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Plot implements Serializable {
    @JsonProperty("title")
    public String title;
    @JsonProperty("contents")
    public String contents;
    @JsonProperty("imgName")
    public String imgName;
    @JsonProperty("titletip")
    public String titletip;

    public String getInsideImage(){
        return "file:///android_asset/plot/inside_pic/10/"+imgName;
    }
}
