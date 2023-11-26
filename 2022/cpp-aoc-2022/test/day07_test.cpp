#include "doctest.h"

#include <fstream>
#include <sstream>

#include "../src/util.hpp"

#include "../src/day07.hpp"

using namespace std::string_literals;

static std::string day_07_test_input{
R"($ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k)"};


TEST_CASE("Day 07 part 1 with test data") {
    std::stringstream input{day_07_test_input};
    CHECK(Day07::part1(input) == 95437);
}

TEST_CASE("Day 07 part 1 with real data") {
    std::ifstream input = open_input_file("day07.txt");
    CHECK(Day07::part1(input) == 1908462);
}

// TEST_CASE("Day 07 part 2 with test data") {

// }

// TEST_CASE("Day 07 part 2 with real data") {
//     CHECK(Day07::part2(read_input_file("day07.txt")[0]) == -1);
// }
