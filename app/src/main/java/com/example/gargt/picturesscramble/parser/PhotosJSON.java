package com.example.gargt.picturesscramble.parser;

import com.example.gargt.picturesscramble.model.Sizes;

/**
 * Created by gargt on 12/10/16.
 */

public class PhotosJSON  {
    public Sizes getSizes() {
        return sizes;
    }

    public void setSizes(Sizes sizes) {
        this.sizes = sizes;
    }

    private Sizes sizes;
}