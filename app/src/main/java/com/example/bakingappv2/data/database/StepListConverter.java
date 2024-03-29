package com.example.bakingappv2.data.database;

import androidx.room.TypeConverter;

import com.example.bakingappv2.data.model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class StepListConverter {

    private static Gson gson = new Gson();

    // returns step list
    @TypeConverter
    public static List<Step> StringToStepsList(String string) {
        if (string == null) {
            return Collections.emptyList();
        }
        Type stepListType = new TypeToken<List<Step>>() {
        }.getType();
        return gson.fromJson(string, stepListType);
    }

    // returns String
    @TypeConverter
    public static String StepListToString(List<Step> stepList) {
        return stepList == null ? null : gson.toJson(stepList);
    }

}
