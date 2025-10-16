package klaxon.klaxon.gluebox;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

public abstract class GlueBoxExtension {

    private static final Logger LOGGER = LogManager.getLogger(GlueBoxExtension.class);
    private long windowHandle;
    public String glVendor;
    public String glRenderer;
    public String glVersion;
    protected final ArrayList<Runnable> tests = new ArrayList<>();

    public void test() {
        beforeAll();
        for (var test : tests) {
            test.run();
            afterEach();
        }
        close();
    }

    public void beforeAll() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new IllegalStateException("Failed to initialize GLFW!");

        windowHandle = glfwCreateWindow(800, 600, "GLSM Tests", NULL, NULL);
        if (windowHandle == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        try (MemoryStack stack = stackPush() ) {
            var pWidth = stack.mallocInt(1); // int*
            var pHeight = stack.mallocInt(1); // int*

            glfwGetWindowSize(windowHandle, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    windowHandle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(windowHandle);

        GL.createCapabilities();

        glVendor = GL11.glGetString(GL11.GL_VENDOR);
        glRenderer = GL11.glGetString(GL11.GL_RENDERER);
        glVersion = GL11.glGetString(GL11.GL_VERSION);

        LOGGER.info("OpenGL Vendor: {}", glVendor);
        LOGGER.info("OpenGL Renderer: {}", glRenderer);
        LOGGER.info("OpenGL Version: {}", glVersion);

    }

    public void close() {
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void afterEach() {
        assert(GL11.GL_NO_ERROR == GL11.glGetError());
    }

    public void assertLog(boolean azzert, String message) {
        if (!azzert) {
            throw new AssertionError(message);
        }
    }
}
