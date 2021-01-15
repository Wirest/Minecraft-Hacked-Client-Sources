package com.ihl.client.util;

import javax.vecmath.Point2d;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ConvexHull {

    public static ArrayList<Point2d> execute(ArrayList<Point2d> points) {
        ArrayList<Point2d> xSorted = (ArrayList<Point2d>) points.clone();
        Collections.sort(xSorted, new XCompare());

        int n = xSorted.size();

        Point2d[] lUpper = new Point2d[n];

        lUpper[0] = xSorted.get(0);
        lUpper[1] = xSorted.get(1);

        int lUpperSize = 2;

        for (int i = 2; i < n; i++) {
            lUpper[lUpperSize] = xSorted.get(i);
            lUpperSize++;

            while (lUpperSize > 2 && !rightTurn(lUpper[lUpperSize - 3], lUpper[lUpperSize - 2], lUpper[lUpperSize - 1])) {
                // Remove the middle point of the three last
                lUpper[lUpperSize - 2] = lUpper[lUpperSize - 1];
                lUpperSize--;
            }
        }

        Point2d[] lLower = new Point2d[n];

        lLower[0] = xSorted.get(n - 1);
        lLower[1] = xSorted.get(n - 2);

        int lLowerSize = 2;

        for (int i = n - 3; i >= 0; i--) {
            lLower[lLowerSize] = xSorted.get(i);
            lLowerSize++;

            while (lLowerSize > 2 && !rightTurn(lLower[lLowerSize - 3], lLower[lLowerSize - 2], lLower[lLowerSize - 1])) {
                // Remove the middle point of the three last
                lLower[lLowerSize - 2] = lLower[lLowerSize - 1];
                lLowerSize--;
            }
        }

        ArrayList<Point2d> result = new ArrayList();

        for (int i = 0; i < lUpperSize; i++) {
            result.add(lUpper[i]);
        }

        for (int i = 1; i < lLowerSize - 1; i++) {
            result.add(lLower[i]);
        }

        return result;
    }

    private static boolean rightTurn(Point2d a, Point2d b, Point2d c) {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x) > 0;
    }

    private static class XCompare implements Comparator<Point2d> {
        @Override
        public int compare(Point2d o1, Point2d o2) {
            return (new Double(o1.x)).compareTo(new Double(o2.x));
        }
    }

}
