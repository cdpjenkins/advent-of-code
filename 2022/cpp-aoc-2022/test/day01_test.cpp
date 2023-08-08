#include <vector>

#include "doctest.h"

#include "../src/day01.hpp"
#include "../src/util.hpp"

std::vector<std::string> day01_test_input{
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


TEST_CASE("Day 01 part 1 with test data - Elf with most calories") {
    CHECK(day01_part1_elf_with_most_calories(day01_test_input) == 24000);
}

TEST_CASE("Day 01 part 1 with real data - Elf with most calories") {
    const std::vector<std::string> input{read_input_file("../../../advent-of-code-input/2022/day01.txt")};

    CHECK(day01_part1_elf_with_most_calories(input) == 67633);
}
