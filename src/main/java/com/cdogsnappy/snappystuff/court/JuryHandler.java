package com.cdogsnappy.snappystuff.court;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class JuryHandler {
    static Random rand = new Random();

    /**
     * @author Cdogsnappy
     * generates a list of 3 citizens (if 3 are available) to act as jury for a court case
     * @param illegalPlayers players unable to serve on the jury (already have a role)
     * @return
     */
    public static List<String> callJury(List<String> illegalPlayers){
        List<String> citizens = CitizenData.citizenRegistry.keySet().stream().toList();
        citizens.removeAll(illegalPlayers);
        List<String> juryMembers = new ArrayList<>();
        if(citizens.size() <= 3){
            return citizens;
        }
        for(int j = 0; j<3; ++j){
            int ind = rand.nextInt(citizens.size());
            juryMembers.add(citizens.get(ind));
            citizens.remove(ind);
        }
        return juryMembers;
    }


}
