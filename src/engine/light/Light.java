package engine.light;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	private Vector3f color;
	private Vector3f position;
	
	public Light(Vector3f color, Vector3f position) {
		this.color = color;
		this.position = position;
	}
	
	public void SetDeltaColor(Vector3f delta_color) {
		this.color.x += delta_color.x;
		this.color.y += delta_color.y;
		this.color.z += delta_color.z;
	}
	
	public void SetDeltaPosition(Vector3f delta_position) {
		this.position.x += delta_position.x;
		this.position.y += delta_position.y;
		this.position.z += delta_position.z;
	}
	
	public void SetColor(Vector3f color) {
		this.color = color;
	}
	
	public void SetPosition(Vector3f position) {
		this.position = position;
	}
	
	public Vector3f GetPosition() {
		return this.position;
	}
	
	public Vector3f GetColor() {
		return this.color;
	}
}
