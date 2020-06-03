package com.micule.handler;

import com.micule.Application;
import com.micule.analysis.Analyser;
import com.micule.analysis.impl.*;
import com.micule.analysis.impl.Character;


public class AnalysisHandler {

    private Analyser[] analysers = {
            new Node(),
            new NodeSub(),
            new Animable(),
            new CollisionMap(),
            new Deque(),
            new Interface(),
            new Character(),
            new Player(),
            new Npc(),
            new NpcDef(),
            new Object1(),
            new Object2(),
            new Object3(),
            new Object4(),
            new Object5(),
            new Ground(),
            new Item(),
            new Scene(),
            new Client(),
    };

    public void run() {
    	
        for (Analyser analyser : analysers) {
            Application.CLASS_NODE_MAP.values().stream().filter(cn -> analyser.specify(cn) != null).forEach(analyser::evaluate);
        }
    }
}
