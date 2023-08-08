#include <algorithm>
#include <string>
#include <vector>
#include <numeric>

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

int day01_part2_three_elves_with_most_calories(const std::vector<std::string> &input) {
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
    if (calories != 0) {
        elves.push_back(calories);
    }

    std::sort(elves.begin(), elves.end(), std::greater<>());

    int total_calories_of_top_three = std::reduce(elves.begin(), elves.begin() + 3);

    return total_calories_of_top_three;
}

