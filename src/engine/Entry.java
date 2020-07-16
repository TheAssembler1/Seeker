package engine;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import org.lwjgl.util.vector.Vector3f;

import engine.camera.Camera;
import engine.camera.CameraInitTransform;
import engine.display.DisplayManager;
import engine.genmanagers.BufferManager;
import engine.genmanagers.TextureManager;
import engine.light.Light;
import engine.model.ModelInitTransform;
import engine.model.ModelRaw;
import engine.model.renderer.ModelRenderer;
import engine.model.Model;

public class Entry {

	public static void main(String[] args) throws Exception {
		DisplayManager.Load();
		
		CameraInitTransform camera_init_transform = new CameraInitTransform(new Vector3f(0.0f, 0.0f, 0.0f), 0, 0, 0);
		Camera camera = new Camera(camera_init_transform);
		
		ModelRaw model_raw = new ModelRaw("image", "dragon_2");
		for(int i = 0; i < 2; i++) {
			ModelInitTransform model_init_transform = new ModelInitTransform(new Vector3f(0, -10, -17f), new Vector3f(0, 0, 0), 1f);
			Model model = new Model(model_raw, model_init_transform);
			ModelRenderer.Add_Model(model_raw, model);
		}
		
		Light light = new Light(new Vector3f(1, 1, 1), new Vector3f(20f, 0f, 0.0f));
		
		while(!glfwWindowShouldClose(DisplayManager.window)){
			DisplayManager.StartFrame();

			
			ModelRenderer.Render(camera, light);
			
			DisplayManager.Update();
			DisplayManager.EndFrame();
		}
		
		//MAKE SURE TO CLEANUP SHADERS HAVENT DONE THAT YET
		TextureManager.Unload();
		BufferManager.UnloadBuffers();
		DisplayManager.Unload();
	}

}
