package com.mattworzala.debug.shape.util;

import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

/**
 * Credit to Moulberry's BuilderMod.
 *
 * @see <min href="https://github.com/Moulberry/BuilderMod/blob/master/remappedSrc/codes/moulberry/buildermod/CatmullRomSpline.java">CatmullRomSpline.java</min>
 */
public class CatmullRomSpline {

    private static float tj(float ti, Vec3d p1, Vec3d p2, float alpha) {
        return (float) Math.pow((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y), alpha) + ti;
    }

    private static List<Float> linspace(float t1, float t2, int points) {
        List<Float> list = new ArrayList<>(points);

        float delta = t2 - t1;

        for (int i = 0; i < points; i++) {
            list.add(t1 + delta * (i / (float) (points - 1)));
        }

        return list;
    }

    private static List<Vec3d> catmullInterp(List<Float> list, Vec3d p1, Vec3d p2, float t1, float t0) {
        List<Vec3d> newList = new ArrayList<>();
        for (float t : list) {
            Vec3d p1_2 = p1.multiply((t1 - t) / (t1 - t0));
            Vec3d p2_2 = p2.multiply((t - t0) / (t1 - t0));

            newList.add(p1_2.add(p2_2));
        }
        return newList;
    }

    private static List<Vec3d> catmullInterp(List<Float> list, List<Vec3d> p1, List<Vec3d> p2, float t1, float t0) {
        List<Vec3d> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            float t = list.get(i);

            Vec3d p1_2 = p1.get(i).multiply((t1 - t) / (t1 - t0));
            Vec3d p2_2 = p2.get(i).multiply((t - t0) / (t1 - t0));

            newList.add(p1_2.add(p2_2));
        }
        return newList;
    }

    private static List<Vec3d> createCatmullRomSpline(Vec3d p1, Vec3d p2, Vec3d p3, Vec3d p4, int pointNum, float alpha) {
        alpha = alpha / 2;

        float t0 = 0;
        float t1 = tj(t0, p1, p2, alpha);
        float t2 = tj(t1, p2, p3, alpha);
        float t3 = tj(t2, p3, p4, alpha);

        List<Float> points = linspace(t1, t2, pointNum);

        List<Vec3d> A1 = catmullInterp(points, p1, p2, t1, t0);
        List<Vec3d> A2 = catmullInterp(points, p2, p3, t2, t1);
        List<Vec3d> A3 = catmullInterp(points, p3, p4, t3, t2);

        List<Vec3d> B1 = catmullInterp(points, A1, A2, t2, t0);
        List<Vec3d> B2 = catmullInterp(points, A2, A3, t3, t1);

        List<Vec3d> C = catmullInterp(points, B1, B2, t2, t1);
        return C;
    }

    public static List<Vec3d> getCatmullRomChain(List<Vec3d> points, boolean looped) {
        if (points.size() < 2) return new ArrayList<>();

        List<Vec3d> interpPoints = new ArrayList<>();

        int size = points.size();
        for (int i = 0; i < (looped ? size : size - 1); i++) {
            int i0 = i - 1;
            int i1 = i + 1;
            int i2 = i + 2;

            if (looped) {
                i0 = i0 % size;
                i1 = i1 % size;
                i2 = i2 % size;
                if (i0 < 0) i0 += size;
            } else {
                if (i0 < 0) i0 = 0;
                if (i1 >= size) i1 = size - 1;
                if (i2 >= size) i2 = size - 1;
            }

            if (i == i1) continue;

            interpPoints.addAll(createCatmullRomSpline(points.get(i0), points.get(i), points.get(i1), points.get(i2), 20, 0f));
        }

        return interpPoints;
    }

}
