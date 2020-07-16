package engine.camera;

import org.lwjgl.util.vector.Vector3f;

public class CameraInitTransform {
	private Vector3f position;
	private float pitch;
	private float yaw;
	private float roll;
	
	public CameraInitTransform(Vector3f position, float pitch, float yaw, float roll) {
		this.position = position;
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}
	
	public Vector3f GetPosition() {
		return this.position;
	}
	
	public float GetPitch() {
		return this.pitch;
	}
	
	public float GetYaw() {
		return this.yaw;
	}
	
	public float GetRoll() {
		return this.roll;
	}
}
