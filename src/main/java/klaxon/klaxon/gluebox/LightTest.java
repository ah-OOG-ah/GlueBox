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

    void testPushPopEnableBit() {
        // Unset all light bits
        final int maxLights = GL11.glGetInteger(GL11.GL_MAX_LIGHTS);
        for (int i = 0; i < maxLights; i++) {
            glDisable(GL_LIGHT0 + i);
        }

        // Push the bits and make sure they haven't changed
        glPushAttrib(GL11.GL_ENABLE_BIT);
        for (int i = 0; i < maxLights; ++i) {
            assert(!glGetBoolean(GL_LIGHT0 + i));
        }
        LOGGER.info("Unset bits successfully passed through!");

        // Set them...
        for (int i = 0; i < maxLights; ++i) {
            glEnable(GL_LIGHT0 + i);
            assert(glGetBoolean(GL_LIGHT0 + i));
        }
        LOGGER.info("All bits successfully set!");

        // And pop them, making sure they get unset
        glPopAttrib();

        for (int i = 0; i < maxLights; ++i) {
            glEnable(GL_LIGHT0 + i);
            assert(!glGetBoolean(GL_LIGHT0 + i));
        }
        LOGGER.info("All bits successfully popped!");
    }
}