package com.owldevs.evolutionsim;


import com.owldevs.evolutionsim.forms.PlainEarthForm;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        PlainEarthForm f = new PlainEarthForm();
        new Thread(f).start();
    }

}