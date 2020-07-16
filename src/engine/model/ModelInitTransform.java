package engine.model;

import org.lwjgl.util.vector.Vector3f;

public class ModelInitTransform {
	private Vector3f position;
	private Vector3f rotation;
	private int scale;
	
	public ModelInitTransform(Vector3f position, Vector3f rotation, int scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public Vector3f GetPosition() {
		return this.position;
	}
	
	public Vector3f GetRotation() {
		return this.rotation;
	}
	
	public int GetScale() {
		return this.scale;
	}
}
