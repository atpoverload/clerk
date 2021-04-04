SOURCES =                              \
$(wildcard java/clerk/*.java)          \
$(wildcard java/clerk/examples/*.java) \
$(wildcard java/clerk/util/*.java)     \
$(wildcard java/clerk/util/**/*.java)

BUILD_DIR = clerk_build
TARGET = clerk.jar

clerk: clean
	javac $(SOURCES) -d $(BUILD_DIR)
	cd $(BUILD_DIR) && jar -cf $(TARGET) * && mv $(TARGET) .. && cd ..
	rm -r $(BUILD_DIR)

clean:
	rm -rf $(BUILD_DIR) $(TARGET)
