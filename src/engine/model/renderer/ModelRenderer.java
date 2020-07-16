package engine.model.renderer;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL32.*;

import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

import engine.camera.Camera;
import engine.display.DisplayManager;
import engine.light.Light;
import engine.maths.Matrices;
import engine.model.Model;

public class ModelRenderer {
	
	private static final int FOV = 70;
	private static final int FAR_PLANE = 1000;
	private static final float NEAR_PLANE = 0.1f;
	
	public void Prepare(Model model) {
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		Matrix4f projection_matrix = Matrices.CreateProjectionMatrix(DisplayManager.WINDOW_WIDTH, DisplayManager.WINDOW_HEIGHT, FOV, NEAR_PLANE, FAR_PLANE);
		model.GetModelRaw().GetModelShader().StartProgram();
		model.GetModelRaw().GetModelShader().LoadMatrix4("projection_matrix", projection_matrix);
		model.GetModelRaw().GetModelShader().StopProgram();
	}
	
	public void Render(Model model, Camera camera, Light light) {
		model.GetModelRaw().GetModelShader().StartProgram();
		Matrix4f transformation_matrix = Matrices.CreateTransformationMatrix(model);
		model.GetModelRaw().GetModelShader().LoadMatrix4("transformation_matrix", transformation_matrix);
		
		Matrix4f view_matrix = Matrices.CreateViewMatrix(camera);
		model.GetModelRaw().GetModelShader().LoadMatrix4("view_matrix", view_matrix);
		
		model.GetModelRaw().GetModelShader().LoadVector3("light_position", light.GetPosition());
		model.GetModelRaw().GetModelShader().LoadVector3("light_color", light.GetColor());
		
		glBindVertexArray(model.GetModelRaw().GetVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glActiveTexture(GL_TEXTURE0);
		model.GetModelRaw().LoadTexture();
		
		glDrawElements(GL_TRIANGLES, model.GetModelRaw().GetIndicesLength(), GL_UNSIGNED_INT, 0);
		
		model.GetModelRaw().UnloadTexture();
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glBindVertexArray(0);
		model.GetModelRaw().GetModelShader().StopProgram();
	}
}
