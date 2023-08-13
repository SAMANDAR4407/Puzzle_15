package uz.gita.puzzle_15.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import uz.gita.puzzle_15.R;


public class StopGameDialog extends AlertDialog {

    private OnPositive onPositive;
    private OnNegative onNegative;
    private OnNeutral onNeutral;

    public StopGameDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_stop_game);

        setCancelable(false);

        Button btnYes = findViewById(R.id.btn_yes_delete);
        Button btnNo = findViewById(R.id.btn_no_delete);
        Button dismiss = findViewById(R.id.btn_neutral);

        btnYes.setOnClickListener(v -> {
            onPositive.onClick();
        });

        btnNo.setOnClickListener(v -> {
            onNegative.onClick();
        });

        dismiss.setOnClickListener(v -> {
            onNeutral.onClick();
        });

    }


    public void setOnPositiveListener(OnPositive onPositive){
        this.onPositive = onPositive;
    }
    public void setOnNegativeListener(OnNegative onNegative){
        this.onNegative = onNegative;
    }
    public void setOnNeutralListener(OnNeutral onNeutral){
        this.onNeutral = onNeutral;
    }


    public interface OnPositive{
        void onClick();
    }
    public interface OnNegative{
        void onClick();
    }
    public interface OnNeutral{
        void onClick();
    }

}
