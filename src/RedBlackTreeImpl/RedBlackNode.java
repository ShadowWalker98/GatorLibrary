package RedBlackTreeImpl;

public class RedBlackNode {

    RedBlackNode left;
    RedBlackNode right;
    RedBlackNode parent;
    Integer color;
    Integer data;

    public RedBlackNode(Integer data) {
        left = null;
        right = null;
        parent = null;
        color = 1;
        this.data = data;
    }


}
