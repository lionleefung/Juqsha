package lightcone.com.pack.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import lightcone.com.pack.R;

public class TipDialog extends Dialog implements android.view.View.OnClickListener {
    private Context context;
    private Button   ok;
    private Button   no;
    private TextView tip;
    private LeaveMyDialogListener2 listener2;
    private String text;

    public interface LeaveMyDialogListener2{
        public void onClick(View view);
    }

    public TipDialog(Context context) {
        super(context);

        this.context = context;
    }

    public TipDialog(Context context, int theme,String text, LeaveMyDialogListener2 listener2) {
        super(context,theme);
        this.text= text;
        this.context = context;
        this.listener2 = listener2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_tip);
        ok = (Button)findViewById(R.id.Ok);
        ok.setOnClickListener(this);
        no = (Button)findViewById(R.id.No);
        no.setOnClickListener(this);
        tip = findViewById(R.id.tip);
        tip.setText(text);
    }

    @Override
    public void onClick(View v) {
        listener2.onClick(v);
    }
}
