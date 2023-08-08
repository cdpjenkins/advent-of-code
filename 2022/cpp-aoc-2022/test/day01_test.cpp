#include "doctest.h"

#include "../src/day01.hpp"

TEST_CASE("Day 01 part 1 with test data - Elf with most calories") {
    CHECK(day01_part1_elf_with_most_calories() == 24000);
}
