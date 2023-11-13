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
    CHECK(day01_part1_elf_with_most_calories(read_input_file("day01.txt")) == 67633);
}

TEST_CASE("Day 01 part 2 with test data - Three elves with most calories") {
    CHECK(day01_part2_three_elves_with_most_calories(day01_test_input) == 45000);
}

TEST_CASE("Day 01 part 2 with real data - Three elves with most calories") {
    CHECK(day01_part2_three_elves_with_most_calories(read_input_file("day01.txt")) == 199628);
}
