# Variables
JAVAC = javac
JAVA = java
SRC_DIR = .
OUT_DIR = bin

# Create the bin directory
$(OUT_DIR):
	mkdir -p $(OUT_DIR)

# Compile first-order classes
$(OUT_DIR)/%.class: $(SRC_DIR)/%.java | $(OUT_DIR)
	$(JAVAC) -d $(OUT_DIR) $<


# First-order targets
first-order: $(OUT_DIR) $(OUT_DIR)/Hmm.class $(OUT_DIR)/DataSet.class $(OUT_DIR)/RunViterbi.class $(OUT_DIR)/Viterbi.class

# Targets for running
run-first-order: first-order
	$(JAVA) -cp $(OUT_DIR) RunViterbi $(SRC_DIR)/data/robot_no_momemtum.data


# Clean the build directory
clean:
	rm -rf $(OUT_DIR)

.PHONY: first-order second-order run-first-order run-second-order clean
