package edu.uco.tlawyer.whereyouat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class RequestFragment extends DialogFragment {

    public static RegisteredUser[] users = new RegisteredUser[4];
    public static String[] names = new String[4];
    PickContactListener pcListener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        users[0] = new RegisteredUser("David Babb", "4053170048");
        users[1] = new RegisteredUser("Addison Toscani", "4052031838");
        users[2] = new RegisteredUser("Seth Howell", "4052099025");
        users[3] = new RegisteredUser("Trevor Lawyer", "4057068256");
        for (int i = 0; i < users.length; i++) {
            names[i] = users[i].getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Contact").setItems(names, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pcListener.onContactClick(i, RequestFragment.this);
            }
        });
        return builder.create();
    }

    public interface PickContactListener {
        void onContactClick(int i, DialogFragment dialogFragment);
    }

    public void onAttach(Context c) {
        super.onAttach(c);
        try {
            pcListener = (PickContactListener) c;
        } catch (ClassCastException e) {
            throw new ClassCastException(c.toString() + " must implement PickContactListener");
        }
    }
}
