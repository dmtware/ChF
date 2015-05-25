package com.dmtware.chf;

/**
 * Created by tzay on 22/05/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TokenFragment extends Fragment{

    TextView textViewToken;
    ImageView imageViewTick;
    Button buttonOpenCatalog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.token_fragment, container, false);

        // initialise widgets
        textViewToken = (TextView) view.findViewById(R.id.textViewToken);
        imageViewTick = (ImageView) view.findViewById(R.id.imageViewTick);
        buttonOpenCatalog = (Button) view.findViewById(R.id.buttonOpenCatalog);

        buttonOpenCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCatalog();
            }
        });

        return view;
    }

    // sets token and enables views
    public void setTokenText(String token){
        textViewToken.setText(token);
        imageViewTick.setVisibility(View.VISIBLE);
        buttonOpenCatalog.setEnabled(true);
    }

    public void openCatalog(){
        Intent intent = new Intent(getActivity(), CatalogActivity.class);
        startActivity(intent);
    }
}