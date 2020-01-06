public class HeapNode {
    int total_time;
    int execution_time;
    RB_Node rb_Node;

    public HeapNode(int total_time, int execution_time, RB_Node rb_Node) {
        this.total_time = total_time;
        this.execution_time = execution_time;
        this.rb_Node = rb_Node;
    }
}
