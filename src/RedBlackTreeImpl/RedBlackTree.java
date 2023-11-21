package RedBlackTreeImpl;

import Library.Book.Book;
import Library.Metrics.MetricCounter;

import java.util.*;

public class RedBlackTree {

    RedBlackNode root;

    MetricCounter metricCounter;

    public RedBlackTree() {
        this.root = null;
        this.metricCounter = new MetricCounter();
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

    private RedBlackNode findBookNode(Integer bookID) {
        if(this.isEmpty()) {
            return null;
        }

        RedBlackNode tracker = this.root;
        while(tracker != null) {
            if(Objects.equals(tracker.getBookId(), bookID)) {
                return tracker;
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

    public String deleteBook(Integer bookId) {
        // first find the RedBlackNode to be deleted
        RedBlackNode nodeToBeDeleted = findBookNode(bookId);
        if(Objects.nonNull(nodeToBeDeleted)) {
            if(nodeToBeDeleted.getLeft() != null && nodeToBeDeleted.getRight() != null) {
                // it is a degree 2 node, therefore we replace it with a node which is either degree 1 or 0
                // We are replacing it with the highest element in the left subtree.
                RedBlackNode replacementNode = findReplacementNode(nodeToBeDeleted);
                // replacing data at the original node with the replacement data
                Book book = nodeToBeDeleted.getBookData();
                nodeToBeDeleted.setData(replacementNode.getBookData());
                // deleting the replacementNode
                deleteNode(replacementNode);
                // remember we have to send back the details of the original node and its deletion
                return createDeletionString(book);
            } else {
                // if it's a degree 1 or degree 0 node then we can call the deletion method directly.
                Book book = nodeToBeDeleted.getBookData();
                deleteNode(nodeToBeDeleted);
                return createDeletionString(book);
            }
        } else {
            return "Book doesn't exist in the library";
        }
    }

    private RedBlackNode findReplacementNode(RedBlackNode nodeToBeDeleted) {
        // returns the highest node in the left subtree of nodeToBeDeleted
        RedBlackNode replacementNode = nodeToBeDeleted.getLeft();
        while(replacementNode.getRight() != null) {
            replacementNode = replacementNode.getRight();
        }
        return replacementNode;
    }

    private void deleteNode(RedBlackNode node) {
        // first lets determine if it's a degree 0 or a degree 1 node
        // we also have to check if the node's child (if it exists) is a black node or not
        // check for the parent of y ie py
        RedBlackNode py = node.getParent();
        boolean yIsExternalNode = node.getLeft() == null && node.getRight() == null;
        boolean isNodeTheRoot = py == null;
        // if y is an external node then y is null otherwise the only child of the node is y
        RedBlackNode y = yIsExternalNode ? null : (node.getLeft() == null ? node.getRight() : node.getLeft());
        boolean yIsLeftChild = !yIsExternalNode && py != null && Objects.equals(y, py.getLeft());

        // if the node being deleted is the root then we have to set y as the new root
        if(isNodeTheRoot) {
            // if node is the root, then if y is external node then the tree is empty
            if(yIsExternalNode) {
                this.root = null;
            } else {
                this.root = y;
                y.setParent(null);
                y.setColor(0);
            }
            return;
        }

        boolean yIsRed;
        if(!yIsExternalNode) {
            yIsRed = y.getColor() == 1;
        } else {
            yIsRed = false;
        }

        // if y is an external node and the node being deleted is red
        // then we can just delete it and return
        boolean nodeIsLeftChild = py != null && Objects.equals(py.getLeft(), node);
        if(yIsExternalNode && node.getColor() == 1) {
            if(nodeIsLeftChild) {
                py.setLeft(null);
            } else {
                py.setRight(null);
            }
            return;
        }

        // the case where y is black and node is degree one and red
        // is not possible because then the number of black nodes along y is one more than the other path from node

        // y is red and the node is a degree 1 node
        // then we can just colour y black and be done with the deletion
        if(yIsRed) {
            // we set y's parent to be py
            // we set py's child to be y
            y.setColor(0);
            if(Objects.equals(node, py.getLeft())) {
                // node being deleted is the left child of py,
                // so we set y to be the left child of py
                py.setLeft(y);
                y.setParent(py);
            } else {
                py.setRight(y);
                y.setParent(py);
            }
            return;
        }

        propagator(nodeIsLeftChild, y, py);

    }

    private void propagator(boolean nodeIsLeftChild, RedBlackNode y, RedBlackNode py) {
        // y is black and can be an external node or a child node
        RedBlackNode v = nodeIsLeftChild ? py.getRight() : py.getLeft();
        // Xcn
        // X = y -> right subtree, X = R (false) else X = L (true)
        // c = color of other child of py (v)
        // n = number of red children of v

        boolean X = nodeIsLeftChild;
        int c = v.getColor() == 0 ? 0 : 1;
        int n = redChildren(v);

        // checking which case it is
        // Rb0
        // Lb0
        // Rb1
        // Lb1
        // Rb2
        // Lb2
        // Rr0
        // Ll0
        if(!X && c == 0 && n == 0) {
            // Rb0 c1, c2
            py.setRight(y);
            if(Objects.nonNull(y)) {
                y.setParent(py);
            }

            if(py.getColor() == 0) {
                // c1. py is black
                v.setColor(1);
                // if py's parent is null then it is the root of the tree
                // hence the whole tree becomes deficient in one black node,
                // we don't need to propagate the deficiency further up
                if(py.getParent() != null) {
                    boolean pyLeftChildOfParent = Objects.equals(py, py.parent.getLeft());
                    propagator(pyLeftChildOfParent , py, py.parent);
                }
            } else {
                // c2. py is red
                py.setColor(0);
                v.setColor(1);
            }
            return;
        } else if(X && c == 0 && n == 0) {
            // Lb0 c1, c2
            py.setLeft(y);
            if(Objects.nonNull(y)) {
                y.setParent(py);
            }
            if(py.getColor() == 0) {
                // c1. py is black
                v.setColor(1);
                if(py.getParent() != null) {
                    boolean pyLeftChildOfParent = Objects.equals(py, py.parent.getLeft());
                    propagator(pyLeftChildOfParent ,py, py.parent);
                }
            } else {
                // c2. py is red
                py.setColor(0);
                v.setColor(1);
            }
            return;
        } else if(!X && c == 0 && n == 1) {
            // Rb1 c1, c2
            py.setRight(y);
            if(Objects.nonNull(y)) {
                y.setParent(py);
            }
            RedBlackNode a = null;
            if(v.getLeft() != null) {
                if(v.getLeft().getColor() == 1) {
                    a = v.getLeft();
                }
            }
            if(a == null) {
                a = v.getRight();
            }

            // a is either the left subtree or the right subtree
            if(Objects.equals(v.getLeft(), a)) {
                // c1 : left subtree of v is red
                RedBlackNode ppy = py.getParent();
                if(ppy == null) {
                    // py is the root
                    // py has to be black because it is the root
                    // changing v to be the new root
                    this.root = v;
                    py.setLeft(v.getRight());
                    if(Objects.nonNull(py.getLeft())) {
                        py.getLeft().setParent(py);
                    }
                    // v's right child is now py
                    v.setRight(py);
                    py.setParent(v);
                    // setting color of a to black
                    a.setColor(0);
                } else {
                    // py can be red or black
                    int pyColor = py.getColor();
                    boolean pyIsLeftChild = Objects.equals(py, ppy.getLeft());
                    if(pyIsLeftChild) {
                        ppy.setLeft(v);
                        v.setParent(ppy);
                    } else {
                        ppy.setRight(v);
                        v.setParent(ppy);
                    }
                    v.setColor(pyColor);
                    py.setColor(0);
                    py.setLeft(v.getRight());
                    if(py.getLeft() != null) {
                        py.getLeft().setParent(py);
                    }
                    // v's right child is now py
                    v.setRight(py);
                    py.setParent(v);
                    // setting color of a to black
                    a.setColor(0);
                }
            } else {
                // c2. right subtree of v is red
                RedBlackNode ppy = py.getParent();
                if(ppy == null) {
                    RedBlackNode w = v.getRight();
                    RedBlackNode b = w.getLeft();
                    RedBlackNode ct = w.getRight();

                    w.setParent(ppy);
                    this.root = w;

                    w.setColor(0);
                    py.setColor(0);


                    py.setLeft(ct);
                    if(ct != null) {
                        ct.setParent(py);
                    }
                    w.setRight(py);
                    py.setParent(w);

                    w.setLeft(v);
                    v.setParent(w);
                    v.setRight(b);
                    if(b != null) {
                        b.setParent(v);
                    }
                } else {
                    int pyColor = py.getColor();
                    RedBlackNode w = v.getRight();
                    RedBlackNode b = w.getLeft();
                    RedBlackNode ct = w.getRight();
                    boolean pyIsLeftChild = Objects.equals(py, ppy.getLeft());

                    w.setParent(ppy);
                    if(pyIsLeftChild) {
                        ppy.setLeft(w);
                    } else {
                        ppy.setRight(w);
                    }

                    w.setColor(pyColor);
                    py.setColor(0);
                    py.setLeft(ct);
                    if(ct != null) {
                        ct.setParent(py);
                    }
                    w.setRight(py);
                    py.setParent(w);
                    w.setLeft(v);
                    v.setParent(w);
                    v.setRight(b);
                    if(b != null) {
                        b.setParent(v);
                    }
                }
            }
            return;
        } else if(X && c == 0 && n == 1) {
            py.setLeft(y);
            if(Objects.nonNull(y)) {
                y.setParent(py);
            }
            // Lb1 c1 and c2
            RedBlackNode a = null;
            if(v.getRight() != null) {
                if(v.getRight().getColor() == 1) {
                    a = v.getRight();
                }
            }
            if(a == null) {
                a = v.getLeft();
            }

            // a is either the left subtree or the right subtree
            if(Objects.equals(v.getRight(), a)) {
                // c1 : right subtree of v is red
                RedBlackNode ppy = py.getParent();
                if(ppy == null) {
                    // py is the root
                    // py has to be black because it is the root
                    // changing v to be the new root
                    this.root = v;
                    py.setRight(v.getLeft());
                    if(Objects.nonNull(py.getRight())) {
                        py.getRight().setParent(py);
                    }
                    // v's left child is now py
                    v.setLeft(py);
                    py.setParent(v);
                    // setting color of a to black
                    a.setColor(0);
                } else {
                    // py can be red or black
                    int pyColor = py.getColor();
                    boolean pyIsLeftChild = Objects.equals(py, ppy.getLeft());
                    if(pyIsLeftChild) {
                        ppy.setLeft(v);
                        v.setParent(ppy);
                    } else {
                        ppy.setRight(v);
                        v.setParent(ppy);
                    }
                    v.setColor(pyColor);
                    py.setColor(0);
                    py.setRight(v.getLeft());
                    if(py.getRight() != null) {
                        py.getRight().setParent(py);
                    }
                    // v's left child is now py
                    v.setLeft(py);
                    py.setParent(v);
                    // setting color of a to black
                    a.setColor(0);
                }
            } else {
                // c2. left subtree of v is red
                RedBlackNode ppy = py.getParent();
                if(ppy == null) {
                    RedBlackNode w = v.getLeft();
                    RedBlackNode b = w.getRight();
                    RedBlackNode ct = w.getLeft();

                    w.setParent(null);
                    this.root = w;

                    w.setColor(0);
                    py.setColor(0);


                    py.setRight(ct);
                    if(ct != null) {
                        ct.setParent(py);
                    }
                    w.setLeft(py);
                    py.setParent(w);

                    w.setRight(v);
                    v.setParent(w);
                    v.setLeft(b);
                    if(b != null) {
                        b.setParent(v);
                    }
                } else {
                    int pyColor = py.getColor();
                    RedBlackNode w = v.getLeft();
                    RedBlackNode b = w.getRight();
                    RedBlackNode ct = w.getLeft();
                    boolean pyIsLeftChild = Objects.equals(py, ppy.getLeft());

                    w.setParent(ppy);
                    if(pyIsLeftChild) {
                        ppy.setLeft(w);
                    } else {
                        ppy.setRight(w);
                    }

                    w.setColor(pyColor);
                    py.setColor(0);
                    py.setRight(ct);
                    if(ct != null) {
                        ct.setParent(py);
                    }
                    w.setLeft(py);
                    py.setParent(w);
                    w.setRight(v);
                    v.setParent(w);
                    v.setLeft(b);
                    if(b != null) {
                        b.setParent(v);
                    }
                }
            }
            return;
        } else if(!X && c == 0 && n == 2) {
            // Rb2
            py.setRight(y);
            if(Objects.nonNull(y)) {
                y.setParent(py);
            }
            RedBlackNode ppy = py.getParent();
            if(ppy == null) {
                RedBlackNode w = v.getRight();
                RedBlackNode b = w.getLeft();
                RedBlackNode ct = w.getRight();

                w.setParent(null);
                this.root = w;

                w.setColor(0);
                py.setColor(0);

                py.setLeft(ct);
                if(ct != null) {
                    ct.setParent(py);
                }
                w.setRight(py);
                py.setParent(w);
                w.setLeft(v);
                v.setParent(w);
                v.setRight(b);
                if(b != null) {
                    b.setParent(v);
                }
            } else {
                int pyColor = py.getColor();
                RedBlackNode w = v.getRight();
                RedBlackNode b = w.getLeft();
                RedBlackNode ct = w.getRight();
                boolean pyIsLeftChild = Objects.equals(py, ppy.getLeft());

                w.setParent(ppy);
                if(pyIsLeftChild) {
                    ppy.setLeft(w);
                } else {
                    ppy.setRight(w);
                }

                w.setColor(pyColor);
                py.setColor(0);
                py.setLeft(ct);
                if(ct != null) {
                    ct.setParent(py);
                }
                w.setRight(py);
                py.setParent(w);
                w.setLeft(v);
                v.setParent(w);
                v.setRight(b);
                if(b != null) {
                    b.setParent(v);
                }
            }
            return;
        } else if(X && c == 0 && n == 2) {
            py.setLeft(y);
            if(Objects.nonNull(y)) {
                y.setParent(py);
            }
            // Lb2
            // mirror of Rb2
            RedBlackNode ppy = py.getParent();
            if(ppy == null) {
                RedBlackNode w = v.getLeft();
                RedBlackNode b = w.getRight();
                RedBlackNode ct = w.getLeft();

                w.setParent(ppy);
                this.root = w;

                w.setColor(0);
                py.setColor(0);

                py.setRight(ct);
                if(ct != null) {
                    ct.setParent(py);
                }
                w.setLeft(py);
                py.setParent(w);

                w.setRight(v);
                v.setParent(w);
                v.setLeft(b);
                if(b != null) {
                    b.setParent(v);
                }
            } else {
                int pyColor = py.getColor();
                RedBlackNode w = v.getLeft();
                RedBlackNode b = w.getRight();
                RedBlackNode ct = w.getLeft();
                boolean pyIsLeftChild = Objects.equals(py, ppy.getLeft());

                w.setParent(ppy);
                if(pyIsLeftChild) {
                    ppy.setLeft(w);
                } else {
                    ppy.setRight(w);
                }

                w.setColor(pyColor);
                py.setColor(0);
                py.setRight(ct);
                if(ct != null) {
                    ct.setParent(py);
                }
                w.setLeft(py);
                py.setParent(w);
                w.setRight(v);
                v.setParent(w);
                v.setLeft(b);
                if(b != null) {
                    b.setParent(v);
                }
            }
            return;
        }

        int nw;
        if(X) {
            nw = redChildren(v.getLeft());
        } else {
            nw = redChildren(v.getRight());
        }

        if(!X && c == 1 && nw == 0) {
            // Rr(0)
            RedBlackNode ppy = py.getParent();
            py.setRight(y);
            if(Objects.nonNull(y)) {
                y.setParent(py);
            }
            if(ppy == null) {
                RedBlackNode b = v.getRight();
                RedBlackNode a = v.getLeft();
                v.setParent(null);
                this.root = v;

                v.setColor(0);
                v.setRight(py);
                py.setParent(v);

                py.setLeft(b);
                if(Objects.nonNull(b)) {
                    b.setParent(py);
                    b.setColor(1);
                }
            } else {
                boolean pyIsLeftChild = Objects.equals(ppy.getLeft(), py);
                RedBlackNode b = v.getRight();
                RedBlackNode a = v.getLeft();
                v.setParent(ppy);
                if(pyIsLeftChild) {
                    ppy.setLeft(v);
                } else {
                    ppy.setRight(v);
                }
                v.setColor(0);
                v.setRight(py);
                py.setParent(v);

                py.setLeft(b);
                if(Objects.nonNull(b)) {
                    b.setParent(py);
                    b.setColor(1);
                }
            }

        } else if(X && c == 1 && nw == 0) {
            // Lr(0)
            RedBlackNode ppy = py.getParent();
            py.setLeft(y);
            if(Objects.nonNull(y)) {
                y.setParent(py);
            }
            if(ppy == null) {
                RedBlackNode b = v.getLeft();
                RedBlackNode a = v.getRight();
                v.setParent(null);
                this.root = v;

                v.setColor(0);
                v.setLeft(py);
                py.setParent(v);

                py.setRight(b);
                if(Objects.nonNull(b)) {
                    b.setParent(py);
                    b.setColor(1);
                }
            } else {
                boolean pyIsLeftChild = Objects.equals(ppy.getLeft(), py);
                RedBlackNode b = v.getLeft();
                RedBlackNode a = v.getRight();
                v.setParent(ppy);
                if(pyIsLeftChild) {
                    ppy.setLeft(v);
                } else {
                    ppy.setRight(v);
                }
                v.setColor(0);
                v.setLeft(py);
                py.setParent(v);

                py.setRight(b);
                if(Objects.nonNull(b)) {
                    b.setParent(py);
                    b.setColor(1);
                }
            }
        } else if(!X && c == 1 && nw == 1) {
            // Rr(1)
            RedBlackNode ppy = py.getParent();
            RedBlackNode w = v.getRight();

            py.setRight(y);
            if(Objects.nonNull(y)) {
                y.setParent(py);
            }

            // Rr(1) c1
            if(w.getLeft().getColor() == 1) {
                RedBlackNode b = w.getLeft().getColor() == 1 ? w.getLeft() : w.getRight();
                RedBlackNode ct = Objects.equals(b, w.getLeft()) ? w.getRight() : w.getLeft();
                if(ppy == null) {

                    w.setParent(null);
                    this.root = w;

                } else {

                    boolean pyIsLeftChild = Objects.equals(ppy.getLeft(), py);
                    w.setParent(ppy);
                    if(pyIsLeftChild) {
                        ppy.setLeft(w);
                    } else {
                        ppy.setRight(w);
                    }

                }
                py.setLeft(ct);
                if(Objects.nonNull(ct)) {
                    ct.setParent(py);
                }
                w.setRight(py);
                py.setParent(w);
                v.setRight(b);
                b.setParent(v);
                b.setColor(0);
                w.setLeft(v);
                v.setParent(w);
            } else {
                RedBlackNode a = v.getLeft();
                RedBlackNode b = w.getLeft();
                RedBlackNode x = w.getRight();
                RedBlackNode ct = x.getLeft();
                RedBlackNode d = x.getRight();

                if(ppy == null) {

                } else {
                    boolean pyIsLeftChild = Objects.equals(ppy.getLeft(), py);
                    x.setParent(ppy);
                    if(pyIsLeftChild) {
                        ppy.setLeft(x);
                    } else {
                        ppy.setRight(x);
                    }
                    x.setColor(0);

                    w.setRight(ct);
                    if(Objects.nonNull(ct)) {
                        ct.setParent(w);
                    }

                    py.setLeft(d);
                    if(Objects.nonNull(d)) {
                        d.setParent(py);
                    }

                    x.setRight(py);
                    py.setParent(x);

                    x.setLeft(v);
                    v.setParent(x);
                }
            }

        } else if(X && c == 1 && nw == 1) {
            // Lr(1)
            RedBlackNode ppy = py.getParent();
            RedBlackNode w = v.getRight();

            py.setLeft(y);
            if(Objects.nonNull(y)) {
                y.setParent(py);
            }

            // Lr(1) c1
            if(w.getRight().getColor() == 1) {
                RedBlackNode b = w.getRight().getColor() == 1 ? w.getRight() : w.getLeft();
                RedBlackNode ct = Objects.equals(b, w.getRight()) ? w.getLeft() : w.getRight();
                if(ppy == null) {

                    w.setParent(null);
                    this.root = w;

                    py.setRight(ct);
                    if(Objects.nonNull(ct)) {
                        ct.setParent(py);
                    }

                    w.setLeft(py);
                    py.setParent(w);

                    v.setLeft(b);
                    b.setParent(v);
                    b.setColor(0);

                    w.setRight(v);
                    v.setParent(w);
                } else {

                    boolean pyIsLeftChild = Objects.equals(ppy.getLeft(), py);
                    w.setParent(ppy);
                    if(pyIsLeftChild) {
                        ppy.setLeft(w);
                    } else {
                        ppy.setRight(w);
                    }

                    py.setRight(ct);
                    if(Objects.nonNull(ct)) {
                        ct.setParent(py);
                    }

                    w.setLeft(py);
                    py.setParent(w);

                    v.setLeft(b);
                    b.setParent(v);
                    b.setColor(0);

                    w.setRight(v);
                    v.setParent(w);
                }
            } else {
                // Lr(1) c2
                RedBlackNode a = v.getRight();
                RedBlackNode b = w.getRight();
                RedBlackNode x = w.getLeft();
                RedBlackNode ct = x.getRight();
                RedBlackNode d = x.getLeft();

                if(ppy == null) {

                    x.setParent(null);
                    x.setColor(0);

                    w.setLeft(ct);
                    if(Objects.nonNull(ct)) {
                        ct.setParent(w);
                    }

                    py.setRight(d);
                    if(Objects.nonNull(d)) {
                        d.setParent(py);
                    }

                    x.setLeft(py);
                    py.setParent(x);

                    x.setRight(v);
                    v.setParent(x);
                } else {
                    boolean pyIsLeftChild = Objects.equals(ppy.getLeft(), py);
                    x.setParent(ppy);
                    if(pyIsLeftChild) {
                        ppy.setLeft(x);
                    } else {
                        ppy.setRight(x);
                    }
                    x.setColor(0);

                    w.setLeft(ct);
                    if(Objects.nonNull(ct)) {
                        ct.setParent(w);
                    }

                    py.setRight(d);
                    if(Objects.nonNull(d)) {
                        d.setParent(py);
                    }

                    x.setLeft(py);
                    py.setParent(x);

                    x.setRight(v);
                    v.setParent(x);
                }
            }
        } else if(!X && c == 1 && nw == 2) {
            // Rr(2)
            py.setRight(y);
            if(Objects.nonNull(y)) {
                y.setParent(py);
            }
            RedBlackNode ppy = py.getParent();

            RedBlackNode w = v.getRight();
            RedBlackNode b = w.getLeft();
            RedBlackNode x = w.getRight();
            RedBlackNode ct = x.getLeft();
            RedBlackNode d = x.getRight();

            if(ppy == null) {
                x.setColor(0);
                x.setParent(null);
                this.root = x;

                w.setRight(ct);
                if(Objects.nonNull(ct)) {
                    ct.setParent(w);
                }
                x.setLeft(null);

                py.setLeft(d);
                if(Objects.nonNull(d)) {
                    d.setParent(py);
                }

                x.setRight(py);
                py.setParent(x);

                v.setParent(x);
                x.setLeft(v);
            } else {
                boolean pyIsLeftChild = Objects.equals(ppy.getLeft(), py);
                x.setColor(0);
                x.setParent(ppy);
                if(pyIsLeftChild) {
                    ppy.setLeft(x);
                } else {
                    ppy.setRight(x);
                }

                w.setRight(ct);
                if(Objects.nonNull(ct)) {
                    ct.setParent(w);
                }
                x.setLeft(null);

                py.setLeft(d);
                if(Objects.nonNull(d)) {
                    d.setParent(py);
                }

                x.setRight(py);
                py.setParent(x);

                v.setParent(x);
                x.setLeft(v);

            }
        } else if(X && c == 1 && nw == 2) {
            // Lr(2)
            py.setLeft(y);
            if(Objects.nonNull(y)) {
                y.setParent(py);
            }
            RedBlackNode ppy = py.getParent();

            RedBlackNode w = v.getLeft();
            RedBlackNode b = w.getRight();
            RedBlackNode x = w.getLeft();
            RedBlackNode ct = x.getRight();
            RedBlackNode d = x.getLeft();

            if(ppy == null) {
                x.setColor(0);
                x.setParent(null);
                this.root = x;

                w.setLeft(ct);
                if(Objects.nonNull(ct)) {
                    ct.setParent(w);
                }

                py.setRight(d);
                if(Objects.nonNull(d)) {
                    d.setParent(py);
                }

                x.setLeft(py);
                py.setParent(x);

                v.setParent(x);
                x.setRight(v);
            } else {
                boolean pyIsLeftChild = Objects.equals(ppy.getLeft(), py);
                x.setColor(0);
                x.setParent(ppy);
                if(pyIsLeftChild) {
                    ppy.setLeft(x);
                } else {
                    ppy.setRight(x);
                }

                w.setLeft(ct);
                if(Objects.nonNull(ct)) {
                    ct.setParent(w);
                }
                x.setRight(null);

                py.setRight(d);
                if(Objects.nonNull(d)) {
                    d.setParent(py);
                }

                x.setLeft(py);
                py.setParent(x);

                v.setParent(x);
                x.setRight(v);

            }
        }
    }

    private int redChildren(RedBlackNode node) {
        int n = 0;
        if(node.getLeft() != null && node.getLeft().getColor() == 1) {
            n++;
        }
        if(node.getRight() != null && node.getRight().getColor() == 1) {
            n++;
        }
        return n;
    }

    public boolean isImbalanced(RedBlackNode p) {
        if(p == null) {
            return false;
        }
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
            metricCounter.increment();
            gp.setColor(1 - gp.getColor());
            metricCounter.increment();
            if(Objects.nonNull(d)) {
                d.setColor(0);
                metricCounter.increment();
            }
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

    public String createDeletionString(Book book) {
        if(book.getReservations().isEmpty()) {
            return "Book " + book.getBookID() + " is no longer available." + "\n";
        } else if (book.getReservations().size() == 1) {
            return "Book " + book.getBookID() + " is no longer available. Reservation made by Patron "
                    + book.getReservations().getNextReservation().getPatronID() + " has been cancelled!" + "\n";
        } else {
            StringBuilder sb = new StringBuilder();
            String patronList = book.getReservations().getReservationPatronList();
            sb.append("Book ").append(book.getBookID()).append(" is no longer available. Reservations made by Patrons ");
            sb.append(patronList).append(" have been cancelled!").append("\n");
            return sb.toString();
        }

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
            return list;
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

    public MetricCounter getMetricCounter() {
        return metricCounter;
    }

}
