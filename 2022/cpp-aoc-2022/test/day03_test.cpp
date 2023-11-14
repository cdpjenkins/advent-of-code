#include <vector>

#include "doctest.h"

#include "../src/day03.hpp"
#include "../src/util.hpp"

std::vector<std::string> day03_test_input{
        "vJrwpWtwJgWrhcsFMMfFFhFp",
        "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL",
        "PmmdzqPrVvPwwTWBwg",
        "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn",
        "ttgJtRGJQctTZtZT",
        "CrZsJsPPZsGzwwsLwLmpwMDw"
};

TEST_CASE("Day 03 part 1 with test data") {
    CHECK(day03_part1(day03_test_input) == 157);
}

TEST_CASE("Day 03 part 1 with real data") {
    CHECK(day03_part1(read_input_file("day03.txt")) == 7766);
}

TEST_CASE("Values of selection of items are correct") {
    CHECK(value_of('a') == 1);
    CHECK(value_of('z') == 26);
    CHECK(value_of('A') == 27);
    CHECK(value_of('Z') == 52);
}

//TEST_CASE("Day 03 part 2 with test data") {
//    CHECK(day03_part2(day02_test_input) == 12);
//}
//
//TEST_CASE("Day 03 part 2 with real data") {
//    CHECK(day03_part2(read_input_file("day02.txt")) == 123);
//}
