package com.cdogsnappy.snappymod.karma;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class KarmaPlayerInfo implements Serializable {
    private static final long serialVersionUID = 7536482295622776147L;
    protected int score;
    protected float health;
    protected int numEndorsements;
    protected int numEndorsed;

    protected EndorsementInfo[] playersEndorsed;

    public KarmaPlayerInfo(){
        this.score = 1;
        this.health = -2.0f;
        this.numEndorsed = 0;
        this.numEndorsements = 0;
        playersEndorsed = new EndorsementInfo[3];
    }

    public KarmaPlayerInfo(int score, float health){
        this.score = score;
        this.health = health;
        this.numEndorsements = 0;
        this.numEndorsed = 0;
        playersEndorsed = new EndorsementInfo[3];
    }
    public KarmaPlayerInfo(int score, float health, int numEndorsements, int numEndorsed, EndorsementInfo[] playersEndorsed){
        this.score = score;
        this.health = health;
        this.numEndorsements = numEndorsements;
        this.numEndorsed = numEndorsed;
        this.playersEndorsed = playersEndorsed;
        playersEndorsed = new EndorsementInfo[3];
    }
    public int getScore(){
        return score;
    }
    public float getHealth(){
        return health;
    }
    public int getEndorsements(){return numEndorsements;}
    public int getEndorsed(){return numEndorsed;}
    public EndorsementInfo[] getPlayersEndorsed(){return playersEndorsed;}
}