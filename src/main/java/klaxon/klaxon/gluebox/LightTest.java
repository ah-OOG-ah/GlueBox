package klaxon.klaxon.gluebox;

import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.glGetBoolean;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glEnable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class LightTest extends GlueBoxExtension {
    private static final Logger LOGGER = LogManager.getLogger(LightTest.class);

    public LightTest() {
        super();
        tests.add(this::testPushPopEnableBit);
    }

    void testPushPopEnableBit() {
        // Unset all light bits
        final int maxLights = GL11.glGetInteger(GL11.GL_MAX_LIGHTS);
        for (int i = 0; i < maxLights; i++) {
            glDisable(GL_LIGHT0 + i);
        }

        // Push the bits and make sure they haven't changed
        glPushAttrib(GL11.GL_ENABLE_BIT);
        for (int i = 0; i < maxLights; ++i) {
            assertLog(!glGetBoolean(GL_LIGHT0 + i), "Passthrough failed!");
        }

        // Set them...
        for (int i = 0; i < maxLights; ++i) {
            glEnable(GL_LIGHT0 + i);
            assertLog(glGetBoolean(GL_LIGHT0 + i), "Failed to set bit!");
        }

        // And pop them, making sure they get unset
        glPopAttrib();

        for (int i = 0; i < maxLights; ++i) {
            glEnable(GL_LIGHT0 + i);
            assertLog(!glGetBoolean(GL_LIGHT0 + i), "Failed to pop bit state!");
        }
    }
}