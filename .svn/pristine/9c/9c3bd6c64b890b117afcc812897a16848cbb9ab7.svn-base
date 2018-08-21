package lightcone.com.pack.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import lightcone.com.pack.R;

    public class BackSelectDialog extends Dialog implements android.view.View.OnClickListener {
        private Context context;
        private Button   btnok;
        private Button   btnno;
        private LeaveMyDialogListener2 listener2;

        public interface LeaveMyDialogListener2{
            public void onClick(View view);
        }

        public BackSelectDialog(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
            this.context = context;
        }

        public BackSelectDialog(Context context, int theme, LeaveMyDialogListener2 listener2) {
            super(context,theme);
            // TODO Auto-generated constructor stub
            this.context = context;
            this.listener2 = listener2;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            this.setContentView(R.layout.dialog_back_role);
            btnok = (Button)findViewById(R.id.yes_back_dialog);
            btnok.setOnClickListener(this);
            btnno = (Button)findViewById(R.id.no_back_dialog);
            btnno.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            listener2.onClick(v);
        }
}
