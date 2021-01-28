SOURCES = $(wildcard java/clerk/*.java) $(wildcard java/clerk/data/*.java)
TARGET = clerk.jar
TEMP = clerk

clerk:
	mkdir $(TEMP)
	javac $(SOURCES)
	jar --create --file $(TARGET) $(TEMP)
	rm -r $(TEMP)
