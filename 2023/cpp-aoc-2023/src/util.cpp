#include <vector>
#include <fstream>
#include <iostream>

#include "util.hpp"

std::vector<std::string> read_input_file(const char* filename) {
    std::ifstream input_stream = open_input_file(filename);
    if (!input_stream) {
        std::cerr << "Failed to open " << filename[1] << std::endl;
        throw std::runtime_error("Failed to open " + std::string(filename));
    }

    std::vector<std::string> lines;
    std::string str;
    while (std::getline(input_stream, str)) {
        lines.push_back(str);
    }
    return lines;
}

std::ifstream open_input_file(const char *filename) {
    std::ifstream input_stream(std::string{"../../../advent-of-code-input/2023/"} + filename);
    return input_stream;
}
