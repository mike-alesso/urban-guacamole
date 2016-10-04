package com.example.michael.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.michael.myapplication.Helpers.Database;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.models.Term;

import java.util.ArrayList;
import java.util.Date;

import static com.example.michael.myapplication.R.id.lvTerms;

/**
 * Created by michael on 9/3/16.
 */
public class Terms extends Fragment {

    Database helper;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //populate initial terms for testing
        rootView = inflater.inflate(R.layout.terms, container, false);

        populateTermsList();
        return rootView;
    }

    private void populateTermsList() {
        // Construct the data source
        helper = new Database(getActivity());

        ArrayList<Term> arrayOfTerms = helper.getAllTerms();
        // Create the adapter to convert the array to views
        CustomTermsAdapter adapter = new CustomTermsAdapter(getActivity(), arrayOfTerms);
        // Attach the adapter to a ListView
        ListView listView = (ListView) rootView.findViewById(lvTerms);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("termIndex", position + 1);
                //startActivity(newActivity);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment termDetailFragment = new TermDetail();
                termDetailFragment.setArguments(bundle);
                ft.replace(R.id.content_frame, termDetailFragment);
                ft.commit();
            }
        });

        Button btn_newTerm=(Button)rootView.findViewById(R.id.BnewtermButton);

        btn_newTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new TermDetail());
                ft.commit();
            }
        });
    }
}
