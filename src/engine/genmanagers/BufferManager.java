package engine.genmanagers;

import static org.lwjgl.opengl.GL32.*;

import java.util.ArrayList;
import java.util.List;

public class BufferManager {
	
	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();
	
	public static int LoadVao(){
		int vao = glGenVertexArrays();
		vaos.add(vao);
		return vao;
	}
	
	public static void LoadVbo(int vao, int data_size, float[] data, int attribute_pointer, int data_type, int stride, int offset) {
		glBindVertexArray(vao);
		int vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		glVertexAttribPointer(attribute_pointer, data_size, data_type, false, stride, offset);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public static void LoadEbo(int vao, int[] indices) {
		int Ebo = glGenBuffers();
		vbos.add(Ebo);
		glBindVertexArray(vao);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, Ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		glBindVertexArray(0);
	}
	
	
	private static void UnloadVaoS() {
		for(int vao:vaos)
			glDeleteVertexArrays(vao);
	}
	
	private static void UnloadVboS() {
		for(int vbo:vbos)
			glDeleteBuffers(vbo);
	}
	
	public static void UnloadBuffers() {
		UnloadVaoS();
		UnloadVboS();
	}
}
