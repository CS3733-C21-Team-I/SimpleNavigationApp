package edu.wpi.ithorian.hospitalMap.mapEditing;

import edu.wpi.ithorian.hospitalMap.HospitalMap;

import java.util.Map;
import java.util.Queue;

public class MapEditManager {

    private static MapEditManager ourInstance;

    public static void init() {
        ourInstance = new MapEditManager();
    }

    public static MapEditManager getInstance() {
        return ourInstance;
    }

    private HospitalMap activeMap;
    private Queue<NavEditOperation> dataOperations;

    private MapEditManager() {

    }
}
