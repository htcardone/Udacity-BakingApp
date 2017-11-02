package com.htcardone.baking.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

@Entity
public class Recipe{

	@SerializedName("image")
	private String image;

	@SerializedName("servings")
	private int servings;

	@SerializedName("name")
	private String name;

	@SerializedName("ingredients")
	private List<IngredientsItem> ingredients;

    @PrimaryKey
	@SerializedName("id")
	private int id;

	@SerializedName("steps")
	private List<StepsItem> steps;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setServings(int servings){
		this.servings = servings;
	}

	public int getServings(){
		return servings;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setIngredients(List<IngredientsItem> ingredients){
		this.ingredients = ingredients;
	}

	public List<IngredientsItem> getIngredients(){
		return ingredients;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setSteps(List<StepsItem> steps){
		this.steps = steps;
	}

	public List<StepsItem> getSteps(){
		return steps;
	}

	public Recipe(String image, int servings, String name, List<IngredientsItem> ingredients,
				  int id, List<StepsItem> steps) {
		this.image = image;
		this.servings = servings;
		this.name = name;
		this.ingredients = ingredients;
		this.id = id;
		this.steps = steps;
	}

	@Override
    public String toString() {
        return "Recipe{" +
                "image='" + image + '\'' +
                ", servings=" + servings +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", id=" + id +
                ", steps=" + steps +
                '}';
    }

    public static class Converters {
        @TypeConverter
        public List<IngredientsItem> ingredientsFromJson(String json) {
            Type listType = new TypeToken<List<IngredientsItem>>(){}.getType();
            return new Gson().fromJson(json, listType);
        }

        @TypeConverter
        public String ingredientsToJson(List<IngredientsItem> ingredients) {
            if (ingredients == null) {
                return null;
            } else {
                return new Gson().toJson(ingredients);
            }
        }

        @TypeConverter
        public List<StepsItem> stepsFromJson(String json) {
            Type listType = new TypeToken<List<StepsItem>>(){}.getType();
            return new Gson().fromJson(json, listType);
        }

        @TypeConverter
        public String stepsToJson(List<StepsItem> steps) {
            if (steps == null) {
                return null;
            } else {
                return new Gson().toJson(steps);
            }
        }
    }
}