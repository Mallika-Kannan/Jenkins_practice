public class Hello {
    public static void main(String[] args) {
        System.out.println("✅ Hello, Jenkins with Maven!");
        System.out.println("Application is now running in an infinite loop...");
        while (true) {
            Thread.sleep(60000); // Sleep for 60 seconds to prevent using CPU
        }
    }
}
