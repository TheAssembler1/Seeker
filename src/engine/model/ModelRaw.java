package engine.model;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.genmanagers.*;
import engine.model.shader.ModelShader;

public class ModelRaw {
	private int[] indices;
	private float[] vertices;
	private float[] normals;
	private float[] texture_coords;
	
	private int texture_id;
	private int vao_id;
	
	ModelShader shader;
	
	public ModelRaw(String Texture_File_Name, String Model_File_Name) {
		this.texture_id = TextureManager.LoadTexturePNG(Texture_File_Name);
		LoadModel(Model_File_Name);
		
		this.vao_id = BufferManager.LoadVao();
		BufferManager.LoadVbo(this.vao_id, 3, this.vertices, 0, GL_FLOAT, 0, 0);
		BufferManager.LoadVbo(this.vao_id, 2, this.texture_coords, 1, GL_FLOAT, 0, 0);
		BufferManager.LoadVbo(this.vao_id, 3, this.normals, 2, GL_FLOAT, 0, 0);
		BufferManager.LoadEbo(this.vao_id, this.indices);
		
		this.shader = new ModelShader("src/engine/model/shader/ModelVert.txt", "src/engine/model/shader/ModelFrag.txt");
	}
	
	public ModelShader GetModelShader() {
		return this.shader;
	}
	
	public void LoadTexture() {
		glBindTexture(GL_TEXTURE_2D, this.texture_id);
	}
	
	public void UnloadTexture() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public int GetIndicesLength() {
		return this.indices.length;
	}
	
	public int GetTextureID() {
		return this.texture_id;
	}
	
	public int GetVaoID() {
		return this.vao_id;
	}
	
	private void LoadModel(String Model_File_Name) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/" + Model_File_Name + ".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load file!");
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(fr);
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		try {
			while(true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if(line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				}else if(line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
				}else if(line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);	
				}else if(line.startsWith("f ")) {
					this.texture_coords = new float[vertices.size() * 2];
					this.normals = new float[vertices.size() * 3];
					break;
				}
			}
			while(line != null) {
				if(!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex_1 = currentLine[1].split("/");
				String[] vertex_2 = currentLine[2].split("/");
				String[] vertex_3 = currentLine[3].split("/");
				
				ProcessVertex(vertex_1, indices, textures, normals, this.texture_coords, this.normals);
				ProcessVertex(vertex_2, indices, textures, normals, this.texture_coords, this.normals);
				ProcessVertex(vertex_3, indices, textures, normals, this.texture_coords, this.normals);
				line = reader.readLine();
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		this.vertices = new float[vertices.size() * 3];
		this.indices = new int[indices.size()];
		
		int vertex_pointer = 0;
		for(Vector3f vertex:vertices) {
			this.vertices[vertex_pointer++] = vertex.x;
			this.vertices[vertex_pointer++] = vertex.y;
			this.vertices[vertex_pointer++] = vertex.z;
		}
		
		for(int i = 0; i < indices.size(); i++) {
			this.indices[i] = indices.get(i);
		}
	}
	private void ProcessVertex(String[] vertexData, List<Integer> indices, List <Vector2f> textures, List <Vector3f> normals, float[] texture_array, float[] normals_array) {
		int current_vertex_pointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(current_vertex_pointer);
		Vector2f current_tex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		texture_array[current_vertex_pointer * 2] = current_tex.x;
		texture_array[current_vertex_pointer * 2 + 1] = 1 - current_tex.y;
		Vector3f current_norm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normals_array[current_vertex_pointer * 3] = current_norm.x;
		normals_array[current_vertex_pointer * 3 + 1] = current_norm.y;
		normals_array[current_vertex_pointer * 3 + 2] = current_norm.z;
	}
}
