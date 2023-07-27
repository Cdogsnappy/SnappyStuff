package com.cdogsnappy.snappystuff.court;

import java.io.Serializable;

public class Crime implements Serializable {

    protected String offense;
    protected int severity;
    protected String victim;

    public Crime(String offense, int severity, String victim){

    }
}
