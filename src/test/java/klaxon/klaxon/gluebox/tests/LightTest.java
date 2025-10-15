package klaxon.klaxon.gluebox.tests;

import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.glGetBoolean;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glEnable;

import klaxon.klaxon.gluebox.GlueBoxExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(GlueBoxExtension.class)
public class LightTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LightTest.class);

    @Test
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