package com.cdogsnappy.snappystuff.court;

import java.io.Serializable;

public class Crime implements Serializable {

    protected String offense;
    protected int severity;
    protected CitizenData victim;
    protected CitizenData offender;

    public Crime(String offense, int severity, CitizenData victim, CitizenData offender){
        this.offense = offense;
        this.severity = severity;
        this.victim = victim;
        this.offender = offender;

    }
}
