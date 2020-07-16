package engine.display;

import static org.lwjgl.opengl.GL32.*;

import org.lwjgl.glfw.GLFWErrorCallback;

import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import imgui.ImFontAtlas;
import imgui.ImGui;
import imgui.ImGuiFreeType;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;

import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public final class DisplayManager {
    public static long window; 
    
    public static final int WINDOW_WIDTH = 1020;
    public static final int WINDOW_HEIGHT = 780;

    private final static ImGuiImplGlfw im_gui_glfw = new ImGuiImplGlfw();
    private final static ImGuiImplGl3 im_gui_gl3 = new ImGuiImplGl3();
    
    private static String glsl_version = null; 

    private final static ImGuiUi im_gui_ui = new ImGuiUi();

    public static void Load() throws Exception {
        ImGui.createContext();

        SetupGlfw();
        im_gui_glfw.init(window, true);

        SetupImGui();
        im_gui_gl3.init(glsl_version);
    }
    
    public static void Unload() {
        im_gui_gl3.dispose();
        im_gui_glfw.dispose();

        ImGui.destroyContext();

        Cleanup();
    }

    private static void SetupGlfw() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints(); 
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); 
        
        DecideVersions();

        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Dear ImGui+LWJGL Example", NULL, NULL);

        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            final IntBuffer pWidth = stack.mallocInt(1); 
            final IntBuffer pHeight = stack.mallocInt(1); 

            glfwGetWindowSize(window, pWidth, pHeight);

            final GLFWVidMode vidmode = Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor()));

            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
        }

        glfwMakeContextCurrent(window); 
        glfwSwapInterval(GLFW_FALSE); 
        glfwShowWindow(window);

        GL.createCapabilities();
    }

    private static void DecideVersions() {
        final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
        if (isMac) {
        	glsl_version = "#version 150";
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);      
        } else {
        	glsl_version = "#version 130";
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
        }
    }

    private static void SetupImGui() {
        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename(null); 
        io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard | ImGuiConfigFlags.DockingEnable);

        final ImFontAtlas fontAtlas = io.getFonts();
        fontAtlas.addFontDefault();

        ImGuiFreeType.buildFontAtlas(fontAtlas, ImGuiFreeType.RasterizerFlags.LightHinting);
    }
    
    public static void Update() {
    	im_gui_glfw.newFrame();
        ImGui.newFrame();
        im_gui_ui.render();
        ImGui.render();
    }

    public static void StartFrame() {
        glClearColor(im_gui_ui.background_color[0], im_gui_ui.background_color[1], im_gui_ui.background_color[2], 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public static void EndFrame() {
    	im_gui_gl3.render(ImGui.getDrawData());
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    private static void Cleanup() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
    
    public static void EnableWireframeMode() {
    	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }
    
    public static void DisableWireframeMode() {
    	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }
}
