package engine.maths;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engine.camera.*;
import engine.model.Model;

public class Matrices {
	public static Matrix4f CreateTransformationMatrix(Model model) {
		Matrix4f matrix = new Matrix4f();
		
		matrix.setIdentity();
		Matrix4f.translate(model.GetPosition(), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(model.GetRotation().x), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(model.GetRotation().y), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(model.GetRotation().z), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(model.GetScale(), model.GetScale(), model.GetScale()), matrix, matrix);
		
		return matrix;
	}
	
	public static Matrix4f CreateProjectionMatrix(float DISPLAY_WIDTH, float DISPLAY_HEIGHT, float FOV, float NEAR_PLANE, float FAR_PLANE) {
		Matrix4f projection_matrix = new Matrix4f();
		
		float aspect_ratio = DISPLAY_WIDTH / DISPLAY_HEIGHT;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspect_ratio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projection_matrix.m00 = x_scale;
		projection_matrix.m11 = y_scale;
		projection_matrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projection_matrix.m23 = -1;
		projection_matrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projection_matrix.m33 = 0;
		
		return projection_matrix;
	}
	
	public static Matrix4f CreateViewMatrix(Camera camera) {
		Matrix4f view_matrix = new Matrix4f();
		view_matrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.GetPitch()), new Vector3f(1, 0, 0), view_matrix, view_matrix);
		Matrix4f.rotate((float) Math.toRadians(camera.GetYaw()), new Vector3f(0, 1, 0), view_matrix, view_matrix);
		Matrix4f.rotate((float) Math.toRadians(camera.GetRoll()), new Vector3f(0, 0, 1), view_matrix, view_matrix);
		Vector3f camera_pos = camera.GetPosition();
		Vector3f negative_camera_pos = new Vector3f(-camera_pos.x, -camera_pos.y, -camera_pos.z);
		Matrix4f.translate(negative_camera_pos, view_matrix, view_matrix);
		return view_matrix;
	}
}
