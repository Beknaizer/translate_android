package com.example.speech2text.models;

public class History {
    private String id;
    private String authorEmail;
    private String textToTranslate;
    private String translatedText;
    private String fromLanguage;
    private String toLanguage;

    public History(String id, String authorEmail, String textToTranslate, String translatedText, String fromLanguage, String toLanguage) {
        this.id = id;
        this.authorEmail = authorEmail;
        this.textToTranslate = textToTranslate;
        this.translatedText = translatedText;
        this.fromLanguage = fromLanguage;
        this.toLanguage = toLanguage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getTextToTranslate() {
        return textToTranslate;
    }

    public void setTextToTranslate(String textToTranslate) {
        this.textToTranslate = textToTranslate;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public String getFromLanguage() {
        return fromLanguage;
    }

    public void setFromLanguage(String fromLanguage) {
        this.fromLanguage = fromLanguage;
    }

    public String getToLanguage() {
        return toLanguage;
    }

    public void setToLanguage(String toLanguage) {
        this.toLanguage = toLanguage;
    }
}
