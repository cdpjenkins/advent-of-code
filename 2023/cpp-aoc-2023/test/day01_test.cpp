#include <vector>

#include "doctest.h"

#include "../src/day01.hpp"
#include "../src/util.hpp"

std::vector<std::string> day01_test_input{
    "1abc2",
    "pqr3stu8vwx",
    "a1b2c3d4e5f",
    "treb7uchet"
};

std::vector<std::string> day01_part_2_test_input{
    "two1nine",
    "eightwothree",
    "abcone2threexyz",
    "xtwone3four",
    "4nineeightseven2",
    "zoneight234",
    "7pqrstsixteen"
};


TEST_CASE("Day 01 part 1 with test data - Elf with most calories") {
    CHECK(day01_part1(day01_test_input) == 142);
}

TEST_CASE("Day 01 part 1 with real data - Elf with most calories") {
    CHECK(day01_part1(read_input_file("day01.txt")) == 54951);
}

TEST_CASE("Day 01 part 2 with test data - Three elves with most calories") {
    CHECK(day01_part2(day01_part_2_test_input) == 281);
}

TEST_CASE("Day 01 part 2 with real data - Three elves with most calories") {
    CHECK(day01_part2(read_input_file("day01.txt")) == 55218);
}
