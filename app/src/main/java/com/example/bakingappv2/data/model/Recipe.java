package com.example.bakingappv2.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.bakingappv2.R;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "recipe")
public class Recipe extends BaseObservable implements Parcelable {

    private String image;
    private int servings;
    private String name;
    @PrimaryKey
    private int id;
    private List<Ingredient> ingredients;
    private List<Step> steps;

    @Ignore
    public Recipe() {
    }

    public Recipe(String image, int servings, String name, List<Ingredient> ingredients, int id, List<Step> steps) {
        this.image = image;
        this.servings = servings;
        this.name = name;
        this.ingredients = ingredients;
        this.id = id;
        this.steps = steps;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    protected Recipe(Parcel in) {
        image = in.readString();
        servings = in.readInt();
        name = in.readString();
        ingredients = new ArrayList<>();
        in.readList(ingredients, Ingredient.class.getClassLoader());
        id = in.readInt();
        steps = new ArrayList<>();
        in.readList(steps, Step.class.getClassLoader());
    }


    @BindingAdapter({"baking:image"})
    public static void loadImage(ImageView view, String imageUrl) {
        GlideApp.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.cheese_cake)
                .error(R.drawable.cheese_cake)
                .into(view);
    }

    @Bindable
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        notifyPropertyChanged(com.example.bakingappv2.BR.image);
    }

    public int getServings() {
        return servings;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(com.example.bakingappv2.BR.name);
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeInt(servings);
        dest.writeString(name);
        dest.writeList(ingredients);
        dest.writeInt(id);
        dest.writeList(steps);
    }

}
