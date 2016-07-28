package com.didasko.eduardo.tender;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by nirav on 05/10/15.
 */
public class Recipe implements Serializable {

    private String description;

    private String imagePath;

    private Type tipo;

    public Recipe(String imagePath, String description, Type tipo) {
        this.imagePath = imagePath;
        this.description = description;
        this.tipo = tipo;
    }

    public Type getTipo() {
        return tipo;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }
}
