package com.oni.ibmtranslator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.BuildConfig;
import com.android.volley.Request;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oni.ibmtranslator.model.LanguageModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFragment extends Fragment  implements View.OnKeyListener,GetDataRequest.OnGetResultsListener{

    ProgressBar progressBar;
    TextInputEditText mInText;
    TextView mOutText;
    Button btnSourceLang;
    Button btnTargetLang;
    List<LanguageModel> all_languages = new ArrayList<>();
    List<String> all_language_names = new ArrayList<>();
    List<String> all_language_ids = new ArrayList<>();
    boolean[] all_language_status;
    String[] added_lang_array;
    List<String> added_language_names = new ArrayList<>();
    List<String> added_language_ids = new ArrayList<>();
    String source_lang = "";
    String target_lang = "";


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID,
                    Context.MODE_PRIVATE);
            Gson gson = new Gson();
            JSONObject all_language_object = new JSONObject(sharedpreferences.getString("language_list",""));
            JSONArray lang_array = all_language_object.getJSONArray("languages");
            all_languages.addAll((Collection<? extends LanguageModel>) gson.fromJson(lang_array.toString(), new TypeToken<List<LanguageModel>>(){}.getType()));

            boolean[] boolArray = new boolean[all_languages.size()];
            Arrays.fill(boolArray, false);

           // Toast.makeText(getContext(), String.valueOf(boolArray.length), Toast.LENGTH_SHORT).show();
            added_lang_array = sharedpreferences.getString("added_language_list","").split(",");
            int index = 0;
            for (LanguageModel language: all_languages){
                all_language_names.add(language.getName());
                all_language_ids.add(language.getLanguage());

                if (Arrays.asList(added_lang_array).contains(String.valueOf(index))) {
                    added_language_names.add(language.getName());
                    added_language_ids.add(language.getLanguage());
                    boolArray[index] = true;
                }
                else {
                    boolArray[index] = false;
                }
                index++;
            }

            all_language_status = boolArray;


        }catch (Exception e){

        }

        mInText = (TextInputEditText) view.findViewById(R.id.text_in);
        mInText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        mOutText = (TextView)view.findViewById(R.id.text_out);
        btnSourceLang = view.findViewById(R.id.button_source_lang);
        btnTargetLang = view.findViewById(R.id.button_target_lang);

        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        btnSourceLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose Source Language");
                builder.setItems(all_language_names.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            default:
                                source_lang = all_language_ids.get(which);
                                btnSourceLang.setText(all_language_names.get(which));
                                break;
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnTargetLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose Target Language");
                builder.setItems(added_language_names.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            default:
                                target_lang = added_language_ids.get(which);
                                btnTargetLang.setText(added_language_names.get(which));
                                break;
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        view.findViewById(R.id.button_translate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String text = String.valueOf(mInText.getText());

                if (source_lang == "") {
                    Toast.makeText(getContext(), "Select Source Language", Toast.LENGTH_SHORT).show();
                }
                else if (target_lang == "") {
                    Toast.makeText(getContext(), "Select Target Language", Toast.LENGTH_SHORT).show();
                }
                else if (text.length() == 0) {
                    Toast.makeText(getContext(), "Type text", Toast.LENGTH_SHORT).show();
                }
                else if (source_lang == target_lang) {
                    mOutText.setText(text);
                }
                else if (mOutText == null) {
                    Toast.makeText(getContext(), "Type text", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    final String main_url = "https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/3caa6664-1e85-45c9-b2cf-e7a13eeccea3/v3/translate?version=2018-05-01";

                    Map<String, String> params =  new HashMap<String, String>();
                    params.put("text", text);
                    params.put("model_id", source_lang + "-" + target_lang);

                    GetDataRequest dataRequest = new GetDataRequest(getContext());
                    dataRequest.JsonRequest(Request.Method.POST,getContext(),main_url,new JSONObject(params),100);
                    dataRequest.onGetResultsData(MainFragment.this);
                }

            }
        });

        view.findViewById(R.id.button_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mInText.setText("");
                mOutText.setText("");

            }
        });

        view.findViewById(R.id.button_to_quary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent quaryIntent = new Intent(getContext(), Quary.class);
                startActivity(quaryIntent);

            }

        });

        view.findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add Language");

                builder.setMultiChoiceItems(all_language_names.toArray(new String[0]), all_language_status, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // user checked or unchecked a box
                        if (isChecked) {
                            if (!Arrays.asList(added_lang_array).contains(String.valueOf(which))) {
                                int current_num = added_lang_array.length;
                                String[] array = new String[current_num + 1];
                                for (int i = 0;i<current_num;i++) {
                                    array[i] = added_lang_array[i];
                                }
                                array[current_num] = String.valueOf(which);
                                added_language_names.add(all_language_names.get(which));
                                added_language_ids.add(all_language_ids.get(which));
                                added_lang_array = array;
                            }
                        }
                        else {
                            if (Arrays.asList(added_lang_array).contains(String.valueOf(which))) {

                                if (added_lang_array.length > 2) {
                                    int current_num = added_lang_array.length;
                                    String[] array = new String[current_num - 1];
                                    final int index = Arrays.asList(added_lang_array).indexOf(String.valueOf(which));
                                    added_language_names.remove(index);
                                    added_language_ids.remove(index);
                                    int j = 0;
                                    for (int i = 0;i<current_num;i++) {
                                        if (i != index)
                                        {
                                            array[j] = added_lang_array[i];
                                            j += 1;
                                        }

                                    }
                                    added_lang_array = array;
                                }

                            }
                        }
                    }
                });

                builder.setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user clicked OK
                        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID,
                                Context.MODE_PRIVATE);
                        final SharedPreferences.Editor editor = sharedpreferences.edit();
                        String str = "";
                        for (int i =0 ; i<added_lang_array.length - 1; i++) {
                            str += (added_lang_array[i] + ",");
                        }
                        str += added_lang_array[added_lang_array.length - 1];
                        editor.putString("added_language_list", str);
                        editor.commit();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        Log.i("value entered", Integer.toString(keyCode));
        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER)) {
            // Perform action on key press
            return true;
        }

        return false;
    }

    @Override
    public void onGetResultsData(JSONObject object, int request_code) {
        progressBar.setVisibility(View.INVISIBLE);
        if (object != null) {
            try {
                Gson gson = new Gson();
                final JSONArray data = object.getJSONArray("translations");
                final JSONObject translated_data = data.getJSONObject(0);
                final String text = (String) translated_data.get("translation");
                mOutText.setText(text);
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        else {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }

    }
}
