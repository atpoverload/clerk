SOURCES = $(wildcard java/clerk/*.java) $(wildcard java/clerk/data/*.java)
BUILD_DIR = clerk-build
CLASS_DIR = clerk
TARGET = clerk.jar

clerk: clean
	mkdir -p $(BUILD_DIR)/$(CLASS_DIR)
	javac $(SOURCES) -d $(BUILD_DIR)/$(CLASS_DIR)
	cd $(BUILD_DIR) && jar -cf $(TARGET) $(CLASS_DIR)
	mv $(BUILD_DIR)/$(TARGET) $(TARGET)
	rm -r $(BUILD_DIR)

clean:
	rm -rf $(BUILD_DIR)/$(CLASS_DIR) $(TARGET)
