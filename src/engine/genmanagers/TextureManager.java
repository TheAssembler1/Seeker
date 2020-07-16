package engine.genmanagers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.matthiasmann.twl.utils.PNGDecoder;

import static org.lwjgl.opengl.GL32.*;

public class TextureManager {
	
	private static List<Integer> textures = new ArrayList<Integer>();
	
	public static int LoadTexturePNG(String texture_file_name){
	    PNGDecoder decoder = null;
	    FileInputStream in = null;
	    
		try {
			in = new FileInputStream("res/" + texture_file_name + ".png");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    
		try {
			decoder = new PNGDecoder(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

	    ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

	    try {
			decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
		} catch (IOException e) {
			e.printStackTrace();
		}

	    buffer.flip();

	    int texture_id = glGenTextures();
	    glBindTexture(GL_TEXTURE_2D, texture_id);
	    
	    glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

	    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
	    glGenerateMipmap(GL_TEXTURE_2D);
	    
	    glBindTexture(GL_TEXTURE_2D, texture_id);

	    return texture_id; 
	}
	
	public static void Unload() {
		for(int texture:textures) {
			glDeleteTextures(texture);
		}
	}
}
