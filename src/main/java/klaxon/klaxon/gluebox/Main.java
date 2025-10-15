package klaxon.klaxon.gluebox;

public class Main {
    static void main() {
        final var lightTest = new LightTest();
        lightTest.beforeAll();
        lightTest.testPushPopEnableBit();
        lightTest.afterEach();
        lightTest.close();
    }
}
