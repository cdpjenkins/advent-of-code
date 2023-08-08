#include <algorithm>
#include <string>
#include <vector>
#include <iostream>
#include <fstream>

#include "day01.hpp"

int day01_part1_elf_with_most_calories(const std::vector<std::string> &input) {
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
