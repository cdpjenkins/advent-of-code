#include <vector>
#include <string>
#include <ranges>
#include <algorithm>
#include <numeric>
#include <cstdint>
#include <iostream>

#include "day01.hpp"


std::vector<int> parse_elves(const std::vector<std::string> &input) {
    using namespace std::string_literals;

    auto elves = input
                 | std::views::lazy_split(""s)
                 | std::views::transform([](const auto& elf) -> uint64_t { // sum up the calories for each elf: range{uint64_t}
        auto to_unsigned = [](const auto& in) { return std::stoull(in); };
        auto rng = elf | std::views::transform(to_unsigned) | std::views::common; // range{string} -> range{uint64_t}
        return std::reduce(rng.begin(), rng.end()); // range{uint64_t} -> uint64_t
    });

    return (std::vector<int>) {elves.begin(), elves.end()};
}

int day01_part1_elf_with_most_calories(const std::vector<std::string> &input) {
    auto elves{parse_elves(input)};
    int max_calories = *(std::max_element(elves.begin(), elves.end()));

    return max_calories;
}

int day01_part2_three_elves_with_most_calories(const std::vector<std::string> &input) {
    auto elves{parse_elves(input)};
    std::sort(elves.begin(), elves.end(), std::greater<>());
    int total_calories_of_top_three = std::reduce(elves.begin(), elves.begin() + 3);

    return total_calories_of_top_three;
}
