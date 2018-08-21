package lightcone.com.pack.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import lightcone.com.pack.R;

public class SelectDialog extends Dialog implements android.view.View.OnClickListener {
    private Context context;
    private Button   btnok;
    private LeaveMyDialogListener listener;

    public interface LeaveMyDialogListener{
        public void onClick(View view);
    }

    public SelectDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public SelectDialog(Context context,int theme,LeaveMyDialogListener listener) {
        super(context,theme);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialong_select_role);
         btnok = (Button)findViewById(R.id.yes_select_dialog);
         btnok.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        listener.onClick(v);
    }
}