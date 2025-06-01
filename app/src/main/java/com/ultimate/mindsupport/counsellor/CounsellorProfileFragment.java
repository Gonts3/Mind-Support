package com.ultimate.mindsupport.counsellor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.ultimate.mindsupport.CurrentUser;
import com.ultimate.mindsupport.GetUser;
import com.ultimate.mindsupport.R;
import com.ultimate.mindsupport.SessionManager;
import com.ultimate.mindsupport.client.Client;
import com.ultimate.mindsupport.client.PersonalInformation;

public class CounsellorProfileFragment extends Fragment {
    private TextView profileName;

    public CounsellorProfileFragment(){
        super(R.layout.counsellor_profile_fragment);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.counsellor_profile_fragment, container, false);

        profileName = view.findViewById(R.id.profileName);
        Counsellor counsellor = (Counsellor) CurrentUser.getCounsellor();
        assert counsellor != null;
        profileName.setText(counsellor.getFname() + " " + counsellor.getLname());

        Button editButton = view.findViewById(R.id.counsellorCredentials);
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), CounsellorPersonalInformation.class);
            startActivity(intent);
        });

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        Counsellor counsellor = (Counsellor) CurrentUser.getCounsellor();
        if (counsellor != null) {
            TextView profileName = getView().findViewById(R.id.profileName);
            String fullName = getString(R.string.full_name, counsellor.getFname(), counsellor.getLname());
            profileName.setText(fullName);
        }
    }
}
