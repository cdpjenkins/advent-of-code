#include <vector>

#include "doctest.h"

#include "../src/day04.hpp"
#include "../src/util.hpp"

std::vector<std::string> day04_test_input{
        "2-4,6-8",
        "2-3,4-5",
        "5-7,7-9",
        "2-8,3-7",
        "6-6,4-6",
        "2-6,4-8"
};

TEST_CASE("Day 04 part 1 with test data") {
    CHECK(day04_part1(day04_test_input) == 2);
}

TEST_CASE("Day 04 part 1 with real data") {
    CHECK(day04_part1(read_input_file("day04.txt")) == 511);
}

TEST_CASE("Day 04 part 2 with test data") {
    CHECK(day04_part2(day04_test_input) == 4);
}

TEST_CASE("Day 04 part 2 with real data") {
    CHECK(day04_part2(read_input_file("day04.txt")) == 821);
}
