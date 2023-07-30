package com.cdogsnappy.snappystuff.karma;

import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;

public class KarmaPlayerInfo implements Serializable {
    private static final long serialVersionUID = 7536482295622776147L;
    protected float score;
    protected float health;
    protected int numEndorsements;
    protected int numEndorsed;

    protected EndorsementInfo[] playersEndorsed;

    public KarmaPlayerInfo(){
        this.score = 1.0f;
        this.health = -2.0f;
        this.numEndorsed = 0;
        this.numEndorsements = 0;
        playersEndorsed = new com.cdogsnappy.snappystuff.karma.EndorsementInfo[3];
    }

    public KarmaPlayerInfo(float score, float health){
        this.score = score;
        this.health = health;
        this.numEndorsements = 0;
        this.numEndorsed = 0;
        playersEndorsed = new EndorsementInfo[3];
    }
    public KarmaPlayerInfo(float score, float health, int numEndorsements, int numEndorsed, EndorsementInfo[] playersEndorsed){
        this.score = score;
        this.health = health;
        this.numEndorsements = numEndorsements;
        this.numEndorsed = numEndorsed;
        this.playersEndorsed = playersEndorsed;
    }
    public float getScore(){
        return score;
    }
    public float getHealth(){
        return health;
    }
    public int getEndorsements(){return numEndorsements;}
    public int getEndorsed(){return numEndorsed;}
    public EndorsementInfo[] getPlayersEndorsed(){return playersEndorsed;}
    public String toString(){
        return "Score: " + this.score + ", Health: " + this.health;
    }
}