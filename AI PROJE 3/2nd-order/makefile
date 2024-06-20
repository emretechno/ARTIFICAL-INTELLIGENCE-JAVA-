# Variables
JAVAC = javac
JAVA = java
SRC_DIR = .
OUT_DIR = bin

# Create the bin directory
$(OUT_DIR):
	mkdir -p $(OUT_DIR)

# Compile Java files in the root directory
$(OUT_DIR)/%.class: $(SRC_DIR)/%.java | $(OUT_DIR)
	$(JAVAC) -d $(OUT_DIR) $<

# Second-order targets
second-order: $(OUT_DIR)/Hmm2.class $(OUT_DIR)/RunViterbi2.class $(OUT_DIR)/Viterbi2.class

# Targets for running
run-second-order: second-order
	$(JAVA) -cp $(OUT_DIR) RunViterbi2 data/typos20.data

# Clean the build directory
clean:
	rm -rf $(OUT_DIR)

.PHONY: second-order run-second-order clean
