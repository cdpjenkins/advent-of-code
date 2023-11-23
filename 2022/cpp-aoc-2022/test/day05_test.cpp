#include <fstream>
#include <sstream>

#include "doctest.h"

#include "../src/day05.hpp"

using namespace std::string_literals;


static std::string day_05_test_input{
R"(    [D]
[N] [C]
[Z] [M] [P]
 1   2   3

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2)"};

TEST_CASE("Day 05 part 1 with test data") {
    std::stringstream input{day_05_test_input};
    CHECK(day05_part1(input) == "CMZ");
}

TEST_CASE("Day 05 part 1 with real data") {
    std::ifstream input{"../../../advent-of-code-input/2022/day05.txt"};
    CHECK(day05_part1(input) == "HNSNMTLHQ");
}

TEST_CASE("Day 05 part 2 with test data") {
    std::stringstream input{day_05_test_input};
    CHECK(day05_part2(input) == "MCD");
}

TEST_CASE("Day 05 part 2 with real data") {
    std::ifstream input{"../../../advent-of-code-input/2022/day05.txt"};
    CHECK(day05_part2(input) == "RNLFDJMCT");
}
