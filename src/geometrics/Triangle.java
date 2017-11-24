package geometrics;
import color.RGB;

import java.util.ArrayList;
import java.util.List;

public class Triangle {
    public Vec3D p1;
    public Vec3D p2;
    public Vec3D p3;
    public RGB color;
    public Vec3D normal;
    float shininess;

    Triangle(Vec3D pp1, Vec3D pp2, Vec3D pp3, RGB col, float sh) {
        p1 = pp1;
        p2 = pp2;
        p3 = pp3;
        color = col;
        shininess = sh;
        Vec3D e1 = p2.minus(p1);
        Vec3D e2 = p3.minus(p1);
        normal = e1.cross(e2);
        normal.normalize();
    }

    public static List<Triangle> buildCube(int x, int y, int z, int w, int h, int d, RGB c, float sh) {  //front
        List<Triangle> triangles = new ArrayList<>();

        Vec3D[] vecArr = {
                new Vec3D(x, y, z), new Vec3D(x + w, y, z), new Vec3D(x, y + h, z),
                new Vec3D(x + w, y, z), new Vec3D(x + w, y + h, z), new Vec3D(x, y + h, z),
                //left
                new Vec3D(x, y, z + d), new Vec3D(x, y, z), new Vec3D(x, y + h, z),
                new Vec3D(x, y + h, z), new Vec3D(x, y + h, z + d), new Vec3D(x, y, z + d),
                //right
                new Vec3D(x + w, y, z), new Vec3D(x + w, y, z + d), new Vec3D(x + w, y + h, z),
                new Vec3D(x + w, y + h, z), new Vec3D(x + w, y, z + d), new Vec3D(x + w, y + h, z + d),
                //top
                new Vec3D(x + w, y + h, z), new Vec3D(x + w, y + h, z + d), new Vec3D(x, y + h, z),
                new Vec3D(x, y + h, z), new Vec3D(x + w, y + h, z + d), new Vec3D(x, y + h, z + d),
                //bottom
                new Vec3D(x + w, y, z), new Vec3D(x, y, z), new Vec3D(x, y, z + d),
                new Vec3D(x, y, z + d), new Vec3D(x + w, y, z + d), new Vec3D(x + w, y, z),
                //back
                new Vec3D(x, y, z + d), new Vec3D(x, y + h, z + d), new Vec3D(x + w, y, z + d),
                new Vec3D(x + w, y, z + d), new Vec3D(x, y + h, z + d), new Vec3D(x + w, y + h, z + d)
        };

        for (int i = 0; i < vecArr.length -2;){
            triangles.add(new Triangle(vecArr[i], vecArr[i+1],vecArr[i+2], c, sh));
            i += 3;
        }
        return triangles;
    }

}
