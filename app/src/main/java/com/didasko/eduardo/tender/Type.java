package com.didasko.eduardo.tender;

import com.didasko.eduardo.tender.App;
import com.didasko.eduardo.tender.R;

/**
 * Created by Eduardo on 12/07/2016.
 */
public enum Type {
    ENTRADA(R.string.entrada), DRINK(R.string.bebida), SALADA(R.string.salada), MASSA(R.string.massa), DOCE(R.string.doce), VEGAN(R.string.vegan);

    private int resourceId;

    private Type(int id) {
        resourceId = id;
    }


}
