package com.example.speech2text.interfaces;

import com.example.speech2text.models.TranslatedText;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TranslateService {
    @GET("translateForAndroid")
    Call<TranslatedText> listRepos(
                                        @Query("userEmail") String user,
                                         @Query("textToTranslate")String textToTranslate,
                                         @Query("fromLanguage")String fromLanguage,
                                         @Query("toLanguage")String toLanguage
                                 );
}
