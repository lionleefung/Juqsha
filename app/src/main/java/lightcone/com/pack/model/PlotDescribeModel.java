package lightcone.com.pack.model;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lightcone.utils.FileUtil;
import com.lightcone.utils.JsonUtil;

import java.io.InputStream;
import java.util.List;

import lightcone.com.pack.bean.PlotBean;
import lightcone.com.pack.util.res.AssetUtil;

public class PlotDescribeModel {
    private static final String TAG = "PlotDescribeModel";
    private static PlotDescribeModel instance;

    private List<PlotBean> plotBeanList;
    public static PlotDescribeModel getInstance(){
        if (instance == null) {
            instance = new PlotDescribeModel();
        }
        return instance;
    }

    private PlotDescribeModel(){
        init();
    }

    public List<PlotBean> getPlotBeanList() {
        if (plotBeanList == null) {
            init();
        }
        return plotBeanList;
    }

    public void setPlotBeanList(List<PlotBean> plotBeanList) {
        this.plotBeanList = plotBeanList;
    }

    public void init() {
        try {
            InputStream fileStream = AssetUtil.instance.getFileStream("plot/plot_describe.json");
            String jsonStr = FileUtil.readFile(fileStream);
            if (jsonStr != null) {
                plotBeanList = JsonUtil.readValue(jsonStr, new TypeReference<List<PlotBean>>() {
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "Plot init error!");
            e.printStackTrace();
        }
    }
}
