package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.Quest;

import java.util.List;

/**
 * Some client side data that gets modified when a client requests data from the server, THIS IS NEVER ACCESSED SERVERSIDE, IT IS USELESS THERE
 */
public class QuestScreensData {
    public static Quest questAcceptScreenDisplay = null;
    public static List<ClosedContractQuest> playerQuests = null;//Player instanced quests, sent all in one packet so as not to require a packet every time the player move to the next quest.
    public static boolean playerQuestsStale = true;//If the player has accepted or completed quests at any point, the client will mark this true so that it knows to request the player quests again.
}
