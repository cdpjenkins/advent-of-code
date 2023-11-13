#include <vector>

#include "doctest.h"

#include "../src/day02.hpp"
#include "../src/util.hpp"

std::vector<std::string> day02_test_input{
        "A Y",
        "B X",
        "C Z"
};

TEST_CASE("Day 02 part 1 with test data - rock paper scissors") {
    CHECK(day02_part1_rock_paper_scissors(day02_test_input) == -1);
}

