package com.HPA.securehealth;

import java.io.Serializable;

/**
 * Created by Hamza on 11/29/16.
 */
public class TutorialEntry implements Serializable {
    int image;
    String description;

    public TutorialEntry(int image, String string) {
        description = string;
        this.image = image;

    }
}
