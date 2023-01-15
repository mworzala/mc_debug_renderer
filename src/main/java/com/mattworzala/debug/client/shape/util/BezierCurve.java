package com.mattworzala.debug.client.shape.util;

import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BezierCurve {

    public static @NotNull List<Vec3d> getBezierChain(@NotNull List<Vec3d> controlSets) {
        System.out.println("CURVE CALL WITH " + controlSets.size());
        if (controlSets.size() < 4)
            return List.of();
        List<Vec3d> points = new ArrayList<>();
        if (controlSets.size() == 4) {
            for (int i = 0; i < 21; i++) {
                double t = i / 20.0;
                points.add(new Vec3d(
                        bezierPoint(t, controlSets.get(0).x, controlSets.get(1).x, controlSets.get(2).x, controlSets.get(3).x),
                        bezierPoint(t, controlSets.get(0).y, controlSets.get(1).y, controlSets.get(2).y, controlSets.get(3).y),
                        bezierPoint(t, controlSets.get(0).z, controlSets.get(1).z, controlSets.get(2).z, controlSets.get(3).z)
                ));
            }
        } else {
            for (int chain = 0; chain < controlSets.size() / 4; chain++) {
                int start = chain * 4;
                int end = Math.min(start + 4, controlSets.size());
                points.addAll(getBezierChain(controlSets.subList(start, end)));
            }
        }

        return points;
    }

    private static double bezierPoint(double t, double p0, double p1, double p2, double p3) {
        return p0*Math.pow(1-t, 3) + 3*p1*Math.pow(1-t, 2)*t + 3*p2*(1-t)*t*t + p3*t*t*t;
    }
}
