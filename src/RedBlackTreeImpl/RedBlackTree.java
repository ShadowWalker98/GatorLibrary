package RedBlackTreeImpl;

import Library.Book.Book;

import java.util.*;

public class RedBlackTree {

    RedBlackNode root;
    int colorFlips = 0;

    public RedBlackTree() {
        this.root = null;
    }

    public Book findBook(Integer bookID) {
        if(this.isEmpty()) {
            return null;
        }

        RedBlackNode tracker = this.root;
        while(tracker != null) {
            if(Objects.equals(tracker.getBookId(), bookID)) {
                return tracker.getBookData();
            } else if(tracker.getBookId() < bookID) {
                tracker = tracker.right;
            } else {
                tracker = tracker.left;
            }
        }
        return null;
    }

    public void insert(Book data) {
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
                if(track.getBookId().equals(data.getBookID())) {
                    // we have found the node, so we don't insert as we can't have duplicates
                    System.out.println("Duplicate inserted");
                    return;
                } else if(track.getBookId() < data.getBookID()) {
                    // we move to the right subtree
                    prev = track;
                    track = track.getRight();
                } else if(track.getBookId() > data.getBookID()) {
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
            if(node.getBookId() > prev.getBookId()) {
                prev.setRight(node);
            } else {
                prev.setLeft(node);
            }
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
        boolean z = X ? (gp.getRight() != null && gp.getRight().getColor() == 1) : (gp.getLeft() != null && gp.getLeft().getColor() == 1);

        RedBlackNode d = X ? gp.getRight() : gp.getLeft();
        RedBlackNode c = Y ? pp.getRight() : pp.getLeft();
        // XYz:
        // if XYr then we need to flipColors and then check for imbalance. If yes we recursively call rebalance
        if(z) {
            // we make pp black and d black and set gp to red
            pp.setColor(0);
            gp.setColor(1 - gp.getColor());
            if(Objects.nonNull(d)) {
                d.setColor(0);
            }
            colorFlips++;
            // check for imbalance and call rebalance if needed
            if(this.root.equals(gp)) {
                gp.setColor(0);
            }
            if(isImbalanced(gp)) {
                rebalance(gp, gp.getParent(), gp.getParent().getParent());
            }
        } else {
            // z is black
            // so we have to rotate the tree
            rotate(p, pp, gp, X, Y);
        }
    }

    public void rotate(RedBlackNode p, RedBlackNode pp, RedBlackNode gp, boolean X, boolean Y) {
        if(X && Y) {
            RedBlackNode gpParent = gp.getParent();
            RedBlackNode temp = pp.getRight();
            gp.setLeft(temp);
            if(Objects.nonNull(temp)) {
                temp.setParent(gp);
            }
            pp.setRight(gp);
            pp.setParent(null);
            gp.setParent(pp);

            gp.setColor(1);
            pp.setColor(0);

            if(Objects.nonNull(gpParent)) {
                if(gpParent.getLeft() == gp) {
                    gpParent.setLeft(pp);
                } else {
                    gpParent.setRight(pp);
                }
            } else {
                this.root = pp;
            }
        } else if(!X && !Y) {
            RedBlackNode gpParent = gp.getParent();
            RedBlackNode d = gp.getLeft();
            RedBlackNode c = pp.getLeft();
            if(Objects.nonNull(c)) {
                c.setParent(gp);
            }
            gp.setRight(c);
            pp.setParent(gpParent);
            pp.setLeft(gp);
            gp.setParent(pp);

            pp.setColor(0);
            gp.setColor(1);

            if(Objects.nonNull(gpParent)) {
                if(gpParent.getLeft() == gp) {
                    gpParent.setLeft(pp);
                } else {
                    gpParent.setRight(pp);
                }
            } else {
                this.root = pp;
            }

        } else if(!X) {

            RedBlackNode gpParent = gp.getParent();

            RedBlackNode a = p.getLeft();
            RedBlackNode b = p.getRight();

            p.setParent(gpParent);
            gp.setRight(a);
            if(Objects.nonNull(a)) {
                a.setParent(gp);
            }

            pp.setLeft(b);
            if(Objects.nonNull(b)) {
                b.setParent(pp);
            }

            p.setRight(pp);
            pp.setParent(p);
            p.setLeft(gp);
            gp.setParent(p);
            p.setColor(0);
            gp.setColor(1);
            if(Objects.nonNull(gpParent)) {
                if(gpParent.getLeft() == gp) {
                    gpParent.setLeft(p);
                } else {
                    gpParent.setRight(p);
                }
            } else {
                this.root = p;
            }


        } else {
            RedBlackNode gpParent = gp.getParent();
            RedBlackNode a = p.getRight();
            RedBlackNode b = p.getLeft();

            p.setParent(gpParent);
            gp.setLeft(a);
            if(Objects.nonNull(a)) {
                a.setParent(gp);
            }
            pp.setRight(b);
            if(Objects.nonNull(b)) {
                b.setParent(pp);
            }

            p.setLeft(pp);
            pp.setParent(p);
            p.setRight(gp);
            gp.setParent(p);
            p.setColor(0);
            gp.setColor(1);
            if(Objects.nonNull(gpParent)) {
                if(gpParent.getLeft() == gp) {
                    gpParent.setLeft(p);
                } else {
                    gpParent.setRight(p);
                }
            } else {
                this.root = p;
            }
        }
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public List<RedBlackNode> levelOrderTraversal() {
        List<RedBlackNode> list = new LinkedList<>();
        Queue<RedBlackNode> queue = new LinkedList<>();
        if(root == null) {
            System.out.println("Tree is empty!");
            return null;
        }
        list.add(root);
        queue.add(root);
        queue.add(null);
        while(!queue.isEmpty()) {
            RedBlackNode node = queue.poll();
            if(node == null) {
                if(queue.size() > 0) {
                    queue.add(null);
                }
                System.out.println();
                continue;
            }
            list.add(node);
            System.out.print("BookID: " + node.getBookId() + " p: " + (node.getParent() == null ? "null" : node.parent.getBookId())
                    + " color: " + ((node.color == 0) ? "black" : "red") + " " + " children: "
                    + ((node.getLeft() != null) ? node.left.getBookId() : "null") + " "
                    + ((node.getRight() != null) ? node.right.getBookId() : "null") + " ");
            if(node.getLeft() != null) {
                queue.add(node.getLeft());
            }
            if(node.getRight() != null) {
                queue.add(node.getRight());
            }
        }
        return list;
    }

    public List<RedBlackNode> inorderTraversal() {
        List<RedBlackNode> list = new LinkedList<>();
        if(this.isEmpty()) {
            return null;
        }

        Stack<RedBlackNode> stack = new Stack<>();
        RedBlackNode current = this.root;

        while(current != null || stack.size() > 0) {

            while(current != null) {
                stack.push(current);
                current = current.left;
            }

            current = stack.pop();
            list.add(current);
            current = current.right;
        }
        return list;
    }


}
