package lightcone.com.pack.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.itheima.library.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lightcone.com.pack.R;

public class ShowImageDialog extends Dialog {
    private Context context;

    @BindView(R.id.image)
    PhotoView image;

    private String imagePath;

    public ShowImageDialog(Context context, String imagePath) {
        super(context,R.style.Dialog);
        this.context = context;
        this.imagePath = imagePath;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_show_image);
        ButterKnife.bind(this);
        image.enable();
        Glide.with(context)
                .load(imagePath)
                .into(image);
    }

    @OnClick(R.id.image)
    void clickImage(View v) {
        dismiss();
    }
}
