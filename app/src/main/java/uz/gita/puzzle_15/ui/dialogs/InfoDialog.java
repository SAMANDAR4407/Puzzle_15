package uz.gita.puzzle_15.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import uz.gita.puzzle_15.R;


public class InfoDialog extends AlertDialog {

    private OnPositive onPositive;

    public InfoDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info);

        TextView textView = findViewById(R.id.link);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        setCancelable(false);
        Button btnOkay = findViewById(R.id.btn_okay);

        btnOkay.setOnClickListener(v -> onPositive.onClick());
    }

    public void setOnPositiveListener(OnPositive onPositive){
        this.onPositive = onPositive;
    }

    public interface OnPositive{
        void onClick();
    }
}
