package klaxon.klaxon.gluebox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;
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

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

public class GlueBoxExtension implements BeforeAllCallback, AfterEachCallback, ExtensionContext.Store.CloseableResource {

    private static long windowHandle;
    public static String glVendor;
    public static String glRenderer;
    public static String glVersion;

    @Override
    public void beforeAll(ExtensionContext context) {

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

        context.getRoot().getStore(GLOBAL).put("GlueBoxExtension", this);
        glVendor = GL11.glGetString(GL11.GL_VENDOR);
        glRenderer = GL11.glGetString(GL11.GL_RENDERER);
        glVersion = GL11.glGetString(GL11.GL_VERSION);

        System.out.println("OpenGL Vendor: " + glVendor);
        System.out.println("OpenGL Renderer: " + glRenderer);
        System.out.println("OpenGL Version: " + glVersion);

    }

    @Override
    public void close() {
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        assertEquals(GL11.GL_NO_ERROR, GL11.glGetError(), "GL Error");
    }

}
