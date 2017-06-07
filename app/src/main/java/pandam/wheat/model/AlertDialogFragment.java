package pandam.wheat.model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import pandam.wheat.R;

public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(getString(R.string.error_title))
                .setMessage(getString(R.string.error_message))
                .setPositiveButton(R.string.error_ok_button_text, null);

        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }
}
