package engine.camera;

import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private Vector3f position;
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera(CameraInitTransform camera_init_transform) {
		this.position = camera_init_transform.GetPosition();
		this.pitch = camera_init_transform.GetPitch();
		this.yaw = camera_init_transform.GetYaw();
		this.roll = camera_init_transform.GetRoll();
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
	
	public void SetPosition(Vector3f position) {
		this.position = position;
	}
	
	public void SetPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public void SetYaw(float yaw) {
		this.yaw = yaw;
	}
	
	public void SetRoll(float roll) {
		this.roll = roll;
	}
	
	public void SetDeltaPosition(Vector3f position) {
		this.position.x += position.x;
		this.position.y += position.y;
		this.position.z += position.z;
	}
	
	public void SetDeltaPitch(float pitch) {
		this.pitch += pitch;
	}
	
	public void SetDeltaYaw(float yaw) {
		this.yaw += yaw;
	}
	
	public void SetDeltaRoll(float roll) {
		this.roll += roll;
	}
}
