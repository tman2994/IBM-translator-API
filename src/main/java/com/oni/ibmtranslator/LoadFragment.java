package com.oni.ibmtranslator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.BuildConfig;
import com.android.volley.Request;

import org.json.JSONObject;

public class LoadFragment extends Fragment  implements GetDataRequest.OnGetResultsListener {
    SharedPreferences sharedpreferences;
    ProgressBar progressBar;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_load, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedpreferences = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID,
                Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        if (!sharedpreferences.contains("added_language_list")) {
            editor.putString("added_language_list", "9,10");
            editor.commit();
        }

        final String main_url = "https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/3caa6664-1e85-45c9-b2cf-e7a13eeccea3/v3/identifiable_languages?version=2018-05-01";
        GetDataRequest dataRequest = new GetDataRequest(getContext());
        dataRequest.JsonRequest(Request.Method.GET,getContext(),main_url,new JSONObject(),100);
        dataRequest.onGetResultsData(LoadFragment.this);
    }

    @Override
    public void onGetResultsData(JSONObject object, int request_code) {
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("language_list", String.valueOf(object));
        editor.commit();
        progressBar.setVisibility(View.INVISIBLE);
        NavHostFragment.findNavController(LoadFragment.this)
                .navigate(R.id.action_LoadFragment_to_MainFragment);
    }
}
