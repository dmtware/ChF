package com.dmtware.chf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by tzay on 22/05/2015.
 */
public class LoginFragment extends Fragment {

    private EditText editTextUserName;
    private EditText editTextPassword;

    // interface for the main activity
    LoginListener activityCommander;

    public interface LoginListener{
        void getLoginDetails(String userName, String password);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            activityCommander = (LoginListener)activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_fragment, container, false);

        // widgets
        editTextUserName = (EditText)view.findViewById(R.id.editTextUserName);
        editTextPassword = (EditText)view.findViewById(R.id.editTextPassword);
        final Button button = (Button)view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked(v);
            }
        });
        // request focus
        editTextUserName.requestFocus();
        editTextUserName.setRawInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }

    // login button
    public void buttonClicked(View view){
        activityCommander.getLoginDetails(editTextUserName.getText().toString(), editTextPassword.getText().toString());

    }
}
