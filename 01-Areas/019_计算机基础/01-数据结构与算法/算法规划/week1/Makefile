CXX := g++
CXXFLAGS := -std=c++17 -O2 -Wall -Wextra -pipe
SRC_DIR := solutions
BIN_DIR := bin
SRCS := $(wildcard $(SRC_DIR)/*.cpp)
BINS := $(patsubst $(SRC_DIR)/%.cpp,$(BIN_DIR)/%,$(SRCS))

all: $(BIN_DIR) $(BINS)

$(BIN_DIR):
	mkdir -p $(BIN_DIR)

$(BIN_DIR)/%: $(SRC_DIR)/%.cpp | $(BIN_DIR)
	$(CXX) $(CXXFLAGS) $< -o $@

.PHONY: clean
clean:
	rm -f $(BIN_DIR)/*
