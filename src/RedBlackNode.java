public class RedBlackNode {

    RedBlackNode left;
    RedBlackNode right;

    RedBlackNode parent;
    // 0 : black
    // 1 : red
    Integer color;

    Book data;

    public RedBlackNode(Book data) {
        left = null;
        right = null;
        parent = null;
        color = 1;
        this.data = data;
    }

    public RedBlackNode getLeft() {
        return left;
    }

    public RedBlackNode getRight() {
        return right;
    }

    public RedBlackNode getParent() {
        return parent;
    }

    public Integer getColor() {
        return color;
    }

    public Book getBookData() {
        return data;
    }

    public void setLeft(RedBlackNode left) {
        this.left = left;
    }

    public void setRight(RedBlackNode right) {
        this.right = right;
    }

    public void setParent(RedBlackNode parent) {
        this.parent = parent;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public void setData(Book data) {
        this.data = data;
    }

    public Integer getBookId() {
        return this.data.getBookID();
    }
}
