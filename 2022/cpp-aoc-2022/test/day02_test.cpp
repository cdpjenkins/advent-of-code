#include <vector>

#include "doctest.h"

#include "../src/day02.hpp"
#include "../src/util.hpp"

std::vector<std::string> day02_test_input{
        "A Y",
        "B X",
        "C Z"
};

TEST_CASE("Day 02 part 1 with test data - rock paper scissors following choice") {
    CHECK(day02_part1_rock_paper_scissors_following_choice(day02_test_input) == 15);
}

TEST_CASE("Day 02 part 1 with real data - rock paper scissors following choice") {
    CHECK(day02_part1_rock_paper_scissors_following_choice(read_input_file("day02.txt")) == 12535);
}

TEST_CASE("Day 02 part 2 with test data - rock paper scissors following result") {
    CHECK(day02_part2_rock_paper_scissors_following_result(day02_test_input) == 12);
}

TEST_CASE("Day 02 part 2 with real data - rock paper scissors following result") {
    CHECK(day02_part2_rock_paper_scissors_following_result(read_input_file("day02.txt")) == 15457);
}
