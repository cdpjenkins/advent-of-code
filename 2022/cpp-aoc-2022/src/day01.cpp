#include <algorithm>
#include <string>
#include <vector>
#include <iostream>
#include <fstream>

#include "day01.hpp"

std::vector<std::string> read_input_file(char *const *argv) {
    std::ifstream input_stream{argv[1]};
    if (!input_stream) {
        std::cerr << "Failed to open " << argv[1] << std::endl;
        throw std::runtime_error("Failed to open " + std::string(argv[1]));
    }

    std::vector<std::string> lines;
    std::string str;
    while (std::getline(input_stream, str)) {
        lines.push_back(str);
    }
    return lines;
}

int day01_part1_elf_with_most_calories() {
    std::vector<std::string> input{
            "1000",
            "2000",
            "3000",
            "",
            "4000",
            "",
            "5000",
            "6000",
            "",
            "7000",
            "8000",
            "9000",
            "",
            "10000"
    };

    std::vector<int> elves{};
    int calories = 0;
    for (auto& line : input) {
        if (line.empty()) {
            elves.push_back(calories);
            calories = 0;
        } else {
            int c = std::stoi(line);
            calories += c;
        }
    }

    int max_calories = *(std::max_element(elves.begin(), elves.end()));

    return max_calories;
}
