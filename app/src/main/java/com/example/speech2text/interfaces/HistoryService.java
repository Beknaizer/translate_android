package com.example.speech2text.interfaces;

import com.example.speech2text.models.History;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HistoryService {
    @GET("translateHistory/{email}")
    Call<List<History>> getUserHistory(@Path("email") String email);
}
