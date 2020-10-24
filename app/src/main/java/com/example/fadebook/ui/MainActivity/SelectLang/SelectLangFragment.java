package com.example.fadebook.ui.MainActivity.SelectLang;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blongho.country_data.World;
import com.example.fadebook.R;
import com.example.fadebook.pojo.modules.LangInfo;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectLangFragment extends Fragment {
    private static final String TAG = "SelectLangFragment";
    private LangInfo langInfo = new LangInfo();
    private LanguagesAdapter adapter;
    @BindView(R.id.powerSpinner)
    MaterialSpinner spinnerView;
    @BindView(R.id.recycler_lang)
    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select_lang, container, false);
        ButterKnife.bind(this, v);
        fillArray(langInfo);
        spinnerView.setItems(langInfo.getLang());
        spinnerView.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                ///
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LanguagesAdapter(getActivity(), langInfo);
        recyclerView.setAdapter(adapter);

        return v;
    }

    private LangInfo fillArray(LangInfo lang) {


        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();

                countries.add(country);

        }
        //Collections.sort(countries);
        for (String country : countries) {
            System.out.println(country);
        }
        String temp;
        World.init(getActivity());
        for (int i = 0; i < Locale.getAvailableLocales().length; i++) {
            temp = Locale.getAvailableLocales()[i].getDisplayLanguage();
            if (i == 0) {

                lang.addLang(temp);
                lang.addLangFlag(World.getFlagOf(countries.get(i)));
            } else if (!temp.equals(lang.getLang().get(lang.getLang().size() - 1))) {

                lang.addLang(temp);
                lang.addLangFlag(World.getFlagOf(countries.get(i)));
            }
        }
        return lang;
    }


}