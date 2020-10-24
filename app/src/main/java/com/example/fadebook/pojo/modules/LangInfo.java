package com.example.fadebook.pojo.modules;

import java.util.ArrayList;
import java.util.List;

public class LangInfo {
    private List<Integer> langFlag = new ArrayList<>();
    private List<String> lang = new ArrayList<>();

    public LangInfo() {
    }

    public LangInfo(List<String> lang, List<Integer> langFlag) {
        this.lang = lang;
        this.langFlag = langFlag;
    }

    public void addLang(String lang) {
        this.lang.add(lang);
    }

    public void addLangFlag(Integer langFlag) {
        this.langFlag.add(langFlag);
    }

    public List<String> getLang() {
        return lang;
    }

    public List<Integer> getLangFlag() {
        return langFlag;
    }
}
