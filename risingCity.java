import java.io.*;
import java.lang.*;
import java.util.*;

class risingCity {
    static HeapNode currentBuilding = null;

    public static void main(String args[]) throws IOException {
        //determine total construction time of the city
        BufferedReader ar = new BufferedReader(new FileReader(args[0]));
        String str;
        long totalConstructionTime = 0;

        while ((str = ar.readLine()) != null) {
            if (str.split(" ")[1].split("\\(")[0].equalsIgnoreCase("Insert"))
                totalConstructionTime = totalConstructionTime + Long.parseLong(str.split(",")[1].split("\\)")[0]) + Long.parseLong(str.split(":")[0]);
        }

        ar.close();

        //parse the input to read the commands
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        String line = br.readLine();
        long inputTime = Integer.parseInt(line.split(":")[0]);
        String split = line.split(" ")[1];
        String test_str = (split.split("\\("))[0];
        String arguments = (split.split("\\("))[1];

        int buildingID, buildingID2;
        Building building = new Building();
        long globalTime = 0;
        int total_time = 0;
        int executionTime = 0;
        int fiveDaysCounter = 0;
        int isSchedulerBusy = 0;
        boolean isCurrentBuildingCompleted = false;

        List<Long> handlePrintBeforeDelete_time = new ArrayList<>();
        List<String> handlePrintBeforeDelete_args = new ArrayList<>();

        for (globalTime = 0; globalTime <= totalConstructionTime; globalTime++) {
            if (inputTime == globalTime) {
                if (test_str.equalsIgnoreCase("Insert")) {
                    buildingID = Integer.parseInt(arguments.split("\\)")[0].split(",")[0]);
                    total_time = Integer.parseInt(arguments.split("\\)")[0].split(",")[1]);
                    building.Insert(buildingID, total_time, executionTime);
                } else if (test_str.equalsIgnoreCase("PrintBuilding") && handlePrintBeforeDelete_time.contains(inputTime)) {
                    if (arguments.split("\\)")[0].split(",").length == 1) {
                        buildingID = Integer.parseInt(arguments.split("\\)")[0].split(",")[0]);
                        building.PrintBuilding(buildingID);
                    } else if (arguments.split("\\)")[0].split(",").length == 2) {
                        buildingID = Integer.parseInt(arguments.split("\\)")[0].split(",")[0]);
                        buildingID2 = Integer.parseInt(arguments.split("\\)")[0].split(",")[1]);
                        building.PrintBuilding(buildingID, buildingID2);
                    }
                }
                // read the next inputTime
                if ((line = br.readLine()) != null) {
                    inputTime = Integer.parseInt(line.split(":")[0]);
                    split = line.split(" ")[1];
                    test_str = (split.split("\\("))[0];
                    arguments = (split.split("\\("))[1];
                    if (test_str.equalsIgnoreCase("PrintBuilding")) {
                        handlePrintBeforeDelete_time.add(inputTime);
                        handlePrintBeforeDelete_args.add(arguments);
                    }
                }
            }
            //scheduling if no buildings are currently being built
            if (isSchedulerBusy == 0) {
                //its a new day. No buildings in the scheduler. Pick the building with lowest execution time and get busy!
                if (building.heap.isEmpty()) {
                    continue;
                } else {
                    currentBuilding = building.heap.extractMin();
                    isSchedulerBusy = 1;
                    fiveDaysCounter = 0;
                    isCurrentBuildingCompleted = false;
                }
            }
            //scheduling the currently chosen building
            if (isSchedulerBusy == 1) {
                fiveDaysCounter++;
                currentBuilding.execution_time += 1;
                currentBuilding.rb_Node.execution_time += 1;
                if (currentBuilding.rb_Node.execution_time == currentBuilding.total_time) {
                    if (handlePrintBeforeDelete_time.contains(globalTime + 1)) {
                        int index = handlePrintBeforeDelete_time.indexOf(globalTime + 1);
                        if (handlePrintBeforeDelete_args.get(index).split("\\)")[0].split(",").length == 2) {
                            buildingID = Integer.parseInt(arguments.split("\\)")[0].split(",")[0]);
                            buildingID2 = Integer.parseInt(arguments.split("\\)")[0].split(",")[1]);
                            handlePrintBeforeDelete_time.remove(index);
                            handlePrintBeforeDelete_args.remove(index);
                            building.PrintBuilding(buildingID, buildingID2);
                        } else {
                            buildingID = Integer.parseInt(arguments.split("\\)")[0].split(",")[0]);
                            handlePrintBeforeDelete_time.remove(index);
                            handlePrintBeforeDelete_args.remove(index);
                            building.PrintBuilding(buildingID);
                        }
                    }
                    isCurrentBuildingCompleted = true;
                    building.printDeletingBuilding(currentBuilding.rb_Node.buildingID, (globalTime + 1));
                    building.rbTree.delete(currentBuilding.rb_Node);
                    //if the currenting building construction got completed before 5 days, reset the counter
                    if (fiveDaysCounter != 5) {
                        fiveDaysCounter = 0;
                        isSchedulerBusy = 0;
                    }
                } else {
                    isCurrentBuildingCompleted = false;
                }
                //if the currenting building construction is not full completed, insert it back into heap
                if (fiveDaysCounter == 5) {
                    fiveDaysCounter = 0;
                    if (!isCurrentBuildingCompleted)
                        building.heap.insert(currentBuilding.total_time, currentBuilding.execution_time, currentBuilding.rb_Node);
                    isSchedulerBusy = 0;
                }
            }
        }

        //write the outputs to file
        building.WritetoFile();
        br.close();
    }
}
