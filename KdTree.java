import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.ArrayList;

public class KdTree {

    private class Node {
        private Point2D key;
        private Node left;
        private Node right;
        private boolean vertical;
        private int count;
        private RectHV rectHV;

        public Node(Point2D key, int count, boolean vertical) {
            this.key = key;
            this.vertical = vertical;
            this.count = count;
        }

        public Point2D getKey() { return this.key; }
    }

    private Node root;
    private int count = 0;
    private double championDistance;
    private Point2D champion;

    public boolean isEmpty() {
        return root == null;
    }

    public int nodeSize(Node node) {
        if (node == null) return 0;
        return node.count;
    }

    public int size() {
        if (root == null) return 0;
        return root.count;
    }

    public void insert(Point2D p) {
        double xmin = 0;
        double ymin = 0;
        double xmax = 1;
        double ymax = 1;
        root = insert(root, p, xmin, ymin, xmax, ymax);
        this.count = 0;
    }

    public Node insert(Node node, Point2D p, double xmin, double ymin, double xmax, double ymax) {
        if (size() == 0) {
            Node rootNode = new Node(p, 1, true);
            rootNode.rectHV = new RectHV(rootNode.getKey().x(), ymin, rootNode.getKey().x(), ymax);
            rootNode.count = 1;
            return rootNode;
        }

        if (node == null) {
            Node newNode = this.count % 2 == 0 ? new Node(p, 1, true) : new Node(p, 1, false);
            newNode.rectHV = new RectHV(xmin, ymin, xmax, ymax);
            return newNode;
        }

        this.count++;

        int cmp = node.vertical ? Double.compare(p.x(), node.getKey().x()) : Double.compare(p.y(), node.getKey().y());
        if (cmp < 0) node.left = node.vertical ? insert(node.left, p, xmin, ymin, node.getKey().x(), ymax) : insert(node.left, p, xmin, ymin, xmax, node.getKey().y());
        else if (cmp > 0) node.right = node.vertical ? insert(node.right, p, node.getKey().x(), ymin, xmax, ymax) : insert(node.right, p, xmin, node.getKey().y(), xmax, ymax);
        else { if (p.equals(node.getKey())) node.key = p; }

        node.count = 1 + nodeSize(node.left) + nodeSize(node.right);

        return node;
    }

    public boolean search(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node trace = root;
        while (trace != null) {
            int cmp = trace.vertical ? Double.compare(p.x(), trace.getKey().x()) : Double.compare(p.y(), trace.getKey().y());
            if (cmp < 0) trace = trace.left;
            else if (cmp > 0) trace = trace.right;
            else return p.equals(trace.getKey());
        }
        return false;
    }

    public void draw() {
        if (root == null) return;
        draw(root);
    }

    public void draw(Node node) {
        if (node == null) return;
        draw(node.left);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setPenRadius(0.01);
        node.getKey().draw();
        drawLine(node);
        draw(node.right);
    }

    public void drawLine(Node node) {
        if (node.vertical) {
            StdDraw.setPenColor(Color.RED);
            StdDraw.setPenRadius(0.01);
            StdDraw.line(node.getKey().x(), node.rectHV.ymin(), node.getKey().x(), node.rectHV.ymax());
        } else {
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.setPenRadius(0.01);
            StdDraw.line(node.rectHV.xmin(), node.getKey().y(), node.rectHV.xmax(), node.getKey().y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> rangeSet =  new ArrayList<Point2D>();
        if (root == null) return rangeSet;
        range(rangeSet, rect, root);
        return rangeSet;
    }

    private void range(ArrayList<Point2D> set, RectHV rect, Node node) {
        if (set == null) throw new IllegalArgumentException();
        if (node == null) return;
        if (rect.contains(node.getKey())) set.add(node.getKey());

        if (node.rectHV.intersects(rect)) {
            range(set, rect, node.right);
            range(set, rect, node.left);
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        championDistance = p.distanceSquaredTo(root.getKey());
        champion = root.getKey();
        nearest(root, p);
        //Point2D first = root.key;
        //return nearest(root, p, first);
        return champion;
    }

    public void  /*Point2D*/ nearest(Node node, Point2D p) {
        /*if (node == null)
            return first;

        if (p.distanceSquaredTo(first) < node.rectHV.distanceSquaredTo(p))
            return first;

        // update min based on current node
        if (node.key.distanceSquaredTo(p) < first.distanceSquaredTo(p))
            first = node.key; */


        if (node == null) return;
        if (node == root && size() == 1) { champion = node.getKey(); return; }
        if (node.getKey().equals(p)) { champion = p; return; }
        if (champion.distanceSquaredTo(p) < node.getKey().distanceSquaredTo(p)) return;
        else if (champion.distanceSquaredTo(p) > node.getKey().distanceSquaredTo(p)) {
            //StdOut.println(champion);
            //StdOut.println(champion.distanceSquaredTo(p));
            champion = node.getKey();
        }

       //int cmp = node.vertical ? Double.compare(p.x(), node.getKey().x()) : Double.compare(p.y(), node.getKey().y());
        if (node.left == null) {
            nearest(node.right, p);
        } else if (node.right == null) {
            nearest(node.left, p);
        } else if (node.left.rectHV.distanceSquaredTo(p) < node.right.rectHV.distanceSquaredTo(p)) {
            nearest(node.left, p);
            nearest(node.right, p);
        } else {
            nearest(node.right, p);
            nearest(node.left, p);
        }

        /*if (node.left == null) {
            return nearest(node.right, p, first);
        } else if (node.right == null) {
            return nearest(node.left, p, first);
        } else if (node.left.rectHV.distanceSquaredTo(p) < node.right.rectHV.distanceSquaredTo(p)) {
            first = nearest(node.left, p, first);
            return nearest(node.right, p, first);
        } else {
            first = nearest(node.right, p, first);
            return nearest(node.left, p, first);
        } */
    }

    public void inOrder(Node node, ArrayList<Point2D> set) {
        if (set == null) throw new IllegalArgumentException();
        if (node == null) return;
        inOrder(node.left, set);
        set.add(node.getKey());
        StdOut.println(node.rectHV);
        inOrder(node.right, set);
    }

    @Override
    public String toString() {
        ArrayList<Point2D> points = new ArrayList<>();
        inOrder(root, points);
        StringBuilder sb = new StringBuilder();
        for (Point2D point : points) sb.append(point).append("|");
        return sb.toString();
    }

    public static void main(String[] args) {
        /*KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.7, 0.2));
        kdTree.insert(new Point2D(0.5, 0.4));
        kdTree.insert(new Point2D(0.2, 0.3));
        kdTree.insert(new Point2D(0.4, 0.7));
        kdTree.insert(new Point2D(0.9, 0.6));
        StdOut.println(kdTree);
        StdOut.println(kdTree.nearest(new Point2D(0.4, 0.7)));
        StdOut.println(kdTree.size());
        kdTree.draw(); */
        /*StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.015);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        kdTree.draw(kdTree.root); */
    }
}

/*if (cmp1 < 0 && cmp2 > 0 && cmp3 < 0 && cmp4 > 0) {
            range(set, rect, node.right);
            range(set, rect, node.left);
        } else if (cmp1 >= 0 && cmp3 >= 0) {
            range(set, rect, node.right);
        } else if (cmp2 <= 0 && cmp4 <= 0) {
            range(set, rect, node.left);
        } */

//code for prune search range
