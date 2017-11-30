package edu.uco.tlawyer.whereyouat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class UsersOnlineViewHolder extends RecyclerView.ViewHolder {

    TextView emailText;

    public UsersOnlineViewHolder(View itemView) {
        super(itemView);
        emailText = (TextView) itemView.findViewById(R.id.show_email);
    }
}
