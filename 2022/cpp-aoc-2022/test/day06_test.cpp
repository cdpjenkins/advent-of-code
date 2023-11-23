#include "doctest.h"

#include "../src/util.hpp"

#include "../src/day06.hpp"

using namespace std::string_literals;

TEST_CASE("Day 06 part 1 with test data") {
    CHECK(day06_part1("bvwbjplbgvbhsrlpgdmjqwftvncz") == 5);
    CHECK(day06_part1("nppdvjthqldpwncqszvftbrmjlhg") == 6);
    CHECK(day06_part1("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") == 10);
    CHECK(day06_part1("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") == 11);
}

TEST_CASE("Day 05 part 1 with real data") {
    CHECK(day06_part1(read_input_file("day06.txt")[0]) == 1542);
}

TEST_CASE("Day 06 part 2 with test data") {
    CHECK(day06_part2("mjqjpqmgbljsphdztnvjfqwrcgsmlb") == 19);
    CHECK(day06_part2("bvwbjplbgvbhsrlpgdmjqwftvncz") == 23);
    CHECK(day06_part2("nppdvjthqldpwncqszvftbrmjlhg") == 23);
    CHECK(day06_part2("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") == 29);
    CHECK(day06_part2("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") == 26);
}

TEST_CASE("Day 05 part 2 with real data") {
    CHECK(day06_part2(read_input_file("day06.txt")[0]) == 3153);
}
