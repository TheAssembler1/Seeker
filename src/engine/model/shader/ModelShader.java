package engine.model.shader;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class ModelShader{
	
	private int FragmentShaderID;
	private int VertexShaderID;
	private int ProgramID;
	
	public ModelShader(String model_fragment_file_name, String model_vertex_file_name) {
		CreateProgram(model_fragment_file_name, model_vertex_file_name);
	}

	private int LoadShader(String file, int type) {
		StringBuilder ShaderSource = new StringBuilder();
		
		try {
			BufferedReader Reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = Reader.readLine()) != null) {
				ShaderSource.append(line).append("\n");
			}
			Reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}
		
		int ShaderID = glCreateShader(type);
		glShaderSource(ShaderID, ShaderSource);
		glCompileShader(ShaderID);
		
		if (glGetShaderi(ShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
		    System.err.println("Could not compile shader.");
		    System.exit(-1);
		}
		
		return ShaderID;
	}
	
	public void CreateProgram(String VertexFile, String FragmentFile) {
		this.VertexShaderID = LoadShader(VertexFile, GL_VERTEX_SHADER);
		this.FragmentShaderID = LoadShader(FragmentFile, GL_FRAGMENT_SHADER);
		this.ProgramID = glCreateProgram();
		
		glAttachShader(this.ProgramID, this.VertexShaderID);
		glAttachShader(this.ProgramID, this.FragmentShaderID);
		
		glLinkProgram(this.ProgramID);
		glValidateProgram(this.ProgramID);
	}
	
	public void ProgramCleanup() {
		glDetachShader(this.ProgramID, this.VertexShaderID);
		glDetachShader(this.ProgramID, this.FragmentShaderID);
		glDeleteShader(this.VertexShaderID);
		glDeleteShader(this.FragmentShaderID);
		glDeleteProgram(this.ProgramID);
	}
	
	public int GetProgramID() {
		return this.ProgramID;
	}
	
	public void LoadFloat(String uniform_name, float value) {
		glUniform1f(glGetUniformLocation(this.ProgramID, uniform_name), value);
	}
	
	public void LoadVector3(String uniform_name, Vector3f vector) {
		glUniform3f(glGetUniformLocation(this.ProgramID, uniform_name), vector.x, vector.y, vector.z);
	}
	
	public void LoadBoolean(String uniform_name, boolean value) {
		float to_load = 0;
		if(value) 
			to_load = 1;
		glUniform1f(glGetUniformLocation(this.ProgramID, uniform_name), to_load);
	}
	
	public void LoadMatrix4(String uniform_name, Matrix4f matrix) {
		FloatBuffer matrix_buffer = BufferUtils.createFloatBuffer(4*4);
		matrix.store(matrix_buffer);
		matrix_buffer.flip();
		glUniformMatrix4fv(glGetUniformLocation(this.ProgramID, uniform_name), false, matrix_buffer);
	}
	
	public void StartProgram() {
		glUseProgram(this.ProgramID);
	}
	
	public void StopProgram() {
		glUseProgram(0);
	}
}
