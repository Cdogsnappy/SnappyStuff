package com.cdogsnappy.snappystuff.court;
import java.io.Serializable;
import java.util.HashMap;

public class CourtCase implements Serializable {

    public static HashMap<Integer,CourtCase> caseArchive = new HashMap<>();
    static int caseID = 1;

    public enum Status{
        IN_PROGRESS, COMPLETED, MISTRIAL;
    }
    public enum Verdict{
        GUILTY, NOT_GUILTY, MISTRIAL, UNDECIDED;
    }

    /**
     * holds all particpant data, with index ordering
     * 0 : Judge
     * 1 : Defendant
     * 2 : Plaintiff
     * 3 : Defendent lawyer (NULLABLE
     * 4 : Plaintiff Lawyer (NULLABLE)
     * 5-7 : Jury
     */
    CitizenData[] participants = new CitizenData[8];
    Status status;
    Verdict verdict;
    int severity;
    int id;
    String punishment;
    Crime crime;


    public CourtCase(CitizenData[] participants, int severity){
        this.participants = participants;
        this.severity = severity;
        this.id = caseID;
        caseID++;
        status = Status.IN_PROGRESS;
        verdict = Verdict.UNDECIDED;
        caseArchive.put(this.id,this);

    }

    /**
     * Concludes a courtcase and updates the case archive
     * @param status status of the case
     * @param verdict verdict for the defendant
     * @param punishment the punishment given, if any.
     * @param crime the crime that was committed.
     */
    public void completeCase(Status status, Verdict verdict, String punishment, Crime crime){
        this.verdict = verdict;
        this.punishment = punishment;
        this.status = status;
        this.crime = crime;
        caseArchive.put(this.id,this);
    }
}
