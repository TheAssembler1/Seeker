package engine.model;

import org.lwjgl.util.vector.Vector3f;

public abstract class ModelBase{
	private Vector3f position;
	private Vector3f rotation;
	private int scale;
	ModelRaw model_raw;
	
	private float ambient = 0.1f;
	private float shine_damper = 1;
	private float reflectivity = .4f;

	public ModelBase(String texture_file_name, String model_file_name, ModelInitTransform model_init_transform) {
		this.model_raw = new ModelRaw(texture_file_name, model_file_name);
		this.position = model_init_transform.GetPosition();
		this.rotation = model_init_transform.GetRotation();
		this.scale = model_init_transform.GetScale();
		
		SetAmbient(this.ambient);
		SetReflectivity(this.reflectivity);
		SetShineDamper(this.shine_damper);
	}
	
	public void SetReflectivity(float reflectivity) {
		this.model_raw.GetModelShader().StartProgram();
		this.model_raw.GetModelShader().LoadFloat("reflectivity", this.reflectivity);
		this.model_raw.GetModelShader().StopProgram();	
	}
	
	public void SetShineDamper(float shine_damper) {
		this.model_raw.GetModelShader().StartProgram();
		this.model_raw.GetModelShader().LoadFloat("shine_damper", this.shine_damper);
		this.model_raw.GetModelShader().StopProgram();	
	}
	
	public void SetAmbient(float ambient) {
		this.model_raw.GetModelShader().StartProgram();
		this.model_raw.GetModelShader().LoadFloat("ambient", this.ambient);
		this.model_raw.GetModelShader().StopProgram();
	}
	
	public ModelRaw GetModelRaw() {
		return this.model_raw;
	}
	
	public void SetDeltaPosition(Vector3f delta_position) {
		this.position.x += delta_position.x;
		this.position.y += delta_position.y;
		this.position.z += delta_position.z;
	}
	
	public void SetDeltaRotation(Vector3f delta_rotation) {
		this.rotation.x += delta_rotation.x;
		this.rotation.y += delta_rotation.y;
		this.rotation.z += delta_rotation.z;
	}
	
	public void SetDeltaScale(int delta_scale) {
		this.scale += delta_scale;
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
	
	public void SetPosition(Vector3f position) {
		this.position = position;
	}
	
	public void SetRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	
	public void SetScale(int scale) {
		this.scale = scale;
	}
}
