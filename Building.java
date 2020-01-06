import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.io.FileWriter;
import java.io.IOException;

public class Building {

    public RB_tree rbTree;
    public MinHeap heap;

    ArrayList<String> result;

    //constructor
    public Building() {
        this.heap = new MinHeap();
        this.rbTree = new RB_tree();
        this.result = new ArrayList<String>();
    }

    //insert new building into Min heap and RBT
    public void Insert(int ID, int total_time, int executionTime) {

        if (rbTree.insertSearch(ID, rbTree.root) == 0) {
            rbTree.Insert(ID, total_time, executionTime);
            RB_Node newNode = rbTree.Search(ID, rbTree.root);
            heap.insert(total_time, executionTime, newNode);
        }
    }

    //Print the building details for the given building ID
    public void PrintBuilding(int ID) {
        RB_Node newNode = rbTree.Search(ID, rbTree.root);
        if (!newNode.isEmpty()) {
            result.add("(" + rbTree.PrintBuilding(ID) + "," + newNode.execution_time + "," + newNode.total_time + ")");
        } else {
            String str = "(0,0,0)";
            result.add(str);
        }
    }

    //print the building when its construction is completed
    public void printDeletingBuilding(int ID, long completed_time) {
        result.add("(" + ID + "," + completed_time + ")");
    }

    //print the status of construction of all buildings in the given range
    public void PrintBuilding(int ID1, int ID2) {
        ArrayList<Integer> res = rbTree.PrintBuilding(ID1, ID2);
        if (res.size() != 0) {
            String str = "";
            for (int i = 0; i < res.size(); i++) {
                if (res.get(i) != 0) {
                    RB_Node newNode = rbTree.Search(res.get(i), rbTree.root);
                    str += "(" + res.get(i) + "," + newNode.execution_time + "," + newNode.total_time + ")";
                    if (i != res.size() - 1)
                        str += ",";
                }
            }
            char[] checkerCharArray = str.toCharArray();
            if (checkerCharArray[checkerCharArray.length - 1] == ',') {
                str = str.substring(0, checkerCharArray.length - 1);
            }
            result.add(str);
        } else {
            String str = "(0,0,0)";
            result.add(str);
        }
    }

    //write all the outputs to output file
    public void WritetoFile() {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter("output_file.txt"));

            for (int i = 0; i < result.size(); i++) {
                br.append(result.get(i));
                br.append('\n');
            }
            br.close();
        } catch (IOException excp) {
            excp.printStackTrace();
        }
    }
}
