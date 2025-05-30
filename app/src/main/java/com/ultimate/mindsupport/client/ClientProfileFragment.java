package com.ultimate.mindsupport.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.ultimate.mindsupport.CurrentUser;
import com.ultimate.mindsupport.R;

public class ClientProfileFragment extends Fragment {
    private CardView myCard;
    private TextView profileName;

    public ClientProfileFragment(){
        super(R.layout.client_profile_fragment);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.client_profile_fragment, container, false);

        profileName = view.findViewById(R.id.profileName);
        Client client = (Client) CurrentUser.getClient();
        assert client != null;
        profileName.setText(client.getUsername());

        Button editButton = view.findViewById(R.id.clientCredentials);
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), PersonalInformation.class);
            startActivity(intent);
        });

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        Client client = (Client) CurrentUser.getClient();
        String username = client.getUsername(); // or whatever getter you use
        profileName.setText(username); // update the TextView
    }
}
