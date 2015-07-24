package watafuru.com.stormy.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import watafuru.com.stormy.R;

/**
 * Created by Watafuru on 6/24/2015.
 */
public class AlertDialogFragment extends DialogFragment {

    ;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.Error_Title);
        builder.setMessage(R.string.Error_Body);
        //null makes the button do nothing
        builder.setPositiveButton(R.string.OK, null);
        AlertDialog dialog = builder.create();
        return dialog;

    }
}
