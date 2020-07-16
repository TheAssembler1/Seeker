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
import engine.model.renderer.ModelRenderer;
import engine.model.Model;

public class Entry {

	public static void main(String[] args) throws Exception {
		DisplayManager.Load();
		
		ModelRenderer model_renderer = new ModelRenderer();
		
		CameraInitTransform camera_init_transform = new CameraInitTransform(new Vector3f(0.0f, 0.0f, 0.0f), 0, 0, 0);
		Camera camera = new Camera(camera_init_transform);
		
		ModelInitTransform model_init_transform = new ModelInitTransform(new Vector3f(0.0f, -4f, -17.0f), new Vector3f(0.0f, 0.0f, 0.0f), 1);
		Model model = new Model("image", "dragon_2", model_init_transform);
		model.SetRotation(new Vector3f(0f, 180f, 0f));
		
		Light light = new Light(new Vector3f(1, 1, 1), new Vector3f(20f, 0f, 0.0f));
		
		while(!glfwWindowShouldClose(DisplayManager.window)){
			DisplayManager.StartFrame();
			
			model.SetDeltaRotation(new Vector3f(0f, -.01f, 0f));
			
			model_renderer.Prepare(model);
			model_renderer.Render(model, camera, light);
			
			DisplayManager.Update();
			DisplayManager.EndFrame();
		}
		
		model.GetModelRaw().GetModelShader().ProgramCleanup();
		TextureManager.Unload();
		BufferManager.UnloadBuffers();
		DisplayManager.Unload();
	}

}
