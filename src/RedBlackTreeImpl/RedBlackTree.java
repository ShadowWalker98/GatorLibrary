package RedBlackTreeImpl;

import java.util.Objects;

public class RedBlackTree {

    RedBlackNode root;
    int colorFlips = 0;

    public RedBlackTree() {
        this.root = null;
    }

    public void insert(Integer data) {
        if(this.isEmpty()) {
            RedBlackNode node = new RedBlackNode(data);
            // since root is null, this is the first node being added in the tree
            // which is why we color it black
            // we also change the color of the node to black which is denoted by 0
            node.setColor(0);
            this.root = node;
        } else {
            // we find a place to insert the node in the tree
            RedBlackNode track = this.root;
            RedBlackNode prev = null;
            while(track != null) {
                if(track.getData().equals(data)) {
                    // we have found the node, so we don't insert as we can't have duplicates
                    System.out.println("Duplicate inserted");
                    return;
                } else if(track.getData() < data) {
                    // we move to the right subtree
                    prev = track;
                    track = track.getRight();
                } else if(track.getData() > data) {
                    // we move to the left subtree
                    prev = track;
                    track = track.getLeft();
                }
            }
            // track should now be an external node
            // prev has the parent node
            RedBlackNode node = new RedBlackNode(data);
            // changing the color to red
            node.setColor(1);
            // setting the parent to the prev pointer
            node.setParent(prev);
            // now we have to check the coinditions for the red black tree to be valid
            // p = node
            // pp = parent(node) == prev
            // gp = parent(prev)

            if(Objects.nonNull(node.getParent().getParent()) && isImbalanced(node)) {
                rebalance(node, node.getParent(), node.getParent().getParent());
            }
        }
    }

    public boolean isImbalanced(RedBlackNode p) {
        if(p.getParent() == null) {
            return false;
        }
        return p.getColor() == 1 && p.getColor().equals(p.getParent().getColor());
    }

    public void rebalance(RedBlackNode p, RedBlackNode pp, RedBlackNode gp) {
        // X is true if pp is the left child of gp
        boolean X = pp.equals(gp.getLeft());
        // Y is true if p is the left child of pp
        boolean Y = p.equals(pp.getLeft());
        // z is false if other child of gp is black
        // z is true if other child of gp is red
        boolean z = X ? gp.getRight().getColor() == 1 : gp.getLeft().getColor() == 1;

        RedBlackNode d = X ? gp.getRight() : gp.getLeft();
        RedBlackNode c = Y ? pp.getRight() : pp.getLeft();
        // XYz:
        // if XYr then we need to flipColors and then check for imbalance. If yes we recursively call rebalance
        if(z) {
            // we make pp black and d black and set gp to red
            pp.setColor(0);
            gp.setColor(1 - gp.getColor());
            d.setColor(0);
            colorFlips++;
            // check for imbalance and call rebalance if needed
            if(isImbalanced(gp)) {
                rebalance(gp, gp.getParent(), gp.getParent().getParent());
            }
        } else {
            // z is black
            // so we have to rotate the tree
            rotate(p, pp, gp, X, Y);

        }

//        RedBlackNode c;
//        if(p.equals(pp.getLeft())) {
//            c = pp.getRight();
//        } else {
//            c = pp.getLeft();
//        }
//        RedBlackNode a = p.getLeft();
//        RedBlackNode b = p.getRight();
    }

    public void rotate(RedBlackNode p, RedBlackNode pp, RedBlackNode gp, boolean X, boolean Y) {
        if(X && Y) {
            RedBlackNode parent = gp.getParent();

            RedBlackNode temp = pp.getRight();
            gp.setLeft(temp);
            pp.setRight(gp);
            pp.setParent(null);
            gp.setParent(pp);

            if(parent != null) {
                if(parent.getLeft().equals(gp)) {
                    parent.setLeft(pp);
                } else {
                    parent.setRight(pp);
                }
            }
        } else if(!X && !Y) {
            RedBlackNode parent = gp.getParent();

            RedBlackNode d = gp.getLeft();
            RedBlackNode c = pp.getLeft();
            gp.setRight(c);

        }
    }

    public boolean isEmpty() {
        return this.root == null;
    }


}
