import java.util.ArrayList;

public class MinHeap {

    private HeapNode heapArray[];
    int heap_counter;

    //constructor
    public MinHeap() {
        this.heapArray = new HeapNode[100];
        this.heap_counter = 0;
    }

    //insert building into MinHeap
    public void insert(int total_time, int executionTime, RB_Node node) {
        HeapNode n = new HeapNode(total_time, executionTime, node);
        if (heap_counter == heapArray.length)
            increaseHeapSize();

        heapArray[heap_counter++] = n;
        int i = heap_counter - 1;
        int parent = parent(i);

        while (parent != i && heapArray[parent].execution_time > heapArray[i].execution_time) {
            swap(i, parent);
            i = parent;
            parent = parent(i);
        }

        buildHeap();
    }

    //construct minHeap
    public void buildHeap() {
        int k = heap_counter / 2;
        while (k >= 0) {
            Heapify(k);
            k--;
        }
    }

    //getter for to get MinHeap size
    public int getSize() {
        return heap_counter;
    }

    //increase MinHeap size when it becomes full
    public void increaseHeapSize() {

        HeapNode newHeapArray[] = new HeapNode[2 * heap_counter];
        for (int i = 0; i < heap_counter; i++)
            newHeapArray[i] = heapArray[i];
        heapArray = newHeapArray;
    }

    //get the minimum element from MinHeap
    public HeapNode extractMin() {

        if (heap_counter == 0) {
            throw new IllegalStateException("Heap is empty!");
        } else if (heap_counter == 1) {
            HeapNode min = removeElement(0);
            heap_counter = heap_counter - 1;
            return min;
        } else {
            HeapNode min = heapArray[0];
            HeapNode lastElemet = removeElement(heap_counter - 1);
            heapArray[0] = lastElemet;
            Heapify(0);
            heap_counter = heap_counter - 1;
            return min;
        }
    }

    //function to remove element from minHeap
    public HeapNode removeElement(int pos) {
        HeapNode deletedElement = heapArray[pos];
        for (int i = pos; i < heap_counter - 1; i++) {
            heapArray[i] = heapArray[i + 1];
        }
        return deletedElement;
    }

    //maintain Heapify property whenever elements are inserted or deleted
    private void Heapify(int i) {

        int leftnode = 2 * i + 1;
        int rightNode = 2 * i + 2;
        int smallest = i;

        //compare left node with parent
        if (leftnode <= heap_counter - 1 && heapArray[leftnode].execution_time < heapArray[i].execution_time) {
            smallest = leftnode;
        } else if (leftnode <= heap_counter - 1 && heapArray[leftnode].execution_time == heapArray[i].execution_time) {
            //resolve ties when execution times are same
            if (heapArray[leftnode].rb_Node.buildingID < heapArray[i].rb_Node.buildingID) {
                smallest = leftnode;
            }
        } else {
            smallest = i;
        }

        //compare right node with parent
        if (rightNode <= heap_counter - 1 && heapArray[rightNode].execution_time < heapArray[smallest].execution_time) {
            smallest = rightNode;
        } else if (rightNode <= heap_counter - 1 && heapArray[rightNode].execution_time == heapArray[smallest].execution_time) {
            //resolve ties when execution times are same
            if (heapArray[rightNode].rb_Node.buildingID < heapArray[smallest].rb_Node.buildingID) {
                smallest = rightNode;
            }
        }

        //swap if there is a change in smallest
        if (smallest != i) {
            swap(i, smallest);
            Heapify(smallest);
        }
    }

    //determines if MinHeap is empty
    public boolean isEmpty() {
        if (heap_counter == 0)
            return true;
        else
            return false;
    }

    //get parent node of i
    private int parent(int i) {
        int parentNode = (i - 1) / 2;
        return parentNode;
    }

    //swap values during heapifying
    private void swap(int i, int parent) {
        HeapNode tempNode = heapArray[parent];
        heapArray[parent] = heapArray[i];
        heapArray[i] = tempNode;
    }
}